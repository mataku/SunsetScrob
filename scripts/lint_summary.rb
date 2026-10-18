#!/usr/bin/env ruby
# frozen_string_literal: true

# Aggregates Android Lint XML reports across all modules into a Markdown
# summary used by `.github/workflows/lint.yml` (Step Summary + PR comment).
#
# Outputs:
#   - Writes the Markdown summary to ./lint-summary.md.
#   - Appends the same summary to $GITHUB_STEP_SUMMARY when set.
#
# Requires `lint.xmlReport = true` in the build-logic Lint configuration so
# that each module emits build/reports/lint-results-<variant>.xml.

require 'rexml/document'

workspace = ENV['GITHUB_WORKSPACE'].to_s
reports = Dir.glob('**/build/reports/lint-results-*.xml').sort.uniq

rows = []
reports.each do |report|
  parts = report.split('/build/', 2)
  mod = parts.length == 2 ? parts[0].sub(%r{^\./}, '') : '?'

  begin
    doc = REXML::Document.new(File.read(report))
  rescue REXML::ParseException
    next
  end

  doc.root.each_element('issue') do |issue|
    next unless issue.attributes['severity'] == 'Error'

    issue_id = issue.attributes['id'] || '?'
    message  = issue.attributes['message'].to_s
    locations = issue.get_elements('location')
    locations = [nil] if locations.empty?

    locations.each do |loc|
      file = loc ? (loc.attributes['file'] || '?') : '?'
      line = loc ? (loc.attributes['line'] || '?') : '?'
      if !workspace.empty? && file.start_with?(workspace + '/')
        file = file[(workspace.length + 1)..]
      end
      rows << [mod, file, line, issue_id, message]
    end
  end
end

out = ['## Android Lint failures', '']
if rows.empty?
  out << '_The lint task failed but no errors were parsed. Check the run log._'
else
  out << "Found **#{rows.length}** error(s)."
  out << ''
  out << '| Module | File:Line | Issue | Message |'
  out << '|---|---|---|---|'
  rows.each do |mod, file, line, issue_id, message|
    msg = message.gsub('|', '\\|').gsub("\n", ' ')
    out << "| `#{mod}` | `#{file}:#{line}` | `#{issue_id}` | #{msg} |"
  end
end

content = out.join("\n") + "\n"
File.write('lint-summary.md', content)
if (summary_path = ENV['GITHUB_STEP_SUMMARY'])
  File.open(summary_path, 'a') { |f| f.write(content) }
end

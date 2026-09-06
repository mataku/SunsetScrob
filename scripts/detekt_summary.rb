#!/usr/bin/env ruby
# frozen_string_literal: true

# Aggregates detekt checkstyle XML reports across all modules into a Markdown
# summary used by `.github/workflows/detekt.yml` (Step Summary + PR comment).
#
# Outputs:
#   - Writes the Markdown summary to ./detekt-summary.md.
#   - Appends the same summary to $GITHUB_STEP_SUMMARY when set.
#
# Requires checkstyle reports to be enabled in the detekt convention plugin so
# that each module emits build/reports/detekt/detekt.xml.

require 'rexml/document'

workspace = ENV['GITHUB_WORKSPACE'].to_s
reports = Dir.glob('**/build/reports/detekt/detekt.xml').sort.uniq

rows = []
reports.each do |report|
  parts = report.split('/build/', 2)
  mod = parts.length == 2 ? parts[0].sub(%r{^\./}, '') : '?'

  begin
    doc = REXML::Document.new(File.read(report))
  rescue REXML::ParseException
    next
  end

  next if doc.root.nil?

  doc.root.each_element('file') do |file_element|
    file = file_element.attributes['name'].to_s
    if !workspace.empty? && file.start_with?(workspace + '/')
      file = file[(workspace.length + 1)..]
    end

    file_element.each_element('error') do |error|
      rows << [
        mod,
        file,
        error.attributes['line'] || '?',
        error.attributes['source'] || '?',
        error.attributes['message'].to_s
      ]
    end
  end
end

out = ['## detekt failures', '']
if rows.empty?
  out << 'No detekt findings were reported, but the detekt job failed.'
  out << 'Check the job log — this usually means a configuration or plugin resolution error.'
else
  out << "#{rows.size} finding(s) across #{rows.map(&:first).uniq.size} module(s)."
  out << ''
  out << '| Module | File | Line | Rule | Message |'
  out << '| --- | --- | --- | --- | --- |'
  rows.each do |mod, file, line, rule, message|
    escaped = message.gsub('|', '\\|').gsub("\n", ' ')
    out << "| `#{mod}` | `#{file}` | #{line} | `#{rule}` | #{escaped} |"
  end
end

summary = out.join("\n") + "\n"
File.write('detekt-summary.md', summary)

step_summary = ENV['GITHUB_STEP_SUMMARY']
File.write(step_summary, summary, mode: 'a') if step_summary && !step_summary.empty?

# frozen_string_literal: true
source "https://rubygems.org"

gem 'danger'
gem 'danger-android_lint'
gem 'danger-android_permissions_checker'
gem 'fastlane'
gem 'danger-ktlint', git: 'git://github.com/mataku/danger-ktlint', branch: 'develop'

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)

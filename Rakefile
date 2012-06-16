require 'json'
require 'fileutils'

=begin
Github Pages:
https://help.github.com/categories/20/articles
http://stackoverflow.com/questions/3939595/making-javadocs-available-with-github-sonatype-maven-repo

TODO: for releases, auto-tweet
TODO: point blog, etc at http://danbernier.github.com/WordCram and http://danbernier.github.com/WordCram/javadoc
=end

desc "Copies a fresh WordCram library into your Processing environment. See build.json!"
task 'publish.local' => :bundleForProcessing do
  sketch_folder = build_properties['processing.sketchFolder']
  lib_folder = File.join(sketch_folder, 'libraries')
  wc_folder = File.join(lib_folder, 'WordCram')

  FileUtils.rm_rf(wc_folder)
  FileUtils.cp_r('build/p5lib/WordCram', File.join(lib_folder))
end

task 'publish.daily' => :bundleForProcessing do
  summary = ask "Give us a quick summary of the release:"
  tstamp = Time.now.strftime '%Y%m%d'

  git_tag "daily/#{tstamp}", "Tagging the #{tstamp} daily build"
  zip_and_tar_and_upload tstamp, summary
end

task 'publish.release' => :bundleForProcessing do

  # git checkout master, first? Warn if you're not on master?

  summary = ask "Give us a quick summary of the release:"
  release_number = ask "...and the release number:"

  git_tag "release/#{release_number}", "Tagging the #{release_number} release"
  zip_and_tar_and_upload release_number, summary

  puts "uploading javadoc to github..."
  puts `git checkout gh-pages`
  puts `cp -r build/p5lib/WordCram/reference javadoc`
  puts `git add javadoc`
  puts `git commit -m "Updating javadoc for #{release_number} release."`
  puts `git push`
end


%w[bundleForProcessing clean compile makeReleaseBranch test].each do |task_name|

  desc "Run ant task #{task_name}"
  task task_name.to_sym do
    puts `ant #{task_name}`
  end

end

task :default => :test

def zip_and_tar_and_upload(version, summary)
  zipfile = "build/wordcram.#{version}.zip"
  tarfile = "build/wordcram.#{version}.tar.gz"

  puts "zipping & tarring..."
  puts `zip -5Tr #{zipfile} build/p5lib/WordCram`
  puts `tar -cvz build/p5lib/WordCram > #{tarfile}`

  puts "uploading to github..."
  puts `github-downloads create -u danbernier -r WordCram -f #{zipfile} -d "#{summary}"`
  puts `github-downloads create -u danbernier -r WordCram -f #{tarfile} -d "#{summary}"`
end

def ask(message)
  puts message
  STDIN.gets.chomp
end

def git_tag(tag_name, commit_message)
  puts "git tagging..."
  puts `git tag #{tag_name} -m "#{commit_message}"`
end

def build_properties
  @props ||= JSON(File.read('build.json'))
end

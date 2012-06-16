require 'json'
require 'fileutils'

=begin
TODO: host javadoc on github? Maybe with github pages?
https://help.github.com/categories/20/articles
http://stackoverflow.com/questions/3939595/making-javadocs-available-with-github-sonatype-maven-repo
=end

desc "Copies a fresh WordCram library into your Processing environment. See build.json!"
task 'publish.local' => :bundleForProcessing do
  sketch_folder = build_properties['processing.sketchFolder']
  lib_folder = File.join(sketch_folder, 'libraries')
  wc_folder = File.join(lib_folder, 'WordCram')

  FileUtils.rm_rf(wc_folder)
  FileUtils.cp_r('build/p5lib/WordCram', File.join(lib_folder))
end

%w[bundleForProcessing clean compile makeReleaseBranch publish.daily publish.release test].each do |task_name|

  desc "Run ant task #{task_name}"
  task task_name.to_sym do
    puts `ant #{task_name}`
  end

end

task :default => :test

def build_properties
  @props ||= JSON(File.read('build.json'))
end

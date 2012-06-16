require 'json'
require 'fileutils'

=begin
Github Pages:
https://help.github.com/categories/20/articles
http://stackoverflow.com/questions/3939595/making-javadocs-available-with-github-sonatype-maven-repo

TODO: for releases, auto-tweet
TODO: point blog, etc at http://danbernier.github.com/WordCram and http://danbernier.github.com/WordCram/javadoc
=end

task :test => :compile do
  Dir.mkdir 'build/tests'

  javac_opts = {
    :d => 'build/tests',  # d = destination
    :source => '1.5',
    :target => '1.5',
    :classpath => test_classpath
  }

  cmd = "javac #{to_flags(javac_opts)} -Xlint test/**/*.java"
  puts `#{cmd}`

  junit_opts = {
    :cp => test_classpath + ':build/tests'
  }
  cmd = "java #{to_flags(junit_opts)} org.junit.runner.JUnitCore #{unit_test_classes}"
  puts cmd
  test_results = `#{cmd}`
  puts test_results

  if test_results.include? 'FAILURES!!!'
    puts "Abort the mission, you have test failures!"
    exit
  end
end

task :bundle => :test do

  # TODO version # in build file - property? Or rather, pass it as an arg, and have it default to 'latest' or something.
  # TODO put these in if it ever seems useful:
  #  - http://processing.googlecode.com/svn/trunk/processing/build/javadoc/
  #  - http://developer.java.sun.com/developer/products/xml/docs/api/

  FileUtils.mkdir_p 'build/p5lib/WordCram/library'
  `jar -cvf build/p5lib/WordCram/library/WordCram.jar build/classes`
  FileUtils.cp 'lib/jsoup-1.3.3.jar', 'build/p5lib/WordCram/library'
  FileUtils.cp 'lib/cue.language.jar', 'build/p5lib/WordCram/library'

  FileUtils.cp_r 'example', 'build/p5lib/WordCram/examples'

  FileUtils.cp_r 'src', 'build/p5lib/WordCram/src'

  javadoc_opts = {
    :classpath => main_classpath,
    :sourcepath => 'src',
    :d => 'build/p5lib/WordCram/reference',  # d = destination
    :windowtitle => "WordCram API",
    :overview => 'src/overview.html',
    :header => "WordCram 0.5",
    :subpackages => 'wordcram'
  }

  # puts "javadoc #{to_flags(javadoc_opts)}"
  `javadoc #{to_flags(javadoc_opts)}`

  FileUtils.cp 'wordcram.png', 'build/p5lib/WordCram/reference'
end

namespace :publish do
  desc "Copies a fresh WordCram library into your Processing environment. See build.json!"
  task :local => :bundle do
    sketch_folder = build_properties['processing.sketchFolder']
    lib_folder = File.join(sketch_folder, 'libraries')
    wc_folder = File.join(lib_folder, 'WordCram')

    FileUtils.rm_rf(wc_folder)
    FileUtils.cp_r('build/p5lib/WordCram', File.join(lib_folder))
  end

  desc "Publish & git-tag a fresh WordCram library to github downloads."
  task :daily => :bundle do
    summary = ask "Give us a quick summary of the release:"
    tstamp = Time.now.strftime '%Y%m%d'

    git_tag "daily/#{tstamp}", "Tagging the #{tstamp} daily build"
    zip_and_tar_and_upload tstamp, summary
  end

  desc "Release WordCram: git-tag, upload to github, update github pages javadoc. And later, Tweet! (And blog?)"
  task :release => :bundle do

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
end
task :publish => 'publish:local'

%w[clean compile].each do |task_name|

  desc "Run ant task #{task_name}"
  task task_name.to_sym do
    puts `ant #{task_name}`
  end

end

task :default => :test

def main_classpath
  ['lib/processing/core.jar', 'lib/jsoup-1.3.3.jar', 'lib/cue.language.jar'] * ':'
end

def test_classpath
  [main_classpath, 'lib/junit/junit-4.8.2.jar', 'lib/mockito-all-1.8.5.jar', 'build/classes'] * ':'
end

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
  # TODO Um, git pull && git push?
end

def unit_test_classes
  Dir.glob('test/**/A*.java').map { |file|
    file.gsub(/^test\//, '').gsub(/\.java$/, '').gsub('/', '.')
  }.join(' ')
end

def to_flags(opts)
  opts.map { |flag, value|
    value = "\"#{value}\"" if value.to_s.include? ' '
    "-#{flag} #{value}"
  }.join(' ')
end

def build_properties
  @props ||= JSON(File.read('build.json'))
end

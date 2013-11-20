require 'json'
require 'fileutils'
require 'aws-sdk'

=begin
Github Pages:
https://help.github.com/categories/20/articles
http://stackoverflow.com/questions/3939595/making-javadocs-available-with-github-sonatype-maven-repo

TODO: for releases, auto-tweet
TODO: point blog, etc at http://danbernier.github.com/WordCram
TODO: add some kind of 'verbose' flag to this. Factor those puts-es into an announce method, which observes the -v flag.
=end

desc "Clean the source files: trim trailing whitespace, & \t -> 4 spaces"
task :clean_source do
  puts "Cleaning source files..."
  (Dir.glob('src/**/*.java') + Dir.glob('test/**/*.java')).each do |file|
    src = File.read(file)

    new_src = src
    new_src.gsub!(/\r/, '')
    # new_src.gsub!(/\t/, ' ' * 4)
    new_src = new_src.each_line.map(&:rstrip).join("\n")

    #puts file
    File.open(file, 'w') do |f|
      f.puts new_src.strip + "\n"
    end
  end
end

desc "Clean the build artifacts: delete the build directory."
task :clean do
  puts "Cleaning..."
  FileUtils.rm_rf('build')
end

desc "Compile the WordCram java files."
task :compile => :clean do
  compile('src', 'build/classes', main_classpath)
end

desc "Compile and run the WordCram unit tests. Aborts the build if there are failures."
task :test => :compile do

  compile('test', 'build/tests', test_classpath)

  puts "Running tests..."
  junit_opts = {
    :cp => test_classpath + ':build/tests'
  }
  test_results = run "java #{to_flags(junit_opts)} org.junit.runner.JUnitCore #{unit_test_classes}"
  puts test_results

  if test_results.include? 'FAILURES!!!'
    puts "Abort the mission, you have test failures!"
    exit
  end
end

desc "Bundle WordCram source, jars, examples, and javadoc in the typical Processing format."
task :bundle => :test do

  # TODO put these in if it ever seems useful:
  #  - http://processing.googlecode.com/svn/trunk/processing/build/javadoc/
  #  - http://developer.java.sun.com/developer/products/xml/docs/api/

  puts "Bundling files together..."
  FileUtils.mkdir_p 'build/p5lib/WordCram/library'
  run "jar -cvf build/p5lib/WordCram/library/WordCram.jar -C build/classes ."
  FileUtils.cp 'lib/jsoup-1.7.2.jar', 'build/p5lib/WordCram/library'
  FileUtils.cp 'lib/cue.language.jar', 'build/p5lib/WordCram/library'

  FileUtils.cp_r 'example', 'build/p5lib/WordCram/examples'

  FileUtils.cp_r 'src', 'build/p5lib/WordCram/src'

  javadoc_opts = {
    :classpath => main_classpath,
    :sourcepath => 'src',
    :d => 'build/p5lib/WordCram/reference',  # d = destination
    :windowtitle => "WordCram API",
    :overview => 'src/overview.html',
    :header => "WordCram #{version}",
    :subpackages => 'wordcram'
  }

  puts "Generating javadocs..."
  run "javadoc #{to_flags(javadoc_opts)} -use -version"

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
    puts "Copied files to #{wc_folder}."
  end

  desc "Publish & git-tag a fresh WordCram library for public download."
  task :daily => :bundle do
    tstamp = Time.now.strftime '%Y%m%d'

    git_tag "daily/#{tstamp}", "Tagging the #{tstamp} daily build"
    zip_and_tar_and_upload tstamp
  end

  desc "Release WordCram: git-tag, upload binaries, update github pages javadoc. And later, Tweet! (And blog?)"
  task :release => :bundle do

    # git checkout master, first? Warn if you're not on master?

    release_number = version
    puts "Release number: #{release_number}"

    git_tag "release/#{release_number}", "Tagging the #{release_number} release"
    zip_and_tar_and_upload release_number

    puts "uploading javadoc to github..."
    run "git checkout gh-pages"
    run "rm -rf javadoc"
    run "cp -r build/p5lib/WordCram/reference javadoc"
    run "git add javadoc"
    run "git commit -m \"Updating javadoc for #{release_number} release.\""
    run "git push"
    run "git push --tags"
    run "git checkout master"
  end
end
task :publish => 'publish:local'

namespace :bump_version do
  desc "Bump the lowest version number"
  task :tiny do
    bump_version(2)
  end

  desc "Bump the middle version number"
  task :minor do
    bump_version(1)
  end

  desc "Bump the highest version number"
  task :major do
    bump_version(0)
  end

  def bump_version(index)
    version = File.read('VERSION').split('.').map(&:to_i)
    version[index] = version[index] + 1
    (index+1).upto(2) do |i|
      version[i] = 0
    end
    version = version.join('.')
    File.open('VERSION', 'w') { |f| f.puts version }
    run "git add VERSION"
    run "git commit -m \"Bump version to #{version}\""
    puts "Bumped version to #{version}"
    puts `git log -1`
  end
end

desc "Bump the lowest version number"
task :bump_version => 'bump_version:tiny'

task :default => :test

def compile(src_dir, dest_dir, classpath)
  puts "Compiling #{src_dir} into #{dest_dir}..."
  FileUtils.mkdir_p dest_dir

  javac_opts = {
    :d => dest_dir,  # d = destination
    :source => '1.5',
    :target => '1.5',
    :classpath => classpath
  }

  src_files = Dir.glob(File.join(src_dir, '**/*.java')).join(' ')
  output = run "javac #{to_flags(javac_opts)} -Xlint #{src_files} 2>&1"
  if output =~ /\d+ error/
    puts output
    puts "Abort the mission! You have compile errors."
    exit
  end
end

def main_classpath
  ['lib/processing/core.jar', 'lib/jsoup-1.7.2.jar', 'lib/cue.language.jar'] * ':'
end

def test_classpath
  [main_classpath, 'lib/junit/junit-4.8.2.jar', 'lib/mockito-all-1.8.5.jar', 'build/classes'] * ':'
end

def zip_and_tar_and_upload(version)
  zipfile = "wordcram.#{version}.zip"
  tarfile = "wordcram.#{version}.tar.gz"

  puts "zipping & tarring..."
  run "cd build/p5lib; zip -5Tr ../#{zipfile} WordCram; cd ../.."
  run "tar -cvz -Cbuild/p5lib/ WordCram > build/#{tarfile}"

  puts "uploading build/#{zipfile} and build/#{tarfile} to AWS..."
  urls = aws_upload("build/#{zipfile}", "build/#{tarfile}")
  puts "uploaded to: #{urls.inspect}"
end

def aws_upload(*filepaths)
  access_key = build_properties.fetch('aws.access_key_id')
  secret_key = build_properties.fetch('aws.secret_access_key')
  AWS.config(:access_key_id => access_key, :secret_access_key => secret_key)

  s3 = AWS::S3.new
  b = s3.buckets['wordcram']

  filepaths.map do |filepath|
    filename = File.basename(filepath)
    release = b.objects["downloads/#{filename}"]
    release.write(File.read(filepath))
    release.acl = :public_read
    release.public_url.to_s
  end
end

def run(cmd)
  File.open('build/log', 'a') do |f|
    f.puts "$ #{cmd}"
    `#{cmd}`.tap do |results|
      f.puts results
      f.puts
    end
  end
end

def git_tag(tag_name, commit_message)
  puts "git tagging..."
  run "git tag #{tag_name} -m \"#{commit_message}\""
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

def version
  File.read('VERSION').strip
end

def build_properties
  @props ||= JSON(File.read('build.json'))
end

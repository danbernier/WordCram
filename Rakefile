require 'json'
require 'fileutils'
require 'aws-sdk'
require 'csv'

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
  FileUtils.rm_rf('_site')
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

  generate_javadoc_to('build/p5lib/WordCram/reference')
end

namespace :publish do
  desc "Copies a fresh WordCram library into your Processing environment. See build.json!"
  task :local => :bundle do
    sketch_folder = build_properties['processing.sketchFolder']
    lib_folder = File.join(sketch_folder, 'libraries')
    wc_folder = File.join(lib_folder, 'WordCram')

    FileUtils.rm_rf(wc_folder)
    FileUtils.cp_r('build/p5lib/WordCram', File.join(lib_folder))
    FileUtils.cp 'library.properties', 'build/p5lib/WordCram'
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

    puts "Release number: #{version}"

    git_tag "release/#{version}", "Tagging the #{version} release"
    zip_and_tar_and_upload version

    update_gh_pages_and_push
  end
end
task :publish => 'publish:local'

desc "Re-generate javadoc, and upload it to the github pages site"
task :update_gh_pages_and_push do
  update_gh_pages_and_push
end

def transform_file(origin, into:, &transformer)
  File.open(into, 'w') do |f|
    f.puts transformer.call(File.read(origin))
  end
end

def update_gh_pages_and_push
  puts "updating gh-pages site and sending it to github..."
  run "git stash save stashed-while-generating-gh-pages"
  run "git checkout gh-pages"
  run "git merge master -m \"Merge master into gh-pages\""  # Merge master into gh-pages, so it's up-to-date.

  turn_readme_into_index

  run "rm -rf javadoc"
  generate_javadoc_to('javadoc')

  import_the_gallery

  run "git add -A index.md javadoc _data gallery"
  run "git commit -m \"Updating gh-pages for #{version} release.\""
  run "git push -f origin gh-pages"
  run "git checkout -"
  run "git stash pop"
end

desc "Import the gallery from examples/gallery into jekyll"
task :import_the_gallery do import_the_gallery end
def import_the_gallery
  gallery_paths = Dir.glob('example/gallery/*').map { |p| File.basename(p) }

  # create _data/gallery.csv
  CSV.open('_data/gallery.csv', 'w') do |csv|
    csv << %w(path title)
    gallery_paths.each do |path|
      title = path.gsub(/([A-Z])/, ' \1').split(' ').map(&:capitalize).join(' ')
      csv << [path, title]
    end
  end

  # run the sketches
  run "mkdir gallery"
  run "rm gallery/*.pde"
  run "rm gallery/*.png"
  gallery_paths.each do |gallery_path|
    File.open("example/gallery/#{gallery_path}/#{gallery_path}.pde", 'a') do |f|
      f.puts %Q{
        void endDraw() {
          save(new File(sketchPath("")).getName() + ".png");
          exit();
        }
      }
    end

    run "processing-java --sketch=example/gallery/#{gallery_path} --output=output/#{gallery_path} --run --force"
    run "mv example/gallery/#{gallery_path}/#{gallery_path}.png gallery/"
    run "git co -- example/gallery/#{gallery_path}/#{gallery_path}.pde"  # to clear out the `void endDraw()`
    run "cp example/gallery/#{gallery_path}/#{gallery_path}.pde gallery/#{gallery_path}.pde"
  end
  run "rm -rf output"
end

def turn_readme_into_index
  transform_file('README.md', into: 'index.md') { |readme|
    [
      '---',
      'layout: default',
      '---',
      '',
      readme
    ].join("\n")
  }
end

def generate_javadoc_to(folder)
  javadoc_opts = {
    :classpath => main_classpath,
    :sourcepath => 'src',
    :d => folder,  # d = destination
    :windowtitle => "WordCram API",
    :overview => 'src/overview.html',
    :header => "WordCram #{version}",
    :subpackages => 'wordcram'
  }

  puts "Generating javadocs..."
  run "javadoc #{to_flags(javadoc_opts)} -use -version"

  FileUtils.cp 'wordcram.png', folder
end


namespace :bump_version do
  desc "Bump the patch version number"
  task :patch do
    bump_version(2)
  end

  desc "Bump the minor version number"
  task :minor do
    bump_version(1)
  end

  desc "Bump the major version number"
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

desc "Bump the patch version number"
task :bump_version => 'bump_version:patch'

task :default => :test

def compile(src_dir, dest_dir, classpath)
  puts "Compiling #{src_dir} into #{dest_dir}..."
  FileUtils.mkdir_p dest_dir

  javac_opts = {
    :d => dest_dir,  # d = destination
    :source => '1.6',
    :target => '1.6',
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
  zipfile = "wordcram.zip"
  tarfile = "wordcram.tar.gz"

  puts "zipping & tarring..."
  run "cd build/p5lib; zip -5Tr ../#{zipfile} WordCram; cd ../.."
  run "tar -cvz -Cbuild/p5lib/ WordCram > build/#{tarfile}"

  FileUtils.cp 'library.properties', 'wordcram.txt'

  puts "uploading files to AWS..."
  urls = aws_upload("build/#{zipfile}", "build/#{tarfile}", 'wordcram.txt')
  puts "uploaded to: #{urls.inspect}"
  FileUtils.rm 'wordcram.txt'
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
  Dir.mkdir('build') unless Dir.exists?('build')
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

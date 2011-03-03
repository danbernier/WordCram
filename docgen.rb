# use java annotations instead?
# http://download.oracle.com/javase/tutorial/java/javaOO/annotations.html?
# though they'll wind up in the .jar, and probably in the P5 imports...

files = Dir.glob('src/**/*.java')

examples = Hash.new
files.each do |file|
	src = File.read(file)
	class_name = File.basename(file).gsub(/\..*$/, '')
	class_examples = src.scan(/\/\/example (.*)$/).flatten.compact
	examples[class_name] = class_examples unless class_examples.empty?
end

puts examples


puts '='*80


publics = files.map do |file|
	src = File.read(file)
	src.scan(/public .*$/)
end

publics = publics.flatten.compact
#puts publics




%w[bundleForProcessing clean compile makeReleaseBranch publish.daily publish.local publish.release test].each do |task_name|

  desc "Run ant task #{task_name}"
  task task_name.to_sym do
    puts `ant #{task_name}`
  end

end

#task :default => :test
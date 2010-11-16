#!/usr/bin/ruby

`wget http://google-guice.googlecode.com/files/guice-2.0.zip`
`sha1sum -c checksums`

if `echo $?`.chomp == "0"
  `unzip guice-2.0.zip`
  `cp #{File.join("guice-2.0", "aopalliance.jar")} .` 
  `cp #{File.join("guice-2.0", "guice-2.0.jar")} .` 
else
  puts retval
  puts "Erroneous download of guice-2.0.zip. Please try again."
end

`rm guice-2.0.zip guice-2.0 -rf`


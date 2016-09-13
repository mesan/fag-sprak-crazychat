#!/usr/bin/env ruby

$stderr = File.new("stderr.log", 'w')

require "sinatra"
require "sinatra/reloader"
require "json"

post '/chat' do
  data = JSON.parse(request.body.read)
  puts data
  'Hello, world!'
end

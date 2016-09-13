#!/usr/bin/env ruby

require "sinatra"
require "sinatra/reloader"
require "json"

post '/chat' do
  data = JSON.parse(request.body.read)
  puts data
  'Hello, world!'
end

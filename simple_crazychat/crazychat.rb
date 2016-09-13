#!/usr/bin/env ruby

$stderr = File.new("stderr.log", 'w')

require 'sinatra/base'
require 'sinatra/reloader'
require 'json'
require 'logger'

class MyApp < Sinatra::Base
  register Sinatra::Reloader

  def initialize(app=nil, foobar="hei")
    super(app)
    @foobar = foobar
  end

  post '/chat' do
    @foobar
  end

  run!
end

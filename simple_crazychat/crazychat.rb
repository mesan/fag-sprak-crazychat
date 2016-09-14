#!/usr/bin/env ruby

$stderr = File.new("stderr.log", 'w')

require 'sinatra/base'
require 'sinatra/reloader'
require 'json'
require 'logger'

class CrazyChat
  def handle_message(body)
    username = body['username']
    message = body['message']
    puts "<#{username}> #{message}"
  end
end

class MyApp < Sinatra::Base
  register Sinatra::Reloader

  post '/chat' do
    message = JSON.parse(request.body.read)
    settings.chat.handle_message(message)
    200
  end
end

MyApp.set :chat, CrazyChat.new
MyApp.run!

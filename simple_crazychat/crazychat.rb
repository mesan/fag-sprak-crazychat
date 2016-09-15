#!/usr/bin/env ruby

require 'sinatra/base'
require 'sinatra/reloader'
require 'rest-client'

my_address, my_port, my_name = ARGV
raise "Oppgi addresse, portnummer og navn!" unless my_address && my_port && my_name 

class CrazyChat
  def initialize(address, port, name, users={})
    @address = address
    @name = name
    @users = users
  end

  def add_user(username, address)
    @users[username] = address
    p @users
  end

  def handle_message(body)
    username = body['username']
    message = body['message']
    address = body['returnAddress']

    add_user(username, address)

    puts "<#{username}> #{message}"
  end

  def send_message(message)
    puts "SEND!"
    @users.each do |name, address|
      puts "Sender til #{name} (#{address})..."
      RestClient.post "http://#{address}", {"message" => message,
                                            "username" => @name,
                                            "returnAddress" => "http://#@address:#@port"}
      exit

    end
  end
end

chat_client = CrazyChat.new(my_address, my_port.to_i, my_name)

class MyApp < Sinatra::Base
  register Sinatra::Reloader

  post '/' do
    settings.chat_client.handle_message(request.params)
    200
  end
end

$stderr = File.new("stderr-#$$.log", 'w') unless $VERBOSE

MyApp.set :chat_client, chat_client
MyApp.set :port, my_port.to_i
server_thread = Thread.new { MyApp.run! }

Thread.new do
  loop do
    print "> "
    input = $stdin.gets.chomp

    case input
    when /^:add (\w+) (.+)/
      chat_client.add_user($1, $2)
    else
      chat_client.send_message(input)
    end
  end
end

server_thread.join

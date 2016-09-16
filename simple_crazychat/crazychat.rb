#!/usr/bin/env ruby

require 'sinatra/base'
require 'sinatra/reloader'
require 'rest-client'

my_address, my_port, my_name = ARGV
raise "Oppgi addresse, portnummer og navn!" unless my_address && my_port && my_name 

class CrazyChat
  attr_reader :users

  def initialize(address, name, users={})
    @return_address = address
    @name = name
    @users = users
  end

  def add_user(username, address)
    @users[address] = username
  end

  def handle_message(body)
    username = body['username']
    message = body['message']
    address = body['returnAddress']

    add_user(username, address)

    puts "<#{username}> #{message}"
  end

  def send_message(message)
    @users.each do |address, name|
      Thread.new do
        begin
          $stderr.puts "Sender til #{name} (#{address})..."
          RestClient.post address, {"message" => message,
                                    "username" => @name,
                                    "returnAddress" => @return_address}
        rescue => e
          puts "feil: #{e}"
        end
      end
    end
  end
end

chat_client = CrazyChat.new("http://#{my_address}:#{my_port}", my_name)

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
Thread.new { MyApp.run! }

loop do
  print "> "
  input = $stdin.gets&.chomp

  exit unless input
  next if input.empty?

  case input
  when /^:add (\S+) (.+)/
    chat_client.add_user($1, $2)
  when /^:users/
    p chat_client.users
  else
    chat_client.send_message(input)
  end
end

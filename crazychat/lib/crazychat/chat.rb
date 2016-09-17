require 'rest-client'

module CrazyChat
  class Chat
    attr_reader :users

    def initialize(address, name, users={})
      @return_address = address
      @name = name
      @users = users
    end

    def add_user(username, address)
      @users[address] = username
    end

    def handle_message(username, address, message)
      add_user(username, address)

      puts "<#{username}> #{message}"
    end

    def send_message(message)
      @users.each do |address, name|
        Thread.new do
          begin
            payload = { message: message,
                        username: @name,
                        returnAddress: @return_address }
            RestClient.post address, payload.to_json, {content_type: :json}
          rescue => e
            puts "feil: #{e}"
          end
        end
      end
    end
  end
end

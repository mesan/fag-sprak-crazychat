require "crazychat/ui"
require "crazychat/chat"
require "crazychat/server"

module CrazyChat

  def self.start(address, port, name)
    chat_client = Chat.new("http://#{address}:#{port}", name)

    Server.set :chat_client, chat_client
    Server.set :port, port
    Thread.new { Server.run! }

    CrazyChat.input_loop(chat_client)
  end

end

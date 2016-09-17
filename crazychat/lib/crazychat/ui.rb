module CrazyChat

  def self.input_loop(chat_client)
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
  end

end

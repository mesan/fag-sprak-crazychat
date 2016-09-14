require 'rx'

input = Enumerator.new do |y|
  loop do
    print "> "
    y << gets
  end
end

source = Rx::Observable.from(input)
published = source.publish

def create_observer(tag)
  Rx::Observer.create(
    ->(nxt) { puts "#{tag} next: #{nxt}" },
    ->(err) { puts 'Error: ' + err.to_s },
    -> { puts 'Completed' })
end

5.times do |i|
  Thread.new do
    sleep rand(5) + 2
    puts "New subscriber: #{i}"
    published.subscribe(create_observer("Subscriber #{i}"))
  end
end

connection = published.connect

(Thread.list - [Thread.current]).each &:join

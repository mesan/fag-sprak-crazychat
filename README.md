# Crazychat

TODO: Write something clever here :)

## Installation

Clone this Git repository and checkout the `ruby` branch.

Make sure [Ruby](https://www.ruby-lang.org/en/) is installed -- preferably the 
[latest version](https://www.ruby-lang.org/en/downloads/).

#### Install [Bundler](http://bundler.io/)

    $ gem install bundler

#### Install project dependencies

    $ bundle

##  Executable

You can create an executable Ruby gem from the source in this repository and 
install in it on your path:

    $ bundle exec rake install

Then, the `crazychat` command should be available anywhere in you shell.

    $ crazychat

Otherwise, you can run CrazyChat locally with the executable found in this 
directory

    $ ./crazychat

### Running

The executable takes three arguments:

* Your return address
* A port number
* Your nickname

#### Examples

    $ ./crazychat localhost 1337 CrazyChatBoi98
    $ ./crazychat vg.no 80 spambot

## Usage

To exit CrazyChat, enter a blank line and press `Ctrl+D`.

To enter a command, start a new line with a colon `:`

* `:add <name> <address>` -- adds `<name>` to your users list with the given `<address>`
* `:users` shows your user list

Anything else you type inn will be sent to your contacts when you press enter.

## Development

After checking out the repo, run `bin/setup` to install dependencies. Then, run `rake spec` to run the tests. You can also run `bin/console` for an interactive prompt that will allow you to experiment.

To install this gem onto your local machine, run `bundle exec rake install`. To release a new version, update the version number in `version.rb`, and then run `bundle exec rake release`, which will create a git tag for the version, push git commits and tags, and push the `.gem` file to [rubygems.org](https://rubygems.org).

## License

The gem is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).


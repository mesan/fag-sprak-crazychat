# coding: utf-8
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'crazychat/version'

Gem::Specification.new do |spec|
  spec.name          = "crazychat"
  spec.version       = Crazychat::VERSION
  spec.authors       = ["Daniel Rødskog"]
  spec.email         = ["danielr@mesan.no"]

  spec.summary       = %q{foo}
  spec.description   = %q{bar}
  spec.homepage      = "http://mesan.no"
  spec.license       = "MIT"

  spec.files         = `git ls-files -z`.split("\x0").reject do |f|
                          f.match(%r{^(test|spec|features)/})
                        end
  spec.bindir        = "exe"
  spec.executables   = spec.files.grep(%r{^exe/}) { |f| File.basename(f) }
  spec.require_paths = ["lib"]

  spec.add_runtime_dependency "sinatra", "~> 1.4.7"
  spec.add_runtime_dependency "rest-client", "~> 2.0"

  spec.add_development_dependency "bundler", "~> 1.13"
  spec.add_development_dependency "rake", "~> 10.0"
  spec.add_development_dependency "rspec", "~> 3.5"
  spec.add_development_dependency "rerun", "~> 0.11"
  spec.add_development_dependency "sinatra-reloader", "~> 1.0"
end

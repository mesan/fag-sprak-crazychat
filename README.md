CrazyChat
=========

Usage
-----
$ crazychat <host> <port> <username>

Development
-----
Install go toolchain using your package manager
$ pacman -S go
$ apt-get install golang-go

Install crazychat
$ go get github.com/mesan/fag-sprak-crazychat/crazychat github.com/mesan/fag-sprak-crazychat/crazychat/cmd/crazychat

After modification, compile executable with go install
$ go install github.com/mesan/fag-sprak-crazychat/crazychat/cmd/crazychat

If making changes to rx classes, install gorx and run go generate to recreate rx.go
$ go get github.com/alecthomas/gorx github.com/alecthomas/gorx/cmd/gorx
$ go generate github.com/mesan/fag-sprak-crazychat/crazychat

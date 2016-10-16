CrazyChat
=========

Usage
-----

    crazychat <host> <port> <username>

Development
-----
Install go toolchain using your package manager

    sudo pacman -S go
    sudo apt-get install golang-go

Install crazychat

    go get -u github.com/mesan/fag-sprak-crazychat/crazychat github.com/mesan/fag-sprak-crazychat/crazychat/cmd/crazychat
    go install github.com/mesan/fag-sprak-crazychat/crazychat/cmd/crazychat

After modification, recompile executable

    go install github.com/mesan/fag-sprak-crazychat/crazychat/cmd/crazychat

If making changes to RX classes, install gorx and run go generate to recreate rx.go

    go get -u github.com/alecthomas/gorx github.com/alecthomas/gorx/cmd/gorx
    go install github.com/alecthomas/gorx/cmd/gorx
    go generate github.com/mesan/fag-sprak-crazychat/crazychat/rx

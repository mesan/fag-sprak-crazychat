CrazyChat
=========

Setup
-----
1. Install .NET Core SDK (https://www.microsoft.com/net/core)
2. Navigate to the project folder and run: `dotnet restore` and `dotnet build`


Usage
-----
Start from terminal: `dotnet run <port> <username>`
-  `<port>` the port your local server will start on

The following commands are available
- `:add <address>` Add another crazychatter
- `:users` List connected crazychatters
- `:adieu` Stop the crazy chat
- Everything else will be sent as a messge


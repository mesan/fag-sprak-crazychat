namespace crazychat {
    using System;
    using System.Text.RegularExpressions;

    public interface ICrazyChatClient {
        void InputLoop();
    }

    public class CrazyChatClient : ICrazyChatClient {

        private readonly ICrazyChatServer _server;
        private string _userName;
        private string _returnAddress;
        
        public CrazyChatClient(string userName, string returnAddress, ICrazyChatServer server)
        {
            _userName = userName;
            _returnAddress = returnAddress;
            _server = server;
        }

        public void InputLoop() {
            
            var input = string.Empty;
            var stillGoing = true;
            while (stillGoing)
            {
                Console.WriteLine("Give me some crazy input > ");
                input = Console.ReadLine();

                if (input.Equals(":adieu")) {
                    stillGoing = false;
                }
                else if (input.Equals(":users")) {
                    _server.ListConnectedUsers();
                }
                else if(input.StartsWith(":add")) {
                    var match = Regex.Match(input, @":add (.*)");
                    if (match.Success) {
                        _server.AddContact(match.Groups[1].Value);
                    }
                }
                else {
                    _server.SendMessage(input, _userName, _returnAddress);
                }
            }
            ColorConsole.WriteLineYellow("Adieu!");
        }
    }
}
namespace crazychat
{

    using Newtonsoft.Json;
    using System.Net.Http;
    using System.Collections.Generic;
    using System.Text;
    using System;

    public interface ICrazyChatServer
    {
        void AddContact(string address);
        void ReceiveMessage(string userName, string address, string message);
        void ListConnectedUsers();
        void SendMessage(string message, string userName, string returnAddress);
    }

    public class CrazyChatServer : ICrazyChatServer
    {
        private readonly IDictionary<string, CrazyChatUser> _users;
        public CrazyChatServer()
        {
            _users = new Dictionary<string, CrazyChatUser>();
        }

        public void AddContact(string address)
        {
            _users[address] = new CrazyChatUser();
        }

        public void ReceiveMessage(string userName, string address, string message)
        {
            _users[address] = new CrazyChatUser
            {
                UserName = userName
            };

            if (message != string.Empty)
            {
                ColorConsole.WriteLineGreen($"{userName} says: {message}");
            }
        }

        public void ListConnectedUsers()
        {
            foreach (KeyValuePair<string, CrazyChatUser> user in _users)
            {
                ColorConsole.WriteLineBlue($"{user.Key} ({user.Value.UserName})");
            }
        }

        public void SendMessage(string message, string userName, string returnAddress)
        {
            foreach (KeyValuePair<string, CrazyChatUser> user in _users)
            {
                using (var client = new HttpClient())
                {
                    var content = new {
                        message = message,
                        username = userName,
                        returnaddress = returnAddress
                    };
                    var json = new StringContent(JsonConvert.SerializeObject(content), Encoding.UTF8, "application/json");
                    var result = client.PostAsync(user.Key, json).Result;
                }
            }
        }
    }
}
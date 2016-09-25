namespace crazychat
{
    using Microsoft.AspNetCore.Hosting;
    using System;
    using System.IO;
    using System.Text;

    public class Program
    {
        public static void Main(string[] args)
        {
            Console.OutputEncoding = Encoding.UTF8;

            var address = "http://localhost";
            var port = args[0];
            var userName = args[1];
            var adr = $"{address}:{port}";
            var host = new WebHostBuilder()
                .UseContentRoot(Directory.GetCurrentDirectory())
                .UseKestrel()
                .UseUrls(adr)
                .UseStartup<Startup>()
                .Build();
            
            using (host) {
                host.Start();
                var server = host.Services.GetService(typeof(ICrazyChatServer)) as ICrazyChatServer;
                new CrazyChatClient(userName, $"{adr}/crazychat", server).InputLoop();
            }
        }
    }
}
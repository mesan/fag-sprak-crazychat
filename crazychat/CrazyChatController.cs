namespace crazychat {

    using Microsoft.AspNetCore.Mvc;

    [Route("crazychat")]
    public class CrazyChatController : Controller 
    {
        private readonly ICrazyChatServer _server;

        public CrazyChatController(ICrazyChatServer crazyChatServer)
        {
            _server = crazyChatServer;
        }

        [HttpPost]
        public IActionResult Post([FromBody] CrazyChatMessage message)
        {
            _server.ReceiveMessage(message.UserName, message.ReturnAddress, message.Message);
            return Ok();
        }
    }
}
import static spark.Spark.*;

public class ChatClient {
    public ChatClient(){
//        post("/hello", (req,res) -> "hei");
        get("/hello/:name", (req,res) -> {"Hello " + req.params(":name");});
    }
}

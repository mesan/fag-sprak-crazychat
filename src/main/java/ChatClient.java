//import javafx.util.Callback;
import rx.subjects.PublishSubject;
import spark.Request;

import static spark.Spark.*;

public class ChatClient {
    public ChatClient(){
        PublishSubject<Request> subject = PublishSubject.create();
        subject.subscribe(this::handle_msg);
        post("/hello", (req,res) -> {subject.onNext(req); return "soiudjflkdsf";});
    }
    public void handle_msg(Request req){
        System.out.println(req);

        System.out.println("body " + req.body());
    }
}

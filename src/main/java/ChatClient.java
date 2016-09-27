import rx.subjects.PublishSubject;
import spark.Request;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Scanner;

import static spark.Spark.*;

public class ChatClient {

    ArrayList<String> receivers = new ArrayList<String>();

    public ChatClient(){
        PublishSubject<Request> subject = PublishSubject.create();
        subject.subscribe(this::handle_msg);
        post("/hello", (req,res) -> {subject.onNext(req); return "soiudjflkdsf";});
    }
    public void handle_msg(Request req){
        JSONObject jsonObject = null;

        try {
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(req.body());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonObject.containsKey("returnAddress")) {
            receivers.add((String) jsonObject.get("returnAddress"));
        }
        else {
            System.err.println("JSON did not include field returnAddress");
        }

        System.out.println("Message from "
                + (String) jsonObject.get("username")
                + ": "  + jsonObject.get("message"));
    }
    private void startClient() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String input = scanner.next();
            System.out.println("melding inn: "+input+"\n");
        }
    }
}

import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import rx.Observable;
import rx.subjects.PublishSubject;
import spark.Request;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.http.client.methods.*;

import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import static spark.Spark.*;

public class ChatClient {

    private CopyOnWriteArrayList<String> receivers = new CopyOnWriteArrayList<>();
    private String address;
    private String name;
    private HttpClient client;

    public ChatClient(String[] args){
        client = HttpClientBuilder.create().build();
        try {
            address = "http://" + Inet4Address.getLocalHost().getHostAddress() + ":4567/hello";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        name = args[0];
        for (int i = 1; i < args.length; i++){
            receivers.add(args[i]);
        }
        PublishSubject<Request> subject = PublishSubject.create();
        subject.subscribe(this::handle_msg);
        post("/hello", (req,res) -> {subject.onNext(req); return "krzy";});

        startClient();
    }
    private void handle_msg(Request req){
        System.out.println("start av handle_msg");
        JSONObject jsonObject = null;
        try {
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(req.body());
            System.out.println("input: " + jsonObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonObject.containsKey("returnAddress")){
            String retAddr = (String) jsonObject.get("returnAddress");
            if (!receivers.contains(retAddr)){
                receivers.add(retAddr);
            }
        }
        else {
            System.err.println("JSON did not include field returnAddress");
        }

        System.out.println("Message from "
                + (String) jsonObject.get("username")
                + ": "  + jsonObject.get("message"));
    }
    private void startClient() {
        Observable<String> observable = Observable.create(subscriber -> {
            try (BufferedReader reader = new BufferedReader( new InputStreamReader(System.in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    subscriber.onNext(line);
                }
                //subscriber.onCompleted();

            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
        observable.subscribe(x -> sendMessage(formatMessage(x)));
    }

    private JSONObject formatMessage(String input){
        JSONObject obj = new JSONObject();
        obj.put("message", input);
        obj.put("username", name);
        obj.put("returnAddress", address);

        return obj;
    }

    private void sendMessage(JSONObject obj) {
        StringEntity body = null;

        try {
            body = new StringEntity(obj.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String rec: receivers){
            System.out.println("prøver å sende til " + rec);
            HttpPost pm = new HttpPost(rec);
            pm.setEntity(body);
            try {
                client.execute(pm);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pm.releaseConnection();
            System.out.println("Har sendt til " + rec);
        }
    }
}

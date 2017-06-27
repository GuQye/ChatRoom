package hk.edu.cuhk.ie.iems5722.a1_1155080902;

/**
 * Created by wataxiwahuohuo on 2017/2/6.
 */

public class ChatMessage {
    public static final int Receive = 0;
    public static final int Sent = 1;
    private String message;
    private String time;
    private int type;

    public ChatMessage(String message, String time,int type){
        this.message = message;
        this.time = time;
        this.type = type;
    }

    public String getMessage(){
        return message;
    }

    public String getTime(){
        return time;
    }

    public  int getType(){
        return type;
    }
}

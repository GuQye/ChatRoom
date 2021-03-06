package hk.edu.cuhk.ie.iems5722.a1_1155080902;

/**
 * Created by wataxiwahuohuo on 2017/2/6.
 */

public class ChatMessage {
    public static final int Receive = 0;
    public static final int Sent = 1;
    private String message;
    private String time;
    private String name;
    private String sys_time;
    private int type;

    public ChatMessage(String message, String time, String name, int type,String sys_time){
        this.message = message;
        this.time = time;
        this.type = type;
        this.name = name;
        this.sys_time = sys_time;
    }

    public String getMessage(){
        return message;
    }

    public String getTime(){
        return time;
    }

    public String getName(){
        return name;
    }

    public  int getType(){
        return type;
    }

    public String getSys_time(){
        return sys_time;
    }
}

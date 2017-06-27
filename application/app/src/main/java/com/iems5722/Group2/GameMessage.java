package com.iems5722.Group2;

/**
 * Created by wataxiwahuohuo on 2017/2/6.
 */

public class GameMessage {

    private String message;
    private String name;
    private int type;
    private int id;

    public GameMessage(String message, int id, int type){
        this.message = message;
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public String getMessage(){
        return message;
    }


    public String getName(){
        return name;
    }

    public  int getType(){
        return type;
    }

    public int getId() {
        return id;
    }
}

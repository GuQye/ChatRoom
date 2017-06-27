package com.iems5722.Group2;

/**
 * Created by wataxiwahuohuo on 2017/2/13.
 */

public class Room {
    private int id;
    private String name;
    private int playerNums;
    public Room(int id, String name,int playerNums){
        this.id = id;
        this.name = name;
        this.playerNums = playerNums;
    }
    public int getId(){
        return id;
    }
    public int getPlayerNums(){
        return playerNums;
    }
    public String getName(){
        return name;
    }
}

package hk.edu.cuhk.ie.iems5722.a1_1155080902;

/**
 * Created by wataxiwahuohuo on 2017/2/13.
 */

public class Room {
    private int id;
    private String name;

    public Room(int id, String name){
        this.id = id;
        this.name = name;
    }
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}

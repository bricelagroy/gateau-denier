package com.coderzheaven.pack;

/**
 * Created with IntelliJ IDEA.
 * User: Noel Flantier
 * Date: 06/02/13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class Station {

    public int id;
    public String address;
    public int available;
    public int free;
    public Boolean isOpen;

    public Station(int pId, String pAddress)
    {
        address = pAddress;
        id = pId;
    }

}

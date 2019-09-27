package com.mhkarazeybek.javalearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by samsung on 20.1.2018.
 */

public class Arrays {
    public static void main(String[] args) {
        // Array
        String[] myArray = new String[3];
        int[] myNumbers = {1,2,3};

        // List
        ArrayList<String> mylist=new ArrayList<>();
        mylist.add("Muhammed");
        mylist.add("Hüseyin");
        mylist.add(1,"Karazeybek");
        System.out.println(mylist.get(1));

        // Set
        HashSet<String> mySet = new HashSet<>();
        mySet.add("Test");
        mySet.add("Test");

        System.out.println(mySet.size());

        // Map

        HashMap<String,String> myMap=new HashMap<>();

        myMap.put("name","M.Hüseyin");
        myMap.put("surname","Karazeybek");

        System.out.println(myMap.get("name"));

    }


}

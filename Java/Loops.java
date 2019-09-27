package com.mhkarazeybek.javalearning;

/**
 * Created by samsung on 20.1.2018.
 */

public class Loops {
    public static void main(String[] args) {
        int[] myNumbers ={12,15,18,21,24};
        int x =myNumbers[0]/3*5;
        System.out.println(x+"\nfor loop");
    // for loop
        for(int i=0;i<myNumbers.length ;i++){
            int y=myNumbers[i]/3*5;
            System.out.println(y);
        }
        for(int number:myNumbers){
            int z=number/3*5;
            System.out.println(z);
        }

    //While

    
    }
}

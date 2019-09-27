package com.mhkarazeybek.javalearning;

/**
 * Created by samsung on 20.1.2018.
 */

public class OperatorAndFlows {
    public static void main(String[] args) {
        int x=5;
        x+=1;
        System.out.println(x);
        /*
        * <
        * >
        * <=
        * >=
        * !=
        * &&
        * ||
        * */
        int y=7;
    // if flows

        if (x>y){
            System.out.println("x is bigger");
        }
        else if (y>x){
            System.out.println("y is bigger");
        }

    // switch-case
        String day ="Monday";
        switch (day) {
            case "Monday":
                System.out.println("Pazartesi");
                break;
            default:
                System.out.println("Hata");
        }
    //


    }


}

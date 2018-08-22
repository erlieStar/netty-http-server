package com.niodemo;


public class MyTest {


    public static void main(String[] args) {
        String result = getResult();
        System.out.println(result);
    }

    private static String getResult() {

        try {
            int num = 100 / 0;
        } catch (Exception e) {
            return "exception";
        }
        return "success";
    }

}

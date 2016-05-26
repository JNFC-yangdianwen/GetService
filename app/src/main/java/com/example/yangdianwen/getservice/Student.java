package com.example.yangdianwen.getservice;

/**
 * Created by yangdianwen on 2016/5/26.
 */
public class Student {

    private static String TAG="Student";
    String name;
     int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String tostring(){

        return this.name + this.age;
    }
}

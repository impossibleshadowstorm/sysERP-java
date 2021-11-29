package com.dsvv.syserp;

public class student {
    private String name;
    private int rno;

    public student(){}

    public student(String name, int rno) {
        this.name = name;
        this.rno = rno;
    }


    public int getRno() {
        return rno;
    }

    public String getName() {
        return name;
    }
}

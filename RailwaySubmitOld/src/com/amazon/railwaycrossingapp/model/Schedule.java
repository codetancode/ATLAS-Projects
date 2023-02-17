package com.amazon.railwaycrossingapp.model;

public class Schedule {
    public int from;
    public int to;

    public Schedule(){}
    public Schedule(int from, int to){
        this.from = Math.min(from, to);
        this.to = Math.max(from, to);
    }

    public String toString(){
        return this.from +":"+ this.to;
    }
}

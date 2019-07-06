package com.example.haikalfluzain.nowadays.model;

public class Today {
    String id,start,end,activity;

    public Today(String id, String start, String end, String activity){
        this.id = id;
        this.start = start;
        this.end = end;
        this.activity = activity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}

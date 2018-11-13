package com.lambdaschool.notetaker;

import java.io.Serializable;

public class Note implements Serializable {
    public static final int NO_ID = -1;

    private String title, content;
    private int id;

    public Note(int id, String title, String content) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public Note(String csvString) {
        String[] values = csvString.split(",");
        this.title = values[0];
        this.content = values[1];
        this.id = Integer.parseInt(values[2]);
    }

    public Note(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toCSVString() {
        return this.title.replaceAll(",","") + "," + this.content.replaceAll(",","") + "," + this.id;
    }
}

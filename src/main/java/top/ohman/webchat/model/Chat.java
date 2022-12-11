package top.ohman.webchat.model;

import java.util.Date;

public class Chat {
    private String name;

    private String content;

    private long time;

    private String type;

    public Chat() {
    }

    public Chat(String name, String content) {
        this.name = name;
        this.content = content;
        this.time = new Date().getTime();
        this.type = "message";
    }

    public Chat(String name, String content,  String type) {
        this.name = name;
        this.content = content;
        this.time = new Date().getTime();
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

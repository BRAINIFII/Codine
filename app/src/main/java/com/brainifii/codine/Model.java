package com.brainifii.codine;

public class Model {

    String id,title,desc,commd;

    public Model(String id, String title, String desc, String commd) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.commd = commd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCommd() {
        return commd;
    }

    public void setCommd(String commd) {
        this.commd = commd;
    }


}

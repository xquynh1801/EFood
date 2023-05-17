package com.example.efood.model;

import java.io.Serializable;

public class ImgComment implements Serializable {
    private int id;
    private byte[] imgPath;
    private Comment comment;

    public ImgComment() {
    }

    public ImgComment(int id, byte[] imgPath, Comment comment) {
        this.id = id;
        this.imgPath = imgPath;
        this.comment = comment;
    }

    public ImgComment(byte[] imgPath, Comment comment) {
        this.imgPath = imgPath;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImgPath() {
        return imgPath;
    }

    public void setImgPath(byte[] imgPath) {
        this.imgPath = imgPath;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}

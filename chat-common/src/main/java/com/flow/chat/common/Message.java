package com.flow.chat.common;

public class Message implements java.io.Serializable {

    private String mesType;

    private String sender;
    private String getter;
    private String con;//内容
    private String sendTime;
    private String font;
    private int size;
    private String col;//字体的颜色设置


    private String fileType;//文件类型
    private byte[] fileByte;//文件类容


    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public byte[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(byte[] b, int n) {
        fileByte = new byte[n];
        fileByte = b;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}

package com.flow.chat.client.picture;

import javax.swing.*;
import java.net.URL;

/**
 * 图片描述
 */
public class ChatPic extends ImageIcon {
    private static final long serialVersionUID = 1L;
    private int im;//图片代号

    public ChatPic(URL url, int im) {
        super(url);
        this.im = im;
    }

    public int getIm() {
        return im;
    }

    public void setIm(int im) {
        this.im = im;
    }

}

package com.flow.chat.client.plugin;

import com.flow.chat.bgd.model.User;

import javax.swing.*;

public class FriendJlabel extends JLabel {

    /**
     * id(可用userId填充)
     */
    private User user;

    public FriendJlabel(User user, Icon icon, int horizontalAlignment) {
        super(user.getName(), icon, horizontalAlignment);
        this.user = user;
    }

    public String getId() {
        return this.user.getUserId();
    }

    public User getUser() {
        return user;
    }
}

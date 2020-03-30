package com.flow.chat.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Service
public class ServerFrame extends JFrame implements ActionListener {


    //    public static void main(String[] args) {
//        new ServerFrame();
//    }
    @Autowired
    private Server server;

    private JButton start, stop;

    public ServerFrame() {
        System.out.println("aaa");
        JPanel panel = new JPanel();
        start = new JButton("启动服务器");
        start.addActionListener(this);
        stop = new JButton("关闭服务器");
        start.addActionListener(this);
        stop.addActionListener(this);
        panel.add(start);
        panel.add(stop);

        this.add(panel);
        this.setSize(500, 400);
//        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == start) {
            server.listening();//开启服务器监听
            start.setEnabled(false);
        } else if (event.getSource() == stop) {
            this.dispose();
        }

    }
}

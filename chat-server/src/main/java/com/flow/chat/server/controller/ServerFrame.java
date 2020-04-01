package com.flow.chat.server.controller;

import com.flow.chat.server.component.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Service
//@Scope("singleton")
public class ServerFrame extends JFrame implements ActionListener {
    private Logger LOGGER = LoggerFactory.getLogger(ServerFrame.class);

    //    public static void main(String[] args) {
//        new ServerFrame();
//    }


    private JButton start, stop;

    @Autowired
    public ServerFrame(TableService tableService) {
        new Server(tableService).start();
        LOGGER.info("初始化 -> ServerFrame");
        JPanel panel = new JPanel();
        start = new JButton("启动服务器");
        start.addActionListener(this);
        stop = new JButton("关闭服务器");
        stop.addActionListener(this);
//        panel.add(start);
        panel.add(stop);

        this.add(panel);
        this.setSize(500, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == start) {
//            new Server(tableService).start();
//            server.listening();//开启服务器监听
//            start.setEnabled(false);
        } else if (event.getSource() == stop) {
//            server.setOn(false);
//            this.dispose();
            System.exit(0);
        }
    }
}

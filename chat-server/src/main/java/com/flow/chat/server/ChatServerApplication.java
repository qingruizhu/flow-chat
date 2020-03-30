package com.flow.chat.server;

import com.flow.chat.server.controller.Server;
import com.flow.chat.server.util.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication(scanBasePackages = "com.flow.chat")
@MapperScan(basePackages = "com.flow.chat.bgd.mapper,com.flow.chat.server.dao")
public class ChatServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ChatServerApplication.class).headless(false).run(args);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //获取ServerFrame界面实例并显示
//                        SpringContextUtils.getBean(ServerFrame.class).setVisible(true);
                    SpringContextUtils.getBean(Server.class).listening();//开启服务器监听
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}

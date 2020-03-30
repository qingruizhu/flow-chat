package com.flow.chat.client;

import com.flow.chat.client.controller.Login;
import com.flow.chat.client.util.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication(scanBasePackages = "com.flow.chat")
@MapperScan(basePackages = "com.flow.chat.bgd.mapper,com.flow.chat.client.dao")
public class ChatClientApplication {

//    public static void main(String[] args) {
//        //SwingUtilities.invokeLater
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new Login();
//            }
//        });
//        SpringApplication.run(FlowclientApplication.class, args);
//    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(ChatClientApplication.class).headless(false).run(args);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //获取ServerFrame界面实例并显示
                    SpringContextUtils.getBean(Login.class).setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

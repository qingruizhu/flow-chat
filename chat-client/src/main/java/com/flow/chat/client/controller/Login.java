package com.flow.chat.client.controller;

import com.flow.chat.bgd.model.User;
import com.flow.chat.client.model.ClientConServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录界面
 */
@Component
public class Login extends JFrame implements ActionListener {

    public static void main(String[] args) {
        Login login = new Login();
    }

    private JTextField accoutFiled;//账号
    private JPasswordField pwdFiled;//密码
    private JButton yes, no;

    public Login() {
        /** 上 */
        JLabel uper = new JLabel(new ImageIcon("image/tou.gif"));

        /** 中 */
        JLabel account = new JLabel("账号", JLabel.CENTER);
        JLabel pwd = new JLabel("密码", JLabel.CENTER);
        accoutFiled = new JTextField();
        pwdFiled = new JPasswordField();
        //控件放入面板中
        JPanel middler = new JPanel(new GridLayout(3, 2));
        middler.add(account);
        middler.add(accoutFiled);
        middler.add(pwd);
        middler.add(pwdFiled);

        /** 下 */
        yes = new JButton(new ImageIcon("image/denglu.gif"));
        //点击登录
        yes.addActionListener(this);
        no = new JButton(new ImageIcon("image/quxiao.gif"));
        JPanel lower = new JPanel();
        lower.add(yes);
        lower.add(no);

        /** 定位 */
        this.add(uper, "North");
        this.add(middler, "Center");
        this.add(lower, "South");
        //设置
        this.setSize(350, 240);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Autowired
    ClientConServer clientConServer;

    @Override
    public void actionPerformed(ActionEvent event) {
        //用户点击登录后
        if (event.getSource() == yes) {
            User user = new User();
            String selfId = accoutFiled.getText().trim();
            user.setUserId(selfId);
            user.setPassword(new String(pwdFiled.getPassword()));
            if (clientConServer.sendLoginInfoToServer(user)) {
                //成功登录
                //发送:请求在线好友列表
//               try {
////                   ObjectOutputStream oos = new ObjectOutputStream(ManagerClientThread.get(selfId).getSocket().getOutputStream());
////                   Message message = new Message();
////                   message.setSender(selfId);
////                   message.setMesType(MessageType.message_get_onLineFriend);
////                   oos.writeObject(message);
////               } catch (IOException e) {
////                   e.printStackTrace();
////               }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误!");
            }

        }

    }
}

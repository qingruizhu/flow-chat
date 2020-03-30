package com.flow.chat.client.controller;

import com.flow.chat.client.picture.ChatPic;
import com.flow.chat.client.picture.PicsJWindow;
import com.flow.chat.client.util.ManagerClientThread;
import com.flow.chat.common.Message;
import com.flow.chat.common.MessageType;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 设置窗口关闭按钮点击后的默认操作, 参考值:
 * WindowConstants.DO_NOTHING_ON_CLOSE: 不执行任何操作。
 * WindowConstants.HIDE_ON_CLOSE: 隐藏窗口（不会结束进程）, 再次调用 setVisible(true) 将再次显示。
 * WindowConstants.DISPOSE_ON_CLOSE: 销毁窗口, 如果所有可显示的窗口都被 DISPOSE, 则可能会自动结束进程。
 * WindowConstants.EXIT_ON_CLOSE: 退出进程。
 */

/**
 * 好友聊天界面
 */
public class Chat extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    public static void main(String[] args) {
        Chat qqChat = new Chat("1", "zqr", "zhuqingrui");
    }

    private String selfId, friendId, friendName;
    private Point newPoint, oldPoint;//坐标
    private int dragEnabled;//拖拽
    /**
     * 第一部分
     */
    private JButton exit_button;//退出✖️按钮
    private JButton transFile_button;//传输文件按钮
    /**
     * 第二部分
     */
    private JTextPane display_textPane;//显示窗格
    private StyledDocument display_stydoc;//显示窗格的样式文档
    private Color text_color = new Color(255, 255, 255);//字体颜色

    /**
     * 第三部分
     */
    private JButton biaoqing_button;//表情按钮
    private JTextPane input_textPane;//输入窗格
    private StyledDocument input_stydoc;//输入窗格的样式文档
    private JButton close_button, send_button;//关闭按钮,发送按钮

    private List<PicInfo> myPicInfo = new LinkedList<PicInfo>();
    private List<PicInfo> receivdPicInfo = new LinkedList<PicInfo>();


    public Chat(String selfId, String friendId, String friendName) {
        this.selfId = selfId;
        this.friendId = friendId;
        this.friendName = friendName;
        this.text_color = Color.BLACK;
        this.setSize(728, 533);
        this.setUndecorated(true);
        getContentPane().setLayout(null);

//        sendContent = new JTextField(15);
//        sendButton = new JButton("发送");
//        sendButton.addActionListener(this);//监听发送按钮
//        JPanel panel = new JPanel();
//        panel.add(sendContent);
//        panel.add(sendButton);
/** 第一部分 */
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 0, 729, 92);
        getContentPane().add(panel_1);
        panel_1.setLayout(null);
        //头像
//        JLabel lb_touxiang = new JLabel(new ImageIcon("image/dialogimage/huisetouxiang.png"));
        JLabel lb_touxiang = new JLabel(new ImageIcon("image/Q.png"));
        lb_touxiang.setBounds(10, 10, 42, 42);
        panel_1.add(lb_touxiang);
        //姓名
        JLabel friendname = new JLabel(this.friendName);
        friendname.setBounds(62, 13, 105, 20);
        panel_1.add(friendname);
        //右上角边框->关闭
        exit_button = new JButton(new ImageIcon("image/dialogimage/dexit.jpg"));
        exit_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_button.addActionListener(this);
        exit_button.setBounds(698, 0, 30, 30);
        panel_1.add(exit_button);
        //右上角边框->缩小
        JButton button_min = new JButton(new ImageIcon("image/dialogimage/dmin.jpg"));
        button_min.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setExtendedState(JFrame.ICONIFIED);
            }
        });
        button_min.setBounds(640, 0, 30, 30);
        panel_1.add(button_min);
        //右上角边框->放大
        JButton button_max = new JButton(new ImageIcon("image/dialogimage/dmax.jpg"));
        button_max.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        button_max.setBounds(669, 0, 30, 30);
        panel_1.add(button_max);
        //文件传输
        transFile_button = new JButton(new ImageIcon("image/dialogimage/tranfile.jpg"));
        transFile_button.setBounds(102, 62, 36, 30);
        transFile_button.addActionListener(this);
        panel_1.add(transFile_button);

/** 第二部分 */
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(0, 91, 446, 259);
        getContentPane().add(panel_2);
        panel_2.setLayout(null);
        //文本面板
        display_textPane = new JTextPane();
        display_textPane.setBounds(0, 0, 446, 259);
        display_stydoc = display_textPane.getStyledDocument();
        //滚动面板
        JScrollPane scrollPane = new JScrollPane(display_textPane);
        scrollPane.setBounds(0, 0, 446, 259);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel_2.add(scrollPane);

/** 第三部分 */
        JPanel panel_3 = new JPanel();
        panel_3.setBounds(0, 385, 446, 148);
        getContentPane().add(panel_3);
        panel_3.setLayout(null);
        //表情按钮
        biaoqing_button = new JButton(new ImageIcon("image/dialogimage/biaoqing.png"));
        biaoqing_button.setBounds(39, 11, 30, 30);
        biaoqing_button.addMouseListener(this);
        panel_3.add(biaoqing_button);
        //输入窗
        input_textPane = new JTextPane();
        input_textPane.setBounds(0, 44, 446, 53);
        input_stydoc = input_textPane.getStyledDocument();
        panel_3.add(input_textPane);
        //关闭+发送按钮
        close_button = new JButton(new ImageIcon("image/dialogimage/close.jpg"));
        close_button.setBounds(307, 114, 64, 24);
        panel_3.add(close_button);
        send_button = new JButton(new ImageIcon("image/dialogimage/send.jpg"));
        send_button.addActionListener(this);
        send_button.setBounds(381, 114, 64, 24);
        panel_3.add(send_button);


//        textArea = new JTextArea();
//        textArea.setSize(200,100);
//        this.add(textArea, "Center");
//        this.add(panel, "South");
//        this.setTitle(selfId + " 正在和 " + friendId + " 聊天");
//        this.setIconImage((new ImageIcon("image/qq.gif").getImage()));
//        this.setSize(300, 200);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        //拖拽功能
        panel_1.addMouseMotionListener(new MouseMotionListener() {
            int old_x;
            int old_y;

            public void mouseDragged(MouseEvent e) {
                Chat.this.setLocation(Chat.this.getLocation().x + e.getX() - old_x,
                        Chat.this.getLocation().y + e.getY() - old_y);
            }

            public void mouseMoved(MouseEvent e) {
                old_x = e.getX();
                old_y = e.getY();
            }

        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == exit_button) {
//            ManagerChat.remove(this.selfId + " " + this.friendId);
//            this.dispose();
            this.setVisible(false);
        } else if (event.getSource() == send_button) {
            /** 1.发送信息 */
            String pic = buildPicInfo();
            Message message = new Message();
            message.setMesType(MessageType.message_comm_mes);
            message.setSender(this.selfId);
            message.setGetter(this.friendId);
            //	JOptionPane.showMessageDialog(this,jtf.getText()+"*"+buildPicInfo());
            message.setCon(input_textPane.getText());
            message.setCol(this.text_color.toString());
//            message.setFont(cd_ziti.getSelectedItem().toString());
//            message.setSize(Integer.parseInt(cd_size.getSelectedItem().toString()));
            DateFormat ddtf = DateFormat.getDateTimeInstance();
            message.setSendTime(ddtf.format(new Date()));
            this.showOwnMessage(message);
            message.setCon(input_textPane.getText() + "*" + pic);
            this.input_textPane.setText("");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(ManagerClientThread.get(selfId).getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == transFile_button) {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.showOpenDialog(this);
            File file = fileDialog.getSelectedFile();
            String type = file.getName().substring(file.getName().indexOf(".") + 1);
            //JOptionPane.showMessageDialog(this,type);
            Message m = new Message();
            m.setMesType(MessageType.message_sendfile);
            m.setSender(this.selfId);
            m.setGetter(this.friendId);
            m.setFileType(type);
            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                int size = dataInputStream.available();
                byte[] by = new byte[size];
                dataInputStream.read(by);
                dataInputStream.close();
                m.setFileByte(by, size);
                m.setSendTime(new Date().toString());
                ObjectOutputStream oos = new ObjectOutputStream
                        (ManagerClientThread.get(selfId).getSocket().getOutputStream());
                oos.writeObject(m);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }

    }

    /**
     * 显示接收到的消息
     */
    public void showMessage(Message message) {
        if (StringUtils.isEmpty(message.getCon())) {
            return;
        }
        String info = friendName + " " + message.getSendTime() + " :\n";
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrset, 14);
        StyleConstants.setForeground(attrset, Color.green);
        StyleConstants.setFontFamily(attrset, "仿宋");
        Document docs = display_textPane.getDocument();
        try {
            docs.insertString(docs.getLength(), info, attrset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        info = message.getCon();
        int index = info.lastIndexOf("*");
//        StyleConstants.setFontSize(attrset, message.getSize());
        String text_color = message.getCol();
        if (null == text_color) {
            text_color = Color.BLACK.toString();
        }
        StyleConstants.setForeground(attrset, recolor(text_color.toString()));
//        StyleConstants.setFontFamily(attrset, message.getFont());
        docs = display_textPane.getDocument();
        pos1 = docs.getLength();
        if (index > 0 && index < info.length() - 1) {
            try {
                docs.insertString(docs.getLength(), info.substring(0, index) + "\r\n", attrset);
                //       jta.setCaretPosition(docChat.getLength()); // 设置滚动到最下边
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            receivedPicInfo(info.substring(index + 1, info.length()));
            insertPics(true);
        } else {
            try {
                docs.insertString(docs.getLength(), info.substring(0, index) + "\r\n", attrset);
                //      jta.setCaretPosition(docChat.getLength()); // 设置滚动到最下边
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 显示发送的消息
     */
    public void showOwnMessage(Message m) {
        if (StringUtils.isEmpty(m.getCon())) {
            return;
        }
        String info = "我 " + " " + m.getSendTime() + " :\r\n";
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrset, 14);
        StyleConstants.setForeground(attrset, Color.orange);
        StyleConstants.setFontFamily(attrset, "仿宋");
        Document docs = display_textPane.getDocument();
        try {
            docs.insertString(docs.getLength(), info, attrset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        info = m.getCon() + "\r\n";
//        StyleConstants.setFontSize(attrset, m.getSize());
        String text_color = m.getCol();
        if (null == text_color) {
            text_color = Color.BLACK.toString();
        }
        StyleConstants.setForeground(attrset, recolor(text_color.toString()));
//        StyleConstants.setFontFamily(attrset, m.getFont());
        docs = display_textPane.getDocument();
        pos2 = docs.getLength();
        try {
            docs.insertString(docs.getLength(), info, attrset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        insertPics(false);
    }

    public Color recolor(String s) {
        String friend[] = s.split(",");
        String friend1[] = new String[friend.length];
        int j;
        for (int i = 0; i < friend.length; i++) {
            j = friend[i].lastIndexOf("=");
            friend1[i] = friend[i].substring(j + 1);
        }
        friend1[2] = friend1[2].substring(0, friend1[2].length() - 1);
        return new Color(Integer.parseInt(friend1[0]), Integer.parseInt(friend1[1]), Integer.parseInt(friend1[2]));
    }

    /**
     * 重组收到的表情信息串
     */
    private void receivedPicInfo(String picInfos) {
        String[] infos = picInfos.split("[+]");
        for (int i = 0; i < infos.length; i++) {
            String[] tem = infos[i].split("[&]");
            if (tem.length == 2) {
                PicInfo pic = new PicInfo(Integer.parseInt(tem[0]), tem[1]);
                receivdPicInfo.add(pic);
            }
        }
    }

    /**
     * 插入图片
     *
     * @param isFriend 是否为朋友发过来的消息
     */
    int pos1;
    int pos2;
    private JButton sendfile;
    private JButton shake;

    private void insertPics(boolean isFriend) {
        if (isFriend) {
            if (this.receivdPicInfo.size() <= 0) {
                return;
            } else {
                for (int i = 0; i < receivdPicInfo.size(); i++) {
                    PicInfo pic = receivdPicInfo.get(i);
                    String fileName;
                    display_textPane.setCaretPosition(pos1 + pic.getPos()); /*设置插入位置*/
                    fileName = "face/" + pic.getVal() + ".gif";/*修改图片路径*/
                    display_textPane.insertIcon(new ImageIcon(PicsJWindow.class.getResource(fileName))); /*插入图片*/
                    /*					jpChat.updateUI();*/
                }
                receivdPicInfo.clear();
            }
        } else {

            if (myPicInfo.size() <= 0) {
                return;
            } else {
                for (int i = 0; i < myPicInfo.size(); i++) {
                    PicInfo pic = myPicInfo.get(i);

                    display_textPane.setCaretPosition(pos2 + pic.getPos()); /*设置插入位置*/
                    String fileName;
                    fileName = "face/" + pic.getVal() + ".gif";/*修改图片路径*/
                    display_textPane.insertIcon(new ImageIcon(PicsJWindow.class.getResource(fileName))); /*插入图片*/
                    /*jpChat.updateUI();*/
                }
                myPicInfo.clear();
            }
        }
        display_textPane.setCaretPosition(display_stydoc.getLength()); /*设置滚动到最下边*/
        //insert(new FontAttrib()); /*这样做可以换行*/
    }

    /**
     * 插入图片
     */
    public void insertSendPic(ImageIcon imgIc) {
        //jpMsg.setCaretPosition(docChat.getLength()); // 设置插入位置
        input_textPane.insertIcon(imgIc); // 插入图片
        //	System.out.print(imgIc.toString());
        //insert(new FontAttrib()); // 这样做可以换行
    }

    public JButton get_Biaoqing_button() {
        return biaoqing_button;
    }

    //实现JFrame的拉动
    public void mouseDragged(MouseEvent e) {
        if (dragEnabled == 0) {
            return;
        }
        oldPoint = newPoint;
        newPoint = e.getPoint();
        if (dragEnabled == 2) {
            this.setSize(new Dimension(this.getSize().width + newPoint.x
                    - oldPoint.x, this.getSize().height));
        }
        if (dragEnabled == 3) {
            this.setSize(new Dimension(this.getSize().width,
                    this.getSize().height + newPoint.y - oldPoint.y));
        }
        if (dragEnabled == 5) {
            this.setSize(new Dimension(this.getSize().width + newPoint.x
                    - oldPoint.x, this.getSize().height + newPoint.y
                    - oldPoint.y));
        }
    }

    public void mouseMoved(MouseEvent e) {
        Dimension ds = this.getSize();
        // 北：1，东：2，南：3，西：4
        if (ds.height - e.getY() > 0 && ds.height - e.getY() <= 5
                && ds.width - e.getX() > 0 && ds.width - e.getX() <= 5) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            dragEnabled = 5;
            newPoint = e.getPoint();
        } else if (ds.width - e.getX() > 0 && ds.width - e.getX() <= 5) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            dragEnabled = 2;
            newPoint = e.getPoint();
        } else if (ds.height - e.getY() > 0 && ds.height - e.getY() <= 5) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            dragEnabled = 3;
            newPoint = e.getPoint();
        } else {
            this.setCursor(Cursor.getDefaultCursor());
            dragEnabled = 0;
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class PicInfo {
        /* 图片信息*/
        int pos;
        String val;

        public PicInfo(int pos, String val) {
            this.pos = pos;
            this.val = val;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

    }


    /**
     * 重组发送的表情信息
     *
     * @return 重组后的信息串  格式为   位置|代号+位置|代号+……
     */
    private String buildPicInfo() {
        StringBuilder sb = new StringBuilder("");
        //遍历jtextpane找出所有的图片信息封装成指定格式
        for (int i = 0; i < this.input_textPane.getText().length(); i++) {
            if (input_stydoc.getCharacterElement(i).getName().equals("icon")) {
                //ChatPic = (ChatPic)
                Icon icon = StyleConstants.getIcon(input_textPane.getStyledDocument().getCharacterElement(i).getAttributes());
                ChatPic cupic = (ChatPic) icon;
                PicInfo picInfo = new PicInfo(i, cupic.getIm() + "");
                myPicInfo.add(picInfo);
                sb.append(i + "&" + cupic.getIm() + "+");
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }


}

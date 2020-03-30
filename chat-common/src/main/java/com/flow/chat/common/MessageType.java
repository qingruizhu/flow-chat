/**
 * �����������
 */
package com.flow.chat.common;

public interface MessageType {

    String message_succeed = "1";//登录成功
    String message_login_fail = "2";//登录失败
    String message_comm_mes = "3";//普通消息
    String message_get_onLineFriend = "4";//获取好友列表
    String message_ret_onLineFriend = "5";//返回好友列表
    String message_login_out = "7";//退出登录
    String message_friend_login_out = "8";//退出登录
    String message_sendfile = "9";//发送文件
    String message_ret_outlineMessage = "10";//返回离线记录
}

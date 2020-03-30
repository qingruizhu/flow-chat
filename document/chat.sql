/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50638
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50638
 File Encoding         : 65001

 Date: 30/03/2020 18:46:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender` varchar(16) DEFAULT NULL COMMENT '发送者userId',
  `getter` varchar(16) DEFAULT NULL COMMENT '接收者userId',
  `content` varchar(512) DEFAULT NULL COMMENT '消息内容',
  `send_time` varchar(32) DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for on_line
-- ----------------------------
DROP TABLE IF EXISTS `on_line`;
CREATE TABLE `on_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) DEFAULT NULL,
  `online` tinyint(1) DEFAULT NULL COMMENT '0-离线；1-在线',
  `login_time` varchar(32) DEFAULT '' COMMENT '登录时间',
  `logout_time` varchar(32) DEFAULT '' COMMENT '退出时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_unique` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of on_line
-- ----------------------------
BEGIN;
INSERT INTO `on_line` VALUES (7, '123', 0, '2020-03-30 17:05:08', '2020-03-30 17:06:10');
INSERT INTO `on_line` VALUES (8, '1234', 0, '2020-03-30 17:05:32', '2020-03-30 17:06:16');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, '123', '小明', '123', 1);
INSERT INTO `user` VALUES (2, '1234', '小红', '1234', 0);
COMMIT;

-- ----------------------------
-- Table structure for user_friend
-- ----------------------------
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend` (
  `id` bigint(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `friend_id` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user_friend
-- ----------------------------
BEGIN;
INSERT INTO `user_friend` VALUES (1, '123', '1234');
INSERT INTO `user_friend` VALUES (2, '1234', '123');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat MySQL Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : book_ticket

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 25/02/2021 01:43:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for line
-- ----------------------------
DROP TABLE IF EXISTS `line`;
CREATE TABLE `line`  (
  `line_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `line_start_station_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '起始站点',
  `line_end_station_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '到达站点',
  PRIMARY KEY (`line_id`) USING BTREE,
  UNIQUE INDEX `unique`(`line_start_station_name`, `line_end_station_name`) USING BTREE,
  INDEX `end_station`(`line_start_station_name`) USING BTREE,
  INDEX `start_station`(`line_end_station_name`) USING BTREE,
  CONSTRAINT `end_station` FOREIGN KEY (`line_start_station_name`) REFERENCES `station` (`station_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `start_station` FOREIGN KEY (`line_end_station_name`) REFERENCES `station` (`station_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of line
-- ----------------------------
INSERT INTO `line` VALUES (26, '三亚市', '广州市');
INSERT INTO `line` VALUES (7, '哈尔滨市', '广州市');
INSERT INTO `line` VALUES (4, '广州市', '上海市');
INSERT INTO `line` VALUES (24, '广州市', '中山市');
INSERT INTO `line` VALUES (9, '广州市', '哈尔滨市');
INSERT INTO `line` VALUES (25, '广州市', '汕头市');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_user_id` int(10) NOT NULL COMMENT '用户编号',
  `order_trips_id` int(10) NOT NULL COMMENT '车次编号',
  `order_create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `order_update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `order_status` int(4) NOT NULL COMMENT '订单状态（0：创建，1：已发车，2：退票，3：改签）',
  `order_passenger_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '乘车人姓名',
  `order_linkman_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `order_linkman_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机号',
  `order_passenger_identity_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '乘车人身份证号码',
  `order_seat_level` int(2) NOT NULL COMMENT '订购的坐席（一等座/二等座，分别为1，2）',
  `order_price` float(7, 2) NOT NULL COMMENT '订单金额',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `user`(`order_user_id`) USING BTREE,
  INDEX `trips`(`order_trips_id`) USING BTREE,
  CONSTRAINT `trips` FOREIGN KEY (`order_trips_id`) REFERENCES `trips` (`trips_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`order_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2258 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1753, 25, 3, '2021-02-23 22:55:41', '2021-02-23 23:27:52', 2, '冲', '马先生', '15914967057', '440582199806065917', 2, 300.50);
INSERT INTO `orders` VALUES (1755, 25, 3, '2021-02-23 22:55:41', '2021-02-23 23:27:56', 3, '冲', '马先生', '15914967057', '440582199806065917', 2, 300.50);
INSERT INTO `orders` VALUES (1773, 25, 3, '2021-02-23 22:55:41', '2021-02-23 23:28:00', 2, '冲', '马先生', '15914967057', '440582199806065917', 2, 300.50);
INSERT INTO `orders` VALUES (2253, 25, 29, '2021-02-23 23:27:56', '2021-02-23 23:27:59', 2, '冲', '马先生', '15914967057', '440582199806065917', 2, 600.80);
INSERT INTO `orders` VALUES (2254, 25, 28, '2021-02-24 01:14:38', '2021-02-24 01:14:41', 2, '马先生', '马先生', '15914967057', '440582199806065921', 1, 600.50);
INSERT INTO `orders` VALUES (2255, 25, 29, '2021-02-24 01:14:53', '2021-02-24 01:15:00', 3, '冲', '马先生', '15914967057', '440582199806065917', 1, 888.80);
INSERT INTO `orders` VALUES (2256, 25, 3, '2021-02-24 01:15:00', '2021-02-24 01:15:03', 2, '冲', '马先生', '15914967057', '440582199806065917', 1, 400.00);
INSERT INTO `orders` VALUES (2257, 25, 3, '2021-02-24 22:25:23', NULL, 0, '马先生', '马先生', '15914967057', '440582199806065921', 1, 400.00);

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `station_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `station_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '站名',
  `station_adress` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`station_id`) USING BTREE,
  UNIQUE INDEX `station_name`(`station_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of station
-- ----------------------------
INSERT INTO `station` VALUES (1, '广州市', '广东省广州市天河区');
INSERT INTO `station` VALUES (2, '上海市', '上海市');
INSERT INTO `station` VALUES (4, '哈尔滨市', '黑龙江省哈尔滨市');
INSERT INTO `station` VALUES (15, '北京市', '北京市');
INSERT INTO `station` VALUES (16, '汕头市', '广东省汕头市潮阳区');
INSERT INTO `station` VALUES (17, '深圳市', '广东省深圳市');
INSERT INTO `station` VALUES (18, '中山市', '广东省中山市');
INSERT INTO `station` VALUES (19, '天津市', '天津市');
INSERT INTO `station` VALUES (20, '重庆市', '重庆市');
INSERT INTO `station` VALUES (21, '三亚市', '海南省三亚市');
INSERT INTO `station` VALUES (22, '长沙市', '湖南省长沙市');

-- ----------------------------
-- Table structure for train
-- ----------------------------
DROP TABLE IF EXISTS `train`;
CREATE TABLE `train`  (
  `train_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `train_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列车名称，类似G6340',
  `train_speed` float(4, 1) NULL DEFAULT NULL COMMENT '列车的最大速度',
  `train_seat_num` int(4) NOT NULL COMMENT '列车的座位数量',
  PRIMARY KEY (`train_id`) USING BTREE,
  UNIQUE INDEX `train_name`(`train_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train
-- ----------------------------
INSERT INTO `train` VALUES (2, 'G789', 306.7, 759);
INSERT INTO `train` VALUES (6, 'T300', 300.5, 1000);
INSERT INTO `train` VALUES (13, 'T456', 300.6, 1000);
INSERT INTO `train` VALUES (14, 'T784', 255.6, 800);

-- ----------------------------
-- Table structure for trips
-- ----------------------------
DROP TABLE IF EXISTS `trips`;
CREATE TABLE `trips`  (
  `trips_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `trips_line_id` int(10) NOT NULL COMMENT '线路编号',
  `trips_train_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列车名称',
  `trips_start_time` datetime(0) NOT NULL COMMENT '出发时间',
  `trips_end_time` datetime(0) NOT NULL COMMENT '到达时间',
  `trips_first_seat_num` int(4) NOT NULL COMMENT '一等座剩余座位数量',
  `trips_second_seat_num` int(4) NOT NULL COMMENT '二等座剩余座位数量',
  `trips_first_seat_price` float(6, 2) NOT NULL COMMENT '一等座票价',
  `trips_second_seat_price` float(6, 2) NOT NULL COMMENT '二等座票价',
  `trips_delete_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除（0 未删除、1 删除）',
  PRIMARY KEY (`trips_id`) USING BTREE,
  INDEX `line`(`trips_line_id`) USING BTREE,
  INDEX `train`(`trips_train_name`) USING BTREE,
  CONSTRAINT `line` FOREIGN KEY (`trips_line_id`) REFERENCES `line` (`line_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `train` FOREIGN KEY (`trips_train_name`) REFERENCES `train` (`train_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trips
-- ----------------------------
INSERT INTO `trips` VALUES (3, 4, 'G789', '2021-02-24 23:00:00', '2021-02-24 00:00:00', 0, 3, 400.00, 300.50, 1);
INSERT INTO `trips` VALUES (28, 4, 'G789', '2021-03-01 15:44:35', '2021-03-01 20:44:55', 1000, 10000, 600.50, 500.00, 0);
INSERT INTO `trips` VALUES (29, 4, 'T456', '2021-03-01 10:46:53', '2021-03-01 14:46:46', 1000, 88887, 888.80, 600.80, 0);
INSERT INTO `trips` VALUES (30, 4, 'T300', '2021-03-01 14:48:10', '2021-03-01 18:48:14', 200, 101, 600.00, 500.00, 0);
INSERT INTO `trips` VALUES (31, 4, 'T456', '2021-03-01 14:57:56', '2021-03-01 14:58:00', 500, 100, 600.00, 500.00, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_login_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登陆名',
  `user_password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码（md5存储+盐）',
  `user_salt` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐',
  `user_sex` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `user_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `user_email` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `user_birth` date NULL DEFAULT NULL COMMENT '生日',
  `user_identity_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_login_name`(`user_login_name`) USING BTREE COMMENT '提高查找用户的速度'
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (24, 'ma', 'b0247e11c3c4676fe7382bab7df76f99', 'f42ecd7a95c6d82aac6dda8657048312', '男', '15913967085', '1908739556@qq.com', '2021-02-19', '440582199002013475');
INSERT INTO `user` VALUES (25, 'GDPU', 'f396d2e1652f041a770531f8de2b9e6d', '0bc25ed9ff7ad7b8eeca8a6b25bbfa07', '男', '15913068084', '1908739556@qq.com', '2021-02-28', '440582199708085830');

SET FOREIGN_KEY_CHECKS = 1;

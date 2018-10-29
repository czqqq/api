/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50620
 Source Host           : localhost:3306
 Source Schema         : app

 Target Server Type    : MySQL
 Target Server Version : 50620
 File Encoding         : 65001

 Date: 29/10/2018 21:38:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for commission
-- ----------------------------
DROP TABLE IF EXISTS `commission`;
CREATE TABLE `commission`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commission` double NOT NULL COMMENT '佣金',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '佣金表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for commission_detail
-- ----------------------------
DROP TABLE IF EXISTS `commission_detail`;
CREATE TABLE `commission_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `comeby` bigint(20) NOT NULL COMMENT '产生佣金的用户ID',
  `commission` double NOT NULL COMMENT '佣金',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '\n创建时间',
  `mt` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '佣金明细表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '\n用户ID',
  `total_price` double NOT NULL COMMENT '\n订单金额',
  `status` tinyint(4) NOT NULL COMMENT '\n0:待支付;1:已支付',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '\n创建时间',
  `mt` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `trade_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `order_id` bigint(20) NOT NULL COMMENT '\n订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `count` int(10) NOT NULL COMMENT '产品数量',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '\n创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单明细表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID\n\n\n',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称\n\n\n',
  `type` bigint(20) NULL DEFAULT NULL COMMENT '产品种类ID\n\n\n',
  `price` double NOT NULL COMMENT '单价',
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片路径',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `mt` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for product_type
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品种类名称',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `mt` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品类型表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `number` int(10) NOT NULL COMMENT '\n库存数量',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '\n0:上架;1:下架',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '\n创建时间',
  `mt` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库存表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `a` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号码/登录账号',
  `pwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名/昵称',
  `pid` bigint(20) NOT NULL COMMENT '上级ID',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '用户级别',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '13333333333', '123', '菜头', 0, 0, '2018-09-15 13:05:42');
INSERT INTO `user` VALUES (2, '16666666666', '123', '坤妞', 1, 0, '2018-09-19 23:50:37');
INSERT INTO `user` VALUES (3, '18888888888', '123', '小红', 1, 0, '2018-09-19 23:51:15');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `rec_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '\n收货手机号码',
  `rec_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '\n收货地址',
  `rec_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '\n收货人',
  `isDefault` tinyint(4) NOT NULL COMMENT '0:默认地址；1:其他',
  `ct` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `mt` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户收货地址表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for withdraw
-- ----------------------------
DROP TABLE IF EXISTS `withdraw`;
CREATE TABLE `withdraw`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '\n用户ID',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '转账方式，如“支付宝”',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '\n联系方式',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '\n转账账号',
  `money` double NOT NULL COMMENT '\n提现金额',
  `status` tinyint(4) NOT NULL COMMENT '0:待处理;1:已处理',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = ' 提现表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

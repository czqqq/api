/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : app

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2018-09-15 13:06:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for commission
-- ----------------------------
DROP TABLE IF EXISTS `commission`;
CREATE TABLE `commission` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commission` double NOT NULL COMMENT '佣金',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='佣金表';

-- ----------------------------
-- Records of commission
-- ----------------------------

-- ----------------------------
-- Table structure for commission_detail
-- ----------------------------
DROP TABLE IF EXISTS `commission_detail`;
CREATE TABLE `commission_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `comeby` bigint(20) NOT NULL COMMENT '产生佣金的用户ID',
  `commission` double NOT NULL COMMENT '佣金',
  `ct` datetime DEFAULT NULL COMMENT '\n创建时间',
  `mt` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='佣金明细表';

-- ----------------------------
-- Records of commission_detail
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '\n用户ID',
  `total_price` double NOT NULL COMMENT '\n订单金额',
  `status` tinyint(4) NOT NULL COMMENT '\n0:待支付;1:已支付',
  `ct` datetime DEFAULT NULL COMMENT '\n创建时间',
  `mt` datetime DEFAULT NULL COMMENT '修改时间',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `trade_number` varchar(255) DEFAULT NULL COMMENT '交易号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `order_id` bigint(20) NOT NULL COMMENT '\n订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `count` int(10) NOT NULL COMMENT '产品数量',
  `ct` datetime DEFAULT NULL COMMENT '\n创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单明细表';

-- ----------------------------
-- Records of order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID\n\n\n',
  `name` varchar(255) NOT NULL COMMENT '产品名称\n\n\n',
  `type` bigint(20) DEFAULT NULL COMMENT '产品种类ID\n\n\n',
  `price` double NOT NULL COMMENT '单价',
  `pic` varchar(255) NOT NULL COMMENT '图片路径',
  `ct` datetime DEFAULT NULL COMMENT '创建时间',
  `mt` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';

-- ----------------------------
-- Records of product
-- ----------------------------

-- ----------------------------
-- Table structure for product_type
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(255) NOT NULL COMMENT '产品种类名称',
  `ct` datetime DEFAULT NULL COMMENT '创建时间',
  `mt` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品类型表';

-- ----------------------------
-- Records of product_type
-- ----------------------------

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `number` int(10) NOT NULL COMMENT '\n库存数量',
  `status` tinyint(4) DEFAULT NULL COMMENT '\n0:上架;1:下架',
  `ct` datetime DEFAULT NULL COMMENT '\n创建时间',
  `mt` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存表';

-- ----------------------------
-- Records of stock
-- ----------------------------

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `a` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `mobile` varchar(20) NOT NULL COMMENT '手机号码/登录账号',
  `pwd` varchar(20) NOT NULL COMMENT '登录密码',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名/昵称',
  `ct` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '13333333333', '123', '菜头', '2018-09-15 13:05:42');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `rec_mobile` varchar(20) NOT NULL COMMENT '\n收货手机号码',
  `rec_address` varchar(255) NOT NULL COMMENT '\n收货地址',
  `rec_name` varchar(20) NOT NULL COMMENT '\n收货人',
  `isDefault` tinyint(4) NOT NULL COMMENT '0:默认地址；1:其他',
  `ct` datetime DEFAULT NULL COMMENT '创建时间',
  `mt` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收货地址表';

-- ----------------------------
-- Records of user_address
-- ----------------------------

-- ----------------------------
-- Table structure for withdraw
-- ----------------------------
DROP TABLE IF EXISTS `withdraw`;
CREATE TABLE `withdraw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '\n自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '\n用户ID',
  `type` varchar(255) NOT NULL COMMENT '转账方式，如“支付宝”',
  `mobile` varchar(255) NOT NULL COMMENT '\n联系方式',
  `account` varchar(255) NOT NULL COMMENT '\n转账账号',
  `money` double NOT NULL COMMENT '\n提现金额',
  `status` tinyint(4) NOT NULL COMMENT '0:待处理;1:已处理',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' 提现表';

-- ----------------------------
-- Records of withdraw
-- ----------------------------

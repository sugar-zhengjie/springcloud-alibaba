-- ----------------------------
-- Table structure for t_order_0
-- ----------------------------
DROP TABLE IF EXISTS `t_order_0`;
CREATE TABLE `t_order_0` (
                             `id` int(11) NOT NULL,
                             `order_id` int(11) DEFAULT NULL,
                             `desc` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of t_order_0
-- ----------------------------
INSERT INTO `t_order_0` VALUES ('1', '12', '老胡爱大米');
-- ----------------------------
-- Table structure for t_order_1
-- ----------------------------
DROP TABLE IF EXISTS `t_order_1`;
CREATE TABLE `t_order_1` (
                             `id` int(11) NOT NULL,
                             `order_id` int(11) DEFAULT NULL,
                             `desc` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
-- ----------------------------
-- Records of t_order_1
-- ----------------------------
INSERT INTO `t_order_1` VALUES ('2', '11', '老王也爱大米');
-- ----------------------------
-- Table structure for t_order_item_0
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_0`;
CREATE TABLE `t_order_item_0` (
                                  `id` int(11) NOT NULL,
                                  `order_id` int(11) DEFAULT NULL,
                                  `desc` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of t_order_item_0
-- ----------------------------
INSERT INTO `t_order_item_0` VALUES ('1', '12', '老胡爱榻榻米');
-- ----------------------------
-- Table structure for t_order_item_1
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_1`;
CREATE TABLE `t_order_item_1` (
                                  `id` int(11) NOT NULL,
                                  `order_id` int(11) DEFAULT NULL,
                                  `desc` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
-- ----------------------------
-- Records of t_order_item_1
-- ----------------------------
INSERT INTO `t_order_item_1` VALUES ('2', '11', '老王爱偷女邻居大米');
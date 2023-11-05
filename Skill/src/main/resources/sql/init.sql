DROP TABLE IF EXISTS `skill_goods`;
CREATE TABLE `skill_goods` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
    `price` decimal(10,2) NULL DEFAULT NULL COMMENT '原价格',
    `cost_price` decimal(10,2) NULL DEFAULT NULL COMMENT '秒杀价格',
    `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态，0未审核，1审核通过，2审材未通过',
    `num` int(11) NULL DEFAULT NULL COMMENT '秒杀商品数',
    `stock_count` int(11) NULL DEFAULT NULL COMMENT '剩余库存数',
    `introduction` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `skill_order`;
CREATE TABLE `skill_order` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `skill_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀商品ID',
    `money` decimal(10,2) NULL DEFAULT NULL COMMENT '支付金额',
    `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
    `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态，0未支付，1已支付',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC;

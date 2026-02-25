/*
 Navicat Premium Data Transfer

 Source Server         : 47.98.37.172
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : 47.98.37.172:3309
 Source Schema         : textbook_trading

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 25/02/2026 13:45:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', NULL, 1, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `admin` VALUES (2, 'content_admin', 'e10adc3949ba59abbe56e057f20f883e', '内容管理员-张伟', 'https://i.pravatar.cc/150?img=51', 2, 1, '2026-01-10 10:00:00', '2026-01-26 20:59:24');
INSERT INTO `admin` VALUES (3, 'order_admin', 'e10adc3949ba59abbe56e057f20f883e', '订单管理员-李娜', 'https://i.pravatar.cc/150?img=52', 3, 1, '2026-01-10 10:30:00', '2026-01-26 20:59:24');
INSERT INTO `admin` VALUES (4, 'content_admin2', 'e10adc3949ba59abbe56e057f20f883e', '内容管理员-王强', 'https://i.pravatar.cc/150?img=53', 2, 1, '2026-01-11 09:00:00', '2026-01-26 20:59:24');
INSERT INTO `admin` VALUES (5, 'order_admin2', 'e10adc3949ba59abbe56e057f20f883e', '订单管理员-刘芳', 'https://i.pravatar.cc/150?img=54', 3, 1, '2026-01-11 14:00:00', '2026-01-26 20:59:24');
INSERT INTO `admin` VALUES (6, 'test_admin', 'e10adc3949ba59abbe56e057f20f883e', '测试管理员', 'https://i.pravatar.cc/150?img=55', 1, 0, '2026-01-12 10:00:00', '2026-01-26 20:59:24');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '轮播图标题',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片URL',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `sort` int NULL DEFAULT 0 COMMENT '排序号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '轮播图表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '教材推荐', '/images/books/banners/banner7.png', '/textbook/list?category=8', 1, 1, '2026-01-21 10:00:00', '2026-01-28 14:25:10');
INSERT INTO `banner` VALUES (2, '交易须知', '/images/books/banners/banner8.png', '/notice/1', 2, 1, '2026-01-22 10:00:00', '2026-01-28 14:25:12');
INSERT INTO `banner` VALUES (3, '新用户专享优惠', '/images/books/banners/banner9.png', '/register', 3, 1, '2026-01-23 10:00:00', '2026-01-28 14:25:13');

-- ----------------------------
-- Table structure for browse_history
-- ----------------------------
DROP TABLE IF EXISTS `browse_history`;
CREATE TABLE `browse_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `textbook_id` bigint NOT NULL COMMENT '教材ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_textbook_id`(`textbook_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '浏览历史表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of browse_history
-- ----------------------------
INSERT INTO `browse_history` VALUES (1, 1, 1, '2026-01-20 09:30:00');
INSERT INTO `browse_history` VALUES (2, 1, 2, '2026-01-20 09:45:00');
INSERT INTO `browse_history` VALUES (3, 1, 11, '2026-01-20 10:00:00');
INSERT INTO `browse_history` VALUES (4, 1, 12, '2026-01-20 10:15:00');
INSERT INTO `browse_history` VALUES (5, 1, 13, '2026-01-21 09:00:00');
INSERT INTO `browse_history` VALUES (6, 2, 3, '2026-01-19 14:00:00');
INSERT INTO `browse_history` VALUES (7, 2, 4, '2026-01-19 14:15:00');
INSERT INTO `browse_history` VALUES (8, 2, 1, '2026-01-20 10:00:00');
INSERT INTO `browse_history` VALUES (9, 2, 6, '2026-01-20 10:30:00');
INSERT INTO `browse_history` VALUES (10, 2, 15, '2026-01-21 15:30:00');
INSERT INTO `browse_history` VALUES (11, 3, 2, '2026-01-18 11:00:00');
INSERT INTO `browse_history` VALUES (12, 3, 4, '2026-01-19 13:00:00');
INSERT INTO `browse_history` VALUES (13, 3, 5, '2026-01-19 13:30:00');
INSERT INTO `browse_history` VALUES (14, 3, 8, '2026-01-20 16:00:00');
INSERT INTO `browse_history` VALUES (15, 3, 9, '2026-01-20 16:30:00');
INSERT INTO `browse_history` VALUES (16, 4, 3, '2026-01-17 10:30:00');
INSERT INTO `browse_history` VALUES (17, 4, 5, '2026-01-18 14:00:00');
INSERT INTO `browse_history` VALUES (18, 4, 7, '2026-01-18 14:30:00');
INSERT INTO `browse_history` VALUES (19, 4, 10, '2026-01-19 09:00:00');
INSERT INTO `browse_history` VALUES (20, 4, 14, '2026-01-19 09:30:00');
INSERT INTO `browse_history` VALUES (21, 5, 7, '2026-01-16 11:00:00');
INSERT INTO `browse_history` VALUES (22, 5, 9, '2026-01-17 15:00:00');
INSERT INTO `browse_history` VALUES (23, 5, 11, '2026-01-17 15:30:00');
INSERT INTO `browse_history` VALUES (24, 5, 14, '2026-01-18 10:00:00');
INSERT INTO `browse_history` VALUES (25, 5, 15, '2026-01-18 10:30:00');
INSERT INTO `browse_history` VALUES (26, 6, 16, '2026-01-20 09:00:00');
INSERT INTO `browse_history` VALUES (27, 6, 17, '2026-01-20 10:00:00');
INSERT INTO `browse_history` VALUES (28, 6, 18, '2026-01-21 14:00:00');
INSERT INTO `browse_history` VALUES (29, 6, 19, '2026-01-21 14:30:00');
INSERT INTO `browse_history` VALUES (30, 6, 20, '2026-01-21 15:00:00');
INSERT INTO `browse_history` VALUES (31, 7, 19, '2026-01-19 11:00:00');
INSERT INTO `browse_history` VALUES (32, 7, 20, '2026-01-20 13:00:00');
INSERT INTO `browse_history` VALUES (33, 7, 21, '2026-01-21 15:00:00');
INSERT INTO `browse_history` VALUES (34, 7, 22, '2026-01-21 15:30:00');
INSERT INTO `browse_history` VALUES (35, 7, 23, '2026-01-21 16:00:00');
INSERT INTO `browse_history` VALUES (36, 8, 22, '2026-01-18 10:00:00');
INSERT INTO `browse_history` VALUES (37, 8, 23, '2026-01-19 14:00:00');
INSERT INTO `browse_history` VALUES (38, 8, 24, '2026-01-20 09:30:00');
INSERT INTO `browse_history` VALUES (39, 8, 25, '2026-01-20 10:00:00');
INSERT INTO `browse_history` VALUES (40, 8, 26, '2026-01-20 10:30:00');
INSERT INTO `browse_history` VALUES (41, 9, 25, '2026-01-17 11:00:00');
INSERT INTO `browse_history` VALUES (42, 9, 26, '2026-01-18 15:00:00');
INSERT INTO `browse_history` VALUES (43, 9, 27, '2026-01-19 10:00:00');
INSERT INTO `browse_history` VALUES (44, 9, 28, '2026-01-19 10:30:00');
INSERT INTO `browse_history` VALUES (45, 9, 29, '2026-01-19 11:00:00');
INSERT INTO `browse_history` VALUES (46, 10, 28, '2026-01-16 09:30:00');
INSERT INTO `browse_history` VALUES (47, 10, 29, '2026-01-17 13:00:00');
INSERT INTO `browse_history` VALUES (48, 10, 30, '2026-01-18 16:00:00');
INSERT INTO `browse_history` VALUES (49, 10, 31, '2026-01-18 16:30:00');
INSERT INTO `browse_history` VALUES (50, 10, 32, '2026-01-18 17:00:00');
INSERT INTO `browse_history` VALUES (51, 11, 31, '2026-01-20 10:30:00');
INSERT INTO `browse_history` VALUES (52, 11, 32, '2026-01-21 14:00:00');
INSERT INTO `browse_history` VALUES (53, 11, 26, '2026-01-21 14:30:00');
INSERT INTO `browse_history` VALUES (54, 11, 27, '2026-01-21 15:00:00');
INSERT INTO `browse_history` VALUES (55, 11, 30, '2026-01-21 15:30:00');
INSERT INTO `browse_history` VALUES (56, 12, 33, '2026-01-19 11:00:00');
INSERT INTO `browse_history` VALUES (57, 12, 34, '2026-01-20 15:00:00');
INSERT INTO `browse_history` VALUES (58, 12, 27, '2026-01-20 15:30:00');
INSERT INTO `browse_history` VALUES (59, 12, 28, '2026-01-20 16:00:00');
INSERT INTO `browse_history` VALUES (60, 12, 31, '2026-01-20 16:30:00');
INSERT INTO `browse_history` VALUES (61, 13, 35, '2026-01-18 10:00:00');
INSERT INTO `browse_history` VALUES (62, 13, 36, '2026-01-19 14:00:00');
INSERT INTO `browse_history` VALUES (63, 13, 28, '2026-01-19 14:30:00');
INSERT INTO `browse_history` VALUES (64, 13, 29, '2026-01-19 15:00:00');
INSERT INTO `browse_history` VALUES (65, 13, 32, '2026-01-19 15:30:00');
INSERT INTO `browse_history` VALUES (66, 14, 37, '2026-01-17 09:30:00');
INSERT INTO `browse_history` VALUES (67, 14, 38, '2026-01-18 13:30:00');
INSERT INTO `browse_history` VALUES (68, 14, 29, '2026-01-18 14:00:00');
INSERT INTO `browse_history` VALUES (69, 14, 30, '2026-01-18 14:30:00');
INSERT INTO `browse_history` VALUES (70, 14, 33, '2026-01-18 15:00:00');
INSERT INTO `browse_history` VALUES (71, 15, 39, '2026-01-16 11:00:00');
INSERT INTO `browse_history` VALUES (72, 15, 40, '2026-01-17 15:00:00');
INSERT INTO `browse_history` VALUES (73, 15, 6, '2026-01-17 15:30:00');
INSERT INTO `browse_history` VALUES (74, 15, 11, '2026-01-17 16:00:00');
INSERT INTO `browse_history` VALUES (75, 15, 12, '2026-01-17 16:30:00');
INSERT INTO `browse_history` VALUES (76, 16, 41, '2026-01-20 09:00:00');
INSERT INTO `browse_history` VALUES (77, 16, 42, '2026-01-21 13:00:00');
INSERT INTO `browse_history` VALUES (78, 16, 36, '2026-01-21 13:30:00');
INSERT INTO `browse_history` VALUES (79, 16, 37, '2026-01-21 14:00:00');
INSERT INTO `browse_history` VALUES (80, 16, 40, '2026-01-21 14:30:00');
INSERT INTO `browse_history` VALUES (81, 17, 43, '2026-01-19 10:30:00');
INSERT INTO `browse_history` VALUES (82, 17, 44, '2026-01-20 14:30:00');
INSERT INTO `browse_history` VALUES (83, 17, 37, '2026-01-20 15:00:00');
INSERT INTO `browse_history` VALUES (84, 17, 38, '2026-01-20 15:30:00');
INSERT INTO `browse_history` VALUES (85, 17, 41, '2026-01-20 16:00:00');
INSERT INTO `browse_history` VALUES (86, 18, 45, '2026-01-18 11:00:00');
INSERT INTO `browse_history` VALUES (87, 18, 46, '2026-01-19 15:00:00');
INSERT INTO `browse_history` VALUES (88, 18, 38, '2026-01-19 15:30:00');
INSERT INTO `browse_history` VALUES (89, 18, 39, '2026-01-19 16:00:00');
INSERT INTO `browse_history` VALUES (90, 18, 42, '2026-01-19 16:30:00');
INSERT INTO `browse_history` VALUES (91, 19, 47, '2026-01-17 10:00:00');
INSERT INTO `browse_history` VALUES (92, 19, 48, '2026-01-18 14:00:00');
INSERT INTO `browse_history` VALUES (93, 19, 39, '2026-01-18 14:30:00');
INSERT INTO `browse_history` VALUES (94, 19, 40, '2026-01-18 15:00:00');
INSERT INTO `browse_history` VALUES (95, 19, 43, '2026-01-18 15:30:00');
INSERT INTO `browse_history` VALUES (96, 20, 49, '2026-01-16 09:00:00');
INSERT INTO `browse_history` VALUES (97, 20, 50, '2026-01-17 13:00:00');
INSERT INTO `browse_history` VALUES (98, 20, 40, '2026-01-17 13:30:00');
INSERT INTO `browse_history` VALUES (99, 20, 41, '2026-01-17 14:00:00');
INSERT INTO `browse_history` VALUES (100, 20, 44, '2026-01-17 14:30:00');
INSERT INTO `browse_history` VALUES (101, 21, 1, '2026-01-20 10:00:00');
INSERT INTO `browse_history` VALUES (102, 21, 11, '2026-01-21 14:00:00');
INSERT INTO `browse_history` VALUES (103, 21, 46, '2026-01-21 14:30:00');
INSERT INTO `browse_history` VALUES (104, 21, 47, '2026-01-21 15:00:00');
INSERT INTO `browse_history` VALUES (105, 21, 50, '2026-01-21 15:30:00');
INSERT INTO `browse_history` VALUES (106, 22, 2, '2026-01-19 11:00:00');
INSERT INTO `browse_history` VALUES (107, 22, 12, '2026-01-20 15:00:00');
INSERT INTO `browse_history` VALUES (108, 22, 47, '2026-01-20 15:30:00');
INSERT INTO `browse_history` VALUES (109, 22, 48, '2026-01-20 16:00:00');
INSERT INTO `browse_history` VALUES (110, 22, 1, '2026-01-20 16:30:00');
INSERT INTO `browse_history` VALUES (111, 23, 3, '2026-01-18 10:30:00');
INSERT INTO `browse_history` VALUES (112, 23, 13, '2026-01-19 14:30:00');
INSERT INTO `browse_history` VALUES (113, 23, 48, '2026-01-19 15:00:00');
INSERT INTO `browse_history` VALUES (114, 23, 49, '2026-01-19 15:30:00');
INSERT INTO `browse_history` VALUES (115, 23, 2, '2026-01-19 16:00:00');
INSERT INTO `browse_history` VALUES (116, 24, 4, '2026-01-17 09:30:00');
INSERT INTO `browse_history` VALUES (117, 24, 14, '2026-01-18 13:30:00');
INSERT INTO `browse_history` VALUES (118, 24, 49, '2026-01-18 14:00:00');
INSERT INTO `browse_history` VALUES (119, 24, 50, '2026-01-18 14:30:00');
INSERT INTO `browse_history` VALUES (120, 24, 3, '2026-01-18 15:00:00');
INSERT INTO `browse_history` VALUES (121, 25, 5, '2026-01-16 11:00:00');
INSERT INTO `browse_history` VALUES (122, 25, 15, '2026-01-17 15:00:00');
INSERT INTO `browse_history` VALUES (123, 25, 50, '2026-01-17 15:30:00');
INSERT INTO `browse_history` VALUES (124, 25, 1, '2026-01-17 16:00:00');
INSERT INTO `browse_history` VALUES (125, 25, 4, '2026-01-17 16:30:00');
INSERT INTO `browse_history` VALUES (126, 26, 56, '2026-01-20 09:00:00');
INSERT INTO `browse_history` VALUES (127, 26, 57, '2026-01-21 13:00:00');
INSERT INTO `browse_history` VALUES (128, 27, 58, '2026-01-19 10:30:00');
INSERT INTO `browse_history` VALUES (129, 27, 59, '2026-01-20 14:30:00');
INSERT INTO `browse_history` VALUES (130, 28, 60, '2026-01-18 11:00:00');
INSERT INTO `browse_history` VALUES (131, 28, 61, '2026-01-19 15:00:00');
INSERT INTO `browse_history` VALUES (132, 29, 62, '2026-01-17 10:00:00');
INSERT INTO `browse_history` VALUES (133, 29, 63, '2026-01-18 14:00:00');
INSERT INTO `browse_history` VALUES (134, 30, 64, '2026-01-16 09:30:00');
INSERT INTO `browse_history` VALUES (135, 30, 65, '2026-01-17 13:30:00');

-- ----------------------------
-- Table structure for campus
-- ----------------------------
DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '校区名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '校区地址',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '校区表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of campus
-- ----------------------------
INSERT INTO `campus` VALUES (1, '主校区', '学校主校区', 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `campus` VALUES (2, '东校区', '学校东校区', 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `campus` VALUES (3, '南校区', '学校南校区', 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `campus` VALUES (4, '北校区', '学校北校区', 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID',
  `sort` int NULL DEFAULT 0 COMMENT '排序号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '计算机类', 0, 1, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (2, '数学类', 0, 2, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (3, '英语类', 0, 3, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (4, '经济管理类', 0, 4, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (5, '理工类', 0, 5, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (6, '文学艺术类', 0, 6, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (7, '法律类', 0, 7, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (8, '医学类', 0, 8, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (9, '考研考公', 0, 9, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (10, '其他', 0, 10, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `category` VALUES (11, 'Java开发', 1, 11, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (12, 'Python开发', 1, 12, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (13, '前端开发', 1, 13, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (14, '数据库', 1, 14, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (15, '人工智能', 1, 15, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (16, '高等数学', 2, 21, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (17, '线性代数', 2, 22, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (18, '概率统计', 2, 23, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (19, '四级', 3, 31, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (20, '六级', 3, 32, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (21, '雅思', 3, 33, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (22, '托福', 3, 34, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (23, '会计', 4, 41, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (24, '金融', 4, 42, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (25, '市场营销', 4, 43, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (26, '电子电路', 5, 51, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (27, '机械工程', 5, 52, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (28, '土木工程', 5, 53, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (29, '中国文学', 6, 61, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (30, '外国文学', 6, 62, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (31, '艺术设计', 6, 63, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (32, '民法', 7, 71, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (33, '刑法', 7, 72, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (34, '行政法', 7, 73, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (35, '基础医学', 8, 81, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (36, '临床医学', 8, 82, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (37, '中医学', 8, 83, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (38, '考研英语', 9, 91, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (39, '考研数学', 9, 92, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (40, '考研政治', 9, 93, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (41, '公务员行测', 9, 94, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');
INSERT INTO `category` VALUES (42, '公务员申论', 9, 95, 1, '2026-01-26 20:59:25', '2026-01-26 20:59:25');

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID（由两个用户ID组合生成）',
  `from_user_id` bigint NOT NULL COMMENT '发送者ID',
  `to_user_id` bigint NOT NULL COMMENT '接收者ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `type` tinyint NULL DEFAULT 1 COMMENT '消息类型（1文本 2图片 3系统消息）',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读（0未读 1已读）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_conversation_id`(`conversation_id` ASC) USING BTREE,
  INDEX `idx_from_user_id`(`from_user_id` ASC) USING BTREE,
  INDEX `idx_to_user_id`(`to_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_message
-- ----------------------------
INSERT INTO `chat_message` VALUES (1, '1_10', 1, 10, '你好', 1, 0, '2026-02-24 15:50:00');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程代码',
  `major_id` bigint NULL DEFAULT NULL COMMENT '所属专业ID',
  `semester` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开课学期（如：大一上）',
  `credit` decimal(3, 1) NULL DEFAULT NULL COMMENT '学分',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_major_id`(`major_id` ASC) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, '高等数学', 'MATH101', 1, '大一上', 5.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (2, '线性代数', 'MATH102', 1, '大一下', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (3, '概率论与数理统计', 'MATH201', 1, '大二上', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (4, '数据结构', 'CS201', 1, '大二上', 4.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (5, '计算机组成原理', 'CS202', 1, '大二下', 4.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (6, '操作系统', 'CS301', 1, '大三上', 4.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (7, '计算机网络', 'CS302', 1, '大三上', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (8, '数据库原理', 'CS303', 1, '大三下', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (9, '软件工程', 'SE301', 2, '大三上', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (10, 'Java程序设计', 'SE201', 2, '大二上', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (11, '大学英语1', 'ENG101', 6, '大一上', 4.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (12, '大学英语2', 'ENG102', 6, '大一下', 4.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (13, '微观经济学', 'ECO101', 10, '大一上', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (14, '宏观经济学', 'ECO102', 10, '大一下', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `course` VALUES (15, '会计学原理', 'ACC101', 9, '大一上', 3.0, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');

-- ----------------------------
-- Table structure for credit_record
-- ----------------------------
DROP TABLE IF EXISTS `credit_record`;
CREATE TABLE `credit_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID',
  `type` tinyint NOT NULL COMMENT '记录类型（1爽约 2实物不符 3好评奖励 4差评扣分 5举报成立）',
  `score_change` int NOT NULL COMMENT '信用分变化（正数加分，负数扣分）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述说明',
  `reporter_id` bigint NULL DEFAULT NULL COMMENT '举报人ID（如有）',
  `evidence` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '证据图片URL（JSON数组）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0待处理 1已处理）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '信用记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of credit_record
-- ----------------------------
INSERT INTO `credit_record` VALUES (1, 2, 52, 5, -10, '不符合 | 处理备注：确实', 1, '[\"/images/books/details/156acaafca9e43c984c0377b75361544.png\"]', 1, '2026-02-06 16:09:38');

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `textbook_id` bigint NOT NULL COMMENT '教材ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_textbook`(`user_id` ASC, `textbook_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (1, 1, 11, '2026-01-20 10:15:00');
INSERT INTO `favorite` VALUES (2, 1, 12, '2026-01-20 11:20:00');
INSERT INTO `favorite` VALUES (3, 1, 13, '2026-01-21 09:30:00');
INSERT INTO `favorite` VALUES (4, 2, 1, '2026-01-19 14:25:00');
INSERT INTO `favorite` VALUES (5, 2, 6, '2026-01-20 10:40:00');
INSERT INTO `favorite` VALUES (6, 2, 15, '2026-01-21 15:50:00');
INSERT INTO `favorite` VALUES (7, 3, 2, '2026-01-18 11:20:00');
INSERT INTO `favorite` VALUES (8, 3, 4, '2026-01-19 13:35:00');
INSERT INTO `favorite` VALUES (9, 3, 8, '2026-01-20 16:45:00');
INSERT INTO `favorite` VALUES (10, 4, 3, '2026-01-17 10:50:00');
INSERT INTO `favorite` VALUES (11, 4, 5, '2026-01-18 14:20:00');
INSERT INTO `favorite` VALUES (12, 4, 10, '2026-01-19 09:15:00');
INSERT INTO `favorite` VALUES (13, 5, 7, '2026-01-16 11:30:00');
INSERT INTO `favorite` VALUES (14, 5, 9, '2026-01-17 15:40:00');
INSERT INTO `favorite` VALUES (15, 5, 14, '2026-01-18 10:25:00');
INSERT INTO `favorite` VALUES (16, 6, 16, '2026-01-20 09:20:00');
INSERT INTO `favorite` VALUES (17, 6, 17, '2026-01-20 10:30:00');
INSERT INTO `favorite` VALUES (18, 6, 18, '2026-01-21 14:15:00');
INSERT INTO `favorite` VALUES (19, 7, 19, '2026-01-19 11:40:00');
INSERT INTO `favorite` VALUES (20, 7, 20, '2026-01-20 13:50:00');
INSERT INTO `favorite` VALUES (21, 7, 21, '2026-01-21 15:20:00');
INSERT INTO `favorite` VALUES (22, 8, 22, '2026-01-18 10:35:00');
INSERT INTO `favorite` VALUES (23, 8, 23, '2026-01-19 14:45:00');
INSERT INTO `favorite` VALUES (24, 8, 24, '2026-01-20 09:55:00');
INSERT INTO `favorite` VALUES (25, 9, 25, '2026-01-17 11:20:00');
INSERT INTO `favorite` VALUES (26, 9, 26, '2026-01-18 15:30:00');
INSERT INTO `favorite` VALUES (27, 9, 27, '2026-01-19 10:40:00');
INSERT INTO `favorite` VALUES (28, 10, 28, '2026-01-16 09:50:00');
INSERT INTO `favorite` VALUES (29, 10, 29, '2026-01-17 13:25:00');
INSERT INTO `favorite` VALUES (30, 10, 30, '2026-01-18 16:35:00');
INSERT INTO `favorite` VALUES (31, 11, 31, '2026-01-20 10:45:00');
INSERT INTO `favorite` VALUES (32, 11, 32, '2026-01-21 14:20:00');
INSERT INTO `favorite` VALUES (33, 12, 33, '2026-01-19 11:30:00');
INSERT INTO `favorite` VALUES (34, 12, 34, '2026-01-20 15:40:00');
INSERT INTO `favorite` VALUES (35, 13, 35, '2026-01-18 10:25:00');
INSERT INTO `favorite` VALUES (36, 13, 36, '2026-01-19 14:35:00');
INSERT INTO `favorite` VALUES (37, 14, 37, '2026-01-17 09:45:00');
INSERT INTO `favorite` VALUES (38, 14, 38, '2026-01-18 13:55:00');
INSERT INTO `favorite` VALUES (39, 15, 39, '2026-01-16 11:15:00');
INSERT INTO `favorite` VALUES (40, 15, 40, '2026-01-17 15:25:00');
INSERT INTO `favorite` VALUES (41, 16, 41, '2026-01-20 09:35:00');
INSERT INTO `favorite` VALUES (42, 16, 42, '2026-01-21 13:45:00');
INSERT INTO `favorite` VALUES (43, 17, 43, '2026-01-19 10:50:00');
INSERT INTO `favorite` VALUES (44, 17, 44, '2026-01-20 14:55:00');
INSERT INTO `favorite` VALUES (45, 18, 45, '2026-01-18 11:40:00');
INSERT INTO `favorite` VALUES (46, 18, 46, '2026-01-19 15:50:00');
INSERT INTO `favorite` VALUES (47, 19, 47, '2026-01-17 10:30:00');
INSERT INTO `favorite` VALUES (48, 19, 48, '2026-01-18 14:40:00');
INSERT INTO `favorite` VALUES (49, 20, 49, '2026-01-16 09:25:00');
INSERT INTO `favorite` VALUES (50, 20, 50, '2026-01-17 13:35:00');
INSERT INTO `favorite` VALUES (51, 21, 1, '2026-01-20 10:20:00');
INSERT INTO `favorite` VALUES (52, 21, 11, '2026-01-21 14:30:00');
INSERT INTO `favorite` VALUES (53, 22, 2, '2026-01-19 11:15:00');
INSERT INTO `favorite` VALUES (54, 22, 12, '2026-01-20 15:25:00');
INSERT INTO `favorite` VALUES (55, 23, 3, '2026-01-18 10:40:00');
INSERT INTO `favorite` VALUES (56, 23, 13, '2026-01-19 14:50:00');
INSERT INTO `favorite` VALUES (57, 24, 4, '2026-01-17 09:55:00');
INSERT INTO `favorite` VALUES (58, 24, 14, '2026-01-18 13:45:00');
INSERT INTO `favorite` VALUES (59, 25, 5, '2026-01-16 11:25:00');
INSERT INTO `favorite` VALUES (60, 25, 15, '2026-01-17 15:35:00');
INSERT INTO `favorite` VALUES (61, 26, 56, '2026-01-20 09:40:00');
INSERT INTO `favorite` VALUES (62, 26, 57, '2026-01-21 13:50:00');
INSERT INTO `favorite` VALUES (63, 27, 58, '2026-01-19 10:55:00');
INSERT INTO `favorite` VALUES (64, 27, 59, '2026-01-20 14:45:00');
INSERT INTO `favorite` VALUES (65, 28, 60, '2026-01-18 11:30:00');
INSERT INTO `favorite` VALUES (66, 28, 61, '2026-01-19 15:40:00');
INSERT INTO `favorite` VALUES (67, 29, 62, '2026-01-17 10:35:00');
INSERT INTO `favorite` VALUES (68, 29, 63, '2026-01-18 14:45:00');
INSERT INTO `favorite` VALUES (69, 30, 64, '2026-01-16 09:50:00');
INSERT INTO `favorite` VALUES (70, 30, 65, '2026-01-17 13:55:00');
INSERT INTO `favorite` VALUES (101, 15, 8, NULL);

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '关注者ID',
  `follow_user_id` bigint NOT NULL COMMENT '被关注者ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_follow`(`user_id` ASC, `follow_user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_follow_user_id`(`follow_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '关注表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (1, 1, 2, '2026-01-15 10:20:00');
INSERT INTO `follow` VALUES (2, 1, 3, '2026-01-16 14:30:00');
INSERT INTO `follow` VALUES (3, 1, 15, '2026-01-17 09:45:00');
INSERT INTO `follow` VALUES (4, 2, 1, '2026-01-15 11:25:00');
INSERT INTO `follow` VALUES (5, 2, 4, '2026-01-16 15:35:00');
INSERT INTO `follow` VALUES (6, 2, 5, '2026-01-17 10:50:00');
INSERT INTO `follow` VALUES (7, 3, 1, '2026-01-14 09:30:00');
INSERT INTO `follow` VALUES (8, 3, 2, '2026-01-15 13:40:00');
INSERT INTO `follow` VALUES (9, 3, 6, '2026-01-16 16:45:00');
INSERT INTO `follow` VALUES (10, 4, 2, '2026-01-13 10:35:00');
INSERT INTO `follow` VALUES (11, 4, 5, '2026-01-14 14:45:00');
INSERT INTO `follow` VALUES (12, 4, 7, '2026-01-15 11:50:00');
INSERT INTO `follow` VALUES (13, 5, 1, '2026-01-12 11:40:00');
INSERT INTO `follow` VALUES (14, 5, 4, '2026-01-13 15:50:00');
INSERT INTO `follow` VALUES (15, 5, 8, '2026-01-14 10:55:00');
INSERT INTO `follow` VALUES (16, 6, 7, '2026-01-15 09:25:00');
INSERT INTO `follow` VALUES (17, 6, 8, '2026-01-16 13:35:00');
INSERT INTO `follow` VALUES (18, 6, 9, '2026-01-17 16:40:00');
INSERT INTO `follow` VALUES (19, 7, 6, '2026-01-14 10:30:00');
INSERT INTO `follow` VALUES (20, 7, 9, '2026-01-15 14:40:00');
INSERT INTO `follow` VALUES (21, 7, 10, '2026-01-16 11:45:00');
INSERT INTO `follow` VALUES (22, 8, 6, '2026-01-13 11:35:00');
INSERT INTO `follow` VALUES (23, 8, 10, '2026-01-14 15:45:00');
INSERT INTO `follow` VALUES (24, 8, 11, '2026-01-15 10:50:00');
INSERT INTO `follow` VALUES (25, 9, 7, '2026-01-12 10:40:00');
INSERT INTO `follow` VALUES (26, 9, 11, '2026-01-13 14:50:00');
INSERT INTO `follow` VALUES (27, 9, 12, '2026-01-14 11:55:00');
INSERT INTO `follow` VALUES (28, 10, 8, '2026-01-11 09:45:00');
INSERT INTO `follow` VALUES (29, 10, 12, '2026-01-12 13:55:00');
INSERT INTO `follow` VALUES (30, 10, 13, '2026-01-13 16:50:00');
INSERT INTO `follow` VALUES (31, 11, 1, '2026-01-15 10:50:00');
INSERT INTO `follow` VALUES (32, 11, 12, '2026-01-16 14:35:00');
INSERT INTO `follow` VALUES (33, 12, 2, '2026-01-14 11:40:00');
INSERT INTO `follow` VALUES (34, 12, 11, '2026-01-15 15:45:00');
INSERT INTO `follow` VALUES (35, 13, 3, '2026-01-13 10:35:00');
INSERT INTO `follow` VALUES (36, 13, 14, '2026-01-14 14:40:00');
INSERT INTO `follow` VALUES (37, 14, 4, '2026-01-12 09:50:00');
INSERT INTO `follow` VALUES (38, 14, 13, '2026-01-13 13:55:00');
INSERT INTO `follow` VALUES (39, 15, 5, '2026-01-11 11:45:00');
INSERT INTO `follow` VALUES (40, 15, 1, '2026-01-12 15:50:00');
INSERT INTO `follow` VALUES (41, 16, 17, '2026-01-15 09:30:00');
INSERT INTO `follow` VALUES (42, 16, 18, '2026-01-16 13:40:00');
INSERT INTO `follow` VALUES (43, 17, 16, '2026-01-14 10:45:00');
INSERT INTO `follow` VALUES (44, 17, 19, '2026-01-15 14:50:00');
INSERT INTO `follow` VALUES (45, 18, 16, '2026-01-13 11:50:00');
INSERT INTO `follow` VALUES (46, 18, 20, '2026-01-14 15:55:00');
INSERT INTO `follow` VALUES (47, 19, 17, '2026-01-12 10:55:00');
INSERT INTO `follow` VALUES (48, 19, 21, '2026-01-13 14:45:00');
INSERT INTO `follow` VALUES (49, 20, 18, '2026-01-11 09:50:00');
INSERT INTO `follow` VALUES (50, 20, 22, '2026-01-12 13:50:00');
INSERT INTO `follow` VALUES (51, 21, 22, '2026-01-15 10:40:00');
INSERT INTO `follow` VALUES (52, 21, 23, '2026-01-16 14:45:00');
INSERT INTO `follow` VALUES (53, 22, 21, '2026-01-14 11:50:00');
INSERT INTO `follow` VALUES (54, 22, 24, '2026-01-15 15:55:00');
INSERT INTO `follow` VALUES (55, 23, 21, '2026-01-13 10:55:00');
INSERT INTO `follow` VALUES (56, 23, 25, '2026-01-14 14:50:00');
INSERT INTO `follow` VALUES (57, 24, 22, '2026-01-12 09:45:00');
INSERT INTO `follow` VALUES (58, 24, 26, '2026-01-13 13:50:00');
INSERT INTO `follow` VALUES (59, 25, 23, '2026-01-11 11:50:00');
INSERT INTO `follow` VALUES (60, 25, 27, '2026-01-12 15:55:00');
INSERT INTO `follow` VALUES (61, 26, 27, '2026-01-15 09:35:00');
INSERT INTO `follow` VALUES (62, 26, 28, '2026-01-16 13:45:00');
INSERT INTO `follow` VALUES (63, 27, 26, '2026-01-14 10:50:00');
INSERT INTO `follow` VALUES (64, 27, 29, '2026-01-15 14:55:00');
INSERT INTO `follow` VALUES (65, 28, 26, '2026-01-13 11:55:00');
INSERT INTO `follow` VALUES (66, 28, 30, '2026-01-14 15:50:00');
INSERT INTO `follow` VALUES (67, 29, 27, '2026-01-12 10:50:00');
INSERT INTO `follow` VALUES (69, 30, 28, '2026-01-11 09:55:00');

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '专业名称',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所属学院',
  `campus_id` bigint NULL DEFAULT NULL COMMENT '所属校区',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_campus_id`(`campus_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专业表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES (1, '计算机科学与技术', '计算机学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (2, '软件工程', '计算机学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (3, '信息安全', '计算机学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (4, '数学与应用数学', '数学学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (5, '统计学', '数学学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (6, '英语', '外国语学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (7, '日语', '外国语学院', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (8, '工商管理', '管理学院', 2, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (9, '会计学', '管理学院', 2, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `major` VALUES (10, '金融学', '经济学院', 2, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int NULL DEFAULT 0 COMMENT '排序号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 0, '数据统计', '/dashboard', 'Dashboard', 'DataLine', 1, 1, '2026-01-26 13:45:16', '2026-01-28 17:05:02');
INSERT INTO `menu` VALUES (2, 0, '用户管理', '/user', 'UserManage', 'User', 2, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (3, 0, '教材管理', '/textbook', 'TextbookManage', 'Reading', 3, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (4, 0, '教材审核', '/audit', 'AuditManage', 'DocumentChecked', 4, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (5, 0, '订单管理', '/order', 'OrderManage', 'List', 5, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (6, 0, '求购管理', '/wanted', 'WantedManage', 'Search', 6, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (7, 0, '分类管理', '/category', 'CategoryManage', 'Menu', 7, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (8, 0, '评价管理', '/review', 'ReviewManage', 'Star', 8, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (9, 0, '公告管理', '/notice', 'NoticeManage', 'Bell', 9, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (10, 0, '轮播图管理', '/banner', 'BannerManage', 'Picture', 10, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (11, 0, '系统管理', '/system', NULL, 'Setting', 11, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (12, 11, '角色管理', '/system/role', 'RoleManage', 'UserFilled', 1, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (13, 11, '菜单管理', '/system/menu', 'MenuManage', 'Grid', 2, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (14, 11, '管理员管理', '/system/admin', 'AdminManage', 'Avatar', 3, 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `menu` VALUES (16, 0, '校区管理', '/campus', 'CampusManage', 'Location', 15, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `menu` VALUES (17, 0, '专业课程', '/course', 'CourseManage', 'School', 16, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `menu` VALUES (18, 0, '交易点管理', '/trading-point', 'TradingPointManage', 'MapLocation', 17, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `menu` VALUES (19, 0, '信用管理', '/credit', 'CreditManage', 'Medal', 18, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `from_user_id` bigint NULL DEFAULT NULL COMMENT '发送者ID（系统消息为空）',
  `to_user_id` bigint NOT NULL COMMENT '接收者ID',
  `type` tinyint NULL DEFAULT 1 COMMENT '消息类型（1系统通知 2私信）',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息内容',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读（0未读 1已读）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_to_user_id`(`to_user_id` ASC) USING BTREE,
  INDEX `idx_from_user_id`(`from_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, NULL, 1, 1, '欢迎使用二手教材交易系统', '欢迎您注册成为本平台用户！请遵守交易规则，诚信交易。', 1, '2026-01-10 10:00:00');
INSERT INTO `message` VALUES (2, NULL, 2, 1, '欢迎使用二手教材交易系统', '欢迎您注册成为本平台用户！请遵守交易规则，诚信交易。', 1, '2026-01-10 10:05:00');
INSERT INTO `message` VALUES (3, NULL, 3, 1, '欢迎使用二手教材交易系统', '欢迎您注册成为本平台用户！请遵守交易规则，诚信交易。', 1, '2026-01-10 10:10:00');
INSERT INTO `message` VALUES (4, NULL, 11, 1, '订单已确认', '您的订单ORD202601260001已被卖家确认，请及时联系卖家完成交易。', 1, '2026-01-20 10:35:00');
INSERT INTO `message` VALUES (5, NULL, 1, 1, '订单已完成', '您的订单ORD202601260001已完成，感谢您的交易！', 1, '2026-01-21 14:20:00');
INSERT INTO `message` VALUES (6, NULL, 12, 1, '订单已确认', '您的订单ORD202601260002已被卖家确认，请及时联系卖家完成交易。', 1, '2026-01-20 14:25:00');
INSERT INTO `message` VALUES (7, NULL, 2, 1, '订单已完成', '您的订单ORD202601260002已完成，感谢您的交易！', 1, '2026-01-22 10:30:00');
INSERT INTO `message` VALUES (8, NULL, 13, 1, '订单已确认', '您的订单ORD202601260003已被卖家确认，请及时联系卖家完成交易。', 1, '2026-01-21 09:20:00');
INSERT INTO `message` VALUES (9, NULL, 3, 1, '订单已完成', '您的订单ORD202601260003已完成，感谢您的交易！', 1, '2026-01-22 15:45:00');
INSERT INTO `message` VALUES (10, NULL, 14, 1, '订单待确认', '您有新的订单ORD202601260004待确认，请及时处理。', 0, '2026-01-21 16:50:00');
INSERT INTO `message` VALUES (11, NULL, 15, 1, '订单待确认', '您有新的订单ORD202601260005待确认，请及时处理。', 0, '2026-01-22 11:25:00');
INSERT INTO `message` VALUES (12, NULL, 16, 1, '订单已确认', '您的订单ORD202601260006已被卖家确认，请及时联系卖家完成交易。', 1, '2026-01-22 13:35:00');
INSERT INTO `message` VALUES (13, NULL, 15, 1, '订单已完成', '您的订单ORD202601260006已完成，感谢您的交易！', 1, '2026-01-23 16:20:00');
INSERT INTO `message` VALUES (14, NULL, 1, 1, '教材审核通过', '您发布的教材《数据结构与算法分析》已通过审核，现已上架。', 1, '2026-01-15 09:30:00');
INSERT INTO `message` VALUES (15, NULL, 2, 1, '教材审核通过', '您发布的教材《深入理解计算机系统》已通过审核，现已上架。', 1, '2026-01-15 10:45:00');
INSERT INTO `message` VALUES (16, NULL, 3, 1, '教材审核通过', '您发布的教材《Java核心技术》已通过审核，现已上架。', 1, '2026-01-15 11:20:00');
INSERT INTO `message` VALUES (17, NULL, 20, 1, '新公告通知', '平台发布新公告：春节期间交易注意事项，请及时查看。', 0, '2026-01-25 10:00:00');
INSERT INTO `message` VALUES (18, NULL, 21, 1, '新公告通知', '平台发布新公告：春节期间交易注意事项，请及时查看。', 0, '2026-01-25 10:00:00');
INSERT INTO `message` VALUES (19, NULL, 22, 1, '新公告通知', '平台发布新公告：春节期间交易注意事项，请及时查看。', 0, '2026-01-25 10:00:00');
INSERT INTO `message` VALUES (20, NULL, 23, 1, '新公告通知', '平台发布新公告：春节期间交易注意事项，请及时查看。', 0, '2026-01-25 10:00:00');
INSERT INTO `message` VALUES (21, 11, 1, 2, '咨询教材', '您好，请问《数据结构与算法分析》这本书有笔记吗？', 1, '2026-01-20 09:45:00');
INSERT INTO `message` VALUES (22, 1, 11, 2, '回复：咨询教材', '您好，这本书几乎全新，没有任何笔记和划痕。', 1, '2026-01-20 10:00:00');
INSERT INTO `message` VALUES (23, 11, 1, 2, '确认购买', '好的，那我下单了，谢谢！', 1, '2026-01-20 10:15:00');
INSERT INTO `message` VALUES (24, 1, 11, 2, '回复：确认购买', '好的，已收到订单，我会尽快安排交易。', 1, '2026-01-20 10:40:00');
INSERT INTO `message` VALUES (25, 12, 2, 2, '咨询教材', '请问CSAPP这本书什么时候可以交易？', 1, '2026-01-20 13:50:00');
INSERT INTO `message` VALUES (26, 2, 12, 2, '回复：咨询教材', '随时可以，您方便的话今天就可以。', 1, '2026-01-20 14:05:00');
INSERT INTO `message` VALUES (27, 13, 3, 2, '咨询教材', 'Java核心技术这本书适合初学者吗？', 1, '2026-01-21 08:45:00');
INSERT INTO `message` VALUES (28, 3, 13, 2, '回复：咨询教材', '非常适合，这是Java入门的经典教材。', 1, '2026-01-21 09:00:00');
INSERT INTO `message` VALUES (29, 14, 4, 2, '咨询交易方式', '可以当面交易吗？我在清华大学。', 1, '2026-01-21 16:30:00');
INSERT INTO `message` VALUES (30, 4, 14, 2, '回复：咨询交易方式', '可以的，我也在清华，可以约个时间地点。', 1, '2026-01-21 16:55:00');
INSERT INTO `message` VALUES (31, 15, 5, 2, '咨询教材', '计算机网络这本书内容完整吗？', 1, '2026-01-22 11:00:00');
INSERT INTO `message` VALUES (32, 5, 15, 2, '回复：咨询教材', '内容完整，只是有少量划线，不影响阅读。', 1, '2026-01-22 11:30:00');
INSERT INTO `message` VALUES (33, 16, 15, 2, '咨询教材', 'Python编程这本书有配套代码吗？', 1, '2026-01-22 13:00:00');
INSERT INTO `message` VALUES (34, 15, 16, 2, '回复：咨询教材', '书中有所有代码示例，可以跟着练习。', 1, '2026-01-22 13:20:00');
INSERT INTO `message` VALUES (35, 17, 1, 2, '咨询教材', '操作系统概念这本书难度如何？', 1, '2026-01-23 08:30:00');
INSERT INTO `message` VALUES (36, 1, 17, 2, '回复：咨询教材', '有一定难度，但讲解很清晰，适合认真学习。', 1, '2026-01-23 08:55:00');
INSERT INTO `message` VALUES (37, 18, 2, 2, '咨询教材', '数据库系统概念有习题答案吗？', 1, '2026-01-23 14:50:00');
INSERT INTO `message` VALUES (38, 2, 18, 2, '回复：咨询教材', '书后有部分习题答案，网上也能找到完整答案。', 1, '2026-01-23 15:20:00');
INSERT INTO `message` VALUES (39, 19, 3, 2, '咨询教材', '编译原理这本书适合自学吗？', 1, '2026-01-24 09:40:00');
INSERT INTO `message` VALUES (40, 3, 19, 2, '回复：咨询教材', '可以自学，但建议配合视频课程一起学习。', 1, '2026-01-24 10:10:00');
INSERT INTO `message` VALUES (41, 20, 4, 2, '咨询教材', '人工智能这本书有实践项目吗？', 1, '2026-01-24 14:10:00');
INSERT INTO `message` VALUES (42, 4, 20, 2, '回复：咨询教材', '有很多实践案例和项目，非常实用。', 1, '2026-01-24 14:40:00');
INSERT INTO `message` VALUES (43, 21, 5, 2, '咨询教材', '西瓜书适合初学者吗？', 1, '2026-01-24 16:00:00');
INSERT INTO `message` VALUES (44, 5, 21, 2, '回复：咨询教材', '需要一定数学基础，建议先学好线性代数和概率论。', 1, '2026-01-24 16:30:00');
INSERT INTO `message` VALUES (45, 26, 6, 2, '咨询教材', '高等数学上册有配套习题集吗？', 1, '2026-01-19 09:50:00');
INSERT INTO `message` VALUES (46, 6, 26, 2, '回复：咨询教材', '建议购买同济大学的配套习题集。', 1, '2026-01-19 10:30:00');
INSERT INTO `message` VALUES (47, 27, 7, 2, '咨询教材', '高等数学下册和上册一起买有优惠吗？', 1, '2026-01-19 10:00:00');
INSERT INTO `message` VALUES (48, 7, 27, 2, '回复：咨询教材', '可以一起买，两本一共35元。', 1, '2026-01-19 10:35:00');
INSERT INTO `message` VALUES (49, 28, 8, 2, '咨询教材', '线性代数这本书讲解详细吗？', 1, '2026-01-20 09:00:00');
INSERT INTO `message` VALUES (50, 8, 28, 2, '回复：咨询教材', '非常详细，是经典教材，适合学习。', 1, '2026-01-20 09:40:00');
INSERT INTO `message` VALUES (51, 29, 9, 2, '咨询教材', '概率论有课后习题答案吗？', 1, '2026-01-20 13:50:00');
INSERT INTO `message` VALUES (52, 9, 29, 2, '回复：咨询教材', '我这本书里有部分习题的答案标注。', 1, '2026-01-20 14:25:00');
INSERT INTO `message` VALUES (53, 30, 10, 2, '咨询教材', '数学分析上册适合自学吗？', 1, '2026-01-21 11:20:00');
INSERT INTO `message` VALUES (54, 10, 30, 2, '回复：咨询教材', '有一定难度，建议配合视频课程学习。', 1, '2026-01-21 11:50:00');
INSERT INTO `message` VALUES (61, 1, 16, 2, '咨询教材', '西方经济学微观部分有课后答案吗？', 1, '2026-01-20 08:50:00');
INSERT INTO `message` VALUES (62, 16, 1, 2, '回复：咨询教材', '我书里标注了部分答案，可以参考。', 1, '2026-01-20 09:30:00');
INSERT INTO `message` VALUES (63, 2, 17, 2, '咨询教材', '管理学这本书案例多吗？', 1, '2026-01-19 11:00:00');
INSERT INTO `message` VALUES (64, 17, 2, 2, '回复：咨询教材', '有很多经典案例，非常实用。', 1, '2026-01-19 11:40:00');
INSERT INTO `message` VALUES (65, 3, 18, 2, '咨询教材', '会计学原理适合零基础吗？', 1, '2026-01-19 14:50:00');
INSERT INTO `message` VALUES (66, 18, 3, 2, '回复：咨询教材', '适合，这是会计入门的经典教材。', 1, '2026-01-19 15:30:00');
INSERT INTO `message` VALUES (67, 6, 21, 2, '咨询教材', '大学物理上册有实验指导吗？', 1, '2026-01-20 09:30:00');
INSERT INTO `message` VALUES (68, 21, 6, 2, '回复：咨询教材', '书中有部分实验内容，详细的需要另外买实验指导书。', 1, '2026-01-20 10:10:00');
INSERT INTO `message` VALUES (69, 7, 22, 2, '咨询教材', '电路这本书难度大吗？', 1, '2026-01-20 13:30:00');
INSERT INTO `message` VALUES (70, 22, 7, 2, '回复：咨询教材', '有一定难度，需要好好学习。', 1, '2026-01-20 14:10:00');
INSERT INTO `message` VALUES (87, NULL, 13, 1, '账号异常提醒', '您的账号因违规操作已被暂时禁用，如有疑问请联系客服。', 0, '2026-01-25 10:00:00');
INSERT INTO `message` VALUES (88, NULL, 16, 1, '账号异常提醒', '您的账号因违规操作已被暂时禁用，如有疑问请联系客服。', 0, '2026-01-25 10:01:00');
INSERT INTO `message` VALUES (89, NULL, 22, 1, '账号异常提醒', '您的账号因违规操作已被暂时禁用，如有疑问请联系客服。', 0, '2026-01-25 10:02:00');
INSERT INTO `message` VALUES (90, NULL, 99, 1, '教材审核未通过', '您发布的教材《公务员考试真题汇编》审核未通过，原因：图片不清晰，请重新上传清晰的教材照片。', 0, '2026-01-25 11:00:00');
INSERT INTO `message` VALUES (91, NULL, 100, 1, '教材审核未通过', '您发布的教材《考研复试英语口语突破》审核未通过，原因：价格设置不合理，请参考市场价格重新定价。', 0, '2026-01-25 11:01:00');
INSERT INTO `message` VALUES (92, NULL, 1, 1, '您的教材已售出', '恭喜！您发布的教材《数据结构与算法分析》已成功售出。', 1, '2026-01-21 14:30:00');
INSERT INTO `message` VALUES (93, NULL, 2, 1, '您的教材已售出', '恭喜！您发布的教材《深入理解计算机系统》已成功售出。', 1, '2026-01-22 10:40:00');
INSERT INTO `message` VALUES (94, NULL, 3, 1, '您的教材已售出', '恭喜！您发布的教材《Java核心技术》已成功售出。', 1, '2026-01-22 15:55:00');
INSERT INTO `message` VALUES (95, NULL, 6, 1, '您的教材已售出', '恭喜！您发布的教材《高等数学上册》已成功售出。', 1, '2026-01-20 15:10:00');
INSERT INTO `message` VALUES (96, NULL, 7, 1, '您的教材已售出', '恭喜！您发布的教材《高等数学下册》已成功售出。', 1, '2026-01-20 16:40:00');
INSERT INTO `message` VALUES (97, 20, 2, 2, NULL, '你好\n', 0, '2026-01-28 13:54:53');
INSERT INTO `message` VALUES (98, 20, 2, 2, NULL, '你好\n', 0, '2026-01-28 13:54:53');
INSERT INTO `message` VALUES (99, 20, 2, 2, NULL, '你好', 0, '2026-01-28 13:57:48');
INSERT INTO `message` VALUES (100, 20, 2, 2, NULL, '你好\n', 0, '2026-01-28 14:07:13');
INSERT INTO `message` VALUES (101, NULL, 29, 1, '新的购买申请', '陆明 想要购买您的教材《外国文学史（第三版）下册》', 0, '2026-01-29 09:44:24');
INSERT INTO `message` VALUES (102, NULL, 30, 1, '订单已确认', '您购买《外国文学史（第三版）下册》的订单已被卖家确认', 0, '2026-01-29 09:55:22');
INSERT INTO `message` VALUES (103, 4, 1, 2, NULL, 'nihao\n', 0, '2026-02-02 10:47:27');
INSERT INTO `message` VALUES (104, 1, 4, 2, NULL, '有什么问题吗\n', 0, '2026-02-02 10:57:05');
INSERT INTO `message` VALUES (105, 4, 1, 2, NULL, '怎么卖的书\n', 0, '2026-02-02 10:59:44');
INSERT INTO `message` VALUES (106, 1, 4, 2, NULL, '看价格，可以小刀一下\n', 0, '2026-02-02 11:00:05');
INSERT INTO `message` VALUES (107, 4, 1, 2, NULL, '好的好的\n', 0, '2026-02-02 11:00:16');
INSERT INTO `message` VALUES (108, 4, 1, 2, NULL, 'OK\n', 0, '2026-02-02 11:10:05');
INSERT INTO `message` VALUES (109, 1, 2, 2, NULL, '你好\n', 0, '2026-02-06 15:30:44');
INSERT INTO `message` VALUES (110, 1, 2, 2, NULL, 'zaim\n', 0, '2026-02-06 15:38:18');
INSERT INTO `message` VALUES (111, 2, 1, 2, NULL, '在的', 0, '2026-02-06 15:39:46');
INSERT INTO `message` VALUES (112, 1, 2, 2, NULL, '书怎么买的\n', 0, '2026-02-06 15:42:38');
INSERT INTO `message` VALUES (113, 2, 1, 2, NULL, '标价就是\n', 0, '2026-02-06 15:42:55');
INSERT INTO `message` VALUES (114, 1, 2, 2, NULL, '可以便宜一点吗\n', 0, '2026-02-06 15:43:09');
INSERT INTO `message` VALUES (115, NULL, 2, 1, '新的购买申请', 'niko 想要购买您的教材《计算机组成与设计：硬件/软件接口（原书第5版）》', 0, '2026-02-06 15:53:28');
INSERT INTO `message` VALUES (116, NULL, 1, 1, '订单已确认', '您购买《计算机组成与设计：硬件/软件接口（原书第5版）》的订单已被卖家确认', 0, '2026-02-06 15:57:59');
INSERT INTO `message` VALUES (117, NULL, 4, 1, '新的购买申请', 'niko 想要购买您的教材《人工智能：一种现代的方法（第3版）》', 0, '2026-02-10 14:20:29');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告内容',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '欢迎使用二手教材交易系统', '本系统旨在为高校学生提供一个便捷的二手教材交易平台，实现教材资源的循环利用，降低学生购书成本。请遵守交易规则，诚信交易！', 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `notice` VALUES (2, '春节期间交易注意事项', '尊敬的用户：\n\n春节期间（2月8日-2月15日），平台正常运营，但快递物流可能会有延迟。建议大家：\n1. 尽量选择当面交易\n2. 如需邮寄，请提前与卖家沟通好发货时间\n3. 保持联系方式畅通\n\n祝大家春节快乐！', 1, '2026-01-25 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (3, '平台升级维护通知', '为了给大家提供更好的服务，平台将于1月28日凌晨2:00-6:00进行系统升级维护，期间可能无法访问，请大家提前安排好交易事宜。给您带来不便，敬请谅解！', 1, '2026-01-24 15:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (4, '诚信交易倡议书', '亲爱的用户们：\n\n为了营造良好的交易环境，我们倡议：\n1. 如实描述教材状态，不夸大不隐瞒\n2. 按时履行交易承诺，不随意取消订单\n3. 文明沟通，相互尊重\n4. 发现问题及时联系平台客服\n\n让我们共同维护诚信交易环境！', 1, '2026-01-23 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (5, '新用户注册送优惠券', '新用户注册即送10元优惠券！\n\n活动时间：即日起至2月29日\n使用规则：\n1. 单笔订单满50元可用\n2. 每个用户限领一张\n3. 有效期30天\n\n快来注册领取吧！', 1, '2026-01-22 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (6, '教材发布规范说明', '为了提高教材信息质量，请大家在发布教材时注意：\n\n1. 标题：书名+版本+作者\n2. 图片：至少上传3张清晰照片（封面、内页、瑕疵处）\n3. 描述：详细说明新旧程度、是否有笔记、适用课程等\n4. 价格：合理定价，参考原价的3-7折\n\n感谢配合！', 1, '2026-01-21 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (7, '防诈骗提醒', '近期发现有不法分子冒充买家/卖家进行诈骗，请大家注意：\n\n1. 不要轻信低价陷阱\n2. 不要在平台外进行交易\n3. 不要向陌生人转账\n4. 遇到可疑情况及时举报\n\n平台客服不会主动要求您提供密码或验证码！', 1, '2026-01-20 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (8, '考研资料专区上线', '考研的同学们注意啦！\n\n平台新增考研资料专区，涵盖：\n- 考研英语真题及解析\n- 考研数学复习全书\n- 考研政治知识点精讲\n- 各专业课参考书\n\n祝大家考研顺利！', 1, '2026-01-19 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (9, '开学季活动开启', '开学季来啦！平台推出系列优惠活动：\n\n1. 新用户注册送优惠券\n2. 推荐好友双方各得5元\n3. 每日签到领积分\n4. 积分可兑换优惠券\n\n活动时间：2月1日-3月31日', 1, '2026-01-18 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (10, '用户实名认证说明', '为了保障交易安全，平台鼓励用户进行实名认证。\n\n认证方式：\n1. 上传学生证照片\n2. 填写真实姓名和学号\n3. 等待审核（1-3个工作日）\n\n认证后可获得：\n- 认证标识\n- 更高的信用度\n- 优先推荐\n\n快来认证吧！', 1, '2026-01-17 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (11, '平台使用指南', '欢迎使用二手教材交易平台！\n\n【买家指南】\n1. 浏览教材列表，找到心仪的书\n2. 查看详情，联系卖家咨询\n3. 下单购买，约定交易方式\n4. 完成交易，互相评价\n\n【卖家指南】\n1. 发布教材信息\n2. 等待买家咨询\n3. 确认订单\n4. 完成交易\n\n有问题请联系客服！', 1, '2026-01-16 10:00:00', '2026-01-26 20:59:14');
INSERT INTO `notice` VALUES (12, '教材分类调整通知', '为了更好地服务大家，我们对教材分类进行了优化调整：\n\n新增分类：\n- 考研考公类\n- 职业资格类\n\n合并分类：\n- 将原\"其他\"类细分为多个子类\n\n请大家在发布教材时选择正确的分类，谢谢配合！', 1, '2026-01-15 10:00:00', '2026-01-26 20:59:24');
INSERT INTO `notice` VALUES (13, '平台用户突破10000人', '感谢大家的支持和信任！\n\n截至今日，平台注册用户已突破10000人，累计成交订单超过5000笔。\n\n为了回馈大家，我们将推出系列优惠活动，敬请期待！\n\n让我们继续努力，打造更好的二手教材交易平台！', 1, '2026-01-14 15:00:00', '2026-01-26 20:59:24');
INSERT INTO `notice` VALUES (14, '违规用户处理公告', '近期我们发现部分用户存在违规行为，包括：\n- 发布虚假信息\n- 恶意取消订单\n- 不文明交流\n\n经核实，以下账号已被禁用：\n- user_xxx（发布虚假信息）\n- user_yyy（恶意取消订单）\n\n请大家遵守平台规则，共同维护良好的交易环境！', 1, '2026-01-13 10:00:00', '2026-01-26 20:59:24');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `textbook_id` bigint NOT NULL COMMENT '教材ID',
  `seller_id` bigint NOT NULL COMMENT '卖家ID',
  `buyer_id` bigint NOT NULL COMMENT '买家ID',
  `price` decimal(10, 2) NOT NULL COMMENT '成交价格',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '买家备注',
  `trading_point_id` bigint NULL DEFAULT NULL COMMENT '交易点ID',
  `meeting_time` datetime NULL DEFAULT NULL COMMENT '约定见面时间',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态（0待确认 1已确认 2已完成 3已取消）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_textbook_id`(`textbook_id` ASC) USING BTREE,
  INDEX `idx_seller_id`(`seller_id` ASC) USING BTREE,
  INDEX `idx_buyer_id`(`buyer_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'ORD202601260001', 1, 1, 11, 45.00, '希望书籍保存完好，谢谢！', NULL, NULL, 2, '2026-01-20 10:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (2, 'ORD202601260002', 2, 2, 12, 75.00, '需要尽快发货', NULL, NULL, 2, '2026-01-20 14:20:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (3, 'ORD202601260003', 3, 3, 13, 80.00, NULL, NULL, NULL, 2, '2026-01-21 09:15:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (4, 'ORD202601260004', 4, 4, 14, 65.00, '可以当面交易吗？', NULL, NULL, 1, '2026-01-21 16:45:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (5, 'ORD202601260005', 5, 5, 15, 25.00, NULL, NULL, NULL, 1, '2026-01-22 11:20:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (6, 'ORD202601260006', 6, 15, 16, 55.00, '希望有配套资料', NULL, NULL, 2, '2026-01-22 13:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (7, 'ORD202601260007', 7, 1, 17, 50.00, NULL, NULL, NULL, 2, '2026-01-23 08:50:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (8, 'ORD202601260008', 8, 2, 18, 52.00, '需要发票', NULL, NULL, 1, '2026-01-23 15:10:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (9, 'ORD202601260009', 9, 3, 19, 45.00, NULL, NULL, NULL, 0, '2026-01-24 10:00:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (10, 'ORD202601260010', 10, 4, 20, 65.00, '请问有笔记吗？', NULL, NULL, 0, '2026-01-24 14:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (11, 'ORD202601260011', 11, 5, 21, 48.00, '西瓜书，很期待！', NULL, NULL, 2, '2026-01-24 16:20:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (12, 'ORD202601260012', 12, 15, 22, 70.00, NULL, NULL, NULL, 2, '2026-01-25 09:40:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (13, 'ORD202601260013', 13, 1, 23, 55.00, '软件工程必备', NULL, NULL, 1, '2026-01-25 11:15:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (14, 'ORD202601260014', 14, 2, 24, 52.00, NULL, NULL, NULL, 1, '2026-01-25 13:50:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (15, 'ORD202601260015', 15, 3, 25, 38.00, '希望书籍干净', NULL, NULL, 2, '2026-01-25 15:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (16, 'ORD202601260016', 16, 6, 26, 20.00, NULL, NULL, NULL, 2, '2026-01-19 10:20:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (17, 'ORD202601260017', 17, 7, 27, 18.00, '配套上册一起买', NULL, NULL, 2, '2026-01-19 10:25:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (18, 'ORD202601260018', 18, 8, 28, 15.00, NULL, NULL, NULL, 2, '2026-01-20 09:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (19, 'ORD202601260019', 19, 9, 29, 17.00, '考试必备', NULL, NULL, 2, '2026-01-20 14:15:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (20, 'ORD202601260020', 20, 10, 30, 22.00, NULL, NULL, NULL, 1, '2026-01-21 11:40:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (26, 'ORD202601260026', 36, 16, 1, 25.00, NULL, NULL, NULL, 2, '2026-01-18 10:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (27, 'ORD202601260027', 37, 17, 2, 24.00, '经济学经典', NULL, NULL, 2, '2026-01-18 14:20:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (28, 'ORD202601260028', 38, 18, 3, 36.00, NULL, NULL, NULL, 2, '2026-01-19 11:15:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (29, 'ORD202601260029', 39, 19, 4, 30.00, '会计专业必备', NULL, NULL, 1, '2026-01-19 15:40:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (30, 'ORD202601260030', 40, 20, 5, 26.00, NULL, NULL, NULL, 1, '2026-01-20 10:25:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (31, 'ORD202601260031', 46, 21, 6, 20.00, NULL, NULL, NULL, 2, '2026-01-20 13:30:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (32, 'ORD202601260032', 47, 22, 7, 19.00, '物理学习', NULL, NULL, 2, '2026-01-21 09:45:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (33, 'ORD202601260033', 48, 23, 8, 24.00, NULL, NULL, NULL, 2, '2026-01-21 14:20:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (34, 'ORD202601260034', 49, 24, 9, 28.00, '电路分析', NULL, NULL, 1, '2026-01-22 10:50:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (35, 'ORD202601260035', 50, 25, 10, 26.00, NULL, NULL, NULL, 1, '2026-01-22 15:15:00', '2026-01-26 20:58:55');
INSERT INTO `orders` VALUES (51, '2016688844747395072', 59, 29, 30, 19.00, '20260129', NULL, NULL, 1, '2026-01-29 09:44:24', '2026-01-29 09:44:24');
INSERT INTO `orders` VALUES (52, '2019680826612609024', 14, 2, 1, 52.00, '记得带手机', 5, '2026-02-06 12:00:00', 1, '2026-02-06 15:53:28', '2026-02-06 15:53:28');
INSERT INTO `orders` VALUES (53, '2021106979344773120', 10, 4, 1, 65.00, '', 5, '2026-02-10 16:00:00', 0, '2026-02-10 14:20:29', '2026-02-10 14:20:29');

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `from_user_id` bigint NOT NULL COMMENT '评价人ID',
  `to_user_id` bigint NOT NULL COMMENT '被评价人ID',
  `score` tinyint NOT NULL COMMENT '评分（1-5）',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_to_user_id`(`to_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (1, 1, 11, 1, 5, '书籍质量很好，卖家态度也很好，推荐！', '2026-01-21 15:00:00');
INSERT INTO `review` VALUES (2, 1, 1, 11, 5, '买家很守信用，交易愉快！', '2026-01-21 15:30:00');
INSERT INTO `review` VALUES (3, 2, 12, 2, 5, '书籍保存完好，物超所值！', '2026-01-22 11:00:00');
INSERT INTO `review` VALUES (4, 2, 2, 12, 5, '交易顺利，好评！', '2026-01-22 11:30:00');
INSERT INTO `review` VALUES (5, 3, 13, 3, 5, '全新正版，非常满意！', '2026-01-22 16:20:00');
INSERT INTO `review` VALUES (6, 3, 3, 13, 5, '买家很nice，推荐！', '2026-01-22 16:50:00');
INSERT INTO `review` VALUES (7, 6, 16, 15, 4, '书籍不错，就是有点旧。', '2026-01-23 17:00:00');
INSERT INTO `review` VALUES (8, 6, 15, 16, 5, '交易愉快！', '2026-01-23 17:30:00');
INSERT INTO `review` VALUES (9, 7, 17, 1, 5, '操作系统经典教材，很满意！', '2026-01-24 09:30:00');
INSERT INTO `review` VALUES (10, 7, 1, 17, 5, '好评，谢谢！', '2026-01-24 10:00:00');
INSERT INTO `review` VALUES (11, 11, 21, 5, 5, '西瓜书质量很好，推荐！', '2026-01-25 10:00:00');
INSERT INTO `review` VALUES (12, 11, 5, 21, 5, '交易顺利，好评！', '2026-01-25 10:30:00');
INSERT INTO `review` VALUES (13, 12, 22, 15, 5, 'C++经典教材，很满意！', '2026-01-25 16:00:00');
INSERT INTO `review` VALUES (14, 12, 15, 22, 5, '买家很守信用！', '2026-01-25 16:30:00');
INSERT INTO `review` VALUES (15, 15, 25, 3, 4, '书有点旧，但内容完整。', '2026-01-26 11:00:00');
INSERT INTO `review` VALUES (16, 15, 3, 25, 5, '交易愉快！', '2026-01-26 11:30:00');
INSERT INTO `review` VALUES (17, 16, 26, 6, 5, '高数教材很好，推荐！', '2026-01-20 15:00:00');
INSERT INTO `review` VALUES (18, 16, 6, 26, 5, '好评！', '2026-01-20 15:30:00');
INSERT INTO `review` VALUES (19, 17, 27, 7, 5, '配套教材，很满意！', '2026-01-20 16:00:00');
INSERT INTO `review` VALUES (20, 17, 7, 27, 5, '交易顺利！', '2026-01-20 16:30:00');
INSERT INTO `review` VALUES (21, 18, 28, 8, 5, '线代经典教材，好评！', '2026-01-21 10:00:00');
INSERT INTO `review` VALUES (22, 18, 8, 28, 5, '谢谢支持！', '2026-01-21 10:30:00');
INSERT INTO `review` VALUES (23, 19, 29, 9, 5, '概率论教材不错！', '2026-01-21 15:00:00');
INSERT INTO `review` VALUES (24, 19, 9, 29, 5, '好评！', '2026-01-21 15:30:00');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限，可管理其他管理员和系统配置', 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `role` VALUES (2, '内容管理员', 'CONTENT_ADMIN', '管理教材、分类、公告、轮播图等内容相关功能', 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');
INSERT INTO `role` VALUES (3, '订单管理员', 'ORDER_ADMIN', '管理订单、用户、评价等交易相关功能', 1, '2026-01-26 13:45:16', '2026-01-26 13:45:16');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1, 1);
INSERT INTO `role_menu` VALUES (2, 1, 2);
INSERT INTO `role_menu` VALUES (3, 1, 3);
INSERT INTO `role_menu` VALUES (4, 1, 4);
INSERT INTO `role_menu` VALUES (5, 1, 5);
INSERT INTO `role_menu` VALUES (6, 1, 6);
INSERT INTO `role_menu` VALUES (7, 1, 7);
INSERT INTO `role_menu` VALUES (8, 1, 8);
INSERT INTO `role_menu` VALUES (9, 1, 9);
INSERT INTO `role_menu` VALUES (10, 1, 10);
INSERT INTO `role_menu` VALUES (11, 1, 11);
INSERT INTO `role_menu` VALUES (12, 1, 12);
INSERT INTO `role_menu` VALUES (13, 1, 13);
INSERT INTO `role_menu` VALUES (14, 1, 14);
INSERT INTO `role_menu` VALUES (27, 1, 16);
INSERT INTO `role_menu` VALUES (28, 1, 17);
INSERT INTO `role_menu` VALUES (29, 1, 18);
INSERT INTO `role_menu` VALUES (30, 1, 19);
INSERT INTO `role_menu` VALUES (16, 2, 1);
INSERT INTO `role_menu` VALUES (17, 2, 3);
INSERT INTO `role_menu` VALUES (18, 2, 4);
INSERT INTO `role_menu` VALUES (19, 2, 6);
INSERT INTO `role_menu` VALUES (20, 2, 7);
INSERT INTO `role_menu` VALUES (21, 2, 9);
INSERT INTO `role_menu` VALUES (22, 2, 10);
INSERT INTO `role_menu` VALUES (23, 3, 1);
INSERT INTO `role_menu` VALUES (24, 3, 2);
INSERT INTO `role_menu` VALUES (25, 3, 5);
INSERT INTO `role_menu` VALUES (26, 3, 8);

-- ----------------------------
-- Table structure for textbook
-- ----------------------------
DROP TABLE IF EXISTS `textbook`;
CREATE TABLE `textbook`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '发布者ID',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '书名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `publisher` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出版社',
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ISBN号',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `price` decimal(10, 2) NOT NULL COMMENT '售价',
  `condition_level` tinyint NULL DEFAULT 8 COMMENT '新旧程度（1-10成新）',
  `course` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '适用课程',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图',
  `campus_id` bigint NULL DEFAULT NULL COMMENT '发布校区ID',
  `course_id` bigint NULL DEFAULT NULL COMMENT '关联课程ID',
  `has_notes` tinyint NULL DEFAULT 0 COMMENT '有无笔记（0无 1有）',
  `has_highlight` tinyint NULL DEFAULT 0 COMMENT '有无划线（0无 1有）',
  `has_missing_pages` tinyint NULL DEFAULT 0 COMMENT '是否缺页（0否 1是）',
  `has_water_stains` tinyint NULL DEFAULT 0 COMMENT '有无水渍（0无 1有）',
  `input_mode` tinyint NULL DEFAULT 0 COMMENT '录入方式（0手动 1ISBN扫描）',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0下架 1在售 2已售）',
  `audit_status` tinyint NULL DEFAULT 1 COMMENT '审核状态（0待审核 1通过 2拒绝）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教材表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of textbook
-- ----------------------------
INSERT INTO `textbook` VALUES (1, 1, 1, '数据结构与算法分析（Java语言描述）', 'Mark Allen Weiss', '机械工业出版社', '9787111528395', 89.00, 45.00, 9, '数据结构', '经典数据结构教材，几乎全新，只用过一学期，无笔记无划痕。', '/images/books/details/283185f0f4ed4045b5050e6fefb740c1.png', 1, NULL, 0, 0, 0, 0, 0, 159, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (2, 2, 1, '深入理解计算机系统（原书第3版）', 'Randal E. Bryant', '机械工业出版社', '9787111544937', 139.00, 75.00, 8, '计算机系统', 'CSAPP经典教材，有少量笔记，整体保存良好。', '/images/books/details/03090c4e773643b98ae2c502bd2d02e0.png', 1, NULL, 0, 0, 0, 0, 0, 235, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (3, 3, 1, 'Java核心技术 卷I：基础知识（原书第11版）', 'Cay S. Horstmann', '机械工业出版社', '9787111612728', 149.00, 80.00, 9, 'Java程序设计', '全新正版，仅翻阅过几页，适合Java初学者。', '/images/books/details/a4327ea2541940ac9f7aa7e1e9ccbc83.png', 1, NULL, 0, 0, 0, 0, 0, 190, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (4, 4, 1, '算法导论（原书第3版）', 'Thomas H. Cormen', '机械工业出版社', '9787111407010', 128.00, 65.00, 7, '算法设计与分析', '有使用痕迹，部分章节有笔记，但不影响阅读。', '/images/books/details/b99d66191008485c97d32209cc6a9ca7.png', 1, NULL, 0, 0, 0, 0, 0, 281, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (5, 5, 1, '计算机网络（第7版）', '谢希仁', '电子工业出版社', '9787121302954', 49.80, 25.00, 8, '计算机网络', '八成新，有少量划线，内容完整。', '/images/books/details/312d4aab65614fad8d79526e856d80bf.png', 1, NULL, 0, 0, 0, 0, 0, 170, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (6, 15, 1, 'Python编程：从入门到实践（第2版）', 'Eric Matthes', '人民邮电出版社', '9787115546081', 99.00, 55.00, 9, 'Python程序设计', '九成新，无笔记，适合Python入门学习。', '/images/books/details/d0a4d7db77ef45a687f6257869c899c0.png', 1, NULL, 0, 0, 0, 0, 0, 313, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (7, 1, 1, '操作系统概念（原书第9版）', 'Abraham Silberschatz', '高等教育出版社', '9787040483062', 99.00, 50.00, 8, '操作系统', '有笔记和标注，但整体保存良好。', '/images/books/details/68b9659df1304c77840fa67a33d6213e.png', 1, NULL, 0, 0, 0, 0, 0, 203, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (8, 2, 1, '数据库系统概念（原书第6版）', 'Abraham Silberschatz', '机械工业出版社', '9787111375296', 98.00, 52.00, 9, '数据库原理', '几乎全新，仅使用一学期，无任何标记。', '/images/books/details/6f3dbdf2c71843269294d09d66cf4f97.png', 1, NULL, 0, 0, 0, 0, 0, 240, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (9, 3, 1, '编译原理（原书第2版）', 'Alfred V. Aho', '机械工业出版社', '9787111251217', 89.00, 45.00, 7, '编译原理', '有一定使用痕迹，部分页面有笔记。', '/images/books/details/1b6422d86366481586c20e50de4e58a8.png', 1, NULL, 0, 0, 0, 0, 0, 149, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (10, 4, 1, '人工智能：一种现代的方法（第3版）', 'Stuart Russell', '清华大学出版社', '9787302331094', 119.00, 65.00, 8, '人工智能导论', '八成新，有少量笔记，内容完整清晰。', '/images/books/details/d5536eba067f419f870651adb0838c92.png', 1, NULL, 0, 0, 0, 0, 0, 269, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (11, 5, 1, '机器学习', '周志华', '清华大学出版社', '9787302423287', 88.00, 48.00, 9, '机器学习', '西瓜书，九成新，无笔记，经典教材。', '/images/books/details/eef5df135b804501a8cbe3b0ecd764de.png', 1, NULL, 0, 0, 0, 0, 0, 390, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (12, 15, 1, 'C++ Primer（第5版）', 'Stanley B. Lippman', '电子工业出版社', '9787121155352', 128.00, 70.00, 8, 'C++程序设计', '有使用痕迹，部分章节有笔记标注。', '/images/books/details/a190c22fa27a42a7b43e6942f8244268.png', 1, NULL, 0, 0, 0, 0, 0, 202, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (13, 1, 1, '软件工程：实践者的研究方法（原书第8版）', 'Roger S. Pressman', '机械工业出版社', '9787111562979', 99.00, 55.00, 9, '软件工程', '九成新，几乎无使用痕迹。', '/images/books/details/d1a3130b80e04c6a88d85865e66f3ec2.png', 1, NULL, 0, 0, 0, 0, 0, 198, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (14, 2, 1, '计算机组成与设计：硬件/软件接口（原书第5版）', 'David A. Patterson', '机械工业出版社', '9787111504825', 99.00, 52.00, 8, '计算机组成原理', '有笔记，整体保存良好。', '/images/books/details/11eaa03917044480911ce83c018c93fc.png', 1, NULL, 0, 0, 0, 0, 0, 165, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (15, 3, 1, 'Linux内核设计与实现（原书第3版）', 'Robert Love', '机械工业出版社', '9787111384991', 69.00, 38.00, 7, 'Linux系统编程', '有一定使用痕迹，部分页面有标注。', '/images/books/details/0f8a0eff3242456e932a6a5726fa4422.png', 1, NULL, 0, 0, 0, 0, 0, 135, 0, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (16, 6, 2, '高等数学（第七版）上册', '同济大学数学系', '高等教育出版社', '9787040396621', 39.20, 20.00, 8, '高等数学', '八成新，有少量笔记，适合大一学生。', '/images/books/details/219e3c4455a540c78687b1834685ec2c.png', 1, NULL, 0, 0, 0, 0, 0, 246, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (17, 7, 2, '高等数学（第七版）下册', '同济大学数学系', '高等教育出版社', '9787040396638', 36.60, 18.00, 8, '高等数学', '配套上册使用，同样八成新。', '/images/books/details/049983e80d8d41abad341502c2433c0b.png', 1, NULL, 0, 0, 0, 0, 0, 224, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (18, 8, 2, '线性代数（第六版）', '同济大学数学系', '高等教育出版社', '9787040396614', 29.30, 15.00, 9, '线性代数', '九成新，几乎无笔记，保存完好。', '/images/books/details/b0b7e8d4fc924199b0ea52bac7403d2e.png', 1, NULL, 0, 0, 0, 0, 0, 199, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (19, 9, 2, '概率论与数理统计（第四版）', '盛骤', '高等教育出版社', '9787040238969', 33.00, 17.00, 8, '概率论与数理统计', '有笔记和习题答案，适合备考。', '/images/books/details/7f0b17239c7a46f18c92531e159c6017.png', 1, NULL, 0, 0, 0, 0, 0, 268, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (20, 10, 2, '数学分析（第四版）上册', '华东师范大学数学系', '高等教育出版社', '9787040295917', 42.00, 22.00, 7, '数学分析', '有一定使用痕迹，部分章节有详细笔记。', '/images/books/details/29fc52094ca6420f9419f0ca7a09bae6.png', 1, NULL, 0, 0, 0, 0, 0, 157, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (21, 6, 2, '数学分析（第四版）下册', '华东师范大学数学系', '高等教育出版社', '9787040295924', 39.00, 20.00, 7, '数学分析', '配套上册，同样有笔记。', '/images/books/details/979cf5e0667f45c2977c0670190b92b1.png', 1, NULL, 0, 0, 0, 0, 0, 146, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (22, 7, 2, '高等代数（第四版）', '北京大学数学系', '高等教育出版社', '9787040396683', 38.00, 20.00, 8, '高等代数', '八成新，有少量笔记。', '/images/books/details/8e059d9e78f34ee09961a978040f47a9.png', 1, NULL, 0, 0, 0, 0, 0, 179, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (23, 8, 2, '数学物理方程（第三版）', '谷超豪', '高等教育出版社', '9787040163964', 35.00, 18.00, 9, '数学物理方程', '九成新，几乎无使用痕迹。', '/images/books/details/a0fd8b08985d4d97881c04f512d5bd6c.png', 1, NULL, 0, 0, 0, 0, 0, 124, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (24, 9, 2, '实变函数论（第三版）', '周民强', '北京大学出版社', '9787301087985', 32.00, 16.00, 8, '实变函数', '有笔记，整体保存良好。', '/images/books/details/e644cc82d20e40fb9f75147bc151991c.png', 1, NULL, 0, 0, 0, 0, 0, 99, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (25, 10, 2, '复变函数论（第四版）', '钟玉泉', '高等教育出版社', '9787040396690', 33.00, 17.00, 9, '复变函数', '九成新，无笔记。', '/images/books/details/63ed89169b074b80a077a112eceee3d1.png', 1, NULL, 0, 0, 0, 0, 0, 135, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (26, 11, 3, '新视野大学英语读写教程1（第三版）', '郑树棠', '外语教学与研究出版社', '9787513576932', 49.90, 25.00, 8, '大学英语', '八成新，有少量笔记和标注。', '/images/books/details/43b9e9cf10784b9ab4418777d883c215.png', 1, NULL, 0, 0, 0, 0, 0, 290, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (27, 12, 3, '新视野大学英语读写教程2（第三版）', '郑树棠', '外语教学与研究出版社', '9787513576949', 49.90, 25.00, 8, '大学英语', '配套教材，同样八成新。', '/images/books/details/b8bc02ee6794496c8a24170beff6df28.png', 1, NULL, 0, 0, 0, 0, 0, 268, 2, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (28, 13, 3, '新视野大学英语读写教程3（第三版）', '郑树棠', '外语教学与研究出版社', '9787513576956', 49.90, 26.00, 9, '大学英语', '九成新，几乎无使用痕迹。', '/images/books/covers/book_28.jpg', 1, NULL, 0, 0, 0, 0, 0, 246, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (29, 14, 3, '新视野大学英语读写教程4（第三版）', '郑树棠', '外语教学与研究出版社', '9787513576963', 49.90, 26.00, 9, '大学英语', '九成新，保存完好。', '/images/books/details/3c521b06c70041be8716ea67c14143be.png', 1, NULL, 0, 0, 0, 0, 0, 224, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (30, 11, 3, '大学英语四级真题详解', '王长喜', '中国石化出版社', '9787511456789', 38.00, 20.00, 7, '英语四级', '有笔记和答案标注，适合备考。', '/images/books/details/84c37ff1a40a4918bc7e593d0d756ae5.png', 1, NULL, 0, 0, 0, 0, 0, 313, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (31, 12, 3, '大学英语六级真题详解', '王长喜', '中国石化出版社', '9787511456796', 42.00, 22.00, 7, '英语六级', '有笔记，部分题目已做。', '/images/books/details/a7367c61fa99431bb76d678dc1fd5919.png', 1, NULL, 0, 0, 0, 0, 0, 290, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (32, 13, 3, '雅思真题集14', 'Cambridge ESOL', '外语教学与研究出版社', '9787521316940', 68.00, 38.00, 8, '雅思备考', '八成新，有少量笔记。', '/images/books/covers/book_32.jpg', 1, NULL, 0, 0, 0, 0, 0, 198, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (33, 14, 3, '托福官方指南（第5版）', 'ETS', '群言出版社', '9787519302450', 128.00, 70.00, 9, '托福备考', '九成新，几乎全新。', '/images/books/details/a5ab8a5097634fb9884f16c1f1674a6f.png', 1, NULL, 0, 0, 0, 0, 0, 168, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (34, 11, 3, '英语语法新思维：初级教程', '张满胜', '群言出版社', '9787802561489', 45.00, 24.00, 8, '英语语法', '有笔记，适合语法学习。', '/images/books/details/91a6e3c88c78469f8b51f6f7cf0d7497.png', 1, NULL, 0, 0, 0, 0, 0, 146, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (35, 12, 3, '英语语法新思维：中级教程', '张满胜', '群言出版社', '9787802561496', 48.00, 26.00, 8, '英语语法', '配套初级教程，同样有笔记。', '/images/books/details/fc009a2e335148da9ae459e656d82c5e.png', 1, NULL, 0, 0, 0, 0, 0, 135, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (36, 16, 4, '西方经济学（微观部分·第七版）', '高鸿业', '中国人民大学出版社', '9787300255347', 48.00, 25.00, 8, '微观经济学', '八成新，有笔记和课后习题答案。', '/images/books/covers/book_36.jpg', 1, NULL, 0, 0, 0, 0, 0, 234, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (37, 17, 4, '西方经济学（宏观部分·第七版）', '高鸿业', '中国人民大学出版社', '9787300255354', 46.00, 24.00, 8, '宏观经济学', '配套微观部分，同样八成新。', '/images/books/details/b942008f40f24a1f92338a9d8612a0ba.png', 1, NULL, 0, 0, 0, 0, 0, 224, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (38, 18, 4, '管理学（第13版）', 'Stephen P. Robbins', '中国人民大学出版社', '9787300248592', 68.00, 36.00, 9, '管理学原理', '九成新，经典管理学教材。', '/images/books/details/c11bb0fae868413db1a33a11d9c41dfa.png', 1, NULL, 0, 0, 0, 0, 0, 268, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (39, 19, 4, '会计学原理（第21版）', 'Jerry J. Weygandt', '中国人民大学出版社', '9787300242842', 58.00, 30.00, 8, '会计学', '有笔记，适合会计专业学生。', '/images/books/details/d5f5ba6ba72745c8b7585f666657be4d.png', 1, NULL, 0, 0, 0, 0, 0, 199, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (40, 20, 4, '市场营销学（第6版）', '吴健安', '高等教育出版社', '9787040507331', 49.00, 26.00, 9, '市场营销', '九成新，几乎无使用痕迹。', '/images/books/details/114a4073f8954c2d8cd403524c467cd0.png', 1, NULL, 0, 0, 0, 0, 0, 179, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (41, 16, 4, '财务管理学（第8版）', '荆新', '中国人民大学出版社', '9787300258232', 52.00, 28.00, 8, '财务管理', '八成新，有少量笔记。', '/images/books/covers/book_41.jpg', 1, NULL, 0, 0, 0, 0, 0, 156, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (42, 17, 4, '战略管理（第5版）', '杨锡怀', '高等教育出版社', '9787040507348', 55.00, 30.00, 9, '战略管理', '九成新，保存完好。', '/images/books/details/90acbea8a3a54e9f84bded970343fe97.png', 1, NULL, 0, 0, 0, 0, 0, 146, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (43, 18, 4, '人力资源管理（第14版）', 'Gary Dessler', '中国人民大学出版社', '9787300248608', 62.00, 34.00, 8, '人力资源管理', '有笔记，整体保存良好。', '/images/books/details/104eb276b08a4ca3866432e663107605.png', 1, NULL, 0, 0, 0, 0, 0, 168, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (44, 19, 4, '运营管理（第13版）', 'Jay Heizer', '中国人民大学出版社', '9787300242859', 68.00, 38.00, 9, '运营管理', '九成新，几乎全新。', '/images/books/details/96e381eeda114fc4a94715260c4aa787.png', 1, NULL, 0, 0, 0, 0, 0, 136, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (45, 20, 4, '金融学（第4版）', '黄达', '中国人民大学出版社', '9787300258249', 58.00, 32.00, 8, '金融学', '八成新，有少量标注。', '/images/books/details/b7fc3e98ed4a408eaa04b34398d51052.png', 1, NULL, 0, 0, 0, 0, 0, 190, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (46, 21, 5, '大学物理（第三版）上册', '张三慧', '清华大学出版社', '9787302396628', 39.00, 20.00, 8, '大学物理', '八成新，有笔记。', '/images/books/details/7a48650f0ff34f7f94b262e91302cd74.png', 1, NULL, 0, 0, 0, 0, 0, 202, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (47, 22, 5, '大学物理（第三版）下册', '张三慧', '清华大学出版社', '9787302396635', 38.00, 19.00, 8, '大学物理', '配套上册，同样八成新。', '/images/books/covers/book_47.jpg', 1, NULL, 0, 0, 0, 0, 0, 189, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (48, 23, 5, '工程力学（第二版）', '范钦珊', '高等教育出版社', '9787040163971', 45.00, 24.00, 9, '工程力学', '九成新，几乎无使用痕迹。', '/images/books/details/6cd4821b5c5846adbd3b39e1772617aa.png', 1, NULL, 0, 0, 0, 0, 0, 157, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (49, 24, 5, '电路（第5版）', '邱关源', '高等教育出版社', '9787040196719', 52.00, 28.00, 8, '电路分析', '有笔记和习题答案，适合学习。', '/images/books/details/49740b7dcaf84c35ae775aa1396d0bab.png', 1, NULL, 0, 0, 0, 0, 0, 235, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (50, 25, 5, '模拟电子技术基础（第5版）', '童诗白', '高等教育出版社', '9787040396706', 48.00, 26.00, 9, '模拟电子技术', '九成新，保存完好。', '/images/books/details/cde209cea5af406e93adacb822be7422.png', 1, NULL, 0, 0, 0, 0, 0, 199, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (51, 21, 5, '数字电子技术基础（第6版）', '阎石', '高等教育出版社', '9787040396713', 46.00, 25.00, 8, '数字电子技术', '八成新，有少量笔记。', '/images/books/covers/book_51.jpg', 1, NULL, 0, 0, 0, 0, 0, 179, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (52, 22, 5, '信号与系统（第3版）', '郑君里', '高等教育出版社', '9787040396720', 49.00, 27.00, 9, '信号与系统', '九成新，几乎全新。', '/images/books/covers/book_52.jpg', 1, NULL, 0, 0, 0, 0, 0, 167, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (53, 23, 5, '自动控制原理（第6版）', '胡寿松', '科学出版社', '9787030392640', 55.00, 30.00, 8, '自动控制原理', '有笔记，整体保存良好。', '/images/books/details/9c8abac6da5244db881d2ba93628b717.png', 1, NULL, 0, 0, 0, 0, 0, 146, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (54, 24, 5, '材料力学（第6版）', '刘鸿文', '高等教育出版社', '9787040396737', 52.00, 28.00, 9, '材料力学', '九成新，无笔记。', '/images/books/details/af1b359e6f7e45f6ae710e12436f5239.png', 1, NULL, 0, 0, 0, 0, 0, 135, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (55, 25, 5, '机械设计（第9版）', '濮良贵', '高等教育出版社', '9787040396744', 58.00, 32.00, 8, '机械设计', '八成新，有少量标注。', '/images/books/details/cd2d8a3b8c6d4ce8b4a0e9c81fab66b8.png', 1, NULL, 0, 0, 0, 0, 0, 157, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (56, 26, 6, '中国文学史（第三版）第一卷', '袁行霈', '高等教育出版社', '9787040396751', 42.00, 22.00, 8, '中国古代文学', '八成新，有笔记。', '/images/books/details/6c2e3f8bf90e4443b8557454af3619c0.png', 1, NULL, 0, 0, 0, 0, 0, 124, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:23:18');
INSERT INTO `textbook` VALUES (57, 27, 6, '中国文学史（第三版）第二卷', '袁行霈', '高等教育出版社', '9787040396768', 45.00, 24.00, 8, '中国古代文学', '配套第一卷，同样八成新。', '/images/books/covers/book_57.jpg', 2, NULL, 0, 0, 0, 0, 0, 112, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:16');
INSERT INTO `textbook` VALUES (58, 28, 6, '外国文学史（第三版）上册', '郑克鲁', '高等教育出版社', '9787040396775', 38.00, 20.00, 9, '外国文学', '九成新，几乎无使用痕迹。', '/images/books/details/eba70761c2da4bf3ab66a14b7f95d306.png', 2, NULL, 0, 0, 0, 0, 0, 99, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:15');
INSERT INTO `textbook` VALUES (59, 29, 6, '外国文学史（第三版）下册', '郑克鲁', '高等教育出版社', '9787040396782', 36.00, 19.00, 9, '外国文学', '配套上册，同样九成新。', '/images/books/details/1a21cddabda6442099dc7643e87c38b9.png', 2, NULL, 0, 0, 0, 0, 0, 91, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:15');
INSERT INTO `textbook` VALUES (60, 30, 6, '艺术概论', '王宏建', '文化艺术出版社', '9787503960789', 48.00, 26.00, 8, '艺术概论', '有笔记，适合艺术专业学生。', '/images/books/details/f01ba0ebf48b45a9b64c7ae7130cf471.png', 3, NULL, 0, 0, 0, 0, 0, 135, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:13');
INSERT INTO `textbook` VALUES (61, 26, 6, '美学原理', '叶朗', '北京大学出版社', '9787301087992', 42.00, 23.00, 9, '美学', '九成新，保存完好。', '/images/books/details/5ac8be072a4d4fde8a098dd7001918d8.png', 3, NULL, 0, 0, 0, 0, 0, 102, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:12');
INSERT INTO `textbook` VALUES (62, 27, 6, '中国美术史', '洪再新', '中国美术学院出版社', '9787550300781', 68.00, 38.00, 8, '中国美术史', '八成新，有少量笔记。', '/images/books/covers/book_62.jpg', 3, NULL, 0, 0, 0, 0, 0, 87, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:11');
INSERT INTO `textbook` VALUES (63, 28, 6, '西方美术史', '李春', '高等教育出版社', '9787040396799', 58.00, 32.00, 9, '西方美术史', '九成新，几乎全新。', '/images/books/details/4ef63bab93c447978d72ab3f2a95fc33.png', 3, NULL, 0, 0, 0, 0, 0, 96, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:11');
INSERT INTO `textbook` VALUES (64, 29, 6, '音乐理论基础', '李重光', '人民音乐出版社', '9787103033685', 35.00, 18.00, 8, '音乐理论', '有笔记，整体保存良好。', '/images/books/details/ab5894d0f8dd459894ce01b9e0321632.png', 4, NULL, 0, 0, 0, 0, 0, 77, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:09');
INSERT INTO `textbook` VALUES (65, 30, 6, '影视艺术概论', '周星', '高等教育出版社', '9787040396805', 45.00, 25.00, 9, '影视艺术', '九成新，无笔记。', '/images/books/details/5b1fb21973c64f0b9a03ba28ef6a20e9.png', 4, NULL, 0, 0, 0, 0, 0, 113, 1, 1, '2026-01-26 20:58:45', '2026-02-06 15:24:07');
INSERT INTO `textbook` VALUES (105, 30, 10, '小猪佩奇', '佩奇', '佩奇', '123456', 15.00, 10.00, 9, '', '非常稀有', '/images/books/details/d71c670ed93e457c984d9161eb28d4e3.png', 4, NULL, 0, 0, 0, 0, 0, 0, 1, 0, '2026-01-29 11:04:18', '2026-02-06 15:24:07');
INSERT INTO `textbook` VALUES (106, 1, 1, '数据结构', '严蔚敏', '清华大学出版社', '9787302023685', 80.00, 50.00, 9, '数据结构', '非常好的书，对计算机数据结构很好', '/images/books/details/f8f65e1ca04d4ab3addc0e29b5303fb7.png', 4, NULL, 1, 1, 1, 1, 1, 0, 1, 0, '2026-02-06 15:18:42', '2026-02-06 15:24:06');

-- ----------------------------
-- Table structure for textbook_image
-- ----------------------------
DROP TABLE IF EXISTS `textbook_image`;
CREATE TABLE `textbook_image`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `textbook_id` bigint NOT NULL COMMENT '教材ID',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_textbook_id`(`textbook_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教材图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of textbook_image
-- ----------------------------
INSERT INTO `textbook_image` VALUES (58, 13, '/images/books/details/d1a3130b80e04c6a88d85865e66f3ec2.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (59, 7, '/images/books/details/68b9659df1304c77840fa67a33d6213e.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (60, 1, '/images/books/details/283185f0f4ed4045b5050e6fefb740c1.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (61, 14, '/images/books/details/11eaa03917044480911ce83c018c93fc.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (62, 8, '/images/books/details/6f3dbdf2c71843269294d09d66cf4f97.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (63, 2, '/images/books/details/03090c4e773643b98ae2c502bd2d02e0.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (64, 15, '/images/books/details/0f8a0eff3242456e932a6a5726fa4422.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (65, 9, '/images/books/details/1b6422d86366481586c20e50de4e58a8.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (66, 3, '/images/books/details/a4327ea2541940ac9f7aa7e1e9ccbc83.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (67, 4, '/images/books/details/b99d66191008485c97d32209cc6a9ca7.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (68, 10, '/images/books/details/d5536eba067f419f870651adb0838c92.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (69, 5, '/images/books/details/312d4aab65614fad8d79526e856d80bf.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (70, 11, '/images/books/details/eef5df135b804501a8cbe3b0ecd764de.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (71, 16, '/images/books/details/219e3c4455a540c78687b1834685ec2c.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (72, 21, '/images/books/details/979cf5e0667f45c2977c0670190b92b1.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (73, 17, '/images/books/details/049983e80d8d41abad341502c2433c0b.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (74, 22, '/images/books/details/8e059d9e78f34ee09961a978040f47a9.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (75, 18, '/images/books/details/b0b7e8d4fc924199b0ea52bac7403d2e.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (76, 23, '/images/books/details/a0fd8b08985d4d97881c04f512d5bd6c.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (77, 19, '/images/books/details/7f0b17239c7a46f18c92531e159c6017.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (78, 24, '/images/books/details/e644cc82d20e40fb9f75147bc151991c.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (79, 20, '/images/books/details/29fc52094ca6420f9419f0ca7a09bae6.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (80, 25, '/images/books/details/63ed89169b074b80a077a112eceee3d1.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (81, 26, '/images/books/details/43b9e9cf10784b9ab4418777d883c215.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (82, 30, '/images/books/details/84c37ff1a40a4918bc7e593d0d756ae5.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (83, 34, '/images/books/details/91a6e3c88c78469f8b51f6f7cf0d7497.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (84, 27, '/images/books/details/b8bc02ee6794496c8a24170beff6df28.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (85, 31, '/images/books/details/a7367c61fa99431bb76d678dc1fd5919.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (86, 35, '/images/books/details/fc009a2e335148da9ae459e656d82c5e.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (87, 29, '/images/books/details/3c521b06c70041be8716ea67c14143be.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (88, 33, '/images/books/details/a5ab8a5097634fb9884f16c1f1674a6f.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (89, 6, '/images/books/details/d0a4d7db77ef45a687f6257869c899c0.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (90, 12, '/images/books/details/a190c22fa27a42a7b43e6942f8244268.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (91, 37, '/images/books/details/b942008f40f24a1f92338a9d8612a0ba.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (92, 42, '/images/books/details/90acbea8a3a54e9f84bded970343fe97.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (93, 38, '/images/books/details/c11bb0fae868413db1a33a11d9c41dfa.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (94, 43, '/images/books/details/104eb276b08a4ca3866432e663107605.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (95, 39, '/images/books/details/d5f5ba6ba72745c8b7585f666657be4d.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (96, 44, '/images/books/details/96e381eeda114fc4a94715260c4aa787.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (97, 40, '/images/books/details/114a4073f8954c2d8cd403524c467cd0.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (98, 45, '/images/books/details/b7fc3e98ed4a408eaa04b34398d51052.png', 0, NULL);
INSERT INTO `textbook_image` VALUES (102, 46, '/images/books/details/7a48650f0ff34f7f94b262e91302cd74.png', 0, '2026-01-28 19:49:46');
INSERT INTO `textbook_image` VALUES (103, 48, '/images/books/details/6cd4821b5c5846adbd3b39e1772617aa.png', 0, '2026-01-29 08:59:18');
INSERT INTO `textbook_image` VALUES (104, 53, '/images/books/details/9c8abac6da5244db881d2ba93628b717.png', 0, '2026-01-29 08:59:44');
INSERT INTO `textbook_image` VALUES (106, 49, '/images/books/details/49740b7dcaf84c35ae775aa1396d0bab.png', 0, '2026-01-29 09:01:04');
INSERT INTO `textbook_image` VALUES (107, 54, '/images/books/details/af1b359e6f7e45f6ae710e12436f5239.png', 0, '2026-01-29 09:01:28');
INSERT INTO `textbook_image` VALUES (108, 50, '/images/books/details/cde209cea5af406e93adacb822be7422.png', 0, '2026-01-29 09:02:15');
INSERT INTO `textbook_image` VALUES (109, 55, '/images/books/details/cd2d8a3b8c6d4ce8b4a0e9c81fab66b8.png', 0, '2026-01-29 09:02:38');
INSERT INTO `textbook_image` VALUES (110, 56, '/images/books/details/6c2e3f8bf90e4443b8557454af3619c0.png', 0, '2026-01-29 09:03:19');
INSERT INTO `textbook_image` VALUES (111, 61, '/images/books/details/5ac8be072a4d4fde8a098dd7001918d8.png', 0, '2026-01-29 09:03:45');
INSERT INTO `textbook_image` VALUES (112, 58, '/images/books/details/eba70761c2da4bf3ab66a14b7f95d306.png', 0, '2026-01-29 09:04:37');
INSERT INTO `textbook_image` VALUES (113, 63, '/images/books/details/4ef63bab93c447978d72ab3f2a95fc33.png', 0, '2026-01-29 09:05:21');
INSERT INTO `textbook_image` VALUES (114, 59, '/images/books/details/1a21cddabda6442099dc7643e87c38b9.png', 0, '2026-01-29 09:06:05');
INSERT INTO `textbook_image` VALUES (115, 64, '/images/books/details/ab5894d0f8dd459894ce01b9e0321632.png', 0, '2026-01-29 09:06:29');
INSERT INTO `textbook_image` VALUES (116, 60, '/images/books/details/f01ba0ebf48b45a9b64c7ae7130cf471.png', 0, '2026-01-29 09:07:11');
INSERT INTO `textbook_image` VALUES (117, 65, '/images/books/details/5b1fb21973c64f0b9a03ba28ef6a20e9.png', 0, '2026-01-29 09:07:35');
INSERT INTO `textbook_image` VALUES (118, 105, '/images/books/details/d71c670ed93e457c984d9161eb28d4e3.png', 0, '2026-01-29 11:04:18');
INSERT INTO `textbook_image` VALUES (119, 106, '/images/books/details/f8f65e1ca04d4ab3addc0e29b5303fb7.png', 0, '2026-02-06 15:18:43');

-- ----------------------------
-- Table structure for trading_point
-- ----------------------------
DROP TABLE IF EXISTS `trading_point`;
CREATE TABLE `trading_point`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '交易点名称',
  `campus_id` bigint NULL DEFAULT NULL COMMENT '所属校区',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '具体位置描述',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注说明',
  `sort` int NULL DEFAULT 0 COMMENT '排序号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_campus_id`(`campus_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '交易点表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of trading_point
-- ----------------------------
INSERT INTO `trading_point` VALUES (1, '图书馆大厅', 1, '图书馆一楼入口处', '人流量大，监控覆盖', 1, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (2, '第一食堂门口', 1, '第一食堂正门外', '用餐时间人多', 2, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (3, '第二食堂门口', 1, '第二食堂正门外', '用餐时间人多', 3, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (4, '教学楼A座大厅', 1, '教学楼A座一楼大厅', '课间人流量大', 4, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (5, '教学楼B座大厅', 1, '教学楼B座一楼大厅', '课间人流量大', 5, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (6, '学生活动中心', 1, '学生活动中心一楼', '全天开放', 6, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (7, '校园超市门口', 1, '校园超市正门外', '人流量稳定', 7, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (8, '体育馆入口', 1, '体育馆正门入口', '下午人较多', 8, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (9, '宿舍区快递站', 1, '快递驿站门口', '取快递时顺便交易', 9, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (10, '校门口', 1, '学校正门内侧', '出入方便', 10, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (11, '东校区图书馆', 2, '东校区图书馆一楼', '东校区推荐地点', 11, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');
INSERT INTO `trading_point` VALUES (12, '东校区食堂', 2, '东校区食堂门口', '东校区推荐地点', 12, 1, '2026-02-06 14:39:02', '2026-02-06 14:39:02');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `school` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学校',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业',
  `campus_id` bigint NULL DEFAULT NULL COMMENT '所属校区ID',
  `credit_score` int NULL DEFAULT 100 COMMENT '信用分（初始100分）',
  `is_verified` tinyint NULL DEFAULT 0 COMMENT '是否认证（0否 1是）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'user', 'fcea920f7412b5da7be0cf42b8c93759', 'niko', '/images/books/avatars/8171e327bece499cb43a0e8068ef439e.png', '13514521452', 'niko@qq.com', NULL, NULL, NULL, NULL, 100, 0, 1, NULL, '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (2, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', '/images/books/avatars/avatar_2.jpg', '13800138001', 'zhangsan@example.com', '清华大学', '计算机学院', '软件工程', NULL, 90, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (3, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', '/images/books/avatars/avatar_3.jpg', '13800138002', 'lisi@example.com', '北京大学', '信息科学技术学院', '计算机科学与技术', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (4, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', '/images/books/avatars/avatar_4.jpg', '13800138003', 'wangwu@example.com', '复旦大学', '数学科学学院', '数学与应用数学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (5, 'zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', '赵六', '/images/books/avatars/avatar_5.jpg', '13800138004', 'zhaoliu@example.com', '上海交通大学', '电子信息与电气工程学院', '电子工程', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (6, 'sunqi', 'e10adc3949ba59abbe56e057f20f883e', '孙七', '/images/books/avatars/avatar_6.jpg', '13800138005', 'sunqi@example.com', '浙江大学', '经济学院', '金融学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (7, 'zhouba', 'e10adc3949ba59abbe56e057f20f883e', '周八', '/images/books/avatars/avatar_7.jpg', '13800138006', 'zhouba@example.com', '南京大学', '外国语学院', '英语', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (8, 'wujiu', 'e10adc3949ba59abbe56e057f20f883e', '吴九', '/images/books/avatars/avatar_8.jpg', '13800138007', 'wujiu@example.com', '中国科学技术大学', '物理学院', '应用物理学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (9, 'zhengshi', 'e10adc3949ba59abbe56e057f20f883e', '郑十', '/images/books/avatars/avatar_9.jpg', '13800138008', 'zhengshi@example.com', '武汉大学', '法学院', '法学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (10, 'chenjia', 'e10adc3949ba59abbe56e057f20f883e', '陈佳', '/images/books/avatars/avatar_10.jpg', '13800138009', 'chenjia@example.com', '中山大学', '医学院', '临床医学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (11, 'linyi', 'e10adc3949ba59abbe56e057f20f883e', '林毅', '/images/books/avatars/avatar_11.jpg', '13800138010', 'linyi@example.com', '厦门大学', '人文学院', '汉语言文学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (12, 'huangxin', 'e10adc3949ba59abbe56e057f20f883e', '黄欣', '/images/books/avatars/avatar_12.jpg', '13800138011', 'huangxin@example.com', '清华大学', '经济管理学院', '工商管理', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (13, 'liuqiang', 'e10adc3949ba59abbe56e057f20f883e', '刘强', '/images/books/avatars/avatar_13.jpg', '13800138012', 'liuqiang@example.com', '北京大学', '光华管理学院', '会计学', NULL, 100, 1, 0, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (14, 'xumin', 'e10adc3949ba59abbe56e057f20f883e', '徐敏', '/images/books/avatars/avatar_14.jpg', '13800138013', 'xumin@example.com', '复旦大学', '新闻学院', '新闻学', NULL, 100, 0, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (15, 'hejun', 'e10adc3949ba59abbe56e057f20f883e', '何俊', '/images/books/avatars/avatar_15.jpg', '13800138014', 'hejun@example.com', '上海交通大学', '机械与动力工程学院', '机械工程', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (16, 'gaofei', 'e10adc3949ba59abbe56e057f20f883e', '高飞', '/images/books/avatars/avatar_16.jpg', '13800138015', 'gaofei@example.com', '浙江大学', '计算机学院', '人工智能', NULL, 100, 1, 0, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (17, 'tanghao', 'e10adc3949ba59abbe56e057f20f883e', '唐浩', '/images/books/avatars/avatar_17.jpg', '13800138016', 'tanghao@example.com', '南京大学', '化学化工学院', '化学', NULL, 100, 0, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (18, 'yujie', 'e10adc3949ba59abbe56e057f20f883e', '于杰', '/images/books/avatars/avatar_18.jpg', '13800138017', 'yujie@example.com', '中国科学技术大学', '生命科学学院', '生物科学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (19, 'dengwei', 'e10adc3949ba59abbe56e057f20f883e', '邓伟', '/images/books/avatars/avatar_19.jpg', '13800138018', 'dengwei@example.com', '武汉大学', '测绘学院', '测绘工程', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (20, 'pangchao', 'e10adc3949ba59abbe56e057f20f883e', '庞超', '/images/books/avatars/avatar_20.jpg', '13800138019', 'pangchao@example.com', '中山大学', '环境科学与工程学院', '环境工程', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (21, 'shilei', 'e10adc3949ba59abbe56e057f20f883e', '石磊', '/images/books/avatars/avatar_21.jpg', '13800138020', 'shilei@example.com', '厦门大学', '经济学院', '经济学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (22, 'maoxue', 'e10adc3949ba59abbe56e057f20f883e', '毛雪', '/images/books/avatars/avatar_22.jpg', '13800138021', 'maoxue@example.com', '清华大学', '建筑学院', '建筑学', NULL, 100, 1, 0, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (23, 'qiuyan', 'e10adc3949ba59abbe56e057f20f883e', '邱燕', '/images/books/avatars/avatar_23.jpg', '13800138022', 'qiuyan@example.com', '北京大学', '艺术学院', '艺术设计', NULL, 100, 0, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (24, 'duanping', 'e10adc3949ba59abbe56e057f20f883e', '段平', '/images/books/avatars/avatar_24.jpg', '13800138023', 'duanping@example.com', '复旦大学', '管理学院', '市场营销', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (25, 'jianghua', 'e10adc3949ba59abbe56e057f20f883e', '江华', '/images/books/avatars/avatar_25.jpg', '13800138024', 'jianghua@example.com', '上海交通大学', '材料科学与工程学院', '材料科学与工程', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (26, 'cuigang', 'e10adc3949ba59abbe56e057f20f883e', '崔刚', '/images/books/avatars/avatar_26.jpg', '13800138025', 'cuigang@example.com', '浙江大学', '控制科学与工程学院', '自动化', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (27, 'chenglin', 'e10adc3949ba59abbe56e057f20f883e', '程琳', '/images/books/avatars/avatar_27.jpg', '13800138026', 'chenglin@example.com', '南京大学', '地球科学与工程学院', '地质学', NULL, 100, 1, 0, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (28, 'gongjing', 'e10adc3949ba59abbe56e057f20f883e', '龚静', '/images/books/avatars/avatar_28.jpg', '13800138027', 'gongjing@example.com', '中国科学技术大学', '数学科学学院', '统计学', NULL, 100, 0, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (29, 'tangbo', 'e10adc3949ba59abbe56e057f20f883e', '汤波', '/images/books/avatars/avatar_29.jpg', '13800138028', 'tangbo@example.com', '武汉大学', '水利水电学院', '水利水电工程', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');
INSERT INTO `user` VALUES (30, 'luming', 'e10adc3949ba59abbe56e057f20f883e', '陆明', '/images/books/avatars/avatar_30.jpg', '13800138029', 'luming@example.com', '中山大学', '社会学与人类学学院', '社会学', NULL, 100, 1, 1, '2026-01-26 20:58:35', '2026-01-26 21:29:06');

-- ----------------------------
-- Table structure for wanted
-- ----------------------------
DROP TABLE IF EXISTS `wanted`;
CREATE TABLE `wanted`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '发布者ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '书名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `max_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '期望最高价',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0已关闭 1求购中）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '求购表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wanted
-- ----------------------------
INSERT INTO `wanted` VALUES (1, 1, '深度学习', 'Ian Goodfellow', 100.00, '求购深度学习花书，八成新以上即可。', 1, '2026-01-20 10:00:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (2, 2, '统计学习方法', '李航', 60.00, '求购李航老师的统计学习方法，有笔记更好。', 1, '2026-01-20 14:30:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (3, 3, '计算机视觉：算法与应用', 'Richard Szeliski', 80.00, '求购计算机视觉教材，新旧不限。', 1, '2026-01-21 09:00:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (4, 4, '自然语言处理综论', 'Daniel Jurafsky', 90.00, 'NLP经典教材，求购！', 1, '2026-01-21 15:20:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (5, 5, '推荐系统实践', '项亮', 50.00, '求购推荐系统实践，有实战案例的优先。', 1, '2026-01-22 10:30:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (6, 6, '数值分析（第5版）', 'Richard L. Burden', 70.00, '求购数值分析教材。', 1, '2026-01-22 14:00:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (7, 7, '抽象代数', '丘维声', 45.00, '求购抽象代数教材，北大版。', 1, '2026-01-23 09:15:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (8, 8, '拓扑学', '尤承业', 40.00, '求购拓扑学教材。', 1, '2026-01-23 13:45:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (9, 9, '泛函分析', '张恭庆', 55.00, '求购泛函分析教材。', 1, '2026-01-24 10:20:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (10, 10, '微分几何', '陈省身', 60.00, '求购微分几何教材。', 1, '2026-01-24 15:30:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (11, 11, '雅思词汇真经', '刘洪波', 35.00, '求购雅思词汇书。', 1, '2026-01-20 11:00:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (12, 12, 'GRE词汇精选', '俞敏洪', 40.00, '求购GRE红宝书。', 1, '2026-01-20 15:45:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (13, 13, '托福听力真题', 'ETS', 50.00, '求购托福听力真题集。', 1, '2026-01-21 10:30:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (14, 14, '商务英语口语', 'Cambridge', 45.00, '求购商务英语口语教材。', 1, '2026-01-21 14:50:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (15, 15, '英语写作手册', '丁往道', 30.00, '求购英语写作手册。', 1, '2026-01-22 09:40:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (16, 16, '投资学（第10版）', 'Bodie', 80.00, '求购投资学教材。', 1, '2026-01-22 13:20:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (17, 17, '公司理财（第11版）', 'Ross', 85.00, '求购公司理财教材。', 1, '2026-01-23 10:50:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (18, 18, '营销管理（第15版）', 'Philip Kotler', 75.00, '求购营销管理教材。', 1, '2026-01-23 14:30:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (19, 19, '组织行为学', 'Stephen P. Robbins', 65.00, '求购组织行为学教材。', 1, '2026-01-24 11:15:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (20, 20, '供应链管理', '马士华', 55.00, '求购供应链管理教材。', 1, '2026-01-24 16:00:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (21, 21, '量子力学教程', '曾谨言', 60.00, '求购量子力学教材。', 1, '2026-01-20 12:00:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (22, 22, '固体物理学', '黄昆', 70.00, '求购固体物理学教材。', 1, '2026-01-20 16:30:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (23, 23, '电磁场与电磁波', '谢处方', 50.00, '求购电磁场教材。', 1, '2026-01-21 11:45:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (24, 24, '通信原理（第7版）', '樊昌信', 55.00, '求购通信原理教材。', 1, '2026-01-21 15:50:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (25, 25, '数字信号处理', 'Alan V. Oppenheim', 65.00, '求购数字信号处理教材。', 1, '2026-01-22 10:40:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (26, 26, '中国现代文学三十年', '钱理群', 45.00, '求购中国现代文学史。', 1, '2026-01-22 14:25:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (27, 27, '西方文论史', '马新国', 50.00, '求购西方文论史教材。', 1, '2026-01-23 09:50:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (28, 28, '艺术的故事', 'E.H.Gombrich', 80.00, '求购艺术的故事。', 1, '2026-01-23 13:35:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (29, 29, '电影艺术词典', '许南明', 60.00, '求购电影艺术词典。', 1, '2026-01-24 10:45:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (30, 30, '中国音乐史', '金文达', 55.00, '求购中国音乐史教材。', 1, '2026-01-24 15:20:00', '2026-01-26 20:59:04');
INSERT INTO `wanted` VALUES (31, 1, '计算机网络', '', 50.00, '全新', 1, '2026-02-24 15:45:14', '2026-02-24 15:45:14');

SET FOREIGN_KEY_CHECKS = 1;

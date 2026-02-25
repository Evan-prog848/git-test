-- =====================================================
-- 二手教材交易系统 数据库初始化脚本
-- 数据库名: textbook_trading
-- 创建日期: 2025-01-24
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS textbook_trading DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE textbook_trading;

-- =====================================================
-- 1. 用户表 (user)
-- =====================================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `school` VARCHAR(100) DEFAULT NULL COMMENT '学校',
    `college` VARCHAR(100) DEFAULT NULL COMMENT '学院',
    `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
    `is_verified` TINYINT DEFAULT 0 COMMENT '是否认证（0否 1是）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 2. 分类表 (category)
-- =====================================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
    `sort` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- =====================================================
-- 3. 教材表 (textbook)
-- =====================================================
DROP TABLE IF EXISTS `textbook`;
CREATE TABLE `textbook` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `title` VARCHAR(200) NOT NULL COMMENT '书名',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
    `publisher` VARCHAR(100) DEFAULT NULL COMMENT '出版社',
    `isbn` VARCHAR(20) DEFAULT NULL COMMENT 'ISBN号',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
    `condition_level` TINYINT DEFAULT 8 COMMENT '新旧程度（1-10成新）',
    `course` VARCHAR(100) DEFAULT NULL COMMENT '适用课程',
    `description` TEXT COMMENT '描述',
    `cover` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0下架 1在售 2已售）',
    `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态（0待审核 1通过 2拒绝）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教材表';

-- =====================================================
-- 4. 教材图片表 (textbook_image)
-- =====================================================
DROP TABLE IF EXISTS `textbook_image`;
CREATE TABLE `textbook_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `textbook_id` BIGINT NOT NULL COMMENT '教材ID',
    `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
    `sort` INT DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_textbook_id` (`textbook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教材图片表';

-- =====================================================
-- 5. 求购表 (wanted)
-- =====================================================
DROP TABLE IF EXISTS `wanted`;
CREATE TABLE `wanted` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `title` VARCHAR(200) NOT NULL COMMENT '书名',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
    `max_price` DECIMAL(10,2) DEFAULT NULL COMMENT '期望最高价',
    `description` TEXT COMMENT '描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0已关闭 1求购中）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求购表';

-- =====================================================
-- 6. 订单表 (orders)
-- =====================================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `textbook_id` BIGINT NOT NULL COMMENT '教材ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家ID',
    `buyer_id` BIGINT NOT NULL COMMENT '买家ID',
    `price` DECIMAL(10,2) NOT NULL COMMENT '成交价格',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '买家备注',
    `status` TINYINT DEFAULT 0 COMMENT '状态（0待确认 1已确认 2已完成 3已取消）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_textbook_id` (`textbook_id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_buyer_id` (`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- =====================================================
-- 7. 收藏表 (favorite)
-- =====================================================
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `textbook_id` BIGINT NOT NULL COMMENT '教材ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_textbook` (`user_id`, `textbook_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- =====================================================
-- 8. 关注表 (follow)
-- =====================================================
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '关注者ID',
    `follow_user_id` BIGINT NOT NULL COMMENT '被关注者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_follow` (`user_id`, `follow_user_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_follow_user_id` (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注表';

-- =====================================================
-- 9. 消息表 (message)
-- =====================================================
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `from_user_id` BIGINT DEFAULT NULL COMMENT '发送者ID（系统消息为空）',
    `to_user_id` BIGINT NOT NULL COMMENT '接收者ID',
    `type` TINYINT DEFAULT 1 COMMENT '消息类型（1系统通知 2私信）',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '消息标题',
    `content` TEXT COMMENT '消息内容',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读（0未读 1已读）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_to_user_id` (`to_user_id`),
    KEY `idx_from_user_id` (`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- =====================================================
-- 10. 评价表 (review)
-- =====================================================
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `from_user_id` BIGINT NOT NULL COMMENT '评价人ID',
    `to_user_id` BIGINT NOT NULL COMMENT '被评价人ID',
    `score` TINYINT NOT NULL COMMENT '评分（1-5）',
    `content` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_to_user_id` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- =====================================================
-- 11. 公告表 (notice)
-- =====================================================
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
    `content` TEXT COMMENT '公告内容',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- =====================================================
-- 12. 轮播图表 (banner)
-- =====================================================
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '轮播图标题',
    `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
    `link_url` VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
    `sort` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';

-- =====================================================
-- 13. 浏览历史表 (browse_history)
-- =====================================================
DROP TABLE IF EXISTS `browse_history`;
CREATE TABLE `browse_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `textbook_id` BIGINT NOT NULL COMMENT '教材ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_textbook_id` (`textbook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览历史表';

-- =====================================================
-- 14. 管理员表 (admin)
-- =====================================================
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role_id` BIGINT DEFAULT NULL COMMENT '角色ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- =====================================================
-- 15. 角色表 (role)
-- =====================================================
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- =====================================================
-- 16. 菜单表 (menu)
-- =====================================================
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `path` VARCHAR(100) DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(100) DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
    `sort` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- =====================================================
-- 17. 角色菜单关联表 (role_menu)
-- =====================================================
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- =====================================================
-- 初始数据
-- =====================================================

-- 插入默认角色
INSERT INTO `role` (`role_name`, `role_code`, `description`) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有所有权限，可管理其他管理员和系统配置'),
('内容管理员', 'CONTENT_ADMIN', '管理教材、分类、公告、轮播图等内容相关功能'),
('订单管理员', 'ORDER_ADMIN', '管理订单、用户、评价等交易相关功能');

-- 插入默认管理员 (密码: admin123，使用MD5加密)
INSERT INTO `admin` (`username`, `password`, `nickname`, `role_id`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 1);

-- 插入默认菜单
INSERT INTO `menu` (`parent_id`, `menu_name`, `path`, `component`, `icon`, `sort`) VALUES
(0, '数据统计', '/dashboard', 'Dashboard', 'DataLine', 1),
(0, '用户管理', '/user', 'UserManage', 'User', 2),
(0, '教材管理', '/textbook', 'TextbookManage', 'Reading', 3),
(0, '教材审核', '/audit', 'AuditManage', 'DocumentChecked', 4),
(0, '订单管理', '/order', 'OrderManage', 'List', 5),
(0, '求购管理', '/wanted', 'WantedManage', 'Search', 6),
(0, '分类管理', '/category', 'CategoryManage', 'Menu', 7),
(0, '评价管理', '/review', 'ReviewManage', 'Star', 8),
(0, '公告管理', '/notice', 'NoticeManage', 'Bell', 9),
(0, '轮播图管理', '/banner', 'BannerManage', 'Picture', 10),
(0, '系统管理', '/system', NULL, 'Setting', 11),
(11, '角色管理', '/system/role', 'RoleManage', 'UserFilled', 1),
(11, '菜单管理', '/system/menu', 'MenuManage', 'Grid', 2),
(11, '管理员管理', '/system/admin', 'AdminManage', 'Avatar', 3);

-- 为超级管理员分配所有菜单
INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `menu`;

-- 为内容管理员分配菜单 (数据统计、教材管理、教材审核、求购管理、分类管理、公告管理、轮播图管理)
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
(2, 1), (2, 3), (2, 4), (2, 6), (2, 7), (2, 9), (2, 10);

-- 为订单管理员分配菜单 (数据统计、用户管理、订单管理、评价管理)
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
(3, 1), (3, 2), (3, 5), (3, 8);

-- 插入默认分类
INSERT INTO `category` (`name`, `sort`) VALUES
('计算机类', 1),
('数学类', 2),
('英语类', 3),
('经济管理类', 4),
('理工类', 5),
('文学艺术类', 6),
('法律类', 7),
('医学类', 8),
('考研考公', 9),
('其他', 10);

-- 插入默认公告
INSERT INTO `notice` (`title`, `content`) VALUES
('欢迎使用二手教材交易系统', '本系统旨在为高校学生提供一个便捷的二手教材交易平台，实现教材资源的循环利用，降低学生购书成本。请遵守交易规则，诚信交易！');

SELECT '数据库初始化完成！' AS message;

-- =====================================================
-- 二手教材交易系统 数据库迁移脚本 V2
-- 新增功能：校区、课程、交易点、信用档案、品相详情
-- =====================================================

USE textbook_trading;

-- =====================================================
-- 1. 校区表 (campus)
-- =====================================================
DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '校区名称',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '校区地址',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='校区表';

-- =====================================================
-- 2. 专业表 (major)
-- =====================================================
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '专业名称',
    `college` VARCHAR(100) DEFAULT NULL COMMENT '所属学院',
    `campus_id` BIGINT DEFAULT NULL COMMENT '所属校区',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_campus_id` (`campus_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专业表';

-- =====================================================
-- 3. 课程表 (course)
-- =====================================================
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `code` VARCHAR(50) DEFAULT NULL COMMENT '课程代码',
    `major_id` BIGINT DEFAULT NULL COMMENT '所属专业ID',
    `semester` VARCHAR(20) DEFAULT NULL COMMENT '开课学期（如：大一上）',
    `credit` DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_major_id` (`major_id`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- =====================================================
-- 4. 交易点表 (trading_point)
-- =====================================================
DROP TABLE IF EXISTS `trading_point`;
CREATE TABLE `trading_point` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '交易点名称',
    `campus_id` BIGINT DEFAULT NULL COMMENT '所属校区',
    `location` VARCHAR(255) DEFAULT NULL COMMENT '具体位置描述',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '备注说明',
    `sort` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用 1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_campus_id` (`campus_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易点表';

-- =====================================================
-- 5. 信用记录表 (credit_record)
-- =====================================================
DROP TABLE IF EXISTS `credit_record`;
CREATE TABLE `credit_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID',
    `type` TINYINT NOT NULL COMMENT '记录类型（1爽约 2实物不符 3好评奖励 4差评扣分 5举报成立）',
    `score_change` INT NOT NULL COMMENT '信用分变化（正数加分，负数扣分）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述说明',
    `reporter_id` BIGINT DEFAULT NULL COMMENT '举报人ID（如有）',
    `evidence` VARCHAR(1000) DEFAULT NULL COMMENT '证据图片URL（JSON数组）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0待处理 1已处理）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信用记录表';

-- =====================================================
-- 6. 聊天消息表 (chat_message)
-- =====================================================
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversation_id` VARCHAR(64) NOT NULL COMMENT '会话ID（由两个用户ID组合生成）',
    `from_user_id` BIGINT NOT NULL COMMENT '发送者ID',
    `to_user_id` BIGINT NOT NULL COMMENT '接收者ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `type` TINYINT DEFAULT 1 COMMENT '消息类型（1文本 2图片 3系统消息）',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读（0未读 1已读）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_conversation_id` (`conversation_id`),
    KEY `idx_from_user_id` (`from_user_id`),
    KEY `idx_to_user_id` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- =====================================================
-- 7. 扩展用户表 - 添加校区和信用分字段
-- =====================================================
ALTER TABLE `user` 
    ADD COLUMN `campus_id` BIGINT DEFAULT NULL COMMENT '所属校区ID' AFTER `major`,
    ADD COLUMN `credit_score` INT DEFAULT 100 COMMENT '信用分（初始100分）' AFTER `campus_id`;

-- =====================================================
-- 8. 扩展教材表 - 添加品相详情和校区字段
-- =====================================================
ALTER TABLE `textbook`
    ADD COLUMN `campus_id` BIGINT DEFAULT NULL COMMENT '发布校区ID' AFTER `cover`,
    ADD COLUMN `course_id` BIGINT DEFAULT NULL COMMENT '关联课程ID' AFTER `campus_id`,
    ADD COLUMN `has_notes` TINYINT DEFAULT 0 COMMENT '有无笔记（0无 1有）' AFTER `course_id`,
    ADD COLUMN `has_highlight` TINYINT DEFAULT 0 COMMENT '有无划线（0无 1有）' AFTER `has_notes`,
    ADD COLUMN `has_missing_pages` TINYINT DEFAULT 0 COMMENT '是否缺页（0否 1是）' AFTER `has_highlight`,
    ADD COLUMN `has_water_stains` TINYINT DEFAULT 0 COMMENT '有无水渍（0无 1有）' AFTER `has_missing_pages`,
    ADD COLUMN `input_mode` TINYINT DEFAULT 0 COMMENT '录入方式（0手动 1ISBN扫描）' AFTER `has_water_stains`;

-- =====================================================
-- 9. 扩展订单表 - 添加交易点字段
-- =====================================================
ALTER TABLE `orders`
    ADD COLUMN `trading_point_id` BIGINT DEFAULT NULL COMMENT '交易点ID' AFTER `remark`,
    ADD COLUMN `meeting_time` DATETIME DEFAULT NULL COMMENT '约定见面时间' AFTER `trading_point_id`;

-- =====================================================
-- 初始化数据
-- =====================================================

-- 插入默认校区
INSERT INTO `campus` (`name`, `address`) VALUES
('主校区', '学校主校区'),
('东校区', '学校东校区'),
('南校区', '学校南校区'),
('北校区', '学校北校区');

-- 插入默认交易点（10+个）
INSERT INTO `trading_point` (`name`, `campus_id`, `location`, `description`, `sort`) VALUES
('图书馆大厅', 1, '图书馆一楼入口处', '人流量大，监控覆盖', 1),
('第一食堂门口', 1, '第一食堂正门外', '用餐时间人多', 2),
('第二食堂门口', 1, '第二食堂正门外', '用餐时间人多', 3),
('教学楼A座大厅', 1, '教学楼A座一楼大厅', '课间人流量大', 4),
('教学楼B座大厅', 1, '教学楼B座一楼大厅', '课间人流量大', 5),
('学生活动中心', 1, '学生活动中心一楼', '全天开放', 6),
('校园超市门口', 1, '校园超市正门外', '人流量稳定', 7),
('体育馆入口', 1, '体育馆正门入口', '下午人较多', 8),
('宿舍区快递站', 1, '快递驿站门口', '取快递时顺便交易', 9),
('校门口', 1, '学校正门内侧', '出入方便', 10),
('东校区图书馆', 2, '东校区图书馆一楼', '东校区推荐地点', 11),
('东校区食堂', 2, '东校区食堂门口', '东校区推荐地点', 12);

-- 插入示例专业
INSERT INTO `major` (`name`, `college`, `campus_id`) VALUES
('计算机科学与技术', '计算机学院', 1),
('软件工程', '计算机学院', 1),
('信息安全', '计算机学院', 1),
('数学与应用数学', '数学学院', 1),
('统计学', '数学学院', 1),
('英语', '外国语学院', 1),
('日语', '外国语学院', 1),
('工商管理', '管理学院', 2),
('会计学', '管理学院', 2),
('金融学', '经济学院', 2);

-- 插入示例课程
INSERT INTO `course` (`name`, `code`, `major_id`, `semester`, `credit`) VALUES
('高等数学', 'MATH101', 1, '大一上', 5.0),
('线性代数', 'MATH102', 1, '大一下', 3.0),
('概率论与数理统计', 'MATH201', 1, '大二上', 3.0),
('数据结构', 'CS201', 1, '大二上', 4.0),
('计算机组成原理', 'CS202', 1, '大二下', 4.0),
('操作系统', 'CS301', 1, '大三上', 4.0),
('计算机网络', 'CS302', 1, '大三上', 3.0),
('数据库原理', 'CS303', 1, '大三下', 3.0),
('软件工程', 'SE301', 2, '大三上', 3.0),
('Java程序设计', 'SE201', 2, '大二上', 3.0),
('大学英语1', 'ENG101', 6, '大一上', 4.0),
('大学英语2', 'ENG102', 6, '大一下', 4.0),
('微观经济学', 'ECO101', 10, '大一上', 3.0),
('宏观经济学', 'ECO102', 10, '大一下', 3.0),
('会计学原理', 'ACC101', 9, '大一上', 3.0);

-- 添加后台菜单
INSERT INTO `menu` (`parent_id`, `menu_name`, `path`, `component`, `icon`, `sort`) VALUES
(0, '校区管理', '/campus', 'CampusManage', 'Location', 15),
(0, '专业课程', '/course', 'CourseManage', 'School', 16),
(0, '交易点管理', '/trading-point', 'TradingPointManage', 'MapLocation', 17),
(0, '信用管理', '/credit', 'CreditManage', 'Medal', 18);

-- 为超级管理员分配新菜单
INSERT INTO `role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `menu` WHERE `path` IN ('/campus', '/course', '/trading-point', '/credit');

SELECT '数据库迁移V2完成！' AS message;

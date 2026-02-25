-- =====================================================
-- 测试数据 Part 6: 管理员、角色、菜单数据
-- =====================================================
USE textbook_trading;

-- 额外的管理员数据 (除了init.sql中的admin，再添加5个管理员)
INSERT INTO `admin` (`username`, `password`, `nickname`, `avatar`, `role_id`, `status`, `create_time`) VALUES
('content_admin', 'e10adc3949ba59abbe56e057f20f883e', '内容管理员-张伟', 'https://i.pravatar.cc/150?img=51', 2, 1, '2026-01-10 10:00:00'),
('order_admin', 'e10adc3949ba59abbe56e057f20f883e', '订单管理员-李娜', 'https://i.pravatar.cc/150?img=52', 3, 1, '2026-01-10 10:30:00'),
('content_admin2', 'e10adc3949ba59abbe56e057f20f883e', '内容管理员-王强', 'https://i.pravatar.cc/150?img=53', 2, 1, '2026-01-11 09:00:00'),
('order_admin2', 'e10adc3949ba59abbe56e057f20f883e', '订单管理员-刘芳', 'https://i.pravatar.cc/150?img=54', 3, 1, '2026-01-11 14:00:00'),
('test_admin', 'e10adc3949ba59abbe56e057f20f883e', '测试管理员', 'https://i.pravatar.cc/150?img=55', 1, 0, '2026-01-12 10:00:00');

-- 更新教材的审核状态（添加一些待审核和被拒绝的教材）
UPDATE `textbook` SET `audit_status` = 0 WHERE `id` IN (96, 97, 98);
UPDATE `textbook` SET `audit_status` = 2 WHERE `id` IN (99, 100);

-- 为待审核和被拒绝的教材添加说明
UPDATE `textbook` SET `description` = CONCAT(`description`, '\n\n【审核中】该教材正在审核中，请耐心等待。') WHERE `id` IN (96, 97, 98);
UPDATE `textbook` SET `description` = CONCAT(`description`, '\n\n【审核未通过】原因：图片不清晰，请重新上传清晰的教材照片。') WHERE `id` = 99;
UPDATE `textbook` SET `description` = CONCAT(`description`, '\n\n【审核未通过】原因：价格设置不合理，请参考市场价格重新定价。') WHERE `id` = 100;

-- 添加更多公告（管理员发布的）
INSERT INTO `notice` (`title`, `content`, `status`, `create_time`) VALUES
('教材分类调整通知', '为了更好地服务大家，我们对教材分类进行了优化调整：\n\n新增分类：\n- 考研考公类\n- 职业资格类\n\n合并分类：\n- 将原"其他"类细分为多个子类\n\n请大家在发布教材时选择正确的分类，谢谢配合！', 1, '2026-01-15 10:00:00'),
('平台用户突破10000人', '感谢大家的支持和信任！\n\n截至今日，平台注册用户已突破10000人，累计成交订单超过5000笔。\n\n为了回馈大家，我们将推出系列优惠活动，敬请期待！\n\n让我们继续努力，打造更好的二手教材交易平台！', 1, '2026-01-14 15:00:00'),
('违规用户处理公告', '近期我们发现部分用户存在违规行为，包括：\n- 发布虚假信息\n- 恶意取消订单\n- 不文明交流\n\n经核实，以下账号已被禁用：\n- user_xxx（发布虚假信息）\n- user_yyy（恶意取消订单）\n\n请大家遵守平台规则，共同维护良好的交易环境！', 1, '2026-01-13 10:00:00');

-- 添加更多轮播图
INSERT INTO `banner` (`title`, `image_url`, `link_url`, `sort`, `status`, `create_time`) VALUES
('新用户专享优惠', 'https://picsum.photos/1200/400?random=banner9', '/register', 9, 1, '2026-01-23 10:00:00'),
('推荐有奖活动', 'https://picsum.photos/1200/400?random=banner10', '/user/invite', 10, 1, '2026-01-24 10:00:00');

-- 添加一些已下架和已售的教材状态
UPDATE `textbook` SET `status` = 0 WHERE `id` IN (1, 2, 3, 6, 7, 11, 12, 15);
UPDATE `textbook` SET `status` = 2 WHERE `id` IN (16, 17, 18, 19, 21, 22, 25, 26, 27);

-- 为部分用户添加禁用状态（用于测试用户管理功能）
UPDATE `user` SET `status` = 0 WHERE `id` IN (13, 16, 22, 27, 33, 38, 44, 49);

-- 添加更多系统消息（管理员发送的通知）
INSERT INTO `message` (`from_user_id`, `to_user_id`, `type`, `title`, `content`, `is_read`, `create_time`) VALUES
(NULL, 13, 1, '账号异常提醒', '您的账号因违规操作已被暂时禁用，如有疑问请联系客服。', 0, '2026-01-25 10:00:00'),
(NULL, 16, 1, '账号异常提醒', '您的账号因违规操作已被暂时禁用，如有疑问请联系客服。', 0, '2026-01-25 10:01:00'),
(NULL, 22, 1, '账号异常提醒', '您的账号因违规操作已被暂时禁用，如有疑问请联系客服。', 0, '2026-01-25 10:02:00'),
(NULL, 99, 1, '教材审核未通过', '您发布的教材《公务员考试真题汇编》审核未通过，原因：图片不清晰，请重新上传清晰的教材照片。', 0, '2026-01-25 11:00:00'),
(NULL, 100, 1, '教材审核未通过', '您发布的教材《考研复试英语口语突破》审核未通过，原因：价格设置不合理，请参考市场价格重新定价。', 0, '2026-01-25 11:01:00'),
(NULL, 1, 1, '您的教材已售出', '恭喜！您发布的教材《数据结构与算法分析》已成功售出。', 1, '2026-01-21 14:30:00'),
(NULL, 2, 1, '您的教材已售出', '恭喜！您发布的教材《深入理解计算机系统》已成功售出。', 1, '2026-01-22 10:40:00'),
(NULL, 3, 1, '您的教材已售出', '恭喜！您发布的教材《Java核心技术》已成功售出。', 1, '2026-01-22 15:55:00'),
(NULL, 6, 1, '您的教材已售出', '恭喜！您发布的教材《高等数学上册》已成功售出。', 1, '2026-01-20 15:10:00'),
(NULL, 7, 1, '您的教材已售出', '恭喜！您发布的教材《高等数学下册》已成功售出。', 1, '2026-01-20 16:40:00');

-- 添加更多分类数据（子分类）
INSERT INTO `category` (`name`, `parent_id`, `sort`, `status`) VALUES
('Java开发', 1, 11, 1),
('Python开发', 1, 12, 1),
('前端开发', 1, 13, 1),
('数据库', 1, 14, 1),
('人工智能', 1, 15, 1),
('高等数学', 2, 21, 1),
('线性代数', 2, 22, 1),
('概率统计', 2, 23, 1),
('四级', 3, 31, 1),
('六级', 3, 32, 1),
('雅思', 3, 33, 1),
('托福', 3, 34, 1),
('会计', 4, 41, 1),
('金融', 4, 42, 1),
('市场营销', 4, 43, 1),
('电子电路', 5, 51, 1),
('机械工程', 5, 52, 1),
('土木工程', 5, 53, 1),
('中国文学', 6, 61, 1),
('外国文学', 6, 62, 1),
('艺术设计', 6, 63, 1),
('民法', 7, 71, 1),
('刑法', 7, 72, 1),
('行政法', 7, 73, 1),
('基础医学', 8, 81, 1),
('临床医学', 8, 82, 1),
('中医学', 8, 83, 1),
('考研英语', 9, 91, 1),
('考研数学', 9, 92, 1),
('考研政治', 9, 93, 1),
('公务员行测', 9, 94, 1),
('公务员申论', 9, 95, 1);

-- 统计数据（用于Dashboard展示）
-- 这些数据会在实际查询时动态计算，这里只是示例

SELECT '管理员相关数据插入完成！' AS message;
SELECT '========================================' AS separator;
SELECT '所有测试数据生成完毕！' AS message;
SELECT '========================================' AS separator;
SELECT '数据统计：' AS title;
SELECT COUNT(*) AS '用户总数' FROM `user`;
SELECT COUNT(*) AS '教材总数' FROM `textbook`;
SELECT COUNT(*) AS '订单总数' FROM `orders`;
SELECT COUNT(*) AS '收藏总数' FROM `favorite`;
SELECT COUNT(*) AS '关注总数' FROM `follow`;
SELECT COUNT(*) AS '消息总数' FROM `message`;
SELECT COUNT(*) AS '评价总数' FROM `review`;
SELECT COUNT(*) AS '求购总数' FROM `wanted`;
SELECT COUNT(*) AS '公告总数' FROM `notice`;
SELECT COUNT(*) AS '轮播图总数' FROM `banner`;
SELECT COUNT(*) AS '管理员总数' FROM `admin`;
SELECT COUNT(*) AS '分类总数' FROM `category`;

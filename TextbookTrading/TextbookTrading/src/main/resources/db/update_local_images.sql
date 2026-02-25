-- =====================================================
-- 更新为本地图片路径
-- 使用说明：
-- 1. 先将图片放入 TextbookTradingWeb/public/images/books/ 对应目录
-- 2. 执行此SQL文件更新数据库中的图片路径
-- =====================================================
USE textbook_trading;

-- 更新用户头像为本地路径
UPDATE `user` SET `avatar` = CONCAT('/images/books/avatars/avatar_', id, '.jpg');

-- 更新教材封面为本地路径
UPDATE `textbook` SET `cover` = CONCAT('/images/books/covers/book_', id, '.jpg');

-- 更新轮播图为本地路径
UPDATE `banner` SET `image_url` = CONCAT('/images/books/banners/banner_', id, '.jpg');

-- 更新教材图片为本地路径
UPDATE `textbook_image` SET `image_url` = CONCAT('/images/books/details/book_', textbook_id, '_', sort, '.jpg');

-- 如果图片文件不存在，可以设置为默认图片
-- 取消下面的注释来使用默认图片

-- UPDATE `user` SET `avatar` = '/images/books/avatars/default.jpg' WHERE `avatar` IS NULL OR `avatar` = '';
-- UPDATE `textbook` SET `cover` = '/images/books/covers/default.jpg' WHERE `cover` IS NULL OR `cover` = '';
-- UPDATE `banner` SET `image_url` = '/images/books/banners/default.jpg' WHERE `image_url` IS NULL OR `image_url` = '';

SELECT '图片路径已更新为本地路径！' AS message;
SELECT '请确保已将图片文件放入对应目录：' AS reminder;
SELECT '  - TextbookTradingWeb/public/images/books/avatars/' AS path1;
SELECT '  - TextbookTradingWeb/public/images/books/covers/' AS path2;
SELECT '  - TextbookTradingWeb/public/images/books/banners/' AS path3;
SELECT '  - TextbookTradingWeb/public/images/books/details/' AS path4;

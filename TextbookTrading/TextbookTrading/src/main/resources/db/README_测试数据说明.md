# 二手教材交易系统 - 测试数据说明

## 📋 概述

本目录包含完整的测试数据SQL脚本，涵盖系统所有功能模块和前端页面所需的数据。

## 📁 文件列表

### 1. `init.sql` - 数据库初始化脚本
- 创建数据库和所有表结构
- 插入基础数据（默认角色、管理员、菜单、分类、公告）

### 2. `test_data_01_users.sql` - 用户数据
- **50个测试用户**
- 包含不同学校、专业的用户
- 部分用户已认证，部分未认证
- 默认密码：`admin123`（MD5加密）

### 3. `test_data_02_textbooks.sql` - 教材数据
- **100本教材**
- 涵盖所有分类：
  - 计算机类（15本）
  - 数学类（10本）
  - 英语类（10本）
  - 经济管理类（10本）
  - 理工类（10本）
  - 文学艺术类（10本）
  - 法律类（10本）
  - 医学类（10本）
  - 考研考公类（15本）
- 不同状态：在售、已售、下架
- 不同审核状态：通过、待审核、拒绝

### 4. `test_data_03_orders_favorites.sql` - 订单、收藏、关注数据
- **50个订单**（不同状态：待确认、已确认、已完成、已取消）
- **100条收藏记录**
- **80条关注关系**

### 5. `test_data_04_messages_reviews.sql` - 消息、评价、求购数据
- **100条消息**（系统通知 + 用户私信）
- **30条评价记录**（买卖双方互评）
- **30条求购信息**

### 6. `test_data_05_banners_notices_images.sql` - 轮播图、公告、图片、浏览历史
- **10个轮播图**
- **13条公告**
- **60张教材图片**（前20本教材各2-4张）
- **150条浏览历史记录**

### 7. `test_data_06_admin_data.sql` - 管理员相关数据
- **6个管理员账号**（不同角色）
- **32个子分类**
- 更新部分教材和用户状态（用于测试管理功能）

## 🚀 使用方法

### 方法一：按顺序执行（推荐）

```bash
# 1. 初始化数据库
mysql -u root -p < init.sql

# 2. 导入用户数据
mysql -u root -p textbook_trading < test_data_01_users.sql

# 3. 导入教材数据
mysql -u root -p textbook_trading < test_data_02_textbooks.sql

# 4. 导入订单、收藏、关注数据
mysql -u root -p textbook_trading < test_data_03_orders_favorites.sql

# 5. 导入消息、评价、求购数据
mysql -u root -p textbook_trading < test_data_04_messages_reviews.sql

# 6. 导入轮播图、公告等数据
mysql -u root -p textbook_trading < test_data_05_banners_notices_images.sql

# 7. 导入管理员相关数据
mysql -u root -p textbook_trading < test_data_06_admin_data.sql
```

### 方法二：一次性导入所有数据

```bash
# 创建合并脚本
cat init.sql test_data_01_users.sql test_data_02_textbooks.sql test_data_03_orders_favorites.sql test_data_04_messages_reviews.sql test_data_05_banners_notices_images.sql test_data_06_admin_data.sql > all_test_data.sql

# 执行合并脚本
mysql -u root -p < all_test_data.sql
```

### 方法三：使用MySQL Workbench

1. 打开MySQL Workbench
2. 连接到数据库
3. 依次打开并执行各个SQL文件

## 👥 测试账号

### 普通用户账号
| 用户名 | 密码 | 昵称 | 学校 | 状态 |
|--------|------|------|------|------|
| zhangsan | admin123 | 张三 | 清华大学 | 正常 |
| lisi | admin123 | 李四 | 北京大学 | 正常 |
| wangwu | admin123 | 王五 | 复旦大学 | 正常 |
| ... | admin123 | ... | ... | ... |

*共50个用户，密码统一为 `admin123`*

### 管理员账号
| 用户名 | 密码 | 昵称 | 角色 | 状态 |
|--------|------|------|------|------|
| admin | admin123 | 超级管理员 | 超级管理员 | 正常 |
| content_admin | admin123 | 内容管理员-张伟 | 内容管理员 | 正常 |
| order_admin | admin123 | 订单管理员-李娜 | 订单管理员 | 正常 |
| content_admin2 | admin123 | 内容管理员-王强 | 内容管理员 | 正常 |
| order_admin2 | admin123 | 订单管理员-刘芳 | 订单管理员 | 正常 |
| test_admin | admin123 | 测试管理员 | 超级管理员 | 已禁用 |

## 📊 数据统计

执行完所有脚本后，数据库将包含：

- ✅ 50个用户
- ✅ 100本教材
- ✅ 50个订单
- ✅ 100条收藏
- ✅ 80条关注
- ✅ 100条消息
- ✅ 30条评价
- ✅ 30条求购
- ✅ 13条公告
- ✅ 10个轮播图
- ✅ 6个管理员
- ✅ 42个分类（10个主分类 + 32个子分类）
- ✅ 60张教材图片
- ✅ 150条浏览历史

## 🎯 覆盖的前端页面

### 用户端页面
- ✅ **首页 (Home)** - 轮播图、热门教材、公告
- ✅ **登录/注册 (Login/Register)** - 用户账号
- ✅ **教材列表 (TextbookList)** - 100本教材，多种分类
- ✅ **教材详情 (TextbookDetail)** - 详细信息、图片、卖家信息
- ✅ **发布教材 (Publish)** - 分类数据
- ✅ **我的订单 (MyOrders)** - 50个订单，各种状态
- ✅ **我的收藏 (MyFavorites)** - 100条收藏
- ✅ **消息中心 (Messages)** - 100条消息
- ✅ **个人中心 (UserCenter)** - 用户信息、统计数据
- ✅ **个人资料 (UserProfile)** - 用户详细信息
- ✅ **求购列表 (WantedList)** - 30条求购信息

### 管理端页面
- ✅ **数据统计 (Dashboard)** - 各类统计数据
- ✅ **用户管理 (UserManage)** - 50个用户，不同状态
- ✅ **教材管理 (TextbookManage)** - 100本教材
- ✅ **教材审核 (TextbookAudit)** - 待审核、已拒绝的教材
- ✅ **订单管理 (OrderManage)** - 50个订单
- ✅ **分类管理 (CategoryManage)** - 42个分类
- ✅ **公告管理 (NoticeManage)** - 13条公告
- ✅ **轮播图管理 (BannerManage)** - 10个轮播图
- ✅ **角色管理 (RoleManage)** - 3个角色
- ✅ **菜单管理 (MenuManage)** - 14个菜单
- ✅ **管理员管理 (AdminManage)** - 6个管理员

## 🔍 数据特点

### 真实性
- 使用真实的教材名称、作者、出版社、ISBN
- 合理的价格设置（原价的3-7折）
- 真实的学校、学院、专业名称

### 多样性
- 不同的教材状态（在售、已售、下架）
- 不同的订单状态（待确认、已确认、已完成、已取消）
- 不同的审核状态（通过、待审核、拒绝）
- 不同的用户状态（正常、禁用、已认证、未认证）

### 关联性
- 订单关联买家、卖家、教材
- 收藏关联用户和教材
- 关注关联用户之间
- 消息包含系统通知和用户私信
- 评价关联订单和用户

## ⚠️ 注意事项

1. **执行顺序**：必须先执行 `init.sql`，再按顺序执行其他测试数据脚本
2. **数据库编码**：确保数据库使用 `utf8mb4` 编码
3. **外键约束**：部分表有外键约束，删除数据时注意顺序
4. **图片URL**：使用的是占位图片服务，实际使用时需要替换为真实图片
5. **密码加密**：所有密码均为 `admin123` 的MD5加密值

## 🔄 重置数据

如需重置所有测试数据：

```sql
-- 删除数据库
DROP DATABASE IF EXISTS textbook_trading;

-- 重新执行所有脚本
-- 按照上述"使用方法"重新导入
```

## 📝 自定义数据

如需添加更多测试数据，可以参考现有SQL文件的格式，注意：
- 保持数据的关联性
- 使用合理的时间戳
- 遵循表结构约束

## 📞 技术支持

如有问题，请检查：
1. MySQL版本是否 >= 5.7
2. 数据库编码是否正确
3. 是否按顺序执行脚本
4. 是否有足够的数据库权限

---

**生成日期**: 2026-01-26  
**版本**: 1.0  
**适用系统**: 二手教材交易系统

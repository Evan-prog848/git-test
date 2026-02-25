# 教材图片目录说明

## 目录结构

```
public/images/books/
├── covers/          # 教材封面图片
├── details/         # 教材详情图片
├── avatars/         # 用户头像
└── banners/         # 轮播图
```

## 图片命名规范

### 教材封面
- 格式：`book_[id].jpg` 或 `book_[id].png`
- 示例：`book_1.jpg`, `book_2.jpg`
- 推荐尺寸：200x300 像素

### 教材详情图片
- 格式：`book_[id]_[序号].jpg`
- 示例：`book_1_1.jpg`, `book_1_2.jpg`
- 推荐尺寸：400x600 像素

### 用户头像
- 格式：`avatar_[id].jpg`
- 示例：`avatar_1.jpg`, `avatar_2.jpg`
- 推荐尺寸：150x150 像素

### 轮播图
- 格式：`banner_[id].jpg`
- 示例：`banner_1.jpg`, `banner_2.jpg`
- 推荐尺寸：1200x400 像素

## 准备图片步骤

### 方法1：使用真实教材照片
1. 拍摄或下载教材封面图片
2. 按照命名规范重命名
3. 放入对应目录

### 方法2：使用占位图片
1. 可以先使用纯色背景 + 文字的占位图
2. 后续替换为真实图片

### 方法3：下载示例图片
我已经在SQL文件中准备了100本教材的信息，你可以：
1. 搜索这些教材名称
2. 下载封面图片
3. 按照ID命名后放入目录

## 图片访问路径

在前端代码中，图片路径应该这样写：
```javascript
// 教材封面
cover: '/images/books/covers/book_1.jpg'

// 教材详情图片
images: [
  '/images/books/details/book_1_1.jpg',
  '/images/books/details/book_1_2.jpg'
]

// 用户头像
avatar: '/images/books/avatars/avatar_1.jpg'

// 轮播图
imageUrl: '/images/books/banners/banner_1.jpg'
```

## 默认占位图片

如果某个教材没有图片，系统会显示默认占位图：
- 默认封面：`/images/books/covers/default.jpg`
- 默认头像：`/images/books/avatars/default.jpg`

## 注意事项

1. **图片大小**：单张图片建议不超过500KB
2. **图片格式**：推荐使用 JPG 格式（文件小）
3. **图片质量**：保持清晰度，避免模糊
4. **版权问题**：确保使用的图片有合法授权

## 快速开始

如果你现在就想测试系统，可以：
1. 暂时使用占位图片服务（已在SQL中配置）
2. 后续逐步替换为本地图片
3. 或者先准备10-20张常用教材的封面图片用于演示

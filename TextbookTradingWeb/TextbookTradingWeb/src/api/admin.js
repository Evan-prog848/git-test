import request from '@/utils/request'

// 角色管理
export const getRoleList = (params) => request.get('/admin/role/list', { params })
export const getAllRoles = () => request.get('/admin/role/all')
export const addRole = (data) => request.post('/admin/role/add', data)
export const updateRole = (data) => request.put('/admin/role/update', data)
export const deleteRole = (id) => request.delete(`/admin/role/${id}`)
export const assignRoleMenus = (roleId, menuIds) => request.post(`/admin/role/assign-menus/${roleId}`, menuIds)
export const getRoleMenuIds = (roleId) => request.get(`/admin/role/menus/${roleId}`)

// 菜单管理
export const getMenuList = () => request.get('/admin/menu/list')
export const getMenuTree = () => request.get('/admin/menu/tree')
export const addMenu = (data) => request.post('/admin/menu/add', data)
export const updateMenu = (data) => request.put('/admin/menu/update', data)
export const deleteMenu = (id) => request.delete(`/admin/menu/${id}`)
export const adminLogin = (data) => request({ url: '/admin/login', method: 'post', data, silent: true })
export const getAdminInfo = () => request.get('/admin/info')
export const getStatistics = (params) => request.get('/admin/statistics', { params })
export const getAdminList = (params) => request.get('/admin/list', { params })
export const addAdmin = (data) => request.post('/admin/add', data)
export const updateAdmin = (data) => request.put('/admin/update', data)
export const deleteAdmin = (id) => request.delete(`/admin/${id}`)
export const updateAdminStatus = (id, status) => request.put(`/admin/status/${id}`, null, { params: { status } })

// 用户管理
export const getUserList = (params) => request.get('/admin/user/list', { params })
export const getUserDetail = (id) => request.get(`/admin/user/detail/${id}`)
export const updateUserStatus = (id, status) => request.put(`/admin/user/status/${id}`, null, { params: { status } })
export const addUser = (data) => request.post('/admin/user/add', data)
export const resetUserPassword = (id) => request.put(`/admin/user/reset-password/${id}`)

// 教材管理
export const getTextbookManageList = (params) => request.get('/admin/textbook/list', { params })
export const auditTextbook = (data) => request.post('/admin/textbook/audit', data)
export const updateTextbookManageStatus = (id, status) => request.put(`/admin/textbook/status/${id}`, null, { params: { status } })
export const deleteTextbook = (id) => request.delete(`/admin/textbook/${id}`)

// 订单管理
export const getOrderManageList = (params) => request.get('/admin/order/list', { params })

// 分类管理
export const getCategoryList = () => request.get('/admin/category/list')
export const addCategory = (data) => request.post('/admin/category/add', data)
export const updateCategory = (data) => request.put('/admin/category/update', data)
export const deleteCategory = (id) => request.delete(`/admin/category/${id}`)

// 公告管理
export const getNoticeList = () => request.get('/admin/notice/list')
export const addNotice = (data) => request.post('/admin/notice/add', data)
export const updateNotice = (data) => request.put('/admin/notice/update', data)
export const deleteNotice = (id) => request.delete(`/admin/notice/${id}`)

// 轮播图管理
export const getBannerList = () => request.get('/admin/banner/list')
export const addBanner = (data) => request.post('/admin/banner/add', data)
export const updateBanner = (data) => request.put('/admin/banner/update', data)
export const deleteBanner = (id) => request.delete(`/admin/banner/${id}`)



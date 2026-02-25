import request from '@/utils/request'

export const sendMessage = (data) => request.post('/message/send', data)
export const getMessageList = (params) => request.get('/message/list', { params })
export const getUnreadCount = () => request.get('/message/unread/count')
export const markAsRead = (id) => request.put(`/message/read/${id}`)
export const markAllAsRead = () => request.put('/message/read/all')
export const deleteMessage = (id) => request.delete(`/message/${id}`)



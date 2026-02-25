import request from '@/utils/request'

// 发送消息
export function sendMessage(data) {
  return request({
    url: '/chat/send',
    method: 'post',
    data
  })
}

// 获取会话列表
export function getConversations() {
  return request({
    url: '/chat/conversations',
    method: 'get'
  })
}

// 获取聊天记录
export function getChatHistory(otherUserId, params) {
  return request({
    url: `/chat/history/${otherUserId}`,
    method: 'get',
    params
  })
}

// 标记消息已读
export function markAsRead(otherUserId) {
  return request({
    url: `/chat/read/${otherUserId}`,
    method: 'put'
  })
}

// 获取未读消息数
export function getUnreadCount() {
  return request({
    url: '/chat/unread/count',
    method: 'get'
  })
}

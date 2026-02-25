import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

class WebSocketService {
  constructor() {
    this.stompClient = null
    this.connected = false
    this.subscriptions = {}
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5
    this.reconnectDelay = 3000
    this.messageHandlers = []
    this.readHandlers = []
  }

  // 连接WebSocket
  connect(token) {
    return new Promise((resolve, reject) => {
      if (this.connected) {
        resolve()
        return
      }

      const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
      const socket = new SockJS(`${baseUrl}/ws`)
      this.stompClient = Stomp.over(socket)

      // 禁用调试日志
      this.stompClient.debug = null

      const headers = {
        Authorization: `Bearer ${token}`
      }

      this.stompClient.connect(
        headers,
        () => {
          this.connected = true
          this.reconnectAttempts = 0
          console.log('WebSocket connected')

          // 订阅私人消息队列
          this.subscribeToMessages()
          this.subscribeToReadNotifications()

          resolve()
        },
        (error) => {
          console.error('WebSocket connection error:', error)
          this.connected = false
          this.handleReconnect(token)
          reject(error)
        }
      )
    })
  }

  // 订阅消息
  subscribeToMessages() {
    if (this.subscriptions.messages) {
      this.subscriptions.messages.unsubscribe()
    }

    this.subscriptions.messages = this.stompClient.subscribe(
      '/user/queue/messages',
      (message) => {
        const data = JSON.parse(message.body)
        this.messageHandlers.forEach(handler => handler(data))
      }
    )
  }

  // 订阅已读通知
  subscribeToReadNotifications() {
    if (this.subscriptions.read) {
      this.subscriptions.read.unsubscribe()
    }

    this.subscriptions.read = this.stompClient.subscribe(
      '/user/queue/read',
      (message) => {
        const userId = JSON.parse(message.body)
        this.readHandlers.forEach(handler => handler(userId))
      }
    )
  }

  // 发送消息
  sendMessage(toUserId, content, type = 1) {
    if (!this.connected || !this.stompClient) {
      console.error('WebSocket not connected')
      return false
    }

    this.stompClient.send(
      '/app/chat.send',
      {},
      JSON.stringify({
        toUserId,
        content,
        type
      })
    )
    return true
  }

  // 发送已读通知
  markAsRead(otherUserId) {
    if (!this.connected || !this.stompClient) {
      return false
    }

    this.stompClient.send(
      '/app/chat.read',
      {},
      JSON.stringify(otherUserId)
    )
    return true
  }

  // 添加消息处理器
  onMessage(handler) {
    this.messageHandlers.push(handler)
    return () => {
      const index = this.messageHandlers.indexOf(handler)
      if (index > -1) {
        this.messageHandlers.splice(index, 1)
      }
    }
  }

  // 添加已读通知处理器
  onRead(handler) {
    this.readHandlers.push(handler)
    return () => {
      const index = this.readHandlers.indexOf(handler)
      if (index > -1) {
        this.readHandlers.splice(index, 1)
      }
    }
  }

  // 处理重连
  handleReconnect(token) {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++
      console.log(`Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)
      setTimeout(() => {
        this.connect(token)
      }, this.reconnectDelay)
    }
  }

  // 断开连接
  disconnect() {
    if (this.stompClient) {
      Object.values(this.subscriptions).forEach(sub => {
        if (sub) sub.unsubscribe()
      })
      this.subscriptions = {}
      this.stompClient.disconnect()
      this.stompClient = null
    }
    this.connected = false
    this.messageHandlers = []
    this.readHandlers = []
  }

  // 检查连接状态
  isConnected() {
    return this.connected
  }
}

// 导出单例
export default new WebSocketService()

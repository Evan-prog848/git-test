package com.textbook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textbook.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket消息处理器
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
    
    // 存储用户ID和WebSocket会话的映射
    private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
            log.info("用户 {} WebSocket连接成功", userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long fromUserId = getUserIdFromSession(session);
        if (fromUserId == null) return;

        try {
            Map<String, Object> msgData = objectMapper.readValue(message.getPayload(), Map.class);
            Long toUserId = Long.valueOf(msgData.get("toUserId").toString());
            String content = (String) msgData.get("content");

            // 构建消息
            Map<String, Object> response = new ConcurrentHashMap<>();
            response.put("fromUserId", fromUserId);
            response.put("toUserId", toUserId);
            response.put("content", content);
            response.put("createTime", System.currentTimeMillis());

            String responseJson = objectMapper.writeValueAsString(response);

            // 发送给接收者
            WebSocketSession toSession = userSessions.get(toUserId);
            if (toSession != null && toSession.isOpen()) {
                toSession.sendMessage(new TextMessage(responseJson));
            }

            // 也发送给发送者确认
            session.sendMessage(new TextMessage(responseJson));

        } catch (Exception e) {
            log.error("处理WebSocket消息失败", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            log.info("用户 {} WebSocket连接关闭", userId);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        return (Long) session.getAttributes().get("userId");
    }

    /**
     * 向指定用户发送消息
     */
    public static void sendToUser(Long userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("发送WebSocket消息失败", e);
            }
        }
    }
}

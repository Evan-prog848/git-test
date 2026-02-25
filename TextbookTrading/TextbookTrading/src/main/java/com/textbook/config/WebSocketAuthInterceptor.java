package com.textbook.config;

import com.textbook.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;

/**
 * WebSocket认证拦截器
 * 在STOMP连接时验证JWT token
 */
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 从STOMP头中获取token
            String token = accessor.getFirstNativeHeader("Authorization");

            if (StringUtils.hasText(token)) {
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                try {
                    // 验证token并获取用户ID
                    Long userId = jwtUtils.getUserId(token);
                    if (userId != null) {
                        // 设置用户Principal
                        accessor.setUser(new StompPrincipal(userId.toString()));
                    }
                } catch (Exception e) {
                    // token无效，不设置用户
                }
            }
        }

        return message;
    }

    /**
     * 自定义Principal实现
     */
    private static class StompPrincipal implements Principal {
        private final String name;

        public StompPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}

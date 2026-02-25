package com.textbook.config;

import com.textbook.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 原生WebSocket配置
 */
@Configuration
@EnableWebSocket
public class NativeWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private com.textbook.config.WebSocketHandler webSocketHandler;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat")
                .addInterceptors(new AuthHandshakeInterceptor(jwtUtils))
                .setAllowedOrigins("*");
    }

    /**
     * 握手拦截器，用于验证Token
     */
    private static class AuthHandshakeInterceptor implements HandshakeInterceptor {

        private final JwtUtils jwtUtils;

        public AuthHandshakeInterceptor(JwtUtils jwtUtils) {
            this.jwtUtils = jwtUtils;
        }

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            if (request instanceof ServletServerHttpRequest) {
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                String token = servletRequest.getServletRequest().getParameter("token");
                
                if (token != null && !token.isEmpty()) {
                    try {
                        if (token.startsWith("Bearer ")) {
                            token = token.substring(7);
                        }
                        Long userId = jwtUtils.getUserId(token);
                        if (userId != null) {
                            attributes.put("userId", userId);
                            return true;
                        }
                    } catch (Exception e) {
                        // Token无效
                    }
                }
            }
            return false;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
        }
    }
}

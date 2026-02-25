package com.textbook.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textbook.common.Constants;
import com.textbook.common.Result;
import com.textbook.utils.JwtUtils;
import com.textbook.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS请求放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 获取Token
        String authHeader = request.getHeader(Constants.TOKEN_HEADER);
        String token = jwtUtils.extractToken(authHeader);

        // 验证Token
        if (token == null || !jwtUtils.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.unauthorized()));
            return false;
        }

        // 设置用户上下文
        Long userId = jwtUtils.getUserId(token);
        String username = jwtUtils.getUsername(token);
        String userType = jwtUtils.getUserType(token);

        UserContext.setUserId(userId);
        UserContext.setUsername(username);
        UserContext.setUserType(userType);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理用户上下文
        UserContext.clear();
    }
}

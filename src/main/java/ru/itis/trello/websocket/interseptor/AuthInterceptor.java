package ru.itis.trello.websocket.interseptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.entity.User;
import ru.itis.trello.security.details.UserDetailsImpl;

import java.util.Map;

@Component("authInterceptor")
public class AuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            map.putIfAbsent("user", UserDto.from(user));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}

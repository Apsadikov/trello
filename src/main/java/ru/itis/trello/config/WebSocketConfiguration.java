package ru.itis.trello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.itis.trello.websocket.handler.WebSocketMessageHandler;

@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {
    private WebSocketMessageHandler webSocketMessageHandler;
    private HandshakeInterceptor handshakeInterceptor;

    @Autowired
    public WebSocketConfiguration(WebSocketMessageHandler webSocketMessageHandler,
                                  @Qualifier("authInterceptor") HandshakeInterceptor handshakeInterceptor) {
        this.webSocketMessageHandler = webSocketMessageHandler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(webSocketMessageHandler, "/chat")
                .addInterceptors(handshakeInterceptor)
                .withSockJS();
    }
}

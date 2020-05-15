package ru.itis.trello.websocket.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.trello.dto.*;
import ru.itis.trello.service.BoardMemberService;
import ru.itis.trello.service.MessageService;
import ru.itis.trello.websocket.event.Event;
import ru.itis.trello.websocket.event.Message;
import ru.itis.trello.websocket.event.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@EnableWebSocket
public class WebSocketMessageHandler extends TextWebSocketHandler {
    private static final HashMap<String, ClientDto> clients = new HashMap<>();
    private static final HashMap<Long, List<ClientDto>> rooms = new HashMap<>();
    private ObjectMapper objectMapper;
    private BoardMemberService boardMemberService;
    private MessageService messageService;

    @Autowired
    public WebSocketMessageHandler(ObjectMapper objectMapper, BoardMemberService boardMemberService, MessageService messageService) {
        this.objectMapper = objectMapper;
        this.boardMemberService = boardMemberService;
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        clients.putIfAbsent(session.getId(), ClientDto.builder()
                .session(session)
                .user((UserDto) session.getAttributes().get("user"))
                .build());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String event = message.getPayload().toString();

        String header = objectMapper.readValue(event, Event.class).getHeader();
        if (header.equals("message")) {
            Event<Message> messageEvent = objectMapper.readValue(event, new TypeReference<Event<Message>>() {
            });
            messageEvent.getPayload().setName(clients.get(session.getId()).getUser().getName());
            TextMessage jsonMessage = new TextMessage(objectMapper.writeValueAsString(messageEvent));

            Long roomId = messageEvent.getPayload().getRoomId();
            Long userId = clients.get(session.getId()).getUser().getId();

            if (boardMemberService.isBoardMemberExist(roomId, userId)) {
                messageService.addMessage(MessageDto.builder()
                        .message(messageEvent.getPayload().getText())
                        .boardDto(BoardDto.builder()
                                .id(roomId)
                                .build())
                        .userDto(UserDto.builder()
                                .id(userId)
                                .build())
                        .build());
                rooms.get(roomId).stream()
                        .filter(client -> !client.getSession().getId().equals(session.getId()))
                        .forEach(client -> {
                            if (client.getSession().isOpen()) {
                                try {
                                    client.getSession().sendMessage(jsonMessage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        } else if (header.equals("room")) {
            Event<Room> roomEvent = objectMapper.readValue(event, new TypeReference<Event<Room>>() {
            });
            rooms.putIfAbsent(roomEvent.getPayload().getBoardId(), new ArrayList<>());
            List<ClientDto> clients = rooms.get(roomEvent.getPayload().getBoardId());
            if (!clients.contains(WebSocketMessageHandler.clients.get(session.getId()))) {
                clients.add(WebSocketMessageHandler.clients.get(session.getId()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
        rooms.forEach((roomId, clients) -> {
            Optional<ClientDto> optionalClient = clients.stream()
                    .filter(client -> client.getSession().getId().equals(session.getId())).findFirst();
            optionalClient.ifPresent(clients::remove);
        });
    }
}

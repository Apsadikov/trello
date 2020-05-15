package ru.itis.trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.trello.aspect.Logging;
import ru.itis.trello.entity.Board;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.BoardMemberService;
import ru.itis.trello.service.BoardService;
import ru.itis.trello.service.MessageService;
import ru.itis.trello.service.StackService;

import java.util.Optional;

@Controller
public class BoardController {
    private BoardMemberService boardMemberService;
    private StackService stackService;
    private MessageService messageService;
    private BoardService boardService;

    @Autowired
    public BoardController(BoardMemberService boardMemberService, StackService stackService,
                           MessageService messageService, BoardService boardService) {
        this.boardMemberService = boardMemberService;
        this.stackService = stackService;
        this.messageService = messageService;
        this.boardService = boardService;
    }

    @Logging
    @GetMapping("/boards/{id}")
    public String board(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("id") Long boardId, Model model) {
        Optional<Board> boardOptional = boardService.getBoard(boardId);
        if (!boardOptional.isPresent() || !boardMemberService.isBoardMemberExist(boardId, userDetails.getUser().getId())) {
            return "redirect:/error";
        }

        model.addAttribute("stacks", stackService.getStacks(boardId));
        model.addAttribute("messages", messageService.getMessages(boardId));
        model.addAttribute("board", boardOptional.get());
        model.addAttribute("userId", userDetails.getUser().getId());
        return "board";
    }
}
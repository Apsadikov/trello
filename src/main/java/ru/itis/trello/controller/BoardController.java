package ru.itis.trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.trello.aspect.Authorization;
import ru.itis.trello.aspect.Logging;
import ru.itis.trello.entity.Board;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.*;

import java.util.Optional;

@Controller
public class BoardController {
    private BoardMemberService boardMemberService;
    private StackService stackService;
    private MessageService messageService;
    private BoardService boardService;
    private CardService cardService;
    private ArchiveService archiveService;

    @Autowired
    public BoardController(BoardMemberService boardMemberService, StackService stackService,
                           MessageService messageService, BoardService boardService, CardService cardService, ArchiveService archiveService) {
        this.boardMemberService = boardMemberService;
        this.stackService = stackService;
        this.messageService = messageService;
        this.boardService = boardService;
        this.cardService = cardService;
        this.archiveService = archiveService;
    }

    @Logging
    @PreAuthorize("isAuthenticated()")
    @Authorization
    @GetMapping("/boards/{board_id}")
    public String board(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Optional<Board> boardOptional = boardService.getBoard(boardId);
        if (!boardOptional.isPresent()) {
            return "redirect:/error";
        }

        model.addAttribute("archivedCards", archiveService.getCards(boardId));
        model.addAttribute("stacks", stackService.getStacks(boardId));
        model.addAttribute("members", boardMemberService.getBoardMembers(boardId));
        model.addAttribute("messages", messageService.getMessages(boardId));
        model.addAttribute("board", boardOptional.get());
        model.addAttribute("userId", userDetails.getUser().getId());
        model.addAttribute("userId", userDetails.getUser().getId());
        return "board";
    }
}
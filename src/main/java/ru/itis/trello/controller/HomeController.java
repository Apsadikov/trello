package ru.itis.trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.trello.dto.AddBoardForm;
import ru.itis.trello.dto.BoardDto;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.BoardService;

import javax.validation.Valid;

@Controller
public class HomeController {
    private BoardService boardService;

    @Autowired
    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        model.addAttribute("css", "home");
        model.addAttribute("boards", boardService.getBoards(userDetails.getUser().getId()));
        model.addAttribute("link", "/logout");
        model.addAttribute("linkTitle", "logout");
        model.addAttribute("addBoardForm", new AddBoardForm());
        return "home";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @Valid AddBoardForm addBoardForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            boardService.addBoard(BoardDto.builder()
                    .title(addBoardForm.getTitle())
                    .userId(userDetails.getUser().getId())
                    .build());
        }
        model.addAttribute("css", "home");
        model.addAttribute("boards", boardService.getBoards(userDetails.getUser().getId()));
        model.addAttribute("link", "/logout");
        model.addAttribute("linkTitle", "logout");
        model.addAttribute("addBoardForm", addBoardForm);
        return "home";
    }
}

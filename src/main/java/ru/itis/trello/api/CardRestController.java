package ru.itis.trello.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.trello.dto.CardDto;
import ru.itis.trello.dto.StackDto;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.BoardMemberService;
import ru.itis.trello.service.CardService;

@RestController
public class CardRestController {
    private CardService cardService;
    private BoardMemberService boardMemberService;

    @Autowired
    public CardRestController(CardService cardService, BoardMemberService boardMemberService) {
        this.cardService = cardService;
        this.boardMemberService = boardMemberService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/api/cards/{id}/action=move", method = RequestMethod.PUT)
    public ResponseEntity addStack(@PathVariable("id") Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestParam("stack_id") Long stackId, @RequestParam("board_id") Long boardId) {
        if (!boardMemberService.isBoardMemberExist(boardId, userDetails.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            cardService.moveCard(CardDto.builder()
                    .id(cardId)
                    .stack(StackDto.builder()
                            .id(stackId)
                            .build())
                    .build());
        }
        return ResponseEntity.ok().build();
    }
}

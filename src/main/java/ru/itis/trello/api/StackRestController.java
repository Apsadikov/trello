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
import ru.itis.trello.service.CardService;
import ru.itis.trello.service.StackService;
import ru.itis.trello.util.exception.AccessException;

@RestController
public class StackRestController {
    private CardService cardService;
    private StackService stackService;

    @Autowired
    public StackRestController(CardService cardService, StackService stackService) {
        this.cardService = cardService;
        this.stackService = stackService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/api/stacks/{stack_id}/cards", method = RequestMethod.POST)
    public ResponseEntity addCard(@PathVariable("stack_id") Long stackId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("title") String title) {
        if (title.trim().length() != 0) {
            try {
                CardDto cardDto = CardDto.builder()
                        .stack(StackDto.builder().id(stackId).build())
                        .title(title)
                        .build();
                cardService.addCard(cardDto, userDetails.getUser().getId());
                return ResponseEntity.ok(cardDto);
            } catch (AccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/api/boards/{id}/stacks", method = RequestMethod.POST)
    public ResponseEntity addStack(@PathVariable("id") Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestParam("title") String title) {
        if (title.trim().length() != 0) {
            StackDto stackDto = StackDto.builder()
                    .title(title.trim())
                    .build();
            try {
                stackService.addStack(stackDto, userDetails.getUser().getId(), boardId);
                return ResponseEntity.ok(stackDto);
            } catch (AccessException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

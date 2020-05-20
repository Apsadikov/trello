package ru.itis.trello.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.ArchiveService;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

@RestController
public class ArchiveRestController {
    private ArchiveService archiveService;

    @Autowired
    public ArchiveRestController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/api/archive", method = RequestMethod.PUT)
    public ResponseEntity addCardToArchive(@RequestParam("card_id") Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            archiveService.addCard(cardId, userDetails.getUser().getId());
            return ResponseEntity.ok().build();
        } catch (NotFoundException | AccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

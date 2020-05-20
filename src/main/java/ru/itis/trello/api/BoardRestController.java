package ru.itis.trello.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.trello.aspect.RestAuthorization;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.BoardMemberService;
import ru.itis.trello.util.exception.NotFoundException;

@RestController
public class BoardRestController {

    private BoardMemberService boardMemberService;

    @Autowired
    public BoardRestController(BoardMemberService boardMemberService) {
        this.boardMemberService = boardMemberService;
    }

    @PreAuthorize("isAuthenticated()")
    @RestAuthorization
    @RequestMapping(value = "/api/boards/{board_id}/members", method = RequestMethod.POST)
    public ResponseEntity addMember(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @RequestParam("invited_user_id") Long invitedUserId) {
        try {
            boardMemberService.addBoardMember(invitedUserId, userDetails.getUser().getId(), boardId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @RestAuthorization
    @RequestMapping(value = "/api/boards/{board_id}/members", method = RequestMethod.DELETE)
    public ResponseEntity deleteMember(@PathVariable("board_id") Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @RequestParam("deleted_user_id") Long invitedUserId) {
        try {
            boardMemberService.deleteBoardMember(invitedUserId, userDetails.getUser().getId(), boardId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}

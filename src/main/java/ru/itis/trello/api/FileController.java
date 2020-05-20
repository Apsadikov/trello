package ru.itis.trello.api;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.trello.dto.FileDto;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.FileService;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/api/files", method = RequestMethod.POST)
    public ResponseEntity addFile(@RequestParam("file") MultipartFile file, @RequestParam("card_id") Long cardId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        try {
            return ResponseEntity.ok(fileService.addFile(userDetails.getUser().getId(), file, cardId));
        } catch (AccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/api/files", method = RequestMethod.GET)
    public void getFile(@RequestParam("file") String file, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        Optional<FileDto> fileDto = fileService.getFile(userDetails.getUser().getId(), file);
        if (fileDto.isPresent()) {
            response.setContentType(Files.probeContentType(Paths.get(fileDto.get().getRealFile().getPath())));
            IOUtils.copy(new FileInputStream(fileDto.get().getRealFile()), response.getOutputStream());
        }
    }
}

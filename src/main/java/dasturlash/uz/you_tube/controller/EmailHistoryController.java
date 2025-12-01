package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.EmailHistoryDTO;
import dasturlash.uz.you_tube.service.EmailHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sms")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/phone")
    public ResponseEntity<List<EmailHistoryDTO>> getByPhone(@RequestParam("phone") String  email) {
        return ResponseEntity.ok(emailHistoryService.getSmsDtoByEmail(email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date")
    public ResponseEntity<List<EmailHistoryDTO>> getByDate(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(emailHistoryService.getSmsDtoByDate(date));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<EmailHistoryDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(emailHistoryService.pagination(page, size));
    }
}
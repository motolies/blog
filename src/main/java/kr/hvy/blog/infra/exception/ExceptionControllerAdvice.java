package kr.hvy.blog.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import kr.hvy.blog.infra.notify.SlackMessenger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> defaultException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);

        SlackMessenger.send(e);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "ERROR");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(map);
    }

}

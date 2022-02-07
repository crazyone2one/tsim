package cn.master.tsim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author by 11's papa on 2022年02月07日
 * @version 1.0.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MentionedFileNotFoundException extends RuntimeException {
    public MentionedFileNotFoundException(String message) {
        super(message);
    }

    public MentionedFileNotFoundException(String message, Throwable caught) {
        super(message, caught);
    }
}

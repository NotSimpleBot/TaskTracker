package com.guzanov.crazy_task_tracker.api.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Пробрасываемая ошибка, перехватим в com.guzanov.crazy_task_tracker.api.exeptions.CustomExceptionHandler*/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

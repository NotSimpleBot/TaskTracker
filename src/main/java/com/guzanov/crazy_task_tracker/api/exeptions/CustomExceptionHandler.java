package com.guzanov.crazy_task_tracker.api.exeptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Перехватывает все возникающие ошибки (Ответ)
 * перехват и проброс на com.guzanov.crazy_task_tracker.api.exeptions.CustomErrorController
 */
@Log4j2
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class) //все подряд ошибки хватаем и оборачиваем в ResponseEntity (что бы передать статус на клиента)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) throws Exception {
        log.error("Exception asdasdasd", ex);
        return handleException(ex, request);
    }
}

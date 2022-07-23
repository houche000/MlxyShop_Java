package com.zbkj.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Crmeb异常类
 */
@Slf4j
@RestControllerAdvice
public class CrmebException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CrmebException() {}

    public CrmebException(String message) {
        super(message);
    }
}

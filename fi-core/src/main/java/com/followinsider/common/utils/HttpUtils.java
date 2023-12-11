package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@UtilityClass
public class HttpUtils {

    public static void throwForbidden(String message) {
        throwError(message, HttpStatus.FORBIDDEN);
    }

    public static void throwBadRequest(String message) {
        throwError(message, HttpStatus.BAD_REQUEST);
    }

    public static void throwServerError(String message) {
        throwError(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void throwError(String message, HttpStatus status) {
        throw new ResponseStatusException(status, message);
    }

}

package com.langlearnquiz.backend.exceptions;

import com.langlearnquiz.backend.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EmptyImageException.class, EmptyImageFilenameException.class, NotAllowedFileExtensionException.class})
    public ResponseEntity<ErrorDTO> handleFilesExceptions(AbstractFileException e) {
        ErrorDTO errorDTO = new ErrorDTO(e, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<ErrorDTO> handleRestClientExceptions(RestClientResponseException e) {
        ErrorDTO errorDTO = new ErrorDTO(e, (HttpStatus) e.getStatusCode());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
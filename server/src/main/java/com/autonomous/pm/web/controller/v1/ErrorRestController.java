package com.autonomous.pm.web.controller.v1;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import com.autonomous.pm.domain.Exception.BaseException;
import com.autonomous.pm.domain.common.FieldErrorDetail;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
//import com.autonomous.pm.storage.StorageFileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//ref https://supawer0728.github.io/2019/04/04/spring-error-handling/

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorRestController {

    public static final Logger logger = LoggerFactory.getLogger(ErrorRestController.class);

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleInputException(BindException ex) {
        BindingResult bindingResult = (BindingResult) ex;

        bindingResult.getFieldErrorCount();
        logger.debug("Field Error Count : " + bindingResult.getFieldErrorCount());
        List<FieldError> re = bindingResult.getFieldErrors();
        for (FieldError f : re) {
            
         
            logger.debug("Default Message : " + f.getDefaultMessage());
            logger.debug("Field : " + f.getField());
            if( f.getRejectedValue()!=null)
                logger.debug("regject value:"+ f.getRejectedValue() );

        }

        List<FieldErrorDetail> details = bindingResult.getFieldErrors().stream()
                .map(error -> FieldErrorDetail.create(error, error.getDefaultMessage())).collect(Collectors.toList());

        return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.INVALID_INPUT_FIELD, details), HttpStatus.OK);

    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleRuntimeException(BaseException exc) {
        // return ResponseEntity.notFound().build();
        return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, exc.getMessage()), HttpStatus.OK);
    }

//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        // return ResponseEntity.notFound().build();
//        return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.NOT_FOUND_RESOURCE, exc.getMessage()),
//                HttpStatus.OK);
//
//    }

    // @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    // public ResponseEntity<?> HttpMessageNotReadableException(StorageFileNotFoundException exc) {
    //     // return ResponseEntity.notFound().build();
    //     return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.NOT_FOUND_RESOURCE, exc.getMessage()),
    //             HttpStatus.OK);

    // }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleRuntimeException(SQLIntegrityConstraintViolationException exc) {
        return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.INVALID_INPUT_FIELD, exc.getMessage()),
                HttpStatus.OK);
    
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) {
        return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.INVALID_INPUT_FIELD, exc.getMessage()),
                HttpStatus.OK);
    
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<?> handleRuntimeException(ServletException exc) {
        // return ResponseEntity.notFound().build();
        return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.INVALID_INPUT_FIELD, exc.getMessage()),
                HttpStatus.OK);
    }

}
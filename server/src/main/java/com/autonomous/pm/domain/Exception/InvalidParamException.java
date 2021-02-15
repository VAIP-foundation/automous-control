package com.autonomous.pm.domain.Exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class InvalidParamException extends BindException {

    private static final long serialVersionUID = 1L;

    public InvalidParamException(BindingResult bindingResult) {
        super(bindingResult);
       
    }

}
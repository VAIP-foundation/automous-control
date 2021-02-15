package com.autonomous.pm.component.validator;

import java.util.Collection;

import javax.validation.Validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

//ref https://github.com/HomoEfficio/dev-tips/wiki/SpringMVC%EC%97%90%EC%84%9C-Collection%EC%9D%98-Validation
@Component
public class CustomCollectionValidator implements Validator {

    private SpringValidatorAdapter validator;

    public CustomCollectionValidator() {
        this.validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Collection collection = (Collection) target;

        for (Object object : collection) {
            validator.validate(object, errors);
        }
    }
}
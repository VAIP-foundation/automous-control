package com.autonomous.pm.domain.common;

import org.springframework.validation.FieldError;

public class FieldErrorEx extends FieldError
{
    private static final long serialVersionUID = 1L;

    private String id;

    private Integer index;

    public FieldErrorEx(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes,
            Object[] arguments, String defaultMessage) {
        super(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
       
        this.id = null;
    }
    
    public FieldErrorEx(String id,Integer index,String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes,
            Object[] arguments, String defaultMessage) {
        super(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
       
        this.id = id;
        this.index = index;
    }

    public FieldErrorEx(String id, Integer index, String objectName, String field, String defaultMessage) {
		this(id,index, objectName, field, null, false, null, null, defaultMessage);
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
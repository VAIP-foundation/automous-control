package com.autonomous.pm.domain.Exception;


public class AlreadyMonthEndClosingException extends BaseException 
{
    private static final long serialVersionUID = 1L;

    public AlreadyMonthEndClosingException(String msg)
    {
        super(msg);
    }
}
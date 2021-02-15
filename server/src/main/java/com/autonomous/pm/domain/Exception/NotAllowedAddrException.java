package com.autonomous.pm.domain.Exception;

public class NotAllowedAddrException extends BaseException 
{
    private static final long serialVersionUID = 1L;

    public NotAllowedAddrException(String msg)
    {
        super(msg);
    }
}
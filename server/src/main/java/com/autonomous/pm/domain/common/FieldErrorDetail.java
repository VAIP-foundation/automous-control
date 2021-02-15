package com.autonomous.pm.domain.common;

import org.springframework.validation.FieldError;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldErrorDetail {
   private String objectName;
   private String field;
   private String errorCode;
   private String errorMessage;
   private Integer index;
   private String id;

   public String getErrorCode()
   {
      if("NotNull".equals(this.errorCode))
       return "301";
      else if("Size".equals(this.errorCode))
      {
         return "302";
      }else if("Pattern".equals(this.errorCode))
      {
         return "302";
      }else      
         return null;
   }

   public static FieldErrorDetail create(FieldError fieldError, String errorMessage) {

      
      return new FieldErrorDetail(fieldError.getObjectName(), 
                                  fieldError.getField(),
                                  fieldError.getCode(),
                                  errorMessage,null,null);
   }

   public static FieldErrorDetail create(Integer index,String id, FieldError fieldError, String errorMessage) {
    
      return new FieldErrorDetail(fieldError.getObjectName()
                                , fieldError.getField()
                                , fieldError.getCode()
                                , errorMessage
                                , index,id);
   }
}
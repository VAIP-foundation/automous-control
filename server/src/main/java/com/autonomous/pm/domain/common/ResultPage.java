package com.autonomous.pm.domain.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@JsonInclude(value = Include.NON_EMPTY)	
public class ResultPage extends ResultValue{
	
	
	private Object rows;
	private int page;           // current page
	private int totalPages;     //총 페이지 수
    private int records;        // 총 레코드 수
    private int pageLength = 10;

	public ResultPage(ResultCode resultCode,Object data,int records) {
		super(resultCode, null);

		if(data != null && data instanceof List<?>)
		{
			this.rows=data;
			this.page = 0;		

			this.records = records; 
			this.totalPages = (records / pageLength)+1;
		}
	}
	
	public ResultPage(ResultCode resultCode, Object data,int records,int currentPage,int pageLength) {
		super(resultCode, null);
		
		if(data != null)
		{
			this.rows=data;
			this.page = currentPage;
			
            this.records =records; 
            this.pageLength = pageLength;
			this.totalPages = (records / pageLength)+1;
		}
	}
	

}
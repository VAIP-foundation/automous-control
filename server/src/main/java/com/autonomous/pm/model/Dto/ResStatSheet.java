package com.autonomous.pm.model.Dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResStatSheet
{
	private String sheet;
	private List<String> list;
	private Object data;
	
}
package com.autonomous.pm.domain.structure.upload;

import com.autonomous.pm.domain.structure.Join;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinUpload {
	
	private String type;
	private Join data;

}
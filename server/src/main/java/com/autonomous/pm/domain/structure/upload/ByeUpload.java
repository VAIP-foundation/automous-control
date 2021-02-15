package com.autonomous.pm.domain.structure.upload;

import com.autonomous.pm.domain.structure.Bye;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ByeUpload {

	private String type;
	private Bye data;

}

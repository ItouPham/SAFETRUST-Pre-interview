package com.safetrust.preinterview.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageEnum {
	GET_DATA_SUCCESS("Get data successfully"),
	CONTACT_NOT_EXISTS("Contact not exists"),
	EXISTED_EMAIL("Email %EMAIL% has existed"),
	EXISTED_PHONE_NUMBER("Phone number %PHONE_NUMBER% has existed"),
	ADD_CONTACT_SUCCESS("Add new contact successfully"),
	ADD_CONTACT_UNSUCCESS("Add new contact unsuccessfully"),
	UPDATE_CONTACT_SUCCESS("Update contact successfully"),
	UPDATE_CONTACT_UNSUCCESS("Update contact unsuccessfully"),
	DELETE_CONTACT_SUCCESS("Delete contact successfully"),
	TOTAL_RESULTS_FOUND("%TOTAL% results found");
	
	private String message;
}

package com.safetrust.preinterview.model.response;

import com.safetrust.preinterview.entity.Contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {
	private Contact contact;
	private String message;
}

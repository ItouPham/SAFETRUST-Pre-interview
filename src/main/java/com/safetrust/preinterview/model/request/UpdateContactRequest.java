package com.safetrust.preinterview.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateContactRequest {
	private String name;

	private String email;

	private String address;

	private String telephoneNumber;

	private String postalAddress;
}

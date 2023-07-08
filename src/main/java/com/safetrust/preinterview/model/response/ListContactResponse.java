package com.safetrust.preinterview.model.response;

import java.util.List;

import com.safetrust.preinterview.entity.Contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListContactResponse {
	List<Contact> contacts;
	private int currentPage;
	private long totalItems;
	private int totalPages;
	private String message;
}

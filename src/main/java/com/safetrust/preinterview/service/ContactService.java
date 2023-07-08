package com.safetrust.preinterview.service;

import org.springframework.http.ResponseEntity;

import com.safetrust.preinterview.model.request.AddContactRequest;
import com.safetrust.preinterview.model.request.UpdateContactRequest;

public interface ContactService {

	ResponseEntity<?> createNewContact(AddContactRequest request);

	ResponseEntity<?> getAllContacts(int page, int size);

	ResponseEntity<?> getContactById(String id);

	ResponseEntity<?> updateContact(UpdateContactRequest request, String id);

	ResponseEntity<?> deleteContact(String id);

	ResponseEntity<?> searchContacts(String keyword);


}

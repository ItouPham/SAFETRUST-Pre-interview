package com.safetrust.preinterview.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetrust.preinterview.model.request.AddContactRequest;
import com.safetrust.preinterview.model.request.SearchRequest;
import com.safetrust.preinterview.model.request.UpdateContactRequest;
import com.safetrust.preinterview.service.ContactService;

@RestController
@RequestMapping("/contact")
public class ContactController {

	@Autowired
	private ContactService contactService;
	
	@GetMapping()
	public ResponseEntity<?> getAllContacts(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size){
		return contactService.getAllContacts(page, size);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getContactById(@PathVariable String id){
		return contactService.getContactById(id);
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> searchContacts(@RequestBody @Valid SearchRequest request){
		return contactService.searchContacts(request.getKeyword());
	}
	
	@PostMapping()
	public ResponseEntity<?> createNewContact(@RequestBody @Valid AddContactRequest request) {
		return contactService.createNewContact(request);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateContact(@RequestBody @Valid UpdateContactRequest request, @PathVariable String id) {
		return contactService.updateContact(request,id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteContact(@PathVariable String id) {
		return contactService.deleteContact(id);
	}
	
}

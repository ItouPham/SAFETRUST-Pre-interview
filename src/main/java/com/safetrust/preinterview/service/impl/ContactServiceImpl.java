package com.safetrust.preinterview.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.safetrust.preinterview.constant.MessageEnum;
import com.safetrust.preinterview.entity.Contact;
import com.safetrust.preinterview.exception.ResourceNotFoundException;
import com.safetrust.preinterview.exception.ServiceException;
import com.safetrust.preinterview.model.request.AddContactRequest;
import com.safetrust.preinterview.model.request.UpdateContactRequest;
import com.safetrust.preinterview.model.response.ContactResponse;
import com.safetrust.preinterview.model.response.ListContactResponse;
import com.safetrust.preinterview.repository.ContactRepository;
import com.safetrust.preinterview.service.ContactService;

import util.ValidateFields;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public ResponseEntity<?> getAllContacts(int page, int size) {
		ListContactResponse listContactResponse = new ListContactResponse();
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Contact> contacts = contactRepository.findAll(paging);
		listContactResponse.setCurrentPage(contacts.getNumber() + 1);
		listContactResponse.setTotalPages(contacts.getTotalPages());
		listContactResponse.setTotalItems(contacts.getTotalElements());
		listContactResponse.setContacts(contacts.toList());
		listContactResponse.setMessage(MessageEnum.GET_DATA_SUCCESS.getMessage());
		return new ResponseEntity<ListContactResponse>(listContactResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getContactById(String id) {
		ContactResponse contactResponse = new ContactResponse();
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		contactResponse.setContact(contact);
		contactResponse.setMessage(MessageEnum.GET_DATA_SUCCESS.getMessage());
		return new ResponseEntity<ContactResponse>(contactResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> createNewContact(AddContactRequest request) {
		ContactResponse contactResponse = new ContactResponse();
		if (contactRepository.existsByEmail(request.getEmail())) {
			throw new ServiceException("The email already in use");
		}
		if (contactRepository.existsByTelephoneNumber(request.getTelephoneNumber())) {
			throw new ServiceException("The phone number already in use");
		}
		Contact contact = new Contact();
		BeanUtils.copyProperties(request, contact);
		contact = contactRepository.save(contact);
		if (contact != null) {
			contactResponse.setContact(contact);
			contactResponse.setMessage(MessageEnum.ADD_CONTACT_SUCCESS.getMessage());
			return new ResponseEntity<ContactResponse>(contactResponse, HttpStatus.CREATED);
		} else {
			contactResponse.setMessage(MessageEnum.ADD_CONTACT_UNSUCCESS.getMessage());
			return new ResponseEntity<ContactResponse>(contactResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> updateContact(UpdateContactRequest request, String id) {
		ContactResponse contactResponse = new ContactResponse();
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		
		if(!StringUtils.isEmpty(request.getEmail())) {
			if(!request.getEmail().equals(contact.getEmail())) {
				if(!ValidateFields.checkFormatEmail(request.getEmail())) {
					throw new ServiceException("Invalid email");
				} 
				
				if (contactRepository.existsByEmail(request.getEmail())) {
					throw new ServiceException("The email already in use");
				}	
			}
		} else {
			request.setEmail(contact.getEmail());
		}
		
		if(!StringUtils.isEmpty(request.getTelephoneNumber())) {
			if(!request.getTelephoneNumber().equals(contact.getTelephoneNumber())) {
				if(!ValidateFields.checkFormatPhoneNumber(request.getTelephoneNumber())) {
					throw new ServiceException("Invalid phone number");
				}
				
				if (contactRepository.existsByTelephoneNumber(request.getTelephoneNumber())) {
					throw new ServiceException("The phone number already in use");
				}
			}
		} else {
			request.setTelephoneNumber(contact.getTelephoneNumber());
		}
		
		if(StringUtils.isEmpty(request.getAddress())){
			request.setAddress(contact.getAddress());
		}
		
		if(StringUtils.isEmpty(request.getName())){
			request.setName(contact.getName());
		}
		
		if(StringUtils.isEmpty(request.getPostalAddress())){
			request.setPostalAddress(contact.getPostalAddress());
		}

		BeanUtils.copyProperties(request, contact);
		Contact savedContact = contactRepository.save(contact);
		if (savedContact != null) {
			contactResponse.setContact(savedContact);
			contactResponse.setMessage(MessageEnum.UPDATE_CONTACT_SUCCESS.getMessage());
			return new ResponseEntity<ContactResponse>(contactResponse, HttpStatus.OK);

		} else {
			contactResponse.setMessage(MessageEnum.UPDATE_CONTACT_UNSUCCESS.getMessage());
			return new ResponseEntity<ContactResponse>(contactResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<?> deleteContact(String id) {
		ContactResponse contactResponse = new ContactResponse();
		if(!contactRepository.existsById(id)) {
			throw new ResourceNotFoundException("Contact not found with id: " + id);
		}
		contactRepository.deleteById(id);
		contactResponse.setContact(null);
		contactResponse.setMessage(MessageEnum.DELETE_CONTACT_SUCCESS.getMessage());
		return new ResponseEntity<ContactResponse>(contactResponse, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> searchContacts(String keyword) {
		ListContactResponse contactsResponse = new ListContactResponse();
		List<Contact> contacts = contactRepository.search(keyword);
		if (contacts != null && contacts.size() > 0) {
			contactsResponse.setContacts(contacts);
		}
		String message = MessageEnum.TOTAL_RESULTS_FOUND.getMessage().replace("%TOTAL%", contacts.size() + "");
		contactsResponse.setMessage(message);
		return new ResponseEntity<ListContactResponse>(contactsResponse, HttpStatus.OK);
	}

}

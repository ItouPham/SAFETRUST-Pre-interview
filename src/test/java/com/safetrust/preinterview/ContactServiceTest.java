package com.safetrust.preinterview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.safetrust.preinterview.entity.Contact;
import com.safetrust.preinterview.model.request.AddContactRequest;
import com.safetrust.preinterview.model.request.UpdateContactRequest;
import com.safetrust.preinterview.repository.ContactRepository;
import com.safetrust.preinterview.service.impl.ContactServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
	
	@Mock
	private ContactRepository contactRepository;
	
	@InjectMocks
	private ContactServiceImpl contactServiceImpl;
	
	@Test
	public void testServiceGetAllContact() {
		Contact contact1 = new Contact("Quynh Ho","quynhho@gmail.com","HCM","0388844444","91000");
		Contact contact2 = new Contact("Nam Tran","namtran@gmail.com","HCM","0388855555","91000");
		Contact contact3 = new Contact("Phung Nguyen","phungnguyen@gmail.com","HCM","0388866666","91000");
		List<Contact> contacts = new ArrayList<>(Arrays.asList(contact1,contact2,contact3));
		int page = 1, size = 5;
		Pageable paging = PageRequest.of(page - 1, size);
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), contacts.size());
		final Page<Contact> pageContacts = new PageImpl<>(contacts.subList(start, end), paging, contacts.size());
		Mockito.when(contactRepository.findAll(paging)).thenReturn(pageContacts);
		ResponseEntity<?> savedContact = contactServiceImpl.getAllContacts(page,size);
		assertThat(savedContact.getBody() != null);
		assertEquals(savedContact.getStatusCode(), HttpStatus.OK);
		assertEquals(pageContacts.getNumber(), page-1);
	}
	
	@Test
	public void testServiceFindById() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		Optional<Contact> contact = Optional.of(new Contact("Quynh Ho","quynhho@gmail.com","HCM","0388844444","91000"));
        contact.get().setId(uuidAsString);
        Mockito.when(contactRepository.findById(uuidAsString)).thenReturn(contact);
        ResponseEntity<?> data = contactServiceImpl.getContactById(uuidAsString);
        assertThat(data.getBody() != null);
		assertEquals(data.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void testServiceCreateContact() {
		AddContactRequest request = new AddContactRequest("Quynh Ho","quynhho@gmail.com","HCM","0388844444","91000");
		Contact contact = new Contact();
		BeanUtils.copyProperties(request, contact);
		Mockito.when(contactRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		Mockito.when(contactRepository.existsByTelephoneNumber(Mockito.anyString())).thenReturn(false);
		Mockito.when(contactRepository.save(contact)).thenReturn(contact);
		ResponseEntity<?> savedContact = contactServiceImpl.createNewContact(request);
		assertThat(savedContact.getBody() != null);
		assertEquals(savedContact.getStatusCode(), HttpStatus.CREATED);
	}
	
	@Test
	public void testServiceUpdateContact() {
		UpdateContactRequest request = new UpdateContactRequest("Quynh Ho","quynhho@gmail.com","HCM","0388844444","91000");
		Optional<Contact> contact = Optional.of(new Contact("Trang Huynh","tranghuynh@gmail.com","HCM","0388844444","91000"));
    	UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        contact.get().setId(uuidAsString);
        Mockito.when(contactRepository.findById(Mockito.anyString())).thenReturn(contact);
		Mockito.when(contactRepository.save(contact.get())).thenReturn(contact.get());
		ResponseEntity<?> savedContact = contactServiceImpl.updateContact(request, uuidAsString);
		assertThat(savedContact.getBody() != null);
		assertEquals(savedContact.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void testServiceDeleteContact() {
		Mockito.when(contactRepository.existsById(Mockito.anyString())).thenReturn(true);
		Mockito.doNothing().when(contactRepository).deleteById(Mockito.anyString());
		ResponseEntity<?> savedContact = contactServiceImpl.deleteContact(Mockito.anyString());
		assertThat(savedContact.getBody() == null);
		assertEquals(savedContact.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void testServiceSearchContact() {
		Contact contact1 = new Contact("Cuong Pham","cuongpham@gmail.com","HCM","0388844444","91000");
		Contact contact2 = new Contact("Viet Cuong","vietcuong@gmail.com","HCM","0388855555","91000");
		Contact contact3 = new Contact("Pham Cuong","pvcuong@gmail.com","HCM","0388866666","91000");
		List<Contact> contacts = new ArrayList<>(Arrays.asList(contact1,contact2,contact3));
		Mockito.when(contactRepository.search(Mockito.anyString())).thenReturn(contacts);
		ResponseEntity<?> savedContact = contactServiceImpl.searchContacts(Mockito.anyString());
		assertThat(savedContact.getBody() != null);
		assertEquals(savedContact.getStatusCode(), HttpStatus.OK);
	}
}

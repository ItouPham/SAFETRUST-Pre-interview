package com.safetrust.preinterview;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetrust.preinterview.controller.ContactController;
import com.safetrust.preinterview.entity.Contact;
import com.safetrust.preinterview.model.request.AddContactRequest;
import com.safetrust.preinterview.model.request.SearchRequest;
import com.safetrust.preinterview.service.ContactService;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ContactService contactService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void testCreateContactReturnBadRequest() throws Exception {
		AddContactRequest contact = new AddContactRequest("", "", "", "", "");
		String requestBody = objectMapper.writeValueAsString(contact);
		this.mockMvc.perform(post("/contact").contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest());
	}
	@Test
	public void testCreateContactInvalidEmail() throws Exception {
		AddContactRequest contact = new AddContactRequest("Thuan Nguyen", "thuannguyen", "HCM", "0388833333", "91000");
		String requestBody = objectMapper.writeValueAsString(contact);
		this.mockMvc.perform(post("/contact").contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateContactInvalidPhoneNumber() throws Exception {
		AddContactRequest contact = new AddContactRequest("Thuan Nguyen", "thuannguyen@gmail.com", "HCM", "123456789", "91000");
		String requestBody = objectMapper.writeValueAsString(contact);
		this.mockMvc.perform(post("/contact").contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateContactReturnCreated() throws Exception {
		AddContactRequest request = new AddContactRequest("Thuan Nguyen", "thuannguyen@gmail.com", "HCM", "0388833333", "91000");
		Mockito.when(contactService.createNewContact(request)).thenReturn(new ResponseEntity(HttpStatus.CREATED));
		String requestBody = objectMapper.writeValueAsString(request);
		this.mockMvc.perform(post("/contact").contentType("application/json")
				.content(requestBody))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	public void testGetAllContacts() throws Exception {
		Contact contact1 = new Contact("Quynh Ho", "quynhho@gmail.com", "HCM", "0388844444", "91000");
		Contact contact2 = new Contact("Nam Tran", "namtran@gmail.com", "HCM", "0388855555", "91000");
		Contact contact3 = new Contact("Phung Nguyen", "phungnguyen@gmail.com", "HCM", "0388866666", "91000");
		List<Contact> contacts = new ArrayList<>(Arrays.asList(contact1, contact2, contact3));
		int page = 1, size = 5;
		Pageable paging = PageRequest.of(page - 1, size);
		final int start = (int) paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), contacts.size());
		final Page<Contact> pageContacts = new PageImpl<>(contacts.subList(start, end), paging, contacts.size());
		Mockito.when(contactService.getAllContacts(page, size)).thenReturn(new ResponseEntity(pageContacts,HttpStatus.OK));
		this.mockMvc.perform(get("/contact?page=1&size=5").contentType("application/json")).andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void testGetContactById() throws Exception {
		UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        String requestURI = "/contact/" +  uuidAsString;
		Contact contact = new Contact("Quynh Ho", "quynhho@gmail.com", "HCM", "0388844444", "91000");
		contact.setId(uuidAsString);
		Mockito.when(contactService.getContactById(Mockito.anyString())).thenReturn(new ResponseEntity(contact,HttpStatus.OK));
		this.mockMvc.perform(get(requestURI)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void testUpdateContact() throws Exception {
		UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        String requestURI = "/contact/" +  uuidAsString;
        AddContactRequest request = new AddContactRequest("Nam Tran", "namtran@gmail.com", "HCM", "0388855555", "91000");
        String requestBody = objectMapper.writeValueAsString(request);
        Contact contact = new Contact("Quynh Ho", "quynhho@gmail.com", "HCM", "0388844444", "91000");
		contact.setId(uuidAsString);
		Mockito.when(contactService.getContactById(Mockito.anyString())).thenReturn(new ResponseEntity(contact,HttpStatus.OK));
		this.mockMvc.perform(put(requestURI)
				.contentType("application/json")
				.content(requestBody))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void testDeleteContact() throws Exception {
		UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
	    String requestURI = "/contact/" +  uuidAsString;
		Mockito.when(contactService.deleteContact(Mockito.anyString())).thenReturn(new ResponseEntity(null,HttpStatus.OK));
		this.mockMvc.perform(delete(requestURI)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void testSearchContactsReturnBadRequest() throws Exception {
		SearchRequest request = new SearchRequest("");
		String requestBody = objectMapper.writeValueAsString(request);
		Mockito.when(contactService.searchContacts(Mockito.anyString())).thenReturn(new ResponseEntity(HttpStatus.OK));
		this.mockMvc.perform(get("/contact/search")
				.contentType("application/json")
				.content(requestBody))
				.andExpect(status().isBadRequest())
				.andDo(print());
	}
	
	@Test
	public void testSearchContacts() throws Exception {
		SearchRequest request = new SearchRequest("Cuong");
		Contact contact1 = new Contact("Cuong Pham","cuongpham@gmail.com","HCM","0388844444","91000");
		Contact contact2 = new Contact("Viet Cuong","vietcuong@gmail.com","HCM","0388855555","91000");
		Contact contact3 = new Contact("Pham Cuong","pvcuong@gmail.com","HCM","0388866666","91000");
		List<Contact> contacts = new ArrayList<>(Arrays.asList(contact1,contact2,contact3));
		String requestBody = objectMapper.writeValueAsString(request);
		Mockito.when(contactService.searchContacts(Mockito.anyString())).thenReturn(new ResponseEntity(contacts,HttpStatus.OK));
		this.mockMvc.perform(get("/contact/search")
				.contentType("application/json")
				.content(requestBody))
				.andExpect(status().isOk())
				.andDo(print());
	}
}

package com.safetrust.preinterview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.safetrust.preinterview.entity.Contact;
import com.safetrust.preinterview.exception.ResourceNotFoundException;
import com.safetrust.preinterview.exception.ServiceException;
import com.safetrust.preinterview.model.request.AddContactRequest;
import com.safetrust.preinterview.repository.ContactRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ContactRepositoryTest {
	@Autowired
	private ContactRepository contactRepository;

	@Test
	public void testCreateContact() {
		Contact contact = new Contact("Ha Nguyen", "hanguyen@gmail.com", "HCM", "038882222", "91000");
		Contact savedContact = contactRepository.save(contact);
		assertThat(savedContact).isNotNull();
		assertThat(savedContact.getId()).isNotNull();
		assertThat(savedContact.getEmail().equals("hungvan@gmail.com"));
	}

	@Test
	public void testGetAllContact() {
		int page = 1;
		int size = 5;
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Contact> contacts = contactRepository.findAll(paging);
		assertTrue(contacts.getContent().size() <= size);
		assertEquals(contacts.getNumber(), page - 1);
	}

	@Test
	public void testFindContactById() {
		String id = "24570cd1-154b-4df3-8466-c0579679192c";
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		assertThat(contact).isNotNull();
		assertThat(contact.getId().equals(id));
		assertThat(contact.getName().equals("Van Hung"));
	}

	@Test
	public void testFindContactByIdNotExists() {
		String id = "24570cd1-154b-4df3-8466-c0579679192c1";
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			Contact contact = contactRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		});
		String expectedMessage = "Contact not found with id: 24570cd1-154b-4df3-8466-c0579679192c1";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	public void testExistsByEmail() {
		String email = "hungvan@gmail.com";
		boolean isExistedEmail = contactRepository.existsByEmail(email);
		assertTrue(isExistedEmail);
	}

	@Test
	public void testExistsByPhoneNumber() {
		String phoneNumber = "0388888888";
		boolean isExistedPhoneNumber = contactRepository.existsByTelephoneNumber(phoneNumber);
		assertTrue(isExistedPhoneNumber);
	}

	@Test
	public void testCreateContactByExistedEmail() {
		String email = "testing@gmail.com";
		Exception exception = assertThrows(ServiceException.class, () -> {
			if (contactRepository.existsByEmail(email)) {
				throw new ServiceException("The email already in use");
			}
		});
		String expectedMessage = "The email already in use";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	public void testCreateContactByExistedPhoneNumber() {
		String phoneNumber = "0388888888";
		Exception exception = assertThrows(ServiceException.class, () -> {
			if (contactRepository.existsByTelephoneNumber(phoneNumber)) {
				throw new ServiceException("The phone number already in use");
			}
		});
		String expectedMessage = "The phone number already in use";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	public void testUpdateContact() {
		String id = "24570cd1-154b-4df3-8466-c0579679192c";
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		AddContactRequest request = new AddContactRequest("Hung Lam", "hunglam@gmail.com", "HCM", "0388888888",
				"91000");
		BeanUtils.copyProperties(request, contact);
		Contact savedContact = contactRepository.save(contact);
		assertThat(savedContact != null);
		assertEquals(savedContact.getId(), id);
		assertEquals(savedContact.getName(), "Hung Lam");
	}

	@Test
	public void testDeleteContact() {
		String id = "24570cd1-154b-4df3-8466-c0579679192c";
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		contactRepository.deleteById(id);
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			Contact contact1 = contactRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
		});
		String expectedMessage = "Contact not found with id: 24570cd1-154b-4df3-8466-c0579679192c";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

}

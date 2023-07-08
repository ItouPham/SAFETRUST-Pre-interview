package com.safetrust.preinterview.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safetrust.preinterview.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {
	Page<Contact> findAll(Pageable pageable);

	boolean existsByEmail(String email);

	boolean existsByTelephoneNumber(String telephoneNumber);
	
	@Query("SELECT c FROM Contact c WHERE c.name LIKE %?1% ")
	public List<Contact> search(String keyword);
}

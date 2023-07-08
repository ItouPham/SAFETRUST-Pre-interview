package com.safetrust.preinterview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(length = 20, nullable = false)
	private String name;
	
	@Column(length = 50, nullable = false, unique = true)
	private String email;
	
	@Column(length = 50, nullable = false)
	private String address;
	
	@Column(length = 12, nullable = false, unique = true)
	private String telephoneNumber;
	
	@Column(length = 10, nullable = false)
	private String postalAddress;

	public Contact(String name, String email, String address, String telephoneNumber, String postalAddress) {
		super();
		this.name = name;
		this.email = email;
		this.address = address;
		this.telephoneNumber = telephoneNumber;
		this.postalAddress = postalAddress;
	}

}

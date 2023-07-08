package com.safetrust.preinterview.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddContactRequest {
	@NotBlank(message = "Field name can not be empty")
	private String name;
	
	@NotBlank(message = "Field email can not be empty")
	@Email(message = "Invalid email")
	private String email;
	
	@NotBlank(message = "Field address can not be empty")
	private String address;
	
	@NotBlank(message = "Field telephoneNumber can not be empty")
	@Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Invalid phone number")
	@Length(max = 12, message = "Invalid phone number")
	private String telephoneNumber;
	
	@NotBlank(message = "Field postalAddress can not be empty")
	private String postalAddress;
}

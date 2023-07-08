package com.safetrust.preinterview.model.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
	@NotBlank(message = "Field keyword can not be empty")
	private String keyword;
}

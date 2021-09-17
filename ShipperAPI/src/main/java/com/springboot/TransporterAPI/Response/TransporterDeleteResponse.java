package com.springboot.TransporterAPI.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransporterDeleteResponse {
	private String status;
	private String message;
}

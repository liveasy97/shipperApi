package com.springboot.ShipperAPI.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipperDeleteResponse {
	private String status;
	private String message;
}

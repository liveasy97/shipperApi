package com.springboot.ShipperAPI.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipperCreateResponse {
	private String status;
	private String message;
	
	private String shipperId;
	private String shipperName;
	private String companyName;
	private Long phoneNo;
	private String kyc;
	private String shipperLocation;
	private boolean companyApproved;
	private boolean accountVerificationInProgress;
}

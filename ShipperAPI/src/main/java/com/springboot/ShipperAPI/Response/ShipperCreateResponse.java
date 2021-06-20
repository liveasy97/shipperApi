package com.springboot.ShipperAPI.Response;

import lombok.Data;

@Data
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

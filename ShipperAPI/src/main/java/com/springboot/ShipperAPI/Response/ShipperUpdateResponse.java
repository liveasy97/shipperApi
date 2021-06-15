package com.springboot.ShipperAPI.Response;

import lombok.Data;

@Data
public class ShipperUpdateResponse {
	private String status;
	private String message;
	
	private String id;
	private String name;
	private String companyName;
	private Long phoneNo;
	private String kyc;
	private String shipperLocation;
	private boolean companyApproved;
	private boolean accountVerificationInProgress;
}

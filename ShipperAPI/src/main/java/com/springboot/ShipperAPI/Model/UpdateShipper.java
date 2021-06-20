package com.springboot.ShipperAPI.Model;

import lombok.Data;

@Data
public class UpdateShipper {
	private Long phoneNo;
	private String shipperName;
	private String companyName;
	private String kyc;
	private String shipperLocation;
	private Boolean companyApproved;
	private Boolean accountVerificationInProgress;
}

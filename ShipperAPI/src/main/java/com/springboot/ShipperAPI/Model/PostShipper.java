package com.springboot.ShipperAPI.Model;

import lombok.Data;

@Data
public class PostShipper {
	private String shipperName;
	private String companyName;
	private String shipperLocation;
	private Long phoneNo;
	private String kyc;
}

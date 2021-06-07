package com.springboot.ShipperAPI.Model;

import lombok.Data;

@Data
public class LoadShipper {
	private String name;
	private String companyName;
	private Long phoneNo;
	private String kyc;
	private boolean approved;
}

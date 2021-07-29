package com.springboot.ShipperAPI.Response;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
public class ShipperCreateResponse {
	private String status;
	private String message;

	private String shipperId;
	private String shipperName;
	private String companyName;
	private String phoneNo;
	private String kyc;
	private String shipperLocation;
	private boolean companyApproved;
	private boolean accountVerificationInProgress;
	public Timestamp timestamp;
}

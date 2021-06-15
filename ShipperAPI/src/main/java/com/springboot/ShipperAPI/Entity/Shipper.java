package com.springboot.ShipperAPI.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "Shipper")
@Entity
public class Shipper {
	@Id
	private String id;
	private String name;
	private String companyName;
	private Long phoneNo;
	private String kyc;
	private String shipperLocation;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean companyApproved;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean accountVerificationInProgress;
}

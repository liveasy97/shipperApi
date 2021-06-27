package com.springboot.ShipperAPI.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Shipper")
public class Shipper {
	@Id
	private String shipperId;
	private String shipperName;
	private String companyName;
	private Long phoneNo;
	private String kyc;
	private String shipperLocation;
	private Boolean companyApproved;
	private Boolean accountVerificationInProgress;
}

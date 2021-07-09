package com.springboot.ShipperAPI.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Table(name = "Shipper")
@Entity
public class Shipper {
	@Id
	private String shipperId;
	private String shipperName;
	private String companyName;
	@Column(unique=true)
	@NotBlank(message = "Phone no. cannot be blank!")
	@Pattern(regexp="(^$|[0-9]{10})", message="must be a 10 digit number")
	private String phoneNo;
	private String kyc;
	private String shipperLocation;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean companyApproved;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean accountVerificationInProgress;
}

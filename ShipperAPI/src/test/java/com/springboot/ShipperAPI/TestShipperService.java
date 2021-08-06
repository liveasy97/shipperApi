package com.springboot.ShipperAPI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.springboot.ShipperAPI.Service.ShipperServiceImpl;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Exception.BusinessException;
import com.springboot.ShipperAPI.Exception.EntityNotFoundException;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

/*

this file contains unit test for service layer

 */

@SpringBootTest
public class TestShipperService {
	
	private static Validator validator;
	
    @BeforeAll
    public static void setUp() {
       ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
       validator = factory.getValidator();
    }
	
	@Autowired
	private ShipperServiceImpl shipperservice;
	
	@MockBean
	private ShipperDao shipperdao;
	
	//add success
	@Test
	@Order(1)
	public void addShippersuccess()
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur","9999999991","link1");
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.PENDING, CommonConstants.APPROVE_REQUEST, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", "9999999991",
				"link1", "Nagpur", false,false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		Shipper shipper = new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findByPhoneNo("9999999991")).thenReturn(null);
		when(shipperdao.save(shipper)).thenReturn(shipper);
		
		Set<ConstraintViolation<PostShipper>> constraintViolations = validator.validate(postshipper);
		assertEquals(0, constraintViolations.size());
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		shippercreateresponseres.setShipperId("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		shippercreateresponseres.setTimestamp(Timestamp.valueOf("2021-07-28 23:28:50.134"));

		assertEquals(shippercreateresponse, shippercreateresponseres);
	}
	
	//add phone no null
	@Test
	@Order(2)
	public void addShipperphonenonull()
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur", null,"link1");
		Set<ConstraintViolation<PostShipper>> constraintViolations = validator.validate( postshipper );
		assertEquals(1, constraintViolations.size());
		assertEquals("Phone no. cannot be blank!", constraintViolations.iterator().next().getMessage());
	}
	
	//blank phone number
	@Test
	@Order(3)
	public void addShipperphonenoblank()
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur", "    ","link1");
		Set<ConstraintViolation<PostShipper>> constraintViolations = validator.validate(postshipper);
		
		Iterator<ConstraintViolation<PostShipper>> itr = constraintViolations.iterator();
		Set<String> set = new HashSet<String>();
		while(itr.hasNext()) set.add(itr.next().getMessage());

		assertEquals(2, constraintViolations.size());
		assertTrue(set.contains("Please enter a valid mobile number"));
		assertTrue(set.contains("Phone no. cannot be blank!"));   
	}
	
	//add invalid phone no
	@Test
	@Order(4)
	public void addShipperinvalidphoneno()
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur", "9999","link1");
		Set<ConstraintViolation<PostShipper>> constraintViolations = validator.validate(postshipper);
		assertEquals(1, constraintViolations.size());
		assertEquals("Please enter a valid mobile number", constraintViolations.iterator().next().getMessage());
	}
	
	//add already present
	@Test
	@Order(5)
	public void addShipperphonenopresent()
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur", "9999999991","link1");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(null, CommonConstants.ACCOUNT_EXIST, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", "9999999991",
				"link1", "Nagpur", false,false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
        Shipper shipper = createShippers().get(0);
		
		when(shipperdao.findShipperByPhoneNo("9999999991")).thenReturn(Optional.of(shipper));
		
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		shippercreateresponseres.setShipperId("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		shippercreateresponseres.setTimestamp(Timestamp.valueOf("2021-07-28 23:28:50.134"));

		Set<ConstraintViolation<PostShipper>> constraintViolations = validator.validate(postshipper);
		assertEquals(0, constraintViolations.size());
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}

	//company approved true
	@Test
	@Order(6)
	public void getShippers_companyapproved_notnull()
	{
		List<Shipper> shippers = createShippers();
		Pageable page = PageRequest.of(0, 15, Sort.Direction.DESC, "timestamp");
		
		when(shipperdao.findByCompanyApproved(true, page)).thenReturn(shippers);
		
		Collections.reverse(shippers);
		List<Shipper> result = shipperservice.getShippers(true, null, 0);
		assertEquals(shippers, result);
	}
	
	//phoneNo not null
	@Test
	@Order(7)
	public void getShippers_phoneNo_not_null()
	{
		Shipper shipper = new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findShipperByPhoneNo("9999999991")).thenReturn(Optional.of(shipper));
		
		List<Shipper> result = shipperservice.getShippers(null, "9999999991", 0);
		assertEquals(List.of(shipper), result);
	}
	
	//page no null
	@Test
	@Order(8)
	public void getShippers_pageno_null()
	{
		List<Shipper> shippers = createShippers();
		Pageable page = PageRequest.of(0, 15, Sort.Direction.DESC, "timestamp");
		
		when(shipperdao.findByCompanyApproved(true, page)).thenReturn(shippers);
		
		Collections.reverse(shippers);
		List<Shipper> result = shipperservice.getShippers(true, null, null);
		assertEquals(shippers, result);
	}
	
	
	@Test
	@Order(9)
	public void getOneShipper_id_present()
	{
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		
		Shipper result = shipperservice.getOneShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		assertEquals(createShippers().get(0), result);
	}
	
	
	@Test
	@Order(10)
	public void getOneShipper_id_not_present()
	{
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.empty());
		
		Throwable exception = assertThrows(
				EntityNotFoundException.class, () -> {
					Shipper result = shipperservice.getOneShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
	            }
	    );
	    assertEquals("Shipper was not found for parameters {id=shipper:0de885e0-5f43-4c68-8dde-0000000000001}", exception.getMessage());
	}
	
	
	//update shipper
	
	@Test
	@Order(11)
	public void updateShipper_success()
	{
		UpdateShipper updateshipper = new UpdateShipper(null, "person11","company11", "link11", "Nagpur", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person11", "company11", "9999999991",
				"link11",	"Nagpur", true, true, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);
		assertEquals(updateresponse, result);
	}
	
	//all null
		@Test
		@Order(12)
		public void updateShipper_all_null()
		{
			UpdateShipper updateshipper = new UpdateShipper(null, null,null, null, null, null, null);
			
			ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
					"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", "9999999991",
					"link1",	"Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
			
			when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
			when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
			
			ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);
			
			assertEquals(updateresponse, result);
		}
	
	
	//phone number cannot be changed
	@Test
	@Order(13)
	public void updateShipper_phoneno_notnull()
	{
		UpdateShipper updateshipper = new UpdateShipper("9999999991", "person11","company11", "link11", "Nagpur", true, true);
		Shipper shipper = new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(shipper));

		Throwable exception = assertThrows(
				BusinessException.class, () -> {
					ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);
	            }
	    );
	    assertEquals("Error: Phone no. can't be updated", exception.getMessage());
	}
	
	
	//shipper name empty
	@Test
	@Order(14)
	public void updateShipper_shippername_empty()
	{
		UpdateShipper updateshipper = new UpdateShipper(null, " ","company11", "link11", "Nagpur", true, true);
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company11", "9999999991",
				"link11",	"Nagpur", true, true, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);
		
		assertEquals(updateresponse, result);
	}
	
	//company name empty
	@Test
	@Order(15)
	public void updateShipper_companyname_empty()
	{
		UpdateShipper updateshipper = new UpdateShipper(null, "person2"," ", "link2", "Nagpur", true, true);
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person2", "company1", "9999999991",
				"link2",	"Nagpur", true, true, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//shipper location empty
	@Test
	@Order(16)
	public void updateShipper_location_empty()
	{
		UpdateShipper updateshipper = new UpdateShipper(null, "person3","company3", "link3", " ", true, true);
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person3", "company3", "9999999991",
				"link3", "Nagpur", true, true, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	
	//account not found 
	@Test
	@Order(17)
	public void updateShipper_fail()
	{
		UpdateShipper updateshipper = new UpdateShipper(null, "person11","company11", "link11", "Nagpur", true, true);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.empty());
		
		Throwable exception = assertThrows(
				EntityNotFoundException.class, () -> {
					ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);
	            }
	    );
	    assertEquals("Shipper was not found for parameters {id=shipper:0de885e0-5f43-4c68-8dde-0000000000001}", exception.getMessage());
	}
	
	
	//delete pass
	@Test
	@Order(18)
	public void deleteShipper_pass()
	{
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		assertDoesNotThrow(()->shipperservice.deleteShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001"));
	}
	
	@Test
	@Order(19)
	public void deleteShipper_fail()
	{
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.empty());
		
		Throwable exception = assertThrows(
				EntityNotFoundException.class, () -> {
					shipperservice.deleteShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
	            }
	    );
	    assertEquals("Shipper was not found for parameters {id=shipper:0de885e0-5f43-4c68-8dde-0000000000001}", exception.getMessage());
	}
	
	public List<Shipper> createShippers()
	{
		List<Shipper> shippers = Arrays.asList(
				new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
						"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000002", "person1",
						"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000003", "person1",
						"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000004", "person1",
						"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"))
		);
		return shippers;
	}
	
}

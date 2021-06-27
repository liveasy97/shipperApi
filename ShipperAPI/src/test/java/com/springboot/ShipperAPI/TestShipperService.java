package com.springboot.ShipperAPI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.springboot.ShipperAPI.Service.ShipperServiceImpl;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;

import org.junit.jupiter.api.Order;
@SpringBootTest
public class TestShipperService {
	
	@Autowired
	private ShipperServiceImpl shipperservice;
	
	@MockBean
	private ShipperDao shipperdao;
	
	//add success
	@Test
	@Order(1)
	public void addShippersuccess()
	{
		PostShipper postshipper = new PostShipper("person1","company1",(Long) 9999999991L,"link1", "Nagpur");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.PENDING, CommonConstants.APPROVE_REQUEST, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", (Long) 9999999991L,"link1",	"Nagpur", false,false);
		Shipper shipper = createShippers().get(0);
		
		when(shipperdao.findByPhoneNo((Long) 9999999991L)).thenReturn(null);
		when(shipperdao.save(shipper)).thenReturn(shipper);
		
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		shippercreateresponseres.setShipperId("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}
	
	//add phone no null
	@Test
	@Order(2)
	public void addShipperphonenonull()
	{
		PostShipper postshipper = new PostShipper("person1","company1", null,"link1", "Nagpur");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.ERROR,
				CommonConstants.PHONE_NUMBER_ERROR, 
				null, null, null, null, null, null, null, null);
		
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}	

	//add invalid phone no
	@Test
	@Order(3)
	public void addShipperinvalidphoneno()
	{
		PostShipper postshipper = new PostShipper("person1","company1", (Long) 999999999L,"link1", "Nagpur");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.ERROR, CommonConstants.INCORRECT_PHONE_NUMBER, 
				null, null, null, null, null, null, null, null);
		
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}	
	
	
	//add already present
	@Test
	@Order(4)
	public void addShipperphonenopresent()
	{
		PostShipper postshipper = new PostShipper("person1","company1",(Long) 9999999991L,"link1", "Nagpur");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.ERROR, CommonConstants.ACCOUNT_EXIST, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", (Long) 9999999991L,"link1",	"Nagpur", false,false);
		Shipper shipper = createShippers().get(0);
		
		when(shipperdao.findByPhoneNo((Long) 9999999991L)).thenReturn((Long) 9999999991L);
		when(shipperdao.findShipperByPhoneNo(9999999991L)).thenReturn(shipper);
		
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}
	
	//shipper name empty
	@Test
	@Order(5)
	public void addShippershippernameempty()
	{
		PostShipper postshipper = new PostShipper("","company1",(Long) 9999999991L,"link1", "Nagpur");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.ERROR, CommonConstants.EMPTY_NAME_ERROR, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findByPhoneNo((Long) 9999999991L)).thenReturn(null);
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}
	
	//company name empty
	@Test
	@Order(6)
	public void addShippercompanynameempty()
	{
		PostShipper postshipper = new PostShipper("person1","",(Long) 9999999991L,"link1", "Nagpur");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.ERROR, CommonConstants.EMPTY_COMPANY_NAME_ERROR, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findByPhoneNo((Long) 9999999991L)).thenReturn(null);
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}
	
	//empty shipper location
	@Test
	@Order(7)
	public void addShipperlocationempty()
	{
		PostShipper postshipper = new PostShipper("person1","company1",(Long) 9999999991L,"link1", "");
		
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.ERROR, CommonConstants.EMPTY_SHIPPER_LOCATION_ERROR, 
				null, null, null, null, null, null, null, null);
	
		when(shipperdao.findByPhoneNo((Long) 9999999991L)).thenReturn(null);
		ShipperCreateResponse shippercreateresponseres = shipperservice.addShipper(postshipper);
		
		assertEquals(shippercreateresponse, shippercreateresponseres);
	}
	
	//company approved null
	@Test
	@Order(8)
	public void getShippers_companyapproved_null()
	{
		List<Shipper> shippers = createShippers();
		Pageable page = PageRequest.of(0, (int) CommonConstants.pagesize);
		
		when(shipperdao.getAll(page)).thenReturn(shippers);
		
		Collections.reverse(shippers);
		List<Shipper> result = shipperservice.getShippers(null, 0);
		assertEquals(shippers, result);
	}
	
	//company approved not null
	@Test
	@Order(9)
	public void getShippers_companyapproved_not_null()
	{
		List<Shipper> shippers = createShippers();
		Pageable page = PageRequest.of(0, (int) CommonConstants.pagesize);
		List<Shipper> list1 = shippers.subList(2, 4);
		
		when(shipperdao.findByCompanyApproved(true, page)).thenReturn(list1);
		
		Collections.reverse(list1);
		List<Shipper> result = shipperservice.getShippers(true, 0);
		assertEquals(list1, result);
	}
	
	//page no null
	@Test
	@Order(10)
	public void getShippers_pageno_null()
	{
		List<Shipper> shippers = createShippers();
		Pageable page = PageRequest.of(0, (int) CommonConstants.pagesize);
		List<Shipper> list1 = shippers.subList(2, 4);
		
		when(shipperdao.findByCompanyApproved(true, page)).thenReturn(list1);
		
		Collections.reverse(list1);
		List<Shipper> result = shipperservice.getShippers(true, null);
		assertEquals(list1, result);
	}
	
	@Test
	@Order(11)
	public void getOneShipper_id_present()
	{
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		
		Shipper result = shipperservice.getOneShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		assertEquals(createShippers().get(0), result);
	}
	
	@Test
	@Order(12)
	public void getOneShipper_id_not_present()
	{
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.empty());
		
		Shipper result = shipperservice.getOneShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		assertEquals(null, result);
	}
	//update shipper
	
	@Test
	@Order(13)
	public void updateShipper_success()
	{
		UpdateShipper updateshipper = new UpdateShipper("person11","company11",null,"link11", "Nagpur", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person11", "company11", (Long) 9999999991L,"link11",	"Nagpur", true, true);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//phone number cannot be changed
	@Test
	@Order(14)
	public void updateShipper_phoneno_notnull()
	{
		UpdateShipper updateshipper = new UpdateShipper("person1","company1",(Long) 9999999991L,"link1", "Nagpur", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.ERROR, CommonConstants.PHONE_NUMBER_UPDATE_ERROR, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//shipper name empty
	@Test
	@Order(15)
	public void updateShipper_shipername_empty()
	{
		UpdateShipper updateshipper = new UpdateShipper("","company1",null,"link1", "Nagpur", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.ERROR, CommonConstants.EMPTY_NAME_ERROR, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//company name empty
	@Test
	@Order(16)
	public void updateShipper_companyname_empty()
	{
		UpdateShipper updateshipper = new UpdateShipper("person1","",null,"link1", "Nagpur", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.ERROR, CommonConstants.EMPTY_COMPANY_NAME_ERROR, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//shipper location empty
	@Test
	@Order(17)
	public void updateShipper_location_empty()
	{
		UpdateShipper updateshipper = new UpdateShipper("person1","company1",null,"link1", "", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.ERROR, CommonConstants.EMPTY_SHIPPER_LOCATION_ERROR, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	@Test
	@Order(18)
	public void updateShipper_allnull()
	{
		UpdateShipper updateshipper = new UpdateShipper(null,null,null,null, null, true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", (Long) 9999999991L,"link1",	"Nagpur", true, true);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//account not found 
	@Test
	@Order(19)
	public void updateShipper_fail()
	{
		UpdateShipper updateshipper = new UpdateShipper("person11","company11",null,"link11", "Nagpur", true, true);
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.NOT_FOUND, CommonConstants.ACCOUNT_NOT_EXIST, 
				null, null, null, null, null, null, null, null);
		
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.empty());
		when(shipperdao.save(createShippers().get(0))).thenReturn(createShippers().get(0));
		
		ShipperUpdateResponse result = shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper);

		assertEquals(updateresponse, result);
	}
	
	//delete pass
	@Test
	@Order(20)
	public void deleteShipper_pass()
	{
		ShipperDeleteResponse deleteResponse = new ShipperDeleteResponse(CommonConstants.SUCCESS, CommonConstants.DELETE_SUCCESS);
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.of(createShippers().get(0)));
		
		ShipperDeleteResponse result = shipperservice.deleteShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		assertEquals(deleteResponse, result);
		
	}
	
	@Test
	@Order(20)
	public void deleteShipper_fail()
	{
		ShipperDeleteResponse deleteResponse = new ShipperDeleteResponse(CommonConstants.NOT_FOUND, CommonConstants.ACCOUNT_NOT_EXIST);
		when(shipperdao.findById("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(Optional.empty());
		
		ShipperDeleteResponse result = shipperservice.deleteShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		assertEquals(deleteResponse, result);
	}
	
	public List<Shipper> createShippers()
	{
		List<Shipper> shippers = Arrays.asList(
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1","company1", (Long) 9999999991L,"link1", "Nagpur", false, false),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000002", "person2","company2", (Long) 9999999992L,"link2", "Nagpur", false, true),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000003", "person3","company3", (Long) 9999999993L,"link3", "Raipur", true, false),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000004", "person4","company4", (Long) 9999999994L,"link4", "Raipur", true, true)
		);
		return shippers;
	}
	
}

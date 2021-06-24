package com.springboot.ShipperAPI;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.Column;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(OrderAnnotation.class)
public class ApiTestRestAssured{

	private static String mapToJson(Object object) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

		return objectMapper.writeValueAsString(object);
	}
	
	private static String shipperid1;
	private static String shipperid2;
	private static String shipperid3;
	
	private static long shippercount_companyapproved_true = 0;
	private static long shippercount_companyapproved_false = 0;
	private static long shippercount_all = 0;
	
	private static long shipperpagecount_companyapproved_true = 0;
	private static long shipperpagecount_companyapproved_false = 0;
	private static long shipperpagecount_all = 0;
	
	@BeforeAll
	public static void setup() throws Exception {
		RestAssured.baseURI = CommonConstants.BASEURI;
		
		//count all
		Response response11;
		while (true)
		{
			response11 = RestAssured.given().param("pageNo", shipperpagecount_all)
					.header("accept", "application/json").header("Content-Type", "application/json").get().then()
					.extract().response();

			shippercount_all += response11.jsonPath().getList("$").size();
			if (response11.jsonPath().getList("$").size() != CommonConstants.pagesize)
				break;

			shipperpagecount_all++;
		}
		
		//company approved true
		Response response22;
		while (true) {
			response22 = RestAssured.given().param("pageNo", shipperpagecount_companyapproved_true).param("companyApproved", true)
					.param("unloadingPoint", "Raipur")
					.header("accept", "application/json").header("Content-Type", "application/json").get().then()
					.extract().response();

			shippercount_companyapproved_true += response22.jsonPath().getList("$").size();
			if (response22.jsonPath().getList("$").size() != CommonConstants.pagesize)
				break;
			shipperpagecount_companyapproved_true++;
		}
		
		//company approved false
		Response response33;
		while (true) {
			response33 = RestAssured.given().param("pageNo", shipperpagecount_companyapproved_false).param("companyApproved", false)
					.header("accept", "application/json").header("Content-Type", "application/json").get().then()
					.extract().response();

			shippercount_companyapproved_false += response33.jsonPath().getList("$").size();
			if (response33.jsonPath().getList("$").size() != CommonConstants.pagesize)
				break;
			shipperpagecount_companyapproved_false++;
		}
		
		//adding three entries
		
		//Entry1
		PostShipper postshipper1 = new PostShipper("person1","company1", (Long) 9999999991L,"link1", "Nagpur");
        String inputJson1 = mapToJson(postshipper1);
        Response response1 = (Response) RestAssured.given().header("", "").body(inputJson1).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        shipperid1 = response1.jsonPath().getString("shipperId");
        assertEquals(200, response1.statusCode());
        assertEquals(CommonConstants.PENDING, response1.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response1.jsonPath().getString("message"));
		assertEquals(postshipper1.getShipperName(), response1.jsonPath().getString("shipperName"));
		assertEquals(postshipper1.getCompanyName(), response1.jsonPath().getString("companyName"));
		assertEquals(postshipper1.getPhoneNo(), response1.jsonPath().getLong("phoneNo"));
		assertEquals(postshipper1.getKyc(), response1.jsonPath().getString("kyc"));
		assertEquals(postshipper1.getShipperLocation(), response1.jsonPath().getString("shipperLocation"));
		assertEquals(false, response1.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response1.jsonPath().getBoolean("accountVerificationInProgress"));
		
		
		//Entry2
		PostShipper postshipper2 = new PostShipper("person2","company2", (Long) 9999999992L,"link2", "Surat");
        String inputJson2 = mapToJson(postshipper2);
        Response response2 = (Response) RestAssured.given().header("", "").body(inputJson2).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        shipperid2 = response2.jsonPath().getString("shipperId");
        assertEquals(200, response2.statusCode());
        assertEquals(CommonConstants.PENDING, response2.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response2.jsonPath().getString("message"));
		assertEquals(postshipper2.getShipperName(), response2.jsonPath().getString("shipperName"));
		assertEquals(postshipper2.getCompanyName(), response2.jsonPath().getString("companyName"));
		assertEquals(postshipper2.getPhoneNo(), response2.jsonPath().getLong("phoneNo"));
		assertEquals(postshipper2.getKyc(), response2.jsonPath().getString("kyc"));
		assertEquals(postshipper2.getShipperLocation(), response2.jsonPath().getString("shipperLocation"));
		assertEquals(false, response2.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response2.jsonPath().getBoolean("accountVerificationInProgress"));
		
		//Entry3
		PostShipper postshipper3 = new PostShipper("person3","company3", (Long) 9999999993L,"link3", "Surat");
        String inputJson3 = mapToJson(postshipper3);
        Response response3 = (Response) RestAssured.given().header("", "").body(inputJson3).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        shipperid3 = response3.jsonPath().getString("shipperId");
        assertEquals(200, response3.statusCode());
        assertEquals(CommonConstants.PENDING, response3.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response3.jsonPath().getString("message"));
		assertEquals(postshipper3.getShipperName(), response3.jsonPath().getString("shipperName"));
		assertEquals(postshipper3.getCompanyName(), response3.jsonPath().getString("companyName"));
		assertEquals(postshipper3.getPhoneNo(), response3.jsonPath().getLong("phoneNo"));
		assertEquals(postshipper3.getKyc(), response3.jsonPath().getString("kyc"));
		assertEquals(postshipper3.getShipperLocation(), response3.jsonPath().getString("shipperLocation"));
		assertEquals(false, response3.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response3.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//add shipper success
	@Test
	@Order(1)
	public void addshippersuccess() throws Exception
	{
		PostShipper postshipper = new PostShipper("person4","company4", (Long) 9999999994L,"link4", "Surat");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("status"));
        System.err.println("**********************");
        String shipperid = response.jsonPath().getString("shipperId");
        assertEquals(200, response.statusCode());
        assertEquals(CommonConstants.PENDING, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response.jsonPath().getString("message"));
		assertEquals(postshipper.getShipperName(), response.jsonPath().getString("shipperName"));
		assertEquals(postshipper.getCompanyName(), response.jsonPath().getString("companyName"));
		assertEquals(postshipper.getPhoneNo(), response.jsonPath().getLong("phoneNo"));
		assertEquals(postshipper.getKyc(), response.jsonPath().getString("kyc"));
		assertEquals(postshipper.getShipperLocation(), response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		assertEquals(200, response2.statusCode());
		assertEquals(CommonConstants.SUCCESS, response2.jsonPath().getString("status"));
		assertEquals(CommonConstants.DELETE_SUCCESS, response2.jsonPath().getString("message"));
	}
	
	//add shipper fail, phone number null
	@Test
	@Order(2)
	public void addshipperfail1() throws Exception
	{
		PostShipper postshipper = new PostShipper("person4","company4", null,"link4", "Surat");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("phoneNo"));
        System.err.println("**********************");
        assertEquals(200, response.statusCode());
        assertEquals(CommonConstants.ERROR, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.PHONE_NUMBER_ERROR, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(null, response.jsonPath().getString("phoneNo"));
		assertEquals(null, response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//add shipper fail phone number invalid (9 digit)
	@Test
	@Order(3)
	public void addshipperfail2() throws Exception
	{
		PostShipper postshipper = new PostShipper("person4","company4", (Long) 999999994L,"link4", "Surat");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("phoneNo"));
        System.err.println("**********************");
        assertEquals(200, response.statusCode());
        assertEquals(CommonConstants.ERROR, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.INCORRECT_PHONE_NUMBER, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(null, response.jsonPath().getString("phoneNo"));
		assertEquals(null, response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//add shipper fail account already exist
	@Test
	@Order(4)
	public void addshipperfail3() throws Exception
	{
		PostShipper postshipper = new PostShipper("person4","company4", (Long) 9999999991L,"link4", "Surat");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("phoneNo"));
        System.err.println("**********************");
        assertEquals(200, response.statusCode());
        assertEquals(shipperid1, response.jsonPath().getString("shipperId"));
        assertEquals(CommonConstants.ERROR, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.ACCOUNT_EXIST, response.jsonPath().getString("message"));
		assertEquals("person1", response.jsonPath().getString("shipperName"));
		assertEquals("company1", response.jsonPath().getString("companyName"));
		assertEquals((Long) 9999999991L, response.jsonPath().getLong("phoneNo"));
		assertEquals("link1", response.jsonPath().getString("kyc"));
		assertEquals("Nagpur", response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//empty shipper name
	@Test
	@Order(5)
	public void addshipperfail4() throws Exception
	{
		PostShipper postshipper = new PostShipper("","company4", (Long) 9999999994L,"link4", "Surat");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("phoneNo"));
        System.err.println("**********************");
        assertEquals(200, response.statusCode());
        assertEquals(CommonConstants.ERROR, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.EMPTY_NAME_ERROR, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(null, response.jsonPath().getString("phoneNo"));
		assertEquals(null, response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//empty company name 
	@Test
	@Order(6)
	public void addshipperfail5() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","", (Long) 9999999994L,"link4", "Surat");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("phoneNo"));
        System.err.println("**********************");
        assertEquals(200, response.statusCode());
        assertEquals(CommonConstants.ERROR, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.EMPTY_COMPANY_NAME_ERROR, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(null, response.jsonPath().getString("phoneNo"));
		assertEquals(null, response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//empty shipper location
	@Test
	@Order(7)
	public void addshipperfail6() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","company4", (Long) 9999999994L,"link4", "");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        System.err.println("**********************");
        System.err.println("a: " + response.jsonPath().getString("phoneNo"));
        System.err.println("**********************");
        assertEquals(200, response.statusCode());
        assertEquals(CommonConstants.ERROR, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.EMPTY_SHIPPER_LOCATION_ERROR, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(null, response.jsonPath().getString("phoneNo"));
		assertEquals(null, response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//update success
	@Test
	@Order(8)
	public void updateshippersuccess() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("person11","company11",null,"link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);

		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();
		assertEquals(200, responseupdate.statusCode());
		assertEquals(shipperid1, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.SUCCESS, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.UPDATE_SUCCESS, responseupdate.jsonPath().getString("message"));
		assertEquals("person11", responseupdate.jsonPath().getString("shipperName"));
		assertEquals("company11", responseupdate.jsonPath().getString("companyName"));
		assertEquals((Long) 9999999991L, responseupdate.jsonPath().getLong("phoneNo"));
		assertEquals("link11", responseupdate.jsonPath().getString("kyc"));
		assertEquals("Nagpur", responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(true, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//update fail account not found 
	@Test
	@Order(9)
	public void updateshipperfail1() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("person11","company11",null,"link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid).then().extract().response();

		assertEquals(200, responseupdate.statusCode());
		assertEquals(null, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.NOT_FOUND, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.ACCOUNT_NOT_EXIST, responseupdate.jsonPath().getString("message"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperName"));
		assertEquals(null, responseupdate.jsonPath().getString("companyName"));
		assertEquals(null, responseupdate.jsonPath().getString("phoneNo"));
		assertEquals(null, responseupdate.jsonPath().getString("kyc"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}	
	
	//phone number cannot be updated
	@Test
	@Order(10)
	public void updateshipperfail2() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("person11","company11",(Long) 9999999994L,"link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();

		assertEquals(200, responseupdate.statusCode());
		assertEquals(null, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.ERROR, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.PHONE_NUMBER_UPDATE_ERROR, responseupdate.jsonPath().getString("message"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperName"));
		assertEquals(null, responseupdate.jsonPath().getString("companyName"));
		assertEquals(null, responseupdate.jsonPath().getString("phoneNo"));
		assertEquals(null, responseupdate.jsonPath().getString("kyc"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}

	//shipper name cannot be empty
	@Test
	@Order(11)
	public void updateshipperfail3() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("","company11",null,"link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();

		assertEquals(200, responseupdate.statusCode());
		assertEquals(null, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.ERROR, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.EMPTY_NAME_ERROR, responseupdate.jsonPath().getString("message"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperName"));
		assertEquals(null, responseupdate.jsonPath().getString("companyName"));
		assertEquals(null, responseupdate.jsonPath().getString("phoneNo"));
		assertEquals(null, responseupdate.jsonPath().getString("kyc"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//company name cannot be empty
	@Test
	@Order(12)
	public void updateshipperfail4() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("person11","",null,"link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();

		assertEquals(200, responseupdate.statusCode());
		assertEquals(null, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.ERROR, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.EMPTY_COMPANY_NAME_ERROR, responseupdate.jsonPath().getString("message"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperName"));
		assertEquals(null, responseupdate.jsonPath().getString("companyName"));
		assertEquals(null, responseupdate.jsonPath().getString("phoneNo"));
		assertEquals(null, responseupdate.jsonPath().getString("kyc"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//location name cannot be empty
	@Test
	@Order(13)
	public void updateshipperfail5() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("person11","company11",null,"link11", "", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();

		assertEquals(200, responseupdate.statusCode());
		assertEquals(null, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.ERROR, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.EMPTY_SHIPPER_LOCATION_ERROR, responseupdate.jsonPath().getString("message"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperName"));
		assertEquals(null, responseupdate.jsonPath().getString("companyName"));
		assertEquals(null, responseupdate.jsonPath().getString("phoneNo"));
		assertEquals(null, responseupdate.jsonPath().getString("kyc"));
		assertEquals(null, responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//get all
	@Test
	@Order(14)
	public void getshipperall() throws Exception
	{
		long lastPageCount = shippercount_all % CommonConstants.pagesize;
		long page = shipperpagecount_all;

		if (lastPageCount >= CommonConstants.pagesize - 2)
			page++;
		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(lastPageCount <= CommonConstants.pagesize-3)
		{
			System.err.println("1");
			assertEquals(lastPageCount+3, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == CommonConstants.pagesize-2)
		{
			System.err.println("2 " + response.jsonPath().getList("$").size());
			assertEquals(1, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == CommonConstants.pagesize-1)
		{
			System.err.println("3");
			assertEquals(2, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == CommonConstants.pagesize)
		{
			System.err.println("4");
			assertEquals(3, response.jsonPath().getList("$").size());
		}
	}
	
	//get company approved true
	
	@Test
	@Order(15)
	public void getshipper_companyapproved_true() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_true % CommonConstants.pagesize;
		long page = shipperpagecount_companyapproved_true;

		if (lastPageCount >= CommonConstants.pagesize)
			page++;
		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.param("companyApproved", true)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(lastPageCount <= CommonConstants.pagesize-1)
		{
			System.err.println("aa: " + response.jsonPath().getList("$").size());
			assertEquals(lastPageCount+1, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == CommonConstants.pagesize)
		{
			System.err.println("4");
			assertEquals(1, response.jsonPath().getList("$").size());
		}
	}
	
	//company approved false
	
	@Test
	@Order(16)
	public void getshipper_companyapproved_false() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_false % CommonConstants.pagesize;
		long page = shipperpagecount_companyapproved_false;

		if (lastPageCount >= CommonConstants.pagesize)
			page++;
		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.param("companyApproved", false)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		System.err.println("2a " + response.jsonPath().getList("companyApproved"));
		
		if(lastPageCount <= CommonConstants.pagesize-2)
		{
			System.err.println("2 " + response.jsonPath().getList("$").size());
			assertEquals(lastPageCount+2, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == CommonConstants.pagesize-1)
		{
			System.err.println("3");
			assertEquals(1, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == CommonConstants.pagesize)
		{
			System.err.println("4");
			assertEquals(2, response.jsonPath().getList("$").size());
		}
	}
	//pageNo null
	@Test
	@Order(17)
	public void getshipper_companyapproved_false_pageno_null() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_false % CommonConstants.pagesize;
		
		Response response = RestAssured.given()
				.param("companyApproved", false)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		System.err.println("2a " + response.jsonPath().getList("companyApproved"));
		
		if(shipperpagecount_companyapproved_false>=1 || lastPageCount>=CommonConstants.pagesize-2)
		{
			assertEquals(CommonConstants.pagesize, response.jsonPath().getList("$").size());
		}
		else
		{
			assertEquals(lastPageCount+2, response.jsonPath().getList("$").size());
		}
	}
	
	//page null
	@Test
	@Order(18)
	public void getshipper_companyapproved_true_pageno_null() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_true % CommonConstants.pagesize;
		
		Response response = RestAssured.given()
				.param("companyApproved", true)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		System.err.println("2a " + response.jsonPath().getList("companyApproved"));
		
		if(shipperpagecount_companyapproved_true>=1 || lastPageCount>=CommonConstants.pagesize-2)
		{
			assertEquals(CommonConstants.pagesize, response.jsonPath().getList("$").size());
		}
		else
		{
			assertEquals(lastPageCount+1, response.jsonPath().getList("$").size());
		}
	}
	
	//page no null
	
	@Test
	@Order(19)
	public void getshipperall_pagenonull() throws Exception
	{
		long lastPageCount = shippercount_all % CommonConstants.pagesize;
		
		Response response = RestAssured.given()
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(shipperpagecount_all>=1 || lastPageCount>=CommonConstants.pagesize-2)
		{
			assertEquals(CommonConstants.pagesize, response.jsonPath().getList("$").size());
		}
		else
		{
			assertEquals(lastPageCount+3, response.jsonPath().getList("$").size());
		}
	}
	
	//getby shipperid success
	@Test
	@Order(20)
	public void getshipperbyshipperid_success() throws Exception
	{
		Response response = RestAssured.given().header("", "").get("/" + shipperid1).then().extract().response();
		assertEquals(200, response.statusCode());
		assertEquals(shipperid1, response.jsonPath().getString("shipperId"));
		assertEquals("person11", response.jsonPath().getString("shipperName"));
		assertEquals("company11", response.jsonPath().getString("companyName"));
		assertEquals((Long) 9999999991L, response.jsonPath().getLong("phoneNo"));
		assertEquals("link11", response.jsonPath().getString("kyc"));
		assertEquals("Nagpur", response.jsonPath().getString("shipperLocation"));
		assertEquals(true, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		System.err.println("********************** get by shipper id ");
        System.err.println("a: " + response.asString());
        System.err.println("**********************");
	}
	
	//getby shipperid fail
	@Test
	@Order(20)
	public void getshipperbyshipperid_fail() throws Exception
	{
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response response = RestAssured.given().header("", "").get("/" + shipperid).then().extract().response();
		
		System.err.println("**********************");
        System.err.println("a: " + response.asString());
        System.err.println("**********************");
		
		assertEquals(200, response.statusCode());
		assertEquals("", response.asString());

	}
	
	//delete fail account not found
	@Test
	@Order(22)
	public void deletedatafail()
	{
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response response = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		assertEquals(200, response.statusCode());
		assertEquals(CommonConstants.NOT_FOUND, response.jsonPath().getString("status"));
		assertEquals(CommonConstants.ACCOUNT_NOT_EXIST, response.jsonPath().getString("message"));
	}
	
	@AfterAll
	public static void deletedata()
	{
		Response response1 = RestAssured.given().header("", "").delete("/" + shipperid1).then().extract().response();
		assertEquals(200, response1.statusCode());
		assertEquals(CommonConstants.SUCCESS, response1.jsonPath().getString("status"));
		assertEquals(CommonConstants.DELETE_SUCCESS, response1.jsonPath().getString("message"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid2).then().extract().response();
		assertEquals(200, response2.statusCode());
		assertEquals(CommonConstants.SUCCESS, response2.jsonPath().getString("status"));
		assertEquals(CommonConstants.DELETE_SUCCESS, response2.jsonPath().getString("message"));
		
		Response response3 = RestAssured.given().header("", "").delete("/" + shipperid3).then().extract().response();
		assertEquals(200, response3.statusCode());
		assertEquals(CommonConstants.SUCCESS, response3.jsonPath().getString("status"));
		assertEquals(CommonConstants.DELETE_SUCCESS, response3.jsonPath().getString("message"));
		
	}
}

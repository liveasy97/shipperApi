/*
package com.springboot.ShipperAPI;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;




//this file contains integrated testing


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
	
	private static int pagesize=15, total_shipper_added=3;
	
	
	//this function will add three objects in database
	@BeforeAll
	public static void setup() throws Exception {
		
		RestAssured.baseURI = "http://localhost:8080/shipper";
		
		Response response11;
		while (true)
		{
			response11 = RestAssured.given().param("pageNo", shipperpagecount_all)
					.header("accept", "application/json").header("Content-Type", "application/json").get().then()
					.extract().response();

			shippercount_all += response11.jsonPath().getList("$").size();
			if (response11.jsonPath().getList("$").size() != pagesize)
				break;

			shipperpagecount_all++;
		}
		
		Response response22;
		while (true) {
			response22 = RestAssured.given().param("pageNo", shipperpagecount_companyapproved_true).param("companyApproved", true)
					.param("unloadingPoint", "Raipur")
					.header("accept", "application/json").header("Content-Type", "application/json").get().then()
					.extract().response();

			shippercount_companyapproved_true += response22.jsonPath().getList("$").size();
			if (response22.jsonPath().getList("$").size() != pagesize)
				break;
			shipperpagecount_companyapproved_true++;
		}
		
		Response response33;
		while (true) {
			response33 = RestAssured.given().param("pageNo", shipperpagecount_companyapproved_false).param("companyApproved", false)
					.header("accept", "application/json").header("Content-Type", "application/json").get().then()
					.extract().response();

			shippercount_companyapproved_false += response33.jsonPath().getList("$").size();
			if (response33.jsonPath().getList("$").size() != pagesize)
				break;
			shipperpagecount_companyapproved_false++;
		}
		
		PostShipper postshipper1 = new PostShipper("person1","company1", "Nagpur","9999999991","link1");
        String inputJson1 = mapToJson(postshipper1);
        Response response1 = (Response) RestAssured.given().header("", "").body(inputJson1).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        shipperid1 = response1.jsonPath().getString("shipperId");
        assertEquals(HttpStatus.CREATED.value(), response1.statusCode());
        assertEquals(CommonConstants.PENDING, response1.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response1.jsonPath().getString("message"));
		assertEquals(postshipper1.getShipperName(), response1.jsonPath().getString("shipperName"));
		assertEquals(postshipper1.getCompanyName(), response1.jsonPath().getString("companyName"));
		assertEquals(postshipper1.getPhoneNo(), response1.jsonPath().getString("phoneNo"));
		assertEquals(postshipper1.getKyc(), response1.jsonPath().getString("kyc"));
		assertEquals(postshipper1.getShipperLocation(), response1.jsonPath().getString("shipperLocation"));
		assertEquals(false, response1.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response1.jsonPath().getBoolean("accountVerificationInProgress"));
		
		
		PostShipper postshipper2 = new PostShipper("person2","company2", "Nagpur","9999999992","link2");
        String inputJson2 = mapToJson(postshipper2);
        Response response2 = (Response) RestAssured.given().header("", "").body(inputJson2).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        shipperid2 = response2.jsonPath().getString("shipperId");
        assertEquals(HttpStatus.CREATED.value(), response2.statusCode());
        assertEquals(CommonConstants.PENDING, response2.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response2.jsonPath().getString("message"));
		assertEquals(postshipper2.getShipperName(), response2.jsonPath().getString("shipperName"));
		assertEquals(postshipper2.getCompanyName(), response2.jsonPath().getString("companyName"));
		assertEquals(postshipper2.getPhoneNo(), response2.jsonPath().getString("phoneNo"));
		assertEquals(postshipper2.getKyc(), response2.jsonPath().getString("kyc"));
		assertEquals(postshipper2.getShipperLocation(), response2.jsonPath().getString("shipperLocation"));
		assertEquals(false, response2.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response2.jsonPath().getBoolean("accountVerificationInProgress"));
		
		PostShipper postshipper3 = new PostShipper("person3","company3", "Nagpur","9999999993","link3");
        String inputJson3 = mapToJson(postshipper3);
        Response response3 = (Response) RestAssured.given().header("", "").body(inputJson3).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        shipperid3 = response3.jsonPath().getString("shipperId");
        assertEquals(HttpStatus.CREATED.value(), response3.statusCode());
        assertEquals(CommonConstants.PENDING, response3.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response3.jsonPath().getString("message"));
		assertEquals(postshipper3.getShipperName(), response3.jsonPath().getString("shipperName"));
		assertEquals(postshipper3.getCompanyName(), response3.jsonPath().getString("companyName"));
		assertEquals(postshipper3.getPhoneNo(), response3.jsonPath().getString("phoneNo"));
		assertEquals(postshipper3.getKyc(), response3.jsonPath().getString("kyc"));
		assertEquals(postshipper3.getShipperLocation(), response3.jsonPath().getString("shipperLocation"));
		assertEquals(false, response3.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response3.jsonPath().getBoolean("accountVerificationInProgress"));
	
	}
	

	//case: adding shipper successfully with all paramaters are not null
	//adding compleate data in database
	//getting proper response
	@Test
	@Order(1)
	public void addshippersuccess1() throws Exception
	{
		RestAssured.baseURI = "http://localhost:8080/shipper";
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur","9989999991","link1");
        String inputJson = mapToJson(postshipper);

        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        String shipperid = response.jsonPath().getString("shipperId");
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals(CommonConstants.PENDING, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response.jsonPath().getString("message"));
		assertEquals(postshipper.getShipperName(), response.jsonPath().getString("shipperName"));
		assertEquals(postshipper.getCompanyName(), response.jsonPath().getString("companyName"));
		assertEquals(postshipper.getPhoneNo(), response.jsonPath().getString("phoneNo"));
		assertEquals(postshipper.getKyc(), response.jsonPath().getString("kyc"));
		assertEquals(postshipper.getShipperLocation(), response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();

		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals("Successfully deleted", response2.asString());
	}
	
	//case: adding shipper successfully with all paramaters are null except phone number
	//getting response as all paramaters null except phoneNo
	@Test
	@Order(2)
	public void addshippersuccess2() throws Exception
	{
		PostShipper postshipper = new PostShipper(null,null, null, "9999999994", null);
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();

        String shipperid = response.jsonPath().getString("shipperId");
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals(CommonConstants.PENDING, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(postshipper.getPhoneNo(), response.jsonPath().getString("phoneNo"));
		assertEquals(null, response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals("Successfully deleted", response2.asString());
	}
	
	
	//case: add shipper with phone number null and other paramaters are not null
	//getting exception from annotation validation (Phone no. cannot be blank!)
	@Test
	@Order(3)
	public void addshipperfail1() throws Exception
	{
		PostShipper postshipper = new PostShipper("person4","company4", "link4", null, "Surat");
        String inputJson = mapToJson(postshipper);
        
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        JsonPath jsonPathEvaluator = response.jsonPath();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("Validation error", jsonPathEvaluator.get("shippererrorresponse.message").toString());
        assertEquals("[{field=phoneNo, message=Phone no. cannot be blank!, rejectedValue=null, object=postShipper}]",
        		jsonPathEvaluator.get("shippererrorresponse.subErrors").toString());
	}
	
	//case: add shipper with invalid phone number (9 digit) and all paramaters are not null
	//getting response as exception from annotation validation (Please enter a valid mobile number)
	@Test
	@Order(4)
	public void addshippersuccess4() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur","998999999","link1");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        JsonPath jsonPathEvaluator = response.jsonPath();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("Validation error", jsonPathEvaluator.get("shippererrorresponse.message").toString());
        assertEquals("[{field=phoneNo, message=Please enter a valid mobile number, rejectedValue=998999999, object=postShipper}]",
        		jsonPathEvaluator.get("shippererrorresponse.subErrors").toString());
	}
	
	//case: add shipper with already exist phonenumber and all other values are not null
	//getting result as messgae as Account already exist and already existed result
	@Test
	@Order(5)
	public void addshipperfail2() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur","9999999992","link1");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals("Account already exist", jsonPathEvaluator.get("message").toString());
	}
	
	//case: adding shipper with empty shipper name and all other paramaters with valid value
	//response with shipper name = null
	@Test
	@Order(6)
	public void addshippersuccess6() throws Exception
	{
		PostShipper postshipper = new PostShipper("","company1", "Nagpur","9999999900","link1");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        JsonPath jsonPathEvaluator = response.jsonPath();
        
        String shipperid = response.jsonPath().getString("shipperId");
        
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals(CommonConstants.PENDING, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response.jsonPath().getString("message"));
		assertEquals(null, response.jsonPath().getString("shipperName"));
		assertEquals(postshipper.getCompanyName(), response.jsonPath().getString("companyName"));
		assertEquals(postshipper.getPhoneNo(), response.jsonPath().getString("phoneNo"));
		assertEquals(postshipper.getKyc(), response.jsonPath().getString("kyc"));
		assertEquals(postshipper.getShipperLocation(), response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals("Successfully deleted", response2.asString());
	}
	
	//case: adding shipper with empty company name and all other paramaters with valid value
	//get shipper with company name null 
	@Test
	@Order(7)
	public void addshippersuccess7() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","", "Nagpur","9999999900","link1");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        String shipperid = response.jsonPath().getString("shipperId");
        
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals(CommonConstants.PENDING, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response.jsonPath().getString("message"));
		assertEquals(postshipper.getShipperName(), response.jsonPath().getString("shipperName"));
		assertEquals(null, response.jsonPath().getString("companyName"));
		assertEquals(postshipper.getPhoneNo(), response.jsonPath().getString("phoneNo"));
		assertEquals(postshipper.getKyc(), response.jsonPath().getString("kyc"));
		assertEquals(postshipper.getShipperLocation(), response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals("Successfully deleted", response2.asString());
	}
	
	//case: adding shipper with empty location name and all other paramaters with valid value
	// getting shipper with location null
	@Test
	@Order(8)
	public void addshippersuccess8() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","company1", "","9999999900","link1");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();
        
        String shipperid = response.jsonPath().getString("shipperId");
        
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals(CommonConstants.PENDING, response.jsonPath().getString("status"));
        assertEquals(CommonConstants.APPROVE_REQUEST, response.jsonPath().getString("message"));
		assertEquals(postshipper.getShipperName(), response.jsonPath().getString("shipperName"));
		assertEquals(postshipper.getCompanyName(), response.jsonPath().getString("companyName"));
		assertEquals(postshipper.getPhoneNo(), response.jsonPath().getString("phoneNo"));
		assertEquals(postshipper.getKyc(), response.jsonPath().getString("kyc"));
		assertEquals(null, response.jsonPath().getString("shipperLocation"));
		assertEquals(false, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(false, response.jsonPath().getBoolean("accountVerificationInProgress"));
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals("Successfully deleted", response2.asString());
	}
	
	
	//case: updating shipper with all paramaters in update shipper request are not null except phone number
	//updating shipper with proper request
	//getting proper response
	@Test
	@Order(9)
	public void updateshippersuccess1() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, "person11","company11", "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);

		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();
		assertEquals(HttpStatus.OK.value(), responseupdate.statusCode());
		assertEquals(shipperid1, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.SUCCESS, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.UPDATE_SUCCESS, responseupdate.jsonPath().getString("message"));
		assertEquals("person11", responseupdate.jsonPath().getString("shipperName"));
		assertEquals("company11", responseupdate.jsonPath().getString("companyName"));
		assertEquals("9999999991", responseupdate.jsonPath().getString("phoneNo"));
		assertEquals("link11", responseupdate.jsonPath().getString("kyc"));
		assertEquals("Nagpur", responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(true, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	
	//case: updating shipper with all paramaters in update shipper request are null
	//updating shipper with all value null
	//getting shipper with prexisted values
	@Test
	@Order(10)
	public void updateshippersuccess2() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null,null,null,null, null, null, null);

		String inputJsonupdate = mapToJson(updateshipper);

		Response responseupdate = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();
		assertEquals(HttpStatus.OK.value(), responseupdate.statusCode());
		assertEquals(shipperid1, responseupdate.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.SUCCESS, responseupdate.jsonPath().getString("status"));
		assertEquals(CommonConstants.UPDATE_SUCCESS, responseupdate.jsonPath().getString("message"));
		assertEquals("person11", responseupdate.jsonPath().getString("shipperName"));
		assertEquals("company11", responseupdate.jsonPath().getString("companyName"));
		assertEquals("9999999991", responseupdate.jsonPath().getString("phoneNo"));
		assertEquals("link11", responseupdate.jsonPath().getString("kyc"));
		assertEquals("Nagpur", responseupdate.jsonPath().getString("shipperLocation"));
		assertEquals(true, responseupdate.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, responseupdate.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//case: updating shipper with invalid(unavailable) pathvariable(shipperid)
	//update fail account not found
	//sending request with shipperid which is nor present in database
	// getting response as exception (Shipper was not found for parameters {shipperid})
	@Test
	@Order(11)
	public void updateshipperfail1() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, "person11","company11", "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
        assertEquals("Shipper was not found for parameters {id=shipperrr:0de885e0-5f43-4c68-8dde-0000000000001}", jsonPathEvaluator.get("shippererrorresponse.message").toString());
	}
	
	//case: updating shipper with valid(available) pathvariable(shipperid)
	//were all the paramaters are not null including phone number
	//sending update request with shipper no not null
	//getting response as exception  {Error: Phone no. can't be updated}
	@Test
	@Order(12)
	public void updateshipperfail2() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("9999999991", "person11","company11", "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.statusCode());
        assertEquals("Error: Phone no. can't be updated", jsonPathEvaluator.get("shippererrorresponse.message").toString());

	}
	
	//case: updating shipper with valid(available) pathvariable(shipperid)
	//were all paramaters are not null except shipper name 
	//sending shipper request with shippername as empty string
	// getting update response as shipper with shippername as previously stored name
	@Test
	@Order(13)
	public void updateshippersuccess3() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, "", "company11", "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();
	    
		assertEquals(HttpStatus.OK.value(), response.statusCode());
		assertEquals(shipperid1, response.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.SUCCESS, response.jsonPath().getString("status"));
		assertEquals(CommonConstants.UPDATE_SUCCESS, response.jsonPath().getString("message"));
		assertEquals("person11", response.jsonPath().getString("shipperName"));
		assertEquals("company11", response.jsonPath().getString("companyName"));
		assertEquals("9999999991", response.jsonPath().getString("phoneNo"));
		assertEquals("link11", response.jsonPath().getString("kyc"));
		assertEquals("Nagpur", response.jsonPath().getString("shipperLocation"));
		assertEquals(true, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, response.jsonPath().getBoolean("accountVerificationInProgress"));

	}
	
	// case: updating shipper with valid(available) pathvariable(shipperid)
	// were all paramaters are null except phone number and companyname
	// sending shipper request with companyname as empty string
	// getting update response as shipper with companyname as previously stored name
	@Test
	@Order(14)
	public void updateshipperfail4() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, "person22", null, "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();

		assertEquals(HttpStatus.OK.value(), response.statusCode());
		assertEquals(shipperid1, response.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.SUCCESS, response.jsonPath().getString("status"));
		assertEquals(CommonConstants.UPDATE_SUCCESS, response.jsonPath().getString("message"));
		assertEquals("person22", response.jsonPath().getString("shipperName"));
		assertEquals("company11", response.jsonPath().getString("companyName"));
		assertEquals("9999999991", response.jsonPath().getString("phoneNo"));
		assertEquals("link11", response.jsonPath().getString("kyc"));
		assertEquals("Nagpur", response.jsonPath().getString("shipperLocation"));
		assertEquals(true, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//case: updating shipper with valid(available) pathvariable(shipperid)
	//were all paramaters are not null except phone number and shipper location 
	//sending shipper request with location as empty string
	//getting update response as shipper with location as previously stored name
	@Test
	@Order(15)
	public void updateshipperfail5() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, "person33","company11", "link11", null, true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();

		assertEquals(HttpStatus.OK.value(), response.statusCode());
		assertEquals(shipperid1, response.jsonPath().getString("shipperId"));
		assertEquals(CommonConstants.SUCCESS, response.jsonPath().getString("status"));
		assertEquals(CommonConstants.UPDATE_SUCCESS, response.jsonPath().getString("message"));
		assertEquals("person33", response.jsonPath().getString("shipperName"));
		assertEquals("company11", response.jsonPath().getString("companyName"));
		assertEquals("9999999991", response.jsonPath().getString("phoneNo"));
		assertEquals("link11", response.jsonPath().getString("kyc"));
		assertEquals("Nagpur", response.jsonPath().getString("shipperLocation"));
		assertEquals(true, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//get all
	//checking that after adding three new shippers in setupfunction git added or not 
	//checking whether last page contains expected number of shippers according to previously added data
	//sending page as last page
	//according to total number of already existed data and the datawe added in setup function of this testing (beforeall)
	//we will check whether the last page contains expected number of data or not
	@Test
	@Order(16)
	public void getshipperall() throws Exception
	{
		long lastPageCount = (shippercount_all + total_shipper_added)%pagesize;
		long page = (shippercount_all + total_shipper_added)/pagesize;

		Response response = RestAssured.given()
				.param("pageNo", page)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		assertEquals(lastPageCount, response.jsonPath().getList("$").size());
	}
	
	//get company approved true
	//checking whether the number of shipeprs with companyapproved true are equal to expected numbers of user
	//sending page as last page
	//according to total number of already existed data and the data we added in setup function of this testing (beforeall)
	//we will check whether the last page contains expected number of data or not
	@Test
	@Order(17)
	public void getshipper_companyapproved_true() throws Exception
	{
		long added_shipper_with_company_approved_true = 1;
		long lastPageCount = (shippercount_companyapproved_true + added_shipper_with_company_approved_true)% pagesize;
		long page = (shippercount_companyapproved_true+added_shipper_with_company_approved_true)/pagesize;

		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.param("companyApproved", true)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();

		assertEquals(lastPageCount, response.jsonPath().getList("$").size());
	}
	
	//company approved false
	//checking whether the number of shipeprs with companyapproved false are equal to expected numbers of user
	//sending page as last page
		//according to total number of already existed data and the datawe added in setup function of this testing (beforeall)
		// we will check whether the last page contains expected number of data or not
	@Test
	@Order(18)
	public void getshipper_companyapproved_false() throws Exception
	{
		long added_shipper_with_company_approved_true = 2;
		long lastPageCount = (shippercount_companyapproved_false+added_shipper_with_company_approved_true) % pagesize;
		long page = (shippercount_companyapproved_false+added_shipper_with_company_approved_true)/pagesize;

		Response response = RestAssured.given()
				.param("pageNo", page)
				.param("companyApproved", false)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		assertEquals(lastPageCount, response.jsonPath().getList("$").size());
	}
	
	//pageNo null
	//sending 
	@Test
	@Order(19)
	public void getshipper_companyapproved_false_pageno_null() throws Exception
	{
		long added_shipper_with_company_approved_true = 2;
		long lastPageCount = (shippercount_companyapproved_false+added_shipper_with_company_approved_true)%pagesize;
		long page = (shippercount_companyapproved_false+added_shipper_with_company_approved_true)/pagesize;
		
		Response response = RestAssured.given()
				.param("companyApproved", false)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(page>0) assertEquals(pagesize, response.jsonPath().getList("$").size());
		else assertEquals(lastPageCount, response.jsonPath().getList("$").size());
	}
	
	//page null
	//sending page as null and shipper approved true
	//so by default it well check whether first page contains expected number of elements for not
	@Test
	@Order(20)
	public void getshipper_companyapproved_true_pageno_null() throws Exception
	{
		long added_shipper_with_company_approved_true = 1;
		long lastPageCount = (shippercount_companyapproved_true+added_shipper_with_company_approved_true) % pagesize;
		long page = (shippercount_companyapproved_true+added_shipper_with_company_approved_true)/pagesize;
		
		Response response = RestAssured.given()
				.param("companyApproved", true)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(page>0) assertEquals(pagesize, response.jsonPath().getList("$").size());
		else assertEquals(lastPageCount, response.jsonPath().getList("$").size());
	}
	
	//page no null
	//sending page as null and shipper approved true
	//so by default it well check whether first page contains expected number of elements for not
	
	@Test
	@Order(21)
	public void getshipperall_pagenonull() throws Exception
	{
		long lastPageCount = (shippercount_all + total_shipper_added)%pagesize;
		long page = (shippercount_all + total_shipper_added)/pagesize;
		
		Response response = RestAssured.given()
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(page>0) assertEquals(pagesize, response.jsonPath().getList("$").size());
		else assertEquals(lastPageCount, response.jsonPath().getList("$").size());
	}
	
	//getby shipperid success
	//sending request as valid shipperid
	//getting valid shipper as response 
	@Test
	@Order(22)
	public void getshipperbyshipperid_success() throws Exception
	{
		Response response = RestAssured.given().header("", "").get("/" + shipperid1).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response.statusCode());
		assertEquals(shipperid1, response.jsonPath().getString("shipperId"));
		assertEquals("person33", response.jsonPath().getString("shipperName"));
		assertEquals("company11", response.jsonPath().getString("companyName"));
		assertEquals("9999999991", response.jsonPath().getString("phoneNo"));
		assertEquals("link11", response.jsonPath().getString("kyc"));
		assertEquals("Nagpur", response.jsonPath().getString("shipperLocation"));
		assertEquals(true, response.jsonPath().getBoolean("companyApproved"));
		assertEquals(true, response.jsonPath().getBoolean("accountVerificationInProgress"));
	}
	
	//getby shipperid fail
	//sending request with shipperid which is not present in database
	// getting response as  exception (Shipper was not found for parameters {shipperid})
	@Test
	@Order(23)
	public void getshipperbyshipperid_fail() throws Exception
	{
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response response = RestAssured.given().header("", "")
				.get("/" + shipperid).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();
	    
		assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
		assertEquals("Shipper was not found for parameters {id=shipperrr:0de885e0-5f43-4c68-8dde-0000000000001}", 
				jsonPathEvaluator.get("shippererrorresponse.message").toString());
	}
	
	//get shipper phoneNo
	//sending paramater as valid phone number
	//getting response with shipper onject respect to phonenumber
	@Test
	@Order(24)
	public void getshipperbyphoneno() throws Exception
	{
		Response response = RestAssured.given()
				.param("phoneNo", "9999999991")
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
	    
	    JsonPath jsonPathEvaluator = response.jsonPath();

		assertEquals(HttpStatus.OK.value(), response.statusCode());
		
		assertEquals(shipperid1, jsonPathEvaluator.get("[0].shipperId").toString());
		assertEquals("person33", jsonPathEvaluator.get("[0].shipperName").toString());
		assertEquals("company11", jsonPathEvaluator.get("[0].companyName").toString());
		assertEquals("9999999991", jsonPathEvaluator.get("[0].phoneNo").toString());
		assertEquals("link11", jsonPathEvaluator.get("[0].kyc").toString());
		assertEquals("Nagpur", jsonPathEvaluator.get("[0].shipperLocation").toString());
		assertEquals(true, jsonPathEvaluator.get("[0].companyApproved"));
		assertEquals(true, jsonPathEvaluator.get("[0].accountVerificationInProgress"));
	}
	
	//delete fail account not found
	//sending request as valid shipperid
	//getting response as exception (Shipper was not found for parameters {shipperid}) 
	@Test
	@Order(25)
	public void deletedatafail()
	{
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response response = RestAssured.given().header("", "").delete("/" + shipperid).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();
	    
		assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
		assertEquals("Shipper was not found for parameters {id=shipperrr:0de885e0-5f43-4c68-8dde-0000000000001}", 
				jsonPathEvaluator.get("shippererrorresponse.message").toString());
	}
	
	@AfterAll
	public static void deletedata()
	{
		Response response1 = RestAssured.given().header("", "").delete("/" + shipperid1).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response1.statusCode());
		assertEquals("Successfully deleted", response1.asString());
		
		Response response2 = RestAssured.given().header("", "").delete("/" + shipperid2).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response2.statusCode());
		assertEquals("Successfully deleted", response2.asString());
		
		Response response3 = RestAssured.given().header("", "").delete("/" + shipperid3).then().extract().response();
		assertEquals(HttpStatus.OK.value(), response3.statusCode());
		assertEquals("Successfully deleted", response3.asString());
	}
	
}
*/

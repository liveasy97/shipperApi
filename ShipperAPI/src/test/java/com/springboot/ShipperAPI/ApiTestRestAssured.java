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


/*

System.err.println("*********0000******" + response.statusCode());
        System.err.println(response.jsonPath().toString());
        
        String shipperid = response.jsonPath().getString("shipperId");
        
        System.err.println("***************" + response.statusCode());
        System.err.println(response.jsonPath().toString());
    	String sss = jsonPathEvaluator.get("$").toString();
        System.err.println(sss);

*/
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
	
	private static int pagesize=15;
	
	
	
	@BeforeAll
	public static void setup() throws Exception {
		
		RestAssured.baseURI = "http://localhost:8080/shipper";
		
		//count all
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
		
		//company approved true
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
		
		//company approved false
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
	

	//add shipper success
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
	
	
	@Test
	@Order(1)
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
	
	//add shipper fail, phone number null
	@Test
	@Order(2)
	public void addshippersuccess3() throws Exception
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
	
	//add shipper fail phone number invalid (9 digit)
	@Test
	@Order(3)
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
	
	//add shipper fail account already exist
	@Test
	@Order(4)
	public void addshippersuccess5() throws Exception
	{
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur","9999999992","link1");
        String inputJson = mapToJson(postshipper);
        Response response = (Response) RestAssured.given().header("", "").body(inputJson).header("accept", "application/json")
				.header("Content-Type", "application/json").post().then().extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals("Account already exist", jsonPathEvaluator.get("message").toString());
	}
	
	//empty shipper name
	@Test
	@Order(5)
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
	
	//empty company name 
	@Test
	@Order(6)
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
	
	//empty shipper location
	@Test
	@Order(7)
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
	
	
	//update success
	@Test
	@Order(8)
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
	
	
	//all values null
	@Test
	@Order(8)
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
	
	//update fail account not found 
	@Test
	@Order(9)
	public void updateshipperfail1() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, "person11","company11", "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		String shipperid = "shipperrr:0de885e0-5f43-4c68-8dde-0000000000001";
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		
		//assertEquals(HttpStatus.OK.value(), response.statusCode());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
        assertEquals("Shipper was not found for parameters {id=shipperrr:0de885e0-5f43-4c68-8dde-0000000000001}", jsonPathEvaluator.get("shippererrorresponse.message").toString());
	}	
	
	//phone number cannot be updated
	@Test
	@Order(10)
	public void updateshipperfail2() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper("9999999991", "person11","company11", "link11", "Nagpur", true, true);

		String inputJsonupdate = mapToJson(updateshipper);
		Response response = RestAssured.given().header("", "").body(inputJsonupdate).header("accept", "application/json")
				.header("Content-Type", "application/json").put("/" + shipperid1).then().extract().response();
		
		JsonPath jsonPathEvaluator = response.jsonPath();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.statusCode());
        assertEquals("Error: Phone no. can't be updated", jsonPathEvaluator.get("shippererrorresponse.message").toString());

	}
	
	//doubt ?
	//shipper name cannot be empty
	@Test
	@Order(11)
	public void updateshipperfail3() throws Exception{
		UpdateShipper updateshipper = new UpdateShipper(null, null,"company11", "link11", "Nagpur", true, true);

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
	
	
	//company name cannot be empty
	@Test
	@Order(12)
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
	
	//location name cannot be empty
	@Test
	@Order(13)
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
	@Test
	@Order(14)
	public void getshipperall() throws Exception
	{
		long lastPageCount = shippercount_all % pagesize;
		long page = shipperpagecount_all;

		if (lastPageCount >= pagesize - 2)
			page++;
		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(lastPageCount <= pagesize-3)
		{
			assertEquals(lastPageCount+3, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == pagesize-2)
		{
			assertEquals(1, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == pagesize-1)
		{
			assertEquals(2, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == pagesize)
		{
			assertEquals(3, response.jsonPath().getList("$").size());
		}
	}
	
	//get company approved true
	
	@Test
	@Order(15)
	public void getshipper_companyapproved_true() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_true % pagesize;
		long page = shipperpagecount_companyapproved_true;

		if (lastPageCount >= pagesize)
			page++;
		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.param("companyApproved", true)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(lastPageCount <= pagesize-1)
		{
			assertEquals(lastPageCount+1, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == pagesize)
		{
			assertEquals(1, response.jsonPath().getList("$").size());
		}
	}
	
	//company approved false
	
	@Test
	@Order(16)
	public void getshipper_companyapproved_false() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_false % pagesize;
		long page = shipperpagecount_companyapproved_false;

		if (lastPageCount >= pagesize-1)
			page++;
		
		Response response = RestAssured.given()
				.param("pageNo", page)
				.param("companyApproved", false)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(lastPageCount <= pagesize-2)
		{
			assertEquals(lastPageCount+2, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == pagesize-1)
		{
			assertEquals(1, response.jsonPath().getList("$").size());
		}
		else if(lastPageCount == pagesize)
		{
			assertEquals(2, response.jsonPath().getList("$").size());
		}
	}
	//pageNo null
	@Test
	@Order(17)
	public void getshipper_companyapproved_false_pageno_null() throws Exception
	{
		long lastPageCount = shippercount_companyapproved_false % pagesize;
		
		Response response = RestAssured.given()
				.param("companyApproved", false)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(shipperpagecount_companyapproved_false>=1 || lastPageCount>=pagesize-2)
		{
			assertEquals(pagesize, response.jsonPath().getList("$").size());
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
		long lastPageCount = shippercount_companyapproved_true % pagesize;
		
		Response response = RestAssured.given()
				.param("companyApproved", true)
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(shipperpagecount_companyapproved_true>=1 || lastPageCount>=pagesize-1)
		{
			assertEquals(pagesize, response.jsonPath().getList("$").size());
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
		long lastPageCount = shippercount_all % pagesize;
		
		Response response = RestAssured.given()
				.header("accept", "application/json").header("Content-Type", "application/json").get().then().extract()
				.response();
		
		if(shipperpagecount_all>=1 || lastPageCount>=pagesize-2)
		{
			assertEquals(pagesize, response.jsonPath().getList("$").size());
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
	@Test
	@Order(21)
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
	@Test
	@Order(22)
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
	@Test
	@Order(23)
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

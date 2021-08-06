package com.springboot.ShipperAPI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Controller.ShipperController;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;
import com.springboot.ShipperAPI.Service.ShipperService;

/*

this file contains unit test for controllerlayer

 */

@WebMvcTest(value = ShipperController.class)
public class TestShipperController {
	
	String URI = "/shipper";
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShipperDao shipperdao;
	
	@MockBean
	private ShipperService shipperservice;
	
	private static String mapToJson(Object object) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		objectMapper.setDateFormat(df);
		return objectMapper.writeValueAsString(object);
	}
	
	@Test
	@Order(1)
	public void addShipper() throws Exception
	{
		ShipperCreateResponse shippercreateresponse = new ShipperCreateResponse(CommonConstants.PENDING, CommonConstants.APPROVE_REQUEST, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person1", "company1", "9999999991",
				"link1", "Nagpur", false,false, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		PostShipper postshipper = new PostShipper("person1","company1", "Nagpur","9999999991","link1");
		
		when(shipperservice.addShipper(Mockito.any(PostShipper.class))).thenReturn(shippercreateresponse);
		
		String inputJson = mapToJson(postshipper);
		String expectedJson = mapToJson(shippercreateresponse);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shipper").accept(MediaType.APPLICATION_JSON).content(inputJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String outputInJson = response.getContentAsString();		

		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}
	
	@Test
	@Order(2)
	public void getShipper() throws Exception
	{
		List<Shipper> shippers = createShippers();
		when(shipperservice.getShippers(true, null, 0)).thenReturn(createShippers().subList(0, 2));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/shipper")
				.queryParam("companyApproved", "true")
				.queryParam("pageNo", "0")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		List<Shipper> shippers1 = shippers.subList(0, 2);
		String expectedJson = mapToJson(shippers1);
		String outputInJson = result.getResponse().getContentAsString();
		
		assertEquals(expectedJson, outputInJson);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}
	
	@Test
	@Order(3)
	public void getShippercompanyapprovednull() throws Exception
	{
		List<Shipper> shippers = createShippers();
		when(shipperservice.getShippers(null, "9999999991", 0)).thenReturn(shippers);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/shipper")
				.queryParam("pageNo", "0")
				.queryParam("phoneNo", "9999999991")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		String expectedJson = mapToJson(shippers);
		String outputInJson = result.getResponse().getContentAsString();

		assertEquals(expectedJson, outputInJson);
		
		MockHttpServletResponse response1 = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response1.getStatus());
	}
	
	
	@Test
	@Order(4)
	public void getbyshipperid() throws Exception
	{
		
		when(shipperservice.getOneShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001")).thenReturn(createShippers().get(0));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/shipper/shipper:0de885e0-5f43-4c68-8dde-0000000000001")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expectedJson = mapToJson(createShippers().get(0));
		String outputInJson = result.getResponse().getContentAsString();

		assertEquals(expectedJson, outputInJson);
		
		MockHttpServletResponse response1 = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response1.getStatus());

	}
	
	@Test
	@Order(5)
	public void updateshipper() throws Exception
	{
		
		ShipperUpdateResponse updateresponse = new ShipperUpdateResponse(CommonConstants.SUCCESS, CommonConstants.UPDATE_SUCCESS, 
				"shipper:0de885e0-5f43-4c68-8dde-0000000000001","person11", "company11", "9999999991",
				"link11",	"Nagpur", true, true, Timestamp.valueOf("2021-07-28 23:28:50.134"));
		
		UpdateShipper updateshipper = new UpdateShipper(null, "person11","company11", "link11", "Nagpur", true, true);
		
		String inputJson = mapToJson(updateshipper);
		
		when(shipperservice.updateShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", updateshipper)).thenReturn(updateresponse);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/shipper/shipper:0de885e0-5f43-4c68-8dde-0000000000001")
				.accept(MediaType.APPLICATION_JSON).content(inputJson).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expectedJson = mapToJson(updateresponse);
		String outputInJson = result.getResponse().getContentAsString();
		
		assertEquals(expectedJson, outputInJson);
		
		MockHttpServletResponse response1 = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response1.getStatus());
		
	}
	
	
	@Test
	@Order(6)
	public void deleteshipper() throws Exception
	{
		
		ShipperDeleteResponse deleteResponse = new ShipperDeleteResponse(CommonConstants.SUCCESS, CommonConstants.DELETE_SUCCESS);	
		
		doNothing().when(shipperservice).deleteShipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/shipper/shipper:0de885e0-5f43-4c68-8dde-0000000000001")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response1 = result.getResponse();
		assertEquals("Successfully deleted", response1.getContentAsString());
		assertEquals(HttpStatus.OK.value(), response1.getStatus());
		
	}
	
	public List<Shipper> createShippers()
	{
		List<Shipper> shippers = Arrays.asList( 
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999992","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999993","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999994","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"))
		);
		return shippers;
	}

}

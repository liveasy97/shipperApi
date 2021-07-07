package com.springboot.ShipperAPI;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ShipperApiApplicationTests {

	@Test
	void contextLoads() {
	}
	/*
	 * @Autowired ShipperService service;
	 * 
	 * @MockBean ShipperDao shipperDao; Shipper shipper1 = new Shipper(); Shipper
	 * shipper2 = new Shipper(); Shipper shipper3 = new Shipper();
	 * 
	 * 
	 * //Unit Testing for saving
	 * 
	 * @Test public void saveTransporterTest() { shipper1.setId("transporter:01");
	 * shipper1.setName("Ram"); shipper1.setKyc("kyc"); shipper1.setApproved(false);
	 * shipper1.setPhoneNo(1234456789); shipper2.setId("transporter:02");
	 * shipper2.setName("Seeta"); shipper2.setKyc("kycSeeta");
	 * shipper2.setApproved(true); shipper2.setPhoneNo(1796546650);
	 * shipper3.setId("transporter:03"); shipper3.setName("Laxman");
	 * shipper3.setKyc("kycLaxman"); shipper3.setApproved(false);
	 * shipper3.setPhoneNo(1287965546); ShipperCreateResponse tcr = new
	 * ShipperCreateResponse(); tcr.setStatus("Pending");
	 * tcr.setMessage("Please wait for liveasy will approve your request");
	 * when(shipperDao.save(shipper1)).thenReturn(shipper1); assertEquals(tcr,
	 * service.addShipper(shipper1));
	 * when(shipperDao.save(shipper2)).thenReturn(shipper2); assertEquals(tcr,
	 * service.addShipper(shipper2));
	 * when(shipperDao.save(shipper3)).thenReturn(shipper3); assertEquals(tcr,
	 * service.addShipper(shipper3)); }
	 * 
	 * //Unit Testing for saving without name
	 * 
	 * @Test public void saveWithoutNameTest() { Shipper shipper4 = new Shipper();
	 * shipper4.setId("transporter:01"); shipper4.setKyc("kyc");
	 * shipper4.setApproved(false); shipper4.setPhoneNo(1234456789);
	 * ShipperCreateResponse tcr = new ShipperCreateResponse();
	 * tcr.setStatus("Error"); tcr.setMessage("Enter name");
	 * when(shipperDao.save(shipper4)).thenReturn(shipper4); assertEquals(tcr,
	 * service.addShipper(shipper4)); }
	 * 
	 * //Unit Testing for saving without phone number
	 * 
	 * @Test public void saveWithoutPhoneNoTest() { Shipper shipper4 = new
	 * Shipper(); shipper4.setName("John"); shipper4.setId("transporter:01");
	 * shipper4.setKyc("kyc"); shipper4.setApproved(false); ShipperCreateResponse
	 * tcr = new ShipperCreateResponse(); tcr.setStatus("Error");
	 * tcr.setMessage("Enter phone number");
	 * when(shipperDao.save(shipper4)).thenReturn(shipper4); assertEquals(tcr,
	 * service.addShipper(shipper4)); }
	 * 
	 * //Unit Testing for saving with incorrect phone number
	 * 
	 * @Test public void saveWithIncorrectPhoneNoTest() { Shipper shipper4 = new
	 * Shipper(); shipper4.setName("Tyler"); shipper4.setId("transporter:01");
	 * shipper4.setKyc("kyc"); shipper4.setApproved(false);
	 * shipper4.setPhoneNo(1234456); ShipperCreateResponse tcr = new
	 * ShipperCreateResponse(); tcr.setStatus("Error");
	 * tcr.setMessage("Enter 10 digits phone number");
	 * when(shipperDao.save(shipper4)).thenReturn(shipper4); assertEquals(tcr,
	 * service.addShipper(shipper4)); }
	 * 
	 * //Unit Testing for adding existing data
	 * 
	 * @Test public void addExistingDataTest() { Shipper shipper4 = new Shipper();
	 * shipper4.setName("Tyler"); shipper4.setId("transporter:01");
	 * shipper4.setKyc("kyc"); shipper4.setApproved(false);
	 * shipper4.setPhoneNo(1234456345);
	 * 
	 * ShipperCreateResponse tcr = new ShipperCreateResponse();
	 * when(shipperDao.findByPhoneNo(shipper4.getPhoneNo())).thenReturn("1234567890"
	 * ); tcr.setStatus("Error"); tcr.setMessage("Account already exist");
	 * assertEquals(tcr, service.addShipper(shipper4));
	 * 
	 * }
	 * 
	 * //Unit Testing for getting all transporters
	 * 
	 * @Test public void getAllTransportersTest() {
	 * when(shipperDao.findAll()).thenReturn(Stream.of(shipper1, shipper2,
	 * shipper3).collect(Collectors.toList())); //assertEquals(3,
	 * service.allShipper().size()); }
	 * 
	 * //Unit Testing for getting all transporters based on approved value
	 * 
	 * @Test public void getApprovedTest() {
	 * when(shipperDao.findByApproved(true)).thenReturn(Stream.of(shipper2).collect(
	 * Collectors.toList())); //assertEquals(1, service.getApproved(true).size()); }
	 * 
	 * //Unit Testing for getting single transporter
	 * 
	 * @Test public void getOneTransporterTest() {
	 * //when(shipperDao.getById("shipper:02")).thenReturn(shipper2);
	 * assertEquals(shipper2, service.getOneShipper("shipper:02")); }
	 * 
	 * //Unit Testing for updating with correct id
	 * 
	 * @Test public void updateWithCorrectIdTest() { shipper3.setName("Adam");
	 * ShipperUpdateResponse tur = new ShipperUpdateResponse();
	 * tur.setStatus("Success"); tur.setMessage("Account updated successfully");
	 * 
	 * Optional<Shipper> t = Optional.of(shipper3);
	 * when(shipperDao.findById("shipper:03")).thenReturn(t); assertEquals(tur,
	 * service.updateShipper("shipper:03", shipper3)); }
	 * 
	 * //Unit Testing for updating with incorrect id
	 * 
	 * @Test public void updateWithIncorrectIdTest() { shipper1.setName("Adam");
	 * ShipperUpdateResponse tur = new ShipperUpdateResponse();
	 * tur.setStatus("Not Found"); tur.setMessage("Account does not exist");
	 * 
	 * Optional<Shipper> t = Optional.of(shipper1);
	 * when(shipperDao.findById("shipper:01")).thenReturn(t); assertEquals(tur,
	 * service.updateShipper("shipper:10", shipper1)); }
	 * 
	 * //Unit Testing for updating with phone number
	 * 
	 * @Test public void updateWithPhoneNoTest() { shipper1.setName("Adam");
	 * shipper1.setPhoneNo(1234567890); ShipperUpdateResponse tur = new
	 * ShipperUpdateResponse(); tur.setStatus("Error");
	 * tur.setMessage("Phone number cannot be changed");
	 * 
	 * Optional<Shipper> t = Optional.of(shipper1);
	 * when(shipperDao.findById("shipper:01")).thenReturn(t); assertEquals(tur,
	 * service.updateShipper("shipper:01", shipper1)); }
	 * 
	 * //Unit Testing for deletion with correct id
	 * 
	 * @Test public void deleteWithCorrectIdTest() { ShipperDeleteResponse tdr = new
	 * ShipperDeleteResponse(); tdr.setStatus("Success");
	 * tdr.setMessage("Account deleted successfully");
	 * 
	 * Optional<Shipper> t = Optional.of(shipper2);
	 * when(shipperDao.findById("shipper:02")).thenReturn(t); assertEquals(tdr,
	 * service.deleteShipper("shipper:02"));
	 * verify(shipperDao,times(1)).delete(shipper2); }
	 * 
	 * ////Unit Testing for deletion with incorrect id
	 * 
	 * @Test public void deleteWithIncorrectIdTest() { ShipperDeleteResponse tdr =
	 * new ShipperDeleteResponse(); tdr.setStatus("Not Found");
	 * tdr.setMessage("Account does not exist");
	 * 
	 * Optional<Shipper> t = Optional.of(shipper1);
	 * when(shipperDao.findById("shipper:01")).thenReturn(t); assertEquals(tdr,
	 * service.deleteShipper("transporter:15"));
	 * verify(shipperDao,times(0)).delete(shipper1); }
	 * 
	 */

}

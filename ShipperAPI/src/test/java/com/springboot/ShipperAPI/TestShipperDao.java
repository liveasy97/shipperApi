package com.springboot.ShipperAPI;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;


/*

this file contains unit test for dao layer

 */
@DataJpaTest
public class TestShipperDao {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ShipperDao shipperdao;
	
	public static void wait(int ms)
	{
	    try
	    {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}
	
	@Test
	public void findByPhoneNo()
	{
		List<Shipper> listshipper = createShippers();   //findShipperByPhoneNo

		Shipper savedInDb = entityManager.persist(listshipper.get(0));
		Long getFromDb = shipperdao.findByPhoneNo("9999999991");
		assertThat(getFromDb.toString()).isEqualTo(savedInDb.getPhoneNo());
	}
	
	@Test
	public void findShipperByPhoneNo()
	{
		List<Shipper> listshipper = createShippers();  
		
		Shipper savedInDb = entityManager.persist(listshipper.get(0));
		Optional<Shipper> getFromDb = shipperdao.findShipperByPhoneNo("9999999991");
		assertThat(getFromDb.isPresent()).isEqualTo(true);
		assertThat(getFromDb.get()).isEqualTo(savedInDb);
	}
	
	//find fail
	@Test
	public void findShipperByPhoneNofail()
	{
		List<Shipper> listshipper = createShippers();  

		Shipper savedInDb = entityManager.persist(listshipper.get(0));
		Optional<Shipper> getFromDb = shipperdao.findShipperByPhoneNo("9999999992");
		assertThat(getFromDb.isPresent()).isEqualTo(false);
	}
	
	
	@Test
	public void getAll() throws InterruptedException
	{
		List<Shipper> shippers = new ArrayList<Shipper>();
		for(int i=1; i<=9; i++)
		{
			
			Shipper savedInDb = entityManager.persist(new Shipper("shipper:0de885e0-5f43-4c68-8dde-000000000000"+i, "person1",
					"company1", "999999999"+i,"link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.13"+i)));
			entityManager.flush();
			shippers.add(savedInDb);
			wait(1);
		}
		Collections.reverse(shippers);

		PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.DESC, "timestamp"),
				    secondPage = PageRequest.of(1, 5, Sort.Direction.DESC, "timestamp"),
				    thirdPage = PageRequest.of(2, 5, Sort.Direction.DESC, "timestamp");

		assertThat(shippers.subList(0, 5)).isEqualTo(shipperdao.getAll(firstPage));
		assertThat(shippers.subList(5, 9)).isEqualTo(shipperdao.getAll(secondPage));
		assertThat(shippers.subList(9, 9)).isEqualTo(shipperdao.getAll(thirdPage));
	}
	
	@Test
	public void findByCompanyApproved()
	{
		List<Shipper> shipperstrue = new ArrayList<Shipper>();
		List<Shipper> shippersfalse = new ArrayList<Shipper>();
		for(int i=11; i<=28; i++)
		{
			Shipper savedInDb = entityManager.persist(new Shipper("shipper:0de885e0-5f43-4c68-8dde-00000000000"+i, "person1",
					"company1", "99999999"+i,"link1", "Nagpur", (i%2==1), (i%2==1), Timestamp.valueOf("2021-07-28 23:28:50.134")));
			entityManager.flush();
			if(i%2==1) shipperstrue.add(savedInDb);
			else  shippersfalse.add(savedInDb);
			wait(1);
		}
		Collections.reverse(shipperstrue);
		Collections.reverse(shippersfalse);
		
		PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.DESC, "timestamp"),
			        secondPage = PageRequest.of(1, 5, Sort.Direction.DESC, "timestamp"),
			        thirdPage = PageRequest.of(2, 5, Sort.Direction.DESC, "timestamp");
	
	    assertThat(shipperstrue.subList(0, 5)).isEqualTo(shipperdao.findByCompanyApproved(true,firstPage));
	    assertThat(shipperstrue.subList(5, 9)).isEqualTo(shipperdao.findByCompanyApproved(true,secondPage));
	    assertThat(shipperstrue.subList(9, 9)).isEqualTo(shipperdao.findByCompanyApproved(true,thirdPage));
	    
	    assertThat(shippersfalse.subList(0, 5)).isEqualTo(shipperdao.findByCompanyApproved(false,firstPage));
	    assertThat(shippersfalse.subList(5, 9)).isEqualTo(shipperdao.findByCompanyApproved(false,secondPage));
	    assertThat(shippersfalse.subList(9, 9)).isEqualTo(shipperdao.findByCompanyApproved(false,thirdPage));
	}
	
	
	public List<Shipper> createShippers()
	{
		List<Shipper> shippers = Arrays.asList( 
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000002", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134")),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000003", "person1",
				"company1", "9999999991","link1", "Nagpur", false, false, Timestamp.valueOf("2021-07-28 23:28:50.134"))
		);
		return shippers;
	}
}

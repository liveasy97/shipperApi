package com.springboot.ShipperAPI;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Id;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;

@DataJpaTest
public class TestShipperDao {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ShipperDao shipperdao;
	
	@Test
	public void findByPhoneNo()
	{
		List<Shipper> listshipper = createShippers();   //findShipperByPhoneNo

		Shipper savedInDb = entityManager.persist(listshipper.get(0));
		Long getFromDb = shipperdao.findByPhoneNo(9999999991L);

		assertThat(getFromDb).isEqualTo(savedInDb.getPhoneNo());
	}
	
	@Test
	public void findShipperByPhoneNo()
	{
		List<Shipper> listshipper = createShippers();  

		Shipper savedInDb = entityManager.persist(listshipper.get(0));
		Shipper getFromDb = shipperdao.findShipperByPhoneNo(9999999991L);
		assertThat(getFromDb).isEqualTo(savedInDb);
	}
	
	//find fail
	@Test
	public void findShipperByPhoneNofail()
	{
		List<Shipper> listshipper = createShippers();  

		Shipper savedInDb = entityManager.persist(listshipper.get(0));
		Shipper getFromDb = shipperdao.findShipperByPhoneNo(9999999992L);
		assertThat(getFromDb).isEqualTo(null);
	}
	
	@Test
	public void getAll()
	{
		for(int i=1; i<=16; i++)
		{
			Shipper savedInDb = entityManager.persist(new Shipper("shipper:0de885e0-5f43-4c68-8dde-000000000000"+i, "person1", "company1",
					9999999991L, "link1", "Nagpur", true, true));
		}
		
		PageRequest firstPage = PageRequest.of(0, (int) CommonConstants.pagesize),
				    secondPage = PageRequest.of(1, (int) CommonConstants.pagesize),
				    thirdPage = PageRequest.of(2, (int) CommonConstants.pagesize);
		
		List<Shipper> getFromDb1 = shipperdao.getAll(firstPage);
		assertThat(getFromDb1.size()).isEqualTo(CommonConstants.pagesize);
		
		List<Shipper> getFromDb2 = shipperdao.getAll(secondPage);
		assertThat(getFromDb2.size()).isEqualTo(1);
		
		List<Shipper> getFromDb3 = shipperdao.getAll(thirdPage);
		assertThat(getFromDb3.size()).isEqualTo(0);
	}
	
	@Test
	public void findByCompanyApproved()
	{
		for(int i=1; i<=33; i++)
		{
			Shipper savedInDb = entityManager.persist(new Shipper("shipper:0de885e0-5f43-4c68-8dde-000000000000"+i, "person1", "company1",
					9999999991L, "link1", "Nagpur", (i%2==1) , true));
		}
		
		PageRequest firstPage = PageRequest.of(0, (int) CommonConstants.pagesize),
			        secondPage = PageRequest.of(1, (int) CommonConstants.pagesize),
			        thirdPage = PageRequest.of(2, (int) CommonConstants.pagesize);
	
	    List<Shipper> getFromDb1 = shipperdao.findByCompanyApproved(true,firstPage);
	    assertThat(getFromDb1.size()).isEqualTo(CommonConstants.pagesize);
	    
	    List<Shipper> getFromDb2 = shipperdao.findByCompanyApproved(true,secondPage);
	    assertThat(getFromDb2.size()).isEqualTo(2);
	
	    List<Shipper> getFromDb3 = shipperdao.findByCompanyApproved(true,thirdPage);
	    assertThat(getFromDb3.size()).isEqualTo(0);
	    
	    List<Shipper> getFromDb4 = shipperdao.findByCompanyApproved(false,firstPage);
	    assertThat(getFromDb4.size()).isEqualTo(CommonConstants.pagesize);
	    
	    List<Shipper> getFromDb5 = shipperdao.findByCompanyApproved(false,secondPage);
	    assertThat(getFromDb5.size()).isEqualTo(1);
	
	    List<Shipper> getFromDb6 = shipperdao.findByCompanyApproved(false,thirdPage);
	    assertThat(getFromDb6.size()).isEqualTo(0);
	}
	
	public List<Shipper> createShippers()
	{
		List<Shipper> shippers = Arrays.asList( 
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000001", "person1","company1", (long) 9999999991L,"link1", "Nagpur", true, true),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000002", "person2","company2", (long) 9999999992L,"link2", "Nagpur", true, false),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000003", "person3","company3", (long) 9999999993L,"link3", "Raipur", false, true),
		new Shipper("shipper:0de885e0-5f43-4c68-8dde-0000000000004", "person4","company4", (long) 9999999994L,"link4", "Raipur", false, false)
		);
		return shippers;
	}

}

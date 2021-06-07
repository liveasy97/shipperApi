package com.springboot.ShipperAPI.Dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.ShipperAPI.Entity.Shipper;

public interface ShipperDao extends JpaRepository<Shipper, String> {
	@Query("select name from Shipper s where s.phoneNo = :phoneNo")
	String findByPhoneNo(Long phoneNo);	
	
	List<Shipper> findByApproved(Boolean approved, Pageable pageable);

}

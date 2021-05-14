package com.springboot.ShipperAPI.Dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.ShipperAPI.Entity.Shipper;

public interface ShipperDao extends JpaRepository<Shipper, String> {
	@Query("select phoneNo from Shipper s where s.phoneNo = :phoneNo")
	String findByPhoneNo(long phoneNo);
	
	@Query("select s from Shipper s where s.approved = :approved")
	List<Shipper> findByApproved(Boolean approved);
	
}

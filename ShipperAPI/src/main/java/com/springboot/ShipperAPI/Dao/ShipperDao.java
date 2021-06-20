package com.springboot.ShipperAPI.Dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.ShipperAPI.Entity.Shipper;

public interface ShipperDao extends JpaRepository<Shipper, String> {
	@Query("select phoneNo from Shipper s where s.phoneNo = :phoneNo")
	Long findByPhoneNo(Long phoneNo);	
	
	@Query("select s from Shipper s where s.phoneNo = :phoneNo")
	Shipper findShipperByPhoneNo(Long phoneNo);	

	@Query("select s from Shipper s")
	List<Shipper> getAll(Pageable pageable);
	
	List<Shipper> findByCompanyApproved(Boolean companyApproved, Pageable pageable);
}

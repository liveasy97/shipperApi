package com.springboot.ShipperAPI.Service;

import java.util.List;

import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.ShipperCreateRequest;
import com.springboot.ShipperAPI.Model.ShipperDeleteRequest;
import com.springboot.ShipperAPI.Model.ShipperUpdateRequest;

public interface ShipperService {

	ShipperCreateRequest addShipper(Shipper shipper);

	List<Shipper> allShipper();

	List<Shipper> getApproved(Boolean approved);

	Shipper getOneShipper(String id);

	ShipperUpdateRequest updateShipper(String id, Shipper shipper);

	ShipperDeleteRequest deleteShipper(String id);


}

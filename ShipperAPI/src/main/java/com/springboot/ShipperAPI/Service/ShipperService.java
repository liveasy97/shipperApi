package com.springboot.ShipperAPI.Service;

import java.util.List;

import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.ShipperCreateResponse;
import com.springboot.ShipperAPI.Model.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Model.ShipperUpdateResponse;

public interface ShipperService {

	ShipperCreateResponse addShipper(Shipper shipper);

	List<Shipper> getShippers(Boolean approved);

	Shipper getOneShipper(String id);

	ShipperUpdateResponse updateShipper(String id, Shipper shipper);

	ShipperDeleteResponse deleteShipper(String id);


}

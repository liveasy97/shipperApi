package com.springboot.ShipperAPI.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.LoadShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;

public interface ShipperService {

	ShipperCreateResponse addShipper(LoadShipper shipper);

	List<Shipper> getShippers(Boolean approved, Integer pageNo);

	Shipper getOneShipper(String id);

	ShipperUpdateResponse updateShipper(String id, LoadShipper shipper);

	ShipperDeleteResponse deleteShipper(String id);


}

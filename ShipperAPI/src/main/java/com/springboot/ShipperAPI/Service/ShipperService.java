package com.springboot.ShipperAPI.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;

public interface ShipperService {

	ShipperCreateResponse addShipper(PostShipper shipper);

	List<Shipper> getShippers(Boolean companyApproved, Integer pageNo);

	Shipper getOneShipper(String shipperId);

	ShipperUpdateResponse updateShipper(String shipperId, UpdateShipper shipper);

	ShipperDeleteResponse deleteShipper(String shipperId);


}

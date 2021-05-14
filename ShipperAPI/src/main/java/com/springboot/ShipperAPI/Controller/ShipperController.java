package com.springboot.ShipperAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.ShipperCreateResponse;
import com.springboot.ShipperAPI.Model.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Model.ShipperUpdateResponse;
import com.springboot.ShipperAPI.Service.ShipperService;

@RestController
public class ShipperController {
	
	@Autowired
	ShipperService service;
	
	@PostMapping("/shipper")
	public ShipperCreateResponse addShipper(@RequestBody Shipper shipper) {
		return service.addShipper(shipper);
	}
	
	@GetMapping("/shipper")
	public List<Shipper> getShippers(@RequestParam(required = false) Boolean approved){
		
		return service.getShippers(approved);
		
	}
	
	@GetMapping("/shipper/{id}")
	private Shipper getOneShipper(@PathVariable String id) {
		// TODO Auto-generated method stub
		return service.getOneShipper(id);
	}
	
	
	@PutMapping("/shipper/{id}")
	public ShipperUpdateResponse updateShipper(@PathVariable String id, @RequestBody Shipper shipper){
		return service.updateShipper(id, shipper);
	}
	
	
	@DeleteMapping("/shipper/{id}")
	public ShipperDeleteResponse deleteShipper(@PathVariable String id){
		return service.deleteShipper(id);
	}

}

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
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;
import com.springboot.ShipperAPI.Service.ShipperService;

@RestController
public class ShipperController {
	
	@Autowired
	ShipperService service;
	
	@GetMapping("/home")
	public String home() {
		return "Welcome to shipperApi git actions test 2...!!!";
	}
	
	
	@PostMapping("/shipper")
	public ShipperCreateResponse addShipper(@RequestBody PostShipper postShipper) {
		return service.addShipper(postShipper);
	}
	
	@GetMapping("/shipper")
	public List<Shipper> getShippers(@RequestParam(required = false) Boolean companyApproved, @RequestParam(required = false) Integer pageNo) {
		return service.getShippers(companyApproved, pageNo);
		
	}
	
	@GetMapping("/shipper/{shipperId}")
	private Shipper getOneShipper(@PathVariable String shipperId) {
		return service.getOneShipper(shipperId);
	}
	
	
	@PutMapping("/shipper/{shipperId}")
	public ShipperUpdateResponse updateShipper(@PathVariable String shipperId, @RequestBody UpdateShipper updateShipper){
		return service.updateShipper(shipperId, updateShipper);
	}
	
	
	@DeleteMapping("/shipper/{shipperId}")
	public ShipperDeleteResponse deleteShipper(@PathVariable String shipperId){
		return service.deleteShipper(shipperId);
	}

}

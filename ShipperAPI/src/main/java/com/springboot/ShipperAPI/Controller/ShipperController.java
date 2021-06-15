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
		return "Welcome to shipperApi...!!!";
	}
	
	
	@PostMapping("/shipper")
	public ShipperCreateResponse addShipper(@RequestBody PostShipper postShipper) {
		return service.addShipper(postShipper);
	}
	
	@GetMapping("/shipper")
	public List<Shipper> getShippers(@RequestParam(required = false) Boolean companyApproved, @RequestParam(required = false) Integer pageNo) {
		return service.getShippers(companyApproved, pageNo);
		
	}
	
	@GetMapping("/shipper/{id}")
	private Shipper getOneShipper(@PathVariable String id) {
		return service.getOneShipper(id);
	}
	
	
	@PutMapping("/shipper/{id}")
	public ShipperUpdateResponse updateShipper(@PathVariable String id, @RequestBody UpdateShipper updateShipper){
		return service.updateShipper(id, updateShipper);
	}
	
	
	@DeleteMapping("/shipper/{id}")
	public ShipperDeleteResponse deleteShipper(@PathVariable String id){
		return service.deleteShipper(id);
	}

}

package com.springboot.ShipperAPI.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.springboot.ShipperAPI.Service.ShipperService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ShipperController {

	@Autowired
	ShipperService service;

	@GetMapping("/home")
	public String home() {
		return "Welcome to shipperApi git actions test...!!!";
	}


	@PostMapping("/shipper")
	public ResponseEntity<Object> addShipper(@Valid @RequestBody PostShipper postShipper) {
		log.info("Post Controller Started");
		return new ResponseEntity<>(service.addShipper(postShipper),HttpStatus.CREATED);
	}

	@GetMapping("/shipper")
	public ResponseEntity<List<Shipper>> getShippers(
			@RequestParam(required = false) Boolean companyApproved,
			@RequestParam(required = false) Integer pageNo) {
		log.info("Get with Params Controller Started");
		return new ResponseEntity<>(service.getShippers(companyApproved, pageNo),HttpStatus.OK);

	}

	@GetMapping("/shipper/{shipperId}")
	private ResponseEntity<Object> getOneShipper(@PathVariable String shipperId) {
		log.info("Get Controller Started");
		return new ResponseEntity<>(service.getOneShipper(shipperId),HttpStatus.OK);
	}


	@PutMapping("/shipper/{shipperId}")
	public ResponseEntity<Object> updateShipper(@PathVariable String shipperId, @RequestBody UpdateShipper updateShipper){
		log.info("Put Controller Started");
		return new ResponseEntity<>( service.updateShipper(shipperId, updateShipper),HttpStatus.OK);
	}


	@DeleteMapping("/shipper/{shipperId}")
	public ResponseEntity<Object> deleteShipper(@PathVariable String shipperId){
		log.info("Delete Controller Started");
		service.deleteShipper(shipperId);
		return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
	}

}

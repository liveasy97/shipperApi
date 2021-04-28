package com.springboot.ShipperAPI.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.ShipperCreateRequest;
import com.springboot.ShipperAPI.Model.ShipperDeleteRequest;
import com.springboot.ShipperAPI.Model.ShipperUpdateRequest;

@Service
public class ShipperServiceImpl implements ShipperService {

	@Autowired
	ShipperDao shipperdao;
	CommonConstants constants = new CommonConstants();
	
	@Override
	public ShipperCreateRequest addShipper(Shipper shipper) {
		// TODO Auto-generated method stub
		ShipperCreateRequest createRequest = new ShipperCreateRequest();
		if (shipper.getName() == null) {
			createRequest.setStatus(constants.getError());
			createRequest.setMessage(constants.getNameError());
			return createRequest;
		}
		
		if (shipper.getPhoneNo() == 0) {
			createRequest.setStatus(constants.getError());
			createRequest.setMessage(constants.getPhoneNoError());
			return createRequest;
		}
		
		if(String.valueOf(shipper.getPhoneNo()).length() != 10) {
			createRequest.setStatus(constants.getError());
			createRequest.setMessage(constants.getIncorrecPhoneNoError());
			return createRequest;
		}
		
		String a = null;
		a = shipperdao.findByPhoneNo(shipper.getPhoneNo());
		if (a != null) {
			createRequest.setStatus(constants.getError());
			createRequest.setMessage(constants.getAccountExist());
			return createRequest;
		}
		
		shipper.setId("shipper:"+UUID.randomUUID());
		shipperdao.save(shipper);
		createRequest.setStatus(constants.getPending());
		createRequest.setMessage(constants.getApproveRequest());
		return createRequest;

	}

	@Override
	public List<Shipper> allShipper() {
		// TODO Auto-generated method stub
		return shipperdao.findAll();
	}

	@Override
	public List<Shipper> getApproved(Boolean approved) {
		// TODO Auto-generated method stub
		return shipperdao.findByApproved(approved);
	}

	@Override
	public Shipper getOneShipper(String id) {
		// TODO Auto-generated method stub
		return shipperdao.getById(id);
	}

	@Override
	public ShipperUpdateRequest updateShipper(String id, Shipper updateshipper) {
		// TODO Auto-generated method stub
		ShipperUpdateRequest updateRequest = new ShipperUpdateRequest();
		Shipper shipper = new Shipper();
		Optional<Shipper> S = shipperdao.findById(id);
		if(S.isPresent()) {
			shipper = S.get();
		}
		else {
			updateRequest.setStatus(constants.getNotFound());
			updateRequest.setMessage(constants.getAccountNotExist());
			return updateRequest;
		}

		if (updateshipper.getPhoneNo() != 0) {			
			updateRequest.setStatus(constants.getError());
			updateRequest.setMessage(constants.getPhoneNoUpdateError());
			return updateRequest;
		}

		if (updateshipper.getName() != null) {
			shipper.setName(updateshipper.getName());
		}

		if (updateshipper.getKyc() != null) {
			shipper.setKyc(updateshipper.getKyc());
		}
		
		if (updateshipper.getCompanyName()!= null) {
			shipper.setCompanyName(updateshipper.getCompanyName());
		}

		shipper.setApproved(updateshipper.isApproved());
		shipperdao.save(shipper);
		updateRequest.setStatus(constants.getSuccess());
		updateRequest.setMessage(constants.getUpdateSuccess());
		return updateRequest;

	}

	@Override
	public ShipperDeleteRequest deleteShipper(String id) {
		ShipperDeleteRequest deleteRequest = new ShipperDeleteRequest();
		Shipper shipper = new Shipper();
		Optional<Shipper> S = shipperdao.findById(id);
		 
		if( S.isPresent()) {
			shipper = S.get();
			shipperdao.delete(shipper);
			deleteRequest.setStatus(constants.getSuccess());
			deleteRequest.setMessage(constants.getDeleteSuccess());
			return deleteRequest;
		}
		else {
			deleteRequest.setStatus(constants.getNotFound());
			deleteRequest.setMessage(constants.getAccountNotExist());
			return deleteRequest;
		}
	}

}

package com.springboot.ShipperAPI.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.ShipperCreateResponse;
import com.springboot.ShipperAPI.Model.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Model.ShipperUpdateResponse;

@Service
public class ShipperServiceImpl implements ShipperService {

	@Autowired
	ShipperDao shipperdao;
	
	@Override
	public ShipperCreateResponse addShipper(Shipper shipper) {
		// TODO Auto-generated method stub
		ShipperCreateResponse createResponse = new ShipperCreateResponse();
		if (shipper.getName() == null) {
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.NAME_ERROR);
			return createResponse;
		}
		
		if (shipper.getPhoneNo() == 0) {
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.PHONE_NUMBER_ERROR);
			return createResponse;
		}
		
		String validate = "[0-9]{10}$";
		Pattern pattern = Pattern.compile(validate);
		Matcher m = pattern.matcher(Long.toString(shipper.getPhoneNo()));
		if(!m.matches()) {
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.INCORRECT_PHONE_NUMBER);
			return createResponse;
		}
		
		String a = null;
		a = shipperdao.findByPhoneNo(shipper.getPhoneNo());
		if (a != null) {
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.ACCOUNT_EXIST);
			return createResponse;
		}
		
		shipper.setId("shipper:"+UUID.randomUUID());
		shipperdao.save(shipper);
		createResponse.setStatus(CommonConstants.PENDING);
		createResponse.setMessage(CommonConstants.APPROVE_REQUEST);
		return createResponse;

	}

	@Override
	public List<Shipper> getShippers(Boolean approved) {
		if(approved==null) {
			return shipperdao.findAll();
		}
		return shipperdao.findByApproved(approved);
	}

	@Override
	public Shipper getOneShipper(String id) {
		Optional<Shipper> S = shipperdao.findById(id);
		if(S.isPresent()) {
			return shipperdao.findById(id).get();
		}
		return null;
	}

	@Override
	public ShipperUpdateResponse updateShipper(String id, Shipper updateShipper) {
		// TODO Auto-generated method stub
		ShipperUpdateResponse updateResponse = new ShipperUpdateResponse();
		Shipper shipper = new Shipper();
		Optional<Shipper> S = shipperdao.findById(id);
		if(S.isPresent()) {
			shipper = S.get();
			if (updateShipper.getPhoneNo() != 0) {			
				updateResponse.setStatus(CommonConstants.ERROR);
				updateResponse.setMessage(CommonConstants.PHONE_NUMBER_UPDATE_ERROR);
				return updateResponse;
			}

			if (updateShipper.getName() != null) {
				shipper.setName(updateShipper.getName());
			}

			if (updateShipper.getKyc() != null) {
				shipper.setKyc(updateShipper.getKyc());
			}
			
			if (updateShipper.getCompanyName()!= null) {
				shipper.setCompanyName(updateShipper.getCompanyName());
			}

			shipper.setApproved(updateShipper.isApproved());
			shipperdao.save(shipper);
			updateResponse.setStatus(CommonConstants.SUCCESS);
			updateResponse.setMessage(CommonConstants.UPDATE_SUCCESS);
			return updateResponse;
		}
		
		else {
			updateResponse.setStatus(CommonConstants.NOT_FOUND);
			updateResponse.setMessage(CommonConstants.ACCOUNT_NOT_EXIST);
			return updateResponse;
		}
	}

	@Override
	public ShipperDeleteResponse deleteShipper(String id) {
		ShipperDeleteResponse deleteResponse = new ShipperDeleteResponse();
		Optional<Shipper> S = shipperdao.findById(id);
		 
		if( S.isPresent()) {
			shipperdao.delete(S.get());
			deleteResponse.setStatus(CommonConstants.SUCCESS);
			deleteResponse.setMessage(CommonConstants.DELETE_SUCCESS);
			return deleteResponse;
		}
		else {
			deleteResponse.setStatus(CommonConstants.NOT_FOUND);
			deleteResponse.setMessage(CommonConstants.ACCOUNT_NOT_EXIST);
			return deleteResponse;
		}
	}

}

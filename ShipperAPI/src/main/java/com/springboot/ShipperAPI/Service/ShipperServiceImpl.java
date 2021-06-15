package com.springboot.ShipperAPI.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.PostShipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperDeleteResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;

@Service
public class ShipperServiceImpl implements ShipperService {

	String companyName = null, name = null, shipperLocation = null;
	
	@Autowired
	ShipperDao shipperdao;
	
	@Override
	public ShipperCreateResponse addShipper(PostShipper addShipper) {
		// TODO Auto-generated method stub
		Shipper shipper = new Shipper();
		ShipperCreateResponse createResponse = new ShipperCreateResponse();
		
		if (addShipper.getPhoneNo() == null) {
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.PHONE_NUMBER_ERROR);
			return createResponse;
		}
		
		String validate = "[0-9]{10}$";
		Pattern pattern = Pattern.compile(validate);
		Matcher m = pattern.matcher(Long.toString(addShipper.getPhoneNo()));
		if(!m.matches()) {
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.INCORRECT_PHONE_NUMBER);
			return createResponse;
		}
		
		Long a = null;
		a = shipperdao.findByPhoneNo(addShipper.getPhoneNo());
		
		if (a != null) {
			shipper = shipperdao.findShipperByPhoneNo(addShipper.getPhoneNo());
			createResponse.setStatus(CommonConstants.ERROR);
			createResponse.setMessage(CommonConstants.ACCOUNT_EXIST);
			createResponse.setId(shipper.getId());
			createResponse.setPhoneNo(shipper.getPhoneNo());
			createResponse.setName(shipper.getName());
			createResponse.setCompanyName(shipper.getCompanyName());
			createResponse.setShipperLocation(shipper.getShipperLocation());
			createResponse.setKyc(shipper.getKyc());
			createResponse.setCompanyApproved(shipper.isCompanyApproved());
			createResponse.setAccountVerificationInProgress(shipper.isAccountVerificationInProgress());
			return createResponse;
		}
		
		if (addShipper.getName() != null) {
			if (addShipper.getName().trim().length()<1) {
				createResponse.setStatus(CommonConstants.ERROR);
				createResponse.setMessage(CommonConstants.EMPTY_NAME_ERROR);
				return createResponse;
			}
			name = addShipper.getName().trim();
		}
		
		if(addShipper.getCompanyName() != null) {
			if (addShipper.getCompanyName().trim().length()<1) {
				createResponse.setStatus(CommonConstants.ERROR);
				createResponse.setMessage(CommonConstants.EMPTY_COMPANY_NAME_ERROR);
				return createResponse;
			}
			companyName = addShipper.getCompanyName().trim();
		}
		
		if(addShipper.getShipperLocation() != null) {
			if(addShipper.getShipperLocation().trim().length()<1) {
				createResponse.setStatus(CommonConstants.ERROR);
				createResponse.setMessage(CommonConstants.EMPTY_SHIPPER_LOCATION_ERROR);
				return createResponse;
			}
			shipperLocation = addShipper.getShipperLocation().trim();
		}
		
		shipper.setId("shipper:"+UUID.randomUUID());
		shipper.setPhoneNo(addShipper.getPhoneNo());
		shipper.setName(name);
		shipper.setCompanyName(companyName);
		shipper.setShipperLocation(shipperLocation);
		shipper.setKyc(addShipper.getKyc());
		shipper.setCompanyApproved(false);
		shipper.setAccountVerificationInProgress(false);
		shipperdao.save(shipper);
		
		createResponse.setStatus(CommonConstants.PENDING);
		createResponse.setMessage(CommonConstants.APPROVE_REQUEST);
		createResponse.setId(shipper.getId());
		createResponse.setPhoneNo(shipper.getPhoneNo());
		createResponse.setName(shipper.getName());
		createResponse.setCompanyName(shipper.getCompanyName());
		createResponse.setShipperLocation(shipper.getShipperLocation());
		createResponse.setKyc(shipper.getKyc());
		createResponse.setCompanyApproved(shipper.isCompanyApproved());
		createResponse.setAccountVerificationInProgress(shipper.isAccountVerificationInProgress());
		return createResponse;

	}

	@Override
	public List<Shipper> getShippers(Boolean companyApproved, Integer pageNo) {
		if(pageNo == null) {
			pageNo = 0;
		}
		Pageable page = PageRequest.of(pageNo, 2);
		
		if(companyApproved == null) {
			return shipperdao.findAll(page).getContent();
		}
		return shipperdao.findByCompanyApproved(companyApproved, page);
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
	public ShipperUpdateResponse updateShipper(String id, UpdateShipper updateShipper) {
		// TODO Auto-generated method stub
		ShipperUpdateResponse updateResponse = new ShipperUpdateResponse();
		Shipper shipper = new Shipper();
		Optional<Shipper> S = shipperdao.findById(id);
		if(S.isPresent()) {
			shipper = S.get();
			if (updateShipper.getPhoneNo() != null) {			
				updateResponse.setStatus(CommonConstants.ERROR);
				updateResponse.setMessage(CommonConstants.PHONE_NUMBER_UPDATE_ERROR);
				return updateResponse;
			}
			
			if (updateShipper.getName() != null) {
				if (updateShipper.getName().trim().length()<1) {
					updateResponse.setStatus(CommonConstants.ERROR);
					updateResponse.setMessage(CommonConstants.EMPTY_NAME_ERROR);
					return updateResponse;
				}
				shipper.setName(updateShipper.getName().trim());
			}
			
			if(updateShipper.getCompanyName() != null) {
				if (updateShipper.getCompanyName().trim().length()<1) {
					updateResponse.setStatus(CommonConstants.ERROR);
					updateResponse.setMessage(CommonConstants.EMPTY_COMPANY_NAME_ERROR);
					return updateResponse;
				}
				shipper.setCompanyName(updateShipper.getCompanyName().trim());
			}
			
			if(updateShipper.getShipperLocation() != null) {
				if (updateShipper.getShipperLocation().trim().length()<1) {
					updateResponse.setStatus(CommonConstants.ERROR);
					updateResponse.setMessage(CommonConstants.EMPTY_SHIPPER_LOCATION_ERROR);
					return updateResponse;
				}
				shipper.setShipperLocation(updateShipper.getShipperLocation().trim());
			}

			if (updateShipper.getKyc() != null) {
				shipper.setKyc(updateShipper.getKyc());
			}

			if(updateShipper.getCompanyApproved() != null) {
				shipper.setCompanyApproved(updateShipper.getCompanyApproved());
			}
			
			if(updateShipper.getAccountVerificationInProgress()) {
				shipper.setAccountVerificationInProgress(updateShipper.getAccountVerificationInProgress());
			}
			
			shipperdao.save(shipper);
			
			updateResponse.setId(shipper.getId());
			updateResponse.setPhoneNo(shipper.getPhoneNo());
			updateResponse.setName(shipper.getName());
			updateResponse.setCompanyName(shipper.getCompanyName());
			updateResponse.setShipperLocation(shipper.getShipperLocation());
			updateResponse.setKyc(shipper.getKyc());
			updateResponse.setCompanyApproved(shipper.isCompanyApproved());
			updateResponse.setAccountVerificationInProgress(shipper.isAccountVerificationInProgress());
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

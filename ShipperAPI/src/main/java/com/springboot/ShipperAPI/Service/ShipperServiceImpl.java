package com.springboot.ShipperAPI.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import com.springboot.ShipperAPI.Constants.CommonConstants;
import com.springboot.ShipperAPI.Dao.ShipperDao;
import com.springboot.ShipperAPI.Entity.Shipper;
import com.springboot.ShipperAPI.Model.UpdateShipper;
import com.springboot.ShipperAPI.Response.ShipperCreateResponse;
import com.springboot.ShipperAPI.Response.ShipperUpdateResponse;

import lombok.extern.slf4j.Slf4j;

import com.springboot.ShipperAPI.Exception.EntityNotFoundException;

@Slf4j
@Service
public class ShipperServiceImpl implements ShipperService {

	String temp="";

	@Autowired
	ShipperDao shipperdao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ShipperCreateResponse addShipper(Shipper shipper) {
		log.info("addShipper service is started");
		ShipperCreateResponse createResponse = new ShipperCreateResponse();

		temp=shipper.getShipperName();
		if (StringUtils.isNotBlank(temp)) {
			shipper.setShipperName(temp.trim());
		}

		temp=shipper.getCompanyName();
		if(StringUtils.isNotBlank(temp)) {
			shipper.setCompanyName(temp.trim());
		}

		temp=shipper.getShipperLocation();
		if(StringUtils.isNotBlank(temp)) {
			shipper.setShipperLocation(temp.trim());
		}

		shipper.setShipperId("shipper:"+UUID.randomUUID());
		shipper.setCompanyApproved(false);
		shipper.setAccountVerificationInProgress(false);

		shipperdao.save(shipper);
		log.info("shipper is saved to the database");

		createResponse.setStatus(CommonConstants.PENDING);
		createResponse.setMessage(CommonConstants.APPROVE_REQUEST);
		createResponse.setShipperId(shipper.getShipperId());
		createResponse.setPhoneNo(shipper.getPhoneNo());
		createResponse.setShipperName(shipper.getShipperName());
		createResponse.setCompanyName(shipper.getCompanyName());
		createResponse.setShipperLocation(shipper.getShipperLocation());
		createResponse.setKyc(shipper.getKyc());
		createResponse.setCompanyApproved(shipper.isCompanyApproved());
		createResponse.setAccountVerificationInProgress(shipper.isAccountVerificationInProgress());

		log.info("addShipper response is returned");
		return createResponse;

	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public List<Shipper> getShippers(Boolean companyApproved, Integer pageNo) { 
		log.info("getShippers service started");
		if(pageNo == null) {
			pageNo = 0;
		}
		Pageable page = PageRequest.of(pageNo, 15);

		if(companyApproved == null) {
			List<Shipper> shipperList = shipperdao.getAll(page);
			Collections.reverse(shipperList);
			return shipperList;
		} 
		List<Shipper> shipperList = shipperdao.findByCompanyApproved(companyApproved, page);
		Collections.reverse(shipperList);
		log.info("getShippers response returned");
		return shipperList;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public Shipper getOneShipper(String shipperId) {
		log.info("getOneShipper service is started");
		Optional<Shipper> S = shipperdao.findById(shipperId);
		if(S.isEmpty()) {
			throw new EntityNotFoundException(Shipper.class, "id",shipperId.toString());
		}

		log.info("getOneShiper response is returned");
		return S.get();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ShipperUpdateResponse updateShipper(String shipperId, UpdateShipper updateShipper) {
		log.info("updateShipper service is started");
		ShipperUpdateResponse updateResponse = new ShipperUpdateResponse();
		Shipper shipper = new Shipper();
		Optional<Shipper> S = shipperdao.findById(shipperId);

		if(S.isEmpty()) {
			throw new EntityNotFoundException(Shipper.class, "id",shipperId.toString());
		}


		shipper = S.get();

		//				 ????????????????????
		//		if (updateShipper.getPhoneNo() != null) {			
		//			updateResponse.setStatus(CommonConstants.ERROR);
		//			updateResponse.setMessage(CommonConstants.PHONE_NUMBER_UPDATE_ERROR);
		//			return updateResponse;
		//		}

		if (updateShipper.getShipperName() != null) {
			shipper.setShipperName(updateShipper.getShipperName().trim());
		}
		temp=updateShipper.getCompanyName();
		if(StringUtils.isNotBlank(temp)) {
			shipper.setCompanyName(temp.trim());
		}

		temp=updateShipper.getShipperLocation();
		if(StringUtils.isNotBlank(temp)) {
			shipper.setShipperLocation(temp.trim());
		}

		if (updateShipper.getKyc() != null) {
			shipper.setKyc(updateShipper.getKyc());
		}

		if(updateShipper.getCompanyApproved() != null) {
			shipper.setCompanyApproved(updateShipper.getCompanyApproved());
		}

		if(updateShipper.getAccountVerificationInProgress() != null) {
			shipper.setAccountVerificationInProgress(updateShipper.getAccountVerificationInProgress());
		}

		shipperdao.save(shipper);
		log.info("shipper is upadated and saved to the database");

		updateResponse.setShipperId(shipper.getShipperId());
		updateResponse.setPhoneNo(shipper.getPhoneNo());
		updateResponse.setShipperName(shipper.getShipperName());
		updateResponse.setCompanyName(shipper.getCompanyName());
		updateResponse.setShipperLocation(shipper.getShipperLocation());
		updateResponse.setKyc(shipper.getKyc());
		updateResponse.setCompanyApproved(shipper.isCompanyApproved());
		updateResponse.setAccountVerificationInProgress(shipper.isAccountVerificationInProgress());
		updateResponse.setStatus(CommonConstants.SUCCESS);
		updateResponse.setMessage(CommonConstants.UPDATE_SUCCESS);

		log.info("updateShipper response is returned");
		return updateResponse;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteShipper(String shipperId) {
		log.info("deleteShipper service is started");
		Optional<Shipper> S = shipperdao.findById(shipperId);

		if( S.isEmpty()) {
			throw new EntityNotFoundException(Shipper.class, "id",shipperId.toString());
		}
		shipperdao.delete(S.get());
		log.info("shipper is deleted in the database");
	}

}

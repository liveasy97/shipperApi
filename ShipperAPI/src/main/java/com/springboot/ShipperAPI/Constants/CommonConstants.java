package com.springboot.ShipperAPI.Constants;

import lombok.Data;

@Data
public class CommonConstants {
	public final String error = "Error";
	public final String nameError = "Enter name";
	public final String phoneNoError = "Enter phone number";
	public final String success = "Success";
	public final String accountExist = "Account already exist";
	public final String accountNotExist = "Account does not exist";
	public final String notFound = "Not Found";
	public final String updateSuccess = "Account updated successfully";
	public final String deleteSuccess = "Account deleted successfully";
	public final String pending = "Pending";
	public final String approveRequest = "Please wait for liveasy will approve your request";
	public final String phoneNoUpdateError = "Phone number cannot be changed";
	public final String IncorrecPhoneNoError = "Enter 10 digits phone number";
}

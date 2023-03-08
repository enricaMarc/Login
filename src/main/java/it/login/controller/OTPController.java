package it.login.controller;

import it.login.service.OTPService;
import it.login.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
	public class OTPController {

	@Autowired
	public OTPService otpService;
	
	@Autowired
	public SMSService smsService;

	@GetMapping("/generateOtp")
	public String generateOTP() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.generateOTP(username);

		String message = "Codice OTP " + otp + " per l'utente " + username;
		smsService.sendMessage(message);

		return "otppage";
	}
	
	@RequestMapping(value ="/validateOtp", method = RequestMethod.GET)
	public String validateOtp(@RequestParam("otpnum") int otpnum){
		
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			//Valida Otp
			if(otpnum >= 0){
				
			  int serverOtp = otpService.getOtp(username);
				if(serverOtp > 0){
				  if(otpnum == serverOtp){
					  otpService.clearOTP(username);

					  return "login_success";
					} 
					else {
						this.generateOTP();
						return "otppage";
					   }
				   }else {
						this.generateOTP();
						return "otppage";
				   }
				 }else {
					this.generateOTP();
					return "otppage";
				}
		  }
	}
	
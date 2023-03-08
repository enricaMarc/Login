package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AppController {

	Logger logger = Logger.getLogger(AppController.class.getName());

	@Autowired
	public OTPService otpService;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private SMSService smsService;

	@Autowired
	private SendMail emailService;

	@GetMapping("/index")
	public String viewHomePage(Model model) {
		model.addAttribute("user", new User());

		return "index";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "register_form";
	}

	@GetMapping("/login_success")
	public String showLoginSuccess() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.generateOTP(username);

		User user = userRepo.findByUsername(username);

		String notifica = user.getNotifica();
		sendNotification(notifica);

		String message = "Codice OTP " + otp + " per l'utente " + username;
		smsService.sendMessage(message);
		return "otppage";
	}

	@GetMapping("/choice")
	public String choiceSendMessage(Model model, @RequestParam(value = "notifica", required = true) String notifica) {

		//prendo l'username dell'utente loggato
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		//find per username su db
		User user = userRepo.findByUsername(username);

		notifica = notifica == null ? "sms": notifica;

		user.setNotifica(notifica);

		userRepo.save(user);

		logger.log(Level.INFO, "Modifica effettuata con successo!");

		model.addAttribute("user", new User());

		return "choice_success";
	}

    @PostMapping("/process_register")
	public String processRegister(User user, @RequestParam(value = "notifica", required = true) String notifica,
								  @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {

		//encode password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		try {

			//check codice fiscale
			checkCodiceFiscale(user);

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			user.setFile(fileName);

			notifica = notifica == null ? "sms": notifica;
			user.setNotifica(notifica);
			//save user in database
			User savedUser = userRepo.save(user);

			//upload file in directory
			if(!fileName.isEmpty()) {
				String uploadDir = "user-file/" + savedUser.getId();
				FileUploadUtil.saveFile(uploadDir, fileName, file);
			}

			logger.log(Level.INFO, "Creazione utente riuscita");

		}catch(DataIntegrityViolationException ex){
			logger.log(Level.SEVERE, "Impossibile salvare l'utente sul db");
			return "register_form";
		}
		return "register_success";
	}

	private void checkCodiceFiscale(User user) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(user.getCodiceFiscale());
		boolean matchFound = matcher.find();

		if(matchFound) {
			logger.log(Level.INFO, "Codice fiscale corretto");
		} else {
			logger.log(Level.SEVERE,"Codice fiscale non corretto");
			throw new IllegalArgumentException();
		}
	}

	private void sendNotification(String notifica) {
		switch (notifica){
			case "email":
				emailService.sendMail();
				break;
			case "sms":
				smsService.sendMessage("Accesso effettuato");
				break;
			case "slack":
				smsService.sendMessageToSlack("Accesso effettuato");
				break;
			case "whatsapp":
				smsService.sendMessageToWhatsapp();
				break;
		}
	}

}

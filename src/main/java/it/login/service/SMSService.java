package it.login.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class SMSService {

	Logger log =  Logger.getLogger(SMSService.class.getName());

	@Value("${slack.token}")
	private String slack_token;

	@Value("${slack.channelID}")
	private String slack_channelID;

	@Value("${phone.numberFrom}")
	private String numberPhoneFrom;

	@Value("${phone.numberTo}")
	private String numberPhoneTo;

	@Value("${whatsapp.phone.numberFrom}")
	private String whatsappNumberPhoneFrom;

	@Value("${twilio.username}")
	private String twilioUsername;

	@Value("${twilio.password}")
	private String twilioPassword;
	public void sendMessage(String text) {

		Twilio.init(twilioUsername,twilioPassword);

		try {
			Message.creator(new PhoneNumber(numberPhoneTo),
				new PhoneNumber(numberPhoneFrom), text).create();

			log.info("Messaggio inviato");

		}catch (Exception e){
			log.severe("Impossibile inviare messaggio");
		}
	}

	public void sendMessageToWhatsapp() {

		Twilio.init(twilioUsername,twilioPassword);

		try {
			Message message = Message.creator(
						new PhoneNumber("whatsapp:"+numberPhoneTo),
						new PhoneNumber("whatsapp:"+whatsappNumberPhoneFrom),
						"Accesso effettuato")
				.create();

		log.info("Messaggio di accesso inviato su whatsapp");

		}catch (Exception e){
			log.severe("Impossibile inviare messaggio su whatsapp");
		}

	}

	public void sendMessageToSlack(String text) {

		Slack slack = Slack.getInstance();

		try {
			ChatPostMessageResponse response = slack.methods(slack_token).chatPostMessage(req -> req
					.channel(slack_channelID)
					.text(text)
			);

			log.info("Messaggio di accesso inviato su slack");

		}catch (SlackApiException | IOException e){
			log.severe("Impossibile inviare messaggio su slack");
		}

	}
}
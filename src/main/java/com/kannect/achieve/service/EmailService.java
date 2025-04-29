package com.kannect.achieve.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kannect.achieve.dto.request.EmailRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final EmailClient emailClient;
	
	public void sendEmail(List<String> to, List<String> cc, String subject, String body) {
	    EmailRequest request = new EmailRequest(to, cc, subject, body);
	    emailClient.sendEmail(request);
	}


}

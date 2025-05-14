package com.kannect.achieve.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kannect.achieve.dto.request.EmailRequest;

@FeignClient(name = "email-service", url = "${email.service.url}")
public interface EmailClient {

    @PostMapping("/email/send")
    void sendEmail(@RequestBody EmailRequest emailRequest);
}
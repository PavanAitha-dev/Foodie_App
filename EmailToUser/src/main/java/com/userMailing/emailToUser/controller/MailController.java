package com.userMailing.emailToUser.controller;

import com.userMailing.emailToUser.mailDTO.MailDtoClass;
import com.userMailing.emailToUser.service.SendMailToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
//@Validated // Ensures that validation annotations in MailDTO are triggered
public class MailController {

    private final SendMailToUserService sendMailToUserService;

    @Autowired
    public MailController(SendMailToUserService sendMailToUserService) {
        this.sendMailToUserService = sendMailToUserService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody MailDtoClass request) {
        String subject = "Order Confirmation from " + request.getRestaurantName();

        String body = "<html><body>" + "<p>Hi " + request.getFullName() + ",</p>" +
                "<p><b>ORDER ID: " + request.getOrderId() + ".</b></p>" +
                "<p><b>Thanks for ordering from " + request.getRestaurantName() + ".</b></p>" +
                "<p>Amount: ₹" + request.getAmount() + " " +
                "<p>Enjoy your Food!</p> " +
                "<div style='text-align: left;'>" +
                "<img src='cid:image' style='width: 40%; height: auto; display: inline-block; border: 0;'/>" + "</div>" + "<div style='padding: 20px;'>" +
                "<p>For more details, contact us at: support@" + request.getRestaurantName() + ".com</p>" + "<p>Thank you for your order!</p>" + "</div>" + "</body></html>";

        sendMailToUserService.sendEmail(request.getMailId(), body, subject, true);

        return ResponseEntity.ok("Sent successfully!");
    }

}

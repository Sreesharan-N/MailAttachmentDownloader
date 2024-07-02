package com.example.MailReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private MailService mailService;
    @GetMapping("/read-emails")
    public String readEmails()
    {
        mailService.readEmails();
        return "Emails are read and message downloaded";
    }

}

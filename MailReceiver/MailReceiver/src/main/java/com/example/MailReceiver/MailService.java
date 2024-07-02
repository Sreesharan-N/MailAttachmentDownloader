package com.example.MailReceiver;


import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SentDateTerm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String  host;

    @Value("${spring.mail.properties.mail.store.protocol}")
    private String protocol;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.imap.ssl.enable}")
    private String enableSSL;

    @Value("${specific.sender.email}")
    private String specificSenderEmail;

    @Value("${email.read.status}")
    private String emailReadStatus;

    public void readEmails()
    {
        Properties props=new Properties();
        props.setProperty("mail.store.protocol",protocol);
        props.setProperty("mail.imap.ssl.enable",enableSSL);

        try{
            Session session=Session.getDefaultInstance(props,null);
            Store store=session.getStore(protocol);
            store.connect(host,port,username,password);

            Folder folder=store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            SimpleDateFormat dateFormat=new SimpleDateFormat("yy-MM-dd");
            Date start=dateFormat.parse("2024-06-30");
            Date end=dateFormat.parse("2024-07-01");

            SearchTerm dateRange=new AndTerm(
                    new SentDateTerm(ComparisonTerm.GE,start),
                    new SentDateTerm(ComparisonTerm.LE,end)
            );

            Message[] messages= folder.search(dateRange);
            for(Message message:messages)
            {
                if(isFromSpecific(message) && emailReadStatus(message))
                {
                    System.out.println("Subject: " + message.getSubject());
                    processMessage(message);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Message message) throws MessagingException, IOException {

        if(message.getContentType().contains("multipart"))
        {
            Multipart multipart=(Multipart) message.getContent();
            for(int i=0;i<multipart.getCount();i++)
            {
                BodyPart bodyPart=multipart.getBodyPart(i);
                if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))
                {
                    MimeBodyPart mimeBodyPart=(MimeBodyPart) bodyPart;
                    String fileName=mimeBodyPart.getFileName();
                    mimeBodyPart.saveFile("C:\\Users\\Sreesharan N\\OneDrive\\Desktop\\mailAttach\\"+fileName);
                }
            }
        }
    }

    private boolean emailReadStatus(Message message) throws MessagingException{
        if("unread".equals(emailReadStatus))
        {
            return !message.isSet(Flags.Flag.SEEN);
        }
        return false;
    }

    private boolean isFromSpecific(Message message) throws MessagingException {
        Address[] fromAddresses=message.getFrom();
        for(Address address:fromAddresses)
        {
            if(address instanceof InternetAddress)
            {
                InternetAddress internetAddress=(InternetAddress) address;
                if(specificSenderEmail.equalsIgnoreCase(internetAddress.getAddress()))
                {
                    return true;
                }

            }
        }
        return false;
    }
}

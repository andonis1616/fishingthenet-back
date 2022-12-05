package com.example.fishingthenet.email_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String sendTo){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("assist.test.alerts@gmail.com");
        message.setSubject("DHL Shipment Notification");
        message.setTo(sendTo);
        message.setText(" Dear Customer DHL Express informs you that your shipment 6540674221 is still waiting for your instructions .It will be delivered as soon as the costs are paid. Fees to be paid : USD 4.65 $You have a period of 48 hours to recover your package otherwise it will be returned to the sender.  Click here to view the status of your order https.//dhl.com/apps/dhltrack/?[[-Email-]]=9467957950Â    \nSupport Team DHL");

        mailSender.send(message);

    }
}

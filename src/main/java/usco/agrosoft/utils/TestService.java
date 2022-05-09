package usco.agrosoft.utils;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class TestService {

    @Autowired
    private EmailService emailService;

    public void sendEmail(String to, String toName, String subject, String body) throws UnsupportedEncodingException {
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("uscoagrosoft@gmail.com",
                        "Agrosoft"))
                .to(newArrayList(
                        new InternetAddress(to, toName)))
                .subject(subject)
                .body(body)
                .encoding("UTF-8").build();

        emailService.send(email);
    }

}

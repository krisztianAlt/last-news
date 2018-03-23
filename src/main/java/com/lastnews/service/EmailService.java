package com.lastnews.service;

import com.lastnews.model.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.context.IWebContext;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

@Service
public class EmailService {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;

    @Value("${spring.webservices.path}")
    private String APP_URL;

    private String ACTIVATION_ROUTE = "activation/";

    private String TEMPLATE_FILE = "src/main/resources/templates/email/authentication_email";

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;

    }

    public void sendMessage(User user, String key) throws MessagingException {

        TemplateEngine templateEngine = new TemplateEngine();
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);

        // Source: https://www.thymeleaf.org/doc/articles/springmail.html

        // Prepare the evaluation context
        final Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("activation_link", APP_URL + ACTIVATION_ROUTE + key);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Registration");
        message.setFrom(MESSAGE_FROM);
        message.setTo(user.getEmail());

        // Create the HTML body using Thymeleaf
        String htmlContent = templateEngine.process(TEMPLATE_FILE, context);
        message.setText(htmlContent, true); // true = isHtml

        // Send mail
        this.javaMailSender.send(mimeMessage);
    }
}
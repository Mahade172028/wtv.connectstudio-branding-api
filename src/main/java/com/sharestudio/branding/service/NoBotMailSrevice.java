package com.sharestudio.branding.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharestudio.branding.entity.HomePage;
import com.sharestudio.branding.entity.email.EmailRecipient;
import com.sharestudio.branding.entity.email.EmailRequestBody;
import com.sharestudio.branding.entity.email.MailSenderInfo;
import com.sharestudio.branding.repository.BrandingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class NoBotMailSrevice {
    private final BrandingRepository brandingRepository;
    public NoBotMailSrevice(BrandingRepository brandingRepository) {
        this.brandingRepository = brandingRepository;
    }

    @Value("${email.sender.mail}")
    private String SENDER_EMAIL;
    @Value("${email.template.id}")
    private String EMAIL_TEMPLATE_ID;
    @Value("${email.single}")
    private String SINGLE_MAIL_URL;
    @Value("${homePage.microservice.url}")
    private String HOMEPAGE_URL;

    @Value("${noboat.admin.email}")
    private String NOBOT_ADMIN;

    public ResponseEntity sendEmailToNoBotAdmin(MailSenderInfo mailSenderInfo) throws Exception {
        HomePage homePage = this.getHomepageData(mailSenderInfo.getPortalId());
        if (homePage == null)
            throw new Exception("Didn't found the homepage data");
        EmailRecipient emailRecipient = new EmailRecipient("Admin", NOBOT_ADMIN);
        emailRecipient.setEventName(homePage.getEventName());
        emailRecipient.setAdminEmail(mailSenderInfo.getEmail());
        EmailRequestBody.EmailContext context = new EmailRequestBody.EmailContext();
        context.setPortalStartDate(homePage.getDate());
        context.setPortalId(homePage.getPortalId());
        String subject = "NoBotChat Integrated to this portal";
        EmailRequestBody emailRequestBody = new EmailRequestBody(SENDER_EMAIL, subject, emailRecipient, this.EMAIL_TEMPLATE_ID);
        emailRequestBody.setContext(context);
        try {
            this.sendEmail(emailRequestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body("Mail Send Successfully!!");
    }


    public void sendEmail(EmailRequestBody emailRequestBody) throws IOException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            logger.info("MAIL SENDING WITH INFO ==>" +"SENDER MAIL:"+SENDER_EMAIL +"TEMPLATE_ID:"+EMAIL_TEMPLATE_ID);
            URI uri = URI.create(SINGLE_MAIL_URL);
            HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(emailRequestBody)))
                            .setHeader("Content-Type", "application/json")
                            .uri(uri)
                            .build();
            logger.info("Mail Service request: " + httpRequest.toString());
            HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            logger.info("MAIL SERVER STATUS CODE ==>" + response.statusCode());
        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            logger.error("SOMETHING WENT WRONG DURING SEND EMAIL WITH ->" + e.getMessage(), e);
        }
    }

    public HomePage getHomepageData(String portalId) throws IOException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        HomePage homePage = null;
        try {
            URI uri = URI.create(HOMEPAGE_URL + portalId);
            HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                            .GET()
                            .setHeader("Content-Type", "application/json")
                            .uri(uri)
                            .build();
            logger.info("Mail Service request: " + httpRequest.toString());
            HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.body() != null) {
                objectMapper.findAndRegisterModules();
                homePage = objectMapper.readValue(response.body().toString(), HomePage.class);
            }
            return homePage;
        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            logger.error("SOMETHING WENT WRONG DURING SEND EMAIL WITH ->" + e.getMessage(), e);
        }
        return homePage;
    }

}

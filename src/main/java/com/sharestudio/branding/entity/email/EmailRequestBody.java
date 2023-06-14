package com.sharestudio.branding.entity.email;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmailRequestBody {
    private String sender;
    private String templateId;
    private String subject;
    private EmailRecipient recipient;
    private EmailRequestor requestor;
    private EmailContext context;
    private List<EmailRecipient> recipientsList;
    @JsonProperty(value="isBlast")
    private Boolean isBlast;
    private String title;
    private String portalUrl;

    public EmailRequestBody(String sender, String subject, EmailRecipient recipient, String templateId){
        this.sender = sender;
        this.subject = subject;
        this.recipient = recipient;
        this.templateId = templateId;
        this.isBlast = false;
    }

    public EmailRequestBody(String sender, String subject, List<EmailRecipient> recipients, EmailRequestor requestor, String templateId){
        this.sender = sender;
        this.subject = subject;
        this.recipientsList = recipients;
        this.templateId = templateId;
        this.requestor = requestor;
        this.isBlast = true;

    }

    public static class EmailRequestor{
        private String firstName;
        private String email;

        public EmailRequestor(String firstName, String email){
            this.firstName = firstName;
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    public static class EmailContext{
        private String status;
        private String portalUrl;
        private String logo;
        private String portalId;
        private String portalStartDate;

        public String getPortalId() {
            return portalId;
        }

        public void setPortalId(String portalId) {
            this.portalId = portalId;
        }

        public String getPortalStartDate() {
            return portalStartDate;
        }

        public void setPortalStartDate(String portalStartDate) {
            this.portalStartDate = portalStartDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPortalUrl() {
            return portalUrl;
        }

        public void setPortalUrl(String portalUrl) {
            this.portalUrl = portalUrl;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public EmailRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(EmailRecipient recipient) {
        this.recipient = recipient;
    }

    public EmailRequestor getRequestor() {
        return requestor;
    }

    public void setRequestor(EmailRequestor requestor) {
        this.requestor = requestor;
    }

    public List<EmailRecipient> getRecipientsList() {
        return recipientsList;
    }

    public void setRecipientsList(List<EmailRecipient> recipientsList) {
        this.recipientsList = recipientsList;
    }

    @JsonProperty(value="isBlast")
    public Boolean getBlast() {
        return isBlast;
    }

    public void setBlast(Boolean blast) {
        isBlast = blast;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public EmailContext getContext() {
        return context;
    }

    public void setContext(EmailContext context) {
        this.context = context;
    }
}

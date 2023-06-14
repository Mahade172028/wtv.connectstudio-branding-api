package com.sharestudio.branding.entity.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRecipient {
    String firstName;
    String email;
    String password;
    String eventName;
    String adminEmail;

    public EmailRecipient(String firstName, String email){
        this.firstName = firstName;
        this.email = email;
    }
}

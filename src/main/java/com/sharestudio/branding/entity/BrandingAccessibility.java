package com.sharestudio.branding.entity;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Schema(description = "model for give portal access role")
public class BrandingAccessibility {
    @Schema(allowableValues = "Is the registration required for this portal",example = "true")
    private Boolean registrationRequired;
    @Schema(allowableValues = "Is the login required for this portal",example = "true")
    private Boolean loginRequired;
    @Schema(allowableValues = "Is the invited required for this portal",example = "true")
    private Boolean invited;
    @Schema(allowableValues = "what kind of authentication required",example = "EMAIL_ONLY")
    private AuthenticationType authType;
    private String password;
    @Schema(allowableValues = "Registration data",example = "{name,email etc...}")
    private List<RegistrationFormInput> registrationInputs;
}

package com.sharestudio.branding.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RegistrationFormInput {
    private String inputName;
    private String label;
    private String inputType;
    private Boolean required;
    private List<String> values;
}

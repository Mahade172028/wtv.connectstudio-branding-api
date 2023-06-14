package com.sharestudio.branding.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    private String message;
    private String error;
    private Branding data;
}

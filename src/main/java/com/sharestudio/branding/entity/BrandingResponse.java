package com.sharestudio.branding.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Model for branding response")
public class BrandingResponse {
    @Schema(allowableValues = "Branding id",example = "945df6sg498df4s6g9d4f")
    private String id;
    @Schema(allowableValues = "Organization id",example = "945df6sg498df45df45df")
    private String organizationId;
    @Schema(allowableValues = "Client id",example = "945df6sg498df4s84dfg")
    private String clientId;
    @Schema(allowableValues = "Name of the event",example = "Test event")
    private String eventName;
    @Schema(allowableValues = "Title of the portal",example = "Test portal")
    private String portalTitle;
    @Schema(allowableValues = "start date of the branding",example ="2022-08-22T10:54:54.965+00:00" )
    private String date;
    @Schema(allowableValues = "closing date of branding",example ="2022-08-22T10:54:54.965+00:00" )
    private String closingDate;
    private String logo;
    private String backgroundImage;
    private String backgroundColor;
    private String primaryColor;
    private String secondaryColor;
    private String font;
    private String fontColor;
    @Schema(allowableValues = "branding is published or not",example = "true")
    private boolean published;
    private boolean liveActive;
    @Schema(allowableValues = "agenda is active or not",example = "true")
    private boolean agendaActive;
    @Schema(allowableValues = "onDemand active or not",example = "true")
    private boolean ondemandActive;
    private boolean resourceActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BrandingAccessibility portalAccessRules;
    private String backgroundImageName;
    private Long backgroundImageSize;
    @Schema(allowableValues = "File name of the logo",example ="pexels-mike-104372.jpg" )
    private String logoName;
    @Schema(allowableValues = "size of the logo",example ="606423" )
    private Long logoSize;
    @Schema(allowableValues = "login page background color",example ="#FFFFFF" )
    private String loginPageBackgroundColor;
    private Boolean imageActive;
    private String portalLanguage;
    private String templateType;
    private String org_templateType;
    private String client_templateType;
    @Schema(allowableValues = "registration logo while select material template",example ="" )
    private String registrationLogo;
    @Schema(allowableValues = "registration logo size while select material template",example ="13546" )
    private Long registrationLogoSize;
    @Schema(allowableValues = "registration Logo name",example ="example.jpg" )
    private String registrationLogoName;
    private Boolean noBotChatEnable;
    private Boolean speakerActive;
    private String privacyPolicyUrl;
    private String headerBgColor;
}

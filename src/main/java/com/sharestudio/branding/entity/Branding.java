package com.sharestudio.branding.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharestudio.branding.entity.email.MailSenderInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "branding")
@Schema(description = "Model for create ,update ,find branding service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Branding {

    @Id
    @Schema(allowableValues = "Branding id auto generate by db",example = "945df6sg498df4s6g9d4f")
    private String id;
    @Schema(allowableValues = "Organization id",example = "945df6sg498df45df45df")
    private String organizationId;
    @Schema(allowableValues = "Client id",example = "945df6sg498df4s84dfg")
    private String clientId;
    @Schema(allowableValues = "logo image of the branding",example = "/path/example")
    private String logo;
    @Schema(allowableValues = "logo image of the branding background",example = "/path/example")
    private String backgroundImage;
    @Schema(allowableValues = "branding background",example = "#dedfe3")
    private String backgroundColor;
    @Schema(allowableValues = "branding primary color",example = "#dedfe3")
    private String primaryColor;
    @Schema(allowableValues = "branding secondary color",example = "#dedfe3")
    private String secondaryColor;
    @Schema(allowableValues = "font family of text",example = "Montserrat,sans-serif")
    private String font;
    @Schema(allowableValues = "font color",example = "#222222")
    private String fontColor;
    @Schema(allowableValues = "branding is published or not",example = "true")
    private Boolean published;
    @Schema(allowableValues = "branding is active or not",example = "true")
    private Boolean liveActive;
    @Schema(allowableValues = "agenda is active or not",example = "true")
    private Boolean agendaActive;
    @Schema(allowableValues = "onDemand active or not",example = "true")
    private Boolean ondemandActive;
    @Schema(allowableValues = "resource active or not",example = "true")
    private Boolean resourceActive;
    @Schema(allowableValues = "access rules of branding",example ="Object of access rule" )
    private BrandingAccessibility portalAccessRules;
    @Schema(allowableValues = "crate date of the branding",example ="2022-08-22T10:54:54.965+00:00" )
    private LocalDateTime createdAt;
    @Schema(allowableValues = "update date of the branding",example ="2022-08-22T10:54:54.965+00:00" )
    private LocalDateTime updatedAt;
    @Schema(allowableValues = "name of the image",example ="pexels-mike-104372.jpg" )
    private String backgroundImageName;
    @Schema(allowableValues = "Size of the background image",example ="131627" )
    private Long backgroundImageSize;
    @Schema(allowableValues = "File name of the logo",example ="pexels-mike-104372.jpg" )
    private String logoName;
    @Schema(allowableValues = "size of the logo",example ="606423" )
    private Long logoSize;
    @Schema(allowableValues = "login page background color",example ="#FFFFFF" )
    private String loginPageBackgroundColor;
    @Schema(allowableValues = "Image active or not",example ="true" )
    private Boolean imageActive;
    private String templateType;
    @Schema(allowableValues = "registration logo while select material template",example ="" )
    private String registrationLogo;
    @Schema(allowableValues = "registration logo size while select material template",example ="13546" )
    private Long registrationLogoSize;
    @Schema(allowableValues = "registration Logo name",example ="example.jpg" )
    private String registrationLogoName;
    private Boolean noBotChatEnable;
    private MailSenderInfo mailSenderInfo;
    private Boolean speakerActive;
    private String privacyPolicyUrl;
    private String headerBgColor;
}

package com.sharestudio.branding.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Schema(description = "model for homepage")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomePage {
    @Schema(allowableValues = "Homepage id",example = "945df6sg498df4s6g9d4f")
    private String id;
    @Schema(allowableValues = "organization id",example = "945df6sg498d8rt4fg87")
    private String organizationId;
    @Schema(allowableValues = "sub organization id",example = "945df6sg498d8rt4f8gfg")
    private String clientId;
    @Schema(allowableValues = "portal id",example = "945df6sg498d8fg8j4h8")
    private String portalId;
    @Schema(allowableValues = "Name of the event",example = "Test event")
    private String eventName;
    @Schema(allowableValues = "Title of the homepage",example = "Test event")
    private String subTitle;
    @Schema(allowableValues = "start date of the branding",example ="2022-08-22T10:54:54.965+00:00" )
    private String date;
    @Schema(allowableValues = "closing date of branding",example ="2022-08-22T10:54:54.965+00:00" )
    private String closingDate;
    @Schema(allowableValues = "Introduction of the event",example ="this is the introductory event" )
    private String eventIntroduction;
    @Schema(allowableValues = "Name of the homepage button",example ="Home" )
    private String homepageButton;
    @Schema(allowableValues = "link of the homepage button",example ="/home" )
    private String homepageButtonLink;
    private String homePageImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Schema(allowableValues = "name of the homepage image",example ="pexels-mike-104372.jpg" )
    private String homePageImageName;
    @Schema(allowableValues = "size of the image",example ="606423" )
    private Long homePageImageSize;
}

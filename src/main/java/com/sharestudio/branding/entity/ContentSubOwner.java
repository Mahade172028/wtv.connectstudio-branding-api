package com.sharestudio.branding.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Schema(description = "Model for contentSubOwner")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentSubOwner {
	 @Schema(allowableValues = "sub organization id",example = "945df6sg498df4s6g9d4f")
     private String id;
	 @Schema(allowableValues = "sub organization name",example = "Test sub org")
	 private String name;
	 @Schema(allowableValues = "sub organization Disable or not",example = "true")
	 private boolean disabled;
	 @Schema(allowableValues = "content of organization which client belong to",example = "Organization content")
	 private Object organization;
	 @Schema(allowableValues = "created date of sub organization",example ="2022-08-22T10:54:54.965+00:00" )
	 private LocalDateTime createdAt;
	 @Schema(allowableValues = "update date of sub organization",example ="2022-08-22T10:54:54.965+00:00" )
	 private LocalDateTime updatedAt;
	@Schema(allowableValues = "legacy sub organization id",example ="1559" )
	private String legacySubOrganizationId;
	@Schema(allowableValues = "Language of the portal",example ="English" )
	private String portalLanguage;
	private String templateType;
	private String org_templateType;
}

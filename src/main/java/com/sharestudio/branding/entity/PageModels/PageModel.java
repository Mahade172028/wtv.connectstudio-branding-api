package com.sharestudio.branding.entity.PageModels;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "page")
@Getter
@Setter
public class PageModel {
    @Id
    @JsonView(Views.Public.class)
    private String id;
    @JsonView(Views.Public.class)
    private String pageName;
    private String iframeUrl;
    private String dimensions;
    private String orgId;
    private String clientId;
    private String portalId;
    @JsonView(Views.Public.class)
    private Boolean active;
    @JsonView(Views.Public.class)
    private Boolean restricted;
    private String password;
    @JsonView(Views.Public.class)
    private String accessMessage;
}

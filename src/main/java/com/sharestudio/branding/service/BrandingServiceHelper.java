package com.sharestudio.branding.service;

import com.sharestudio.branding.entity.Branding;
import com.sharestudio.branding.entity.BrandingResponse;
import com.sharestudio.branding.entity.ContentSubOwner;
import com.sharestudio.branding.entity.HomePage;
import com.sharestudio.branding.repository.BrandingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandingServiceHelper {
    private final RestTemplateBuilder restTemplateBuilder;
    @Value("${homePage.microservice.url}")
    private String homePageUrl;
    @Value("${organization.microservice.url}")
    private String organizationUrl;

    public List<BrandingResponse> getAllHomePageData(List<Branding> brandingList) {

        Set<String> brandingIdList = brandingList.stream().map(x->x.getId()).collect(Collectors.toSet());
        Set<String> clientIdList = brandingList.stream().map(x->x.getClientId()).collect(Collectors.toSet());
        List<BrandingResponse> brandingResponseList = new ArrayList<>();
        try {
            HomePage[] homepageArr = restTemplateBuilder.build().getForObject(homePageUrl+ "portalIdList?portalIdList=" + String.join(",",brandingIdList), HomePage[].class);
            List<HomePage> homePageList = List.of(homepageArr);
            HashMap<String,HomePage> homePageHashMap = new HashMap<>();
            homePageList.forEach(x-> homePageHashMap.put( x.getPortalId(), x));

            ContentSubOwner[] contentSubOwnerArr =  restTemplateBuilder.build().getForObject(organizationUrl +"ByList?subOrgIdList="+ String.join(",",clientIdList), ContentSubOwner[].class);
            List<ContentSubOwner> contentSubOwnerList = List.of(contentSubOwnerArr);
            HashMap<String,ContentSubOwner> contentSubOwnerHashMap = new HashMap<>();
            contentSubOwnerList.forEach(x->contentSubOwnerHashMap.put(x.getId(),x));

            for(Branding branding : brandingList){
                BrandingResponse brandingResponse = new BrandingResponse();
                HomePage homepage = homePageHashMap.get(branding.getId());
                ContentSubOwner contentSubOwner = contentSubOwnerHashMap.get(branding.getClientId());

                if (homepage != null) {
                    if (homepage.getSubTitle() != null) {
                        brandingResponse.setPortalTitle(homepage.getSubTitle());
                    }
                    if (homepage.getEventName() != null) {
                        brandingResponse.setEventName(homepage.getEventName());
                    }
                    if (homepage.getDate() != null) {
                        brandingResponse.setDate(homepage.getDate());
                    }
                    if (homepage.getClosingDate() != null) {
                        brandingResponse.setClosingDate(homepage.getClosingDate());
                    }
                }
                if (contentSubOwner != null) {
                    if (contentSubOwner.getPortalLanguage() != null) {
                        brandingResponse.setPortalLanguage(contentSubOwner.getPortalLanguage());
                    }
                    if (contentSubOwner.getTemplateType() != null) {
                        brandingResponse.setClient_templateType(contentSubOwner.getTemplateType());
                    }
                    if (contentSubOwner.getOrg_templateType() != null) {
                        brandingResponse.setOrg_templateType(contentSubOwner.getOrg_templateType());
                    }
                }
                if (branding.getTemplateType() != null) {
                    brandingResponse.setTemplateType(branding.getTemplateType());
                }
                brandingResponse.setId(branding.getId());
                brandingResponse.setOrganizationId(branding.getOrganizationId());
                brandingResponse.setClientId(branding.getClientId());
                brandingResponse.setLogo(branding.getLogo());
                brandingResponse.setBackgroundImage(branding.getBackgroundImage());
                brandingResponse.setBackgroundColor(branding.getBackgroundColor());
                brandingResponse.setPrimaryColor(branding.getPrimaryColor());
                brandingResponse.setSecondaryColor(branding.getSecondaryColor());
                brandingResponse.setFont(branding.getFont());
                brandingResponse.setFontColor(branding.getFontColor());
                brandingResponse.setPublished(branding.getPublished());
                brandingResponse.setNoBotChatEnable(branding.getNoBotChatEnable());
                brandingResponse.setLiveActive(branding.getLiveActive());
                brandingResponse.setAgendaActive(branding.getAgendaActive());
                brandingResponse.setOndemandActive(branding.getOndemandActive());
                brandingResponse.setResourceActive(branding.getResourceActive());
                brandingResponse.setCreatedAt(branding.getCreatedAt());
                brandingResponse.setUpdatedAt(branding.getUpdatedAt());
                brandingResponse.setPortalAccessRules(branding.getPortalAccessRules());
                brandingResponse.setBackgroundImageName(branding.getBackgroundImageName());
                brandingResponse.setBackgroundImageSize(branding.getBackgroundImageSize());
                brandingResponse.setLogoName(branding.getLogoName());
                brandingResponse.setLogoSize(branding.getLogoSize());
                brandingResponse.setImageActive(branding.getImageActive());
                brandingResponse.setLoginPageBackgroundColor(branding.getLoginPageBackgroundColor());
                brandingResponse.setSpeakerActive(branding.getSpeakerActive());
                brandingResponse.setHeaderBgColor(branding.getHeaderBgColor());
                brandingResponse.setPrivacyPolicyUrl(branding.getPrivacyPolicyUrl());
                if (branding.getTemplateType() != null && branding.getTemplateType().equals("material") && branding.getRegistrationLogo() != null) {
                    brandingResponse.setRegistrationLogo(branding.getRegistrationLogo());
                    brandingResponse.setRegistrationLogoName(branding.getRegistrationLogoName());
                    brandingResponse.setRegistrationLogoSize(branding.getRegistrationLogoSize());
                }
                brandingResponseList.add(brandingResponse);
            }
            return brandingResponseList;
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG DURING FETCH FROM HOMEPAGE and ORGANIZATION MICROSERVICE WITH->{}", e.getMessage());
        }
        return brandingResponseList;
    }

    public BrandingResponse getSingleHomePageData(Branding branding) {
        BrandingResponse brandingResponse = new BrandingResponse();
        try {
            HomePage homepage = restTemplateBuilder.build().getForObject(homePageUrl + branding.getId(), HomePage.class);
            if (homepage != null) {
                if (homepage.getSubTitle() != null) {
                    brandingResponse.setPortalTitle(homepage.getSubTitle());
                }
                if (homepage.getEventName() != null) {
                    brandingResponse.setEventName(homepage.getEventName());
                }
                if (homepage.getDate() != null) {
                    brandingResponse.setDate(homepage.getDate());
                }
                if (homepage.getClosingDate() != null) {
                    brandingResponse.setClosingDate(homepage.getClosingDate());
                }
            }
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG DURING FETCH FROM HOMEPAGE MICROSERVICE WITH->{}", e.getMessage());
        }
        try {
            ContentSubOwner contentSubOwner =  restTemplateBuilder.build().getForObject(organizationUrl + branding.getClientId(), ContentSubOwner.class);
            if (contentSubOwner != null) {
                if (contentSubOwner.getPortalLanguage() != null) {
                    brandingResponse.setPortalLanguage(contentSubOwner.getPortalLanguage());
                }
                if (contentSubOwner.getTemplateType() != null) {
                    brandingResponse.setClient_templateType(contentSubOwner.getTemplateType());
                }
                if (contentSubOwner.getOrg_templateType() != null) {
                    brandingResponse.setOrg_templateType(contentSubOwner.getOrg_templateType());
                }

            }

        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG DURING FETCH FROM ORGANIZATION MICROSERVICE WITH->{}", e.getMessage());
        }
        if (branding.getTemplateType() != null) {
            brandingResponse.setTemplateType(branding.getTemplateType());
        }
        brandingResponse.setId(branding.getId());
        brandingResponse.setOrganizationId(branding.getOrganizationId());
        brandingResponse.setClientId(branding.getClientId());
        brandingResponse.setLogo(branding.getLogo());
        brandingResponse.setBackgroundImage(branding.getBackgroundImage());
        brandingResponse.setBackgroundColor(branding.getBackgroundColor());
        brandingResponse.setPrimaryColor(branding.getPrimaryColor());
        brandingResponse.setSecondaryColor(branding.getSecondaryColor());
        brandingResponse.setFont(branding.getFont());
        brandingResponse.setFontColor(branding.getFontColor());
        brandingResponse.setPublished(branding.getPublished());
        brandingResponse.setNoBotChatEnable(branding.getNoBotChatEnable());

        brandingResponse.setLiveActive(branding.getLiveActive());
        brandingResponse.setAgendaActive(branding.getAgendaActive());
        brandingResponse.setOndemandActive(branding.getOndemandActive());
        brandingResponse.setResourceActive(branding.getResourceActive());

        brandingResponse.setCreatedAt(branding.getCreatedAt());
        brandingResponse.setUpdatedAt(branding.getUpdatedAt());

        brandingResponse.setPortalAccessRules(branding.getPortalAccessRules());

        brandingResponse.setBackgroundImageName(branding.getBackgroundImageName());
        brandingResponse.setBackgroundImageSize(branding.getBackgroundImageSize());
        brandingResponse.setLogoName(branding.getLogoName());
        brandingResponse.setLogoSize(branding.getLogoSize());

        brandingResponse.setImageActive(branding.getImageActive());
        brandingResponse.setLoginPageBackgroundColor(branding.getLoginPageBackgroundColor());
        brandingResponse.setSpeakerActive(branding.getSpeakerActive());
        brandingResponse.setHeaderBgColor(branding.getHeaderBgColor());
        brandingResponse.setPrivacyPolicyUrl(branding.getPrivacyPolicyUrl());

        if (branding.getTemplateType() != null && branding.getTemplateType().equals("material") && branding.getRegistrationLogo() != null) {
            brandingResponse.setRegistrationLogo(branding.getRegistrationLogo());
            brandingResponse.setRegistrationLogoName(branding.getRegistrationLogoName());
            brandingResponse.setRegistrationLogoSize(branding.getRegistrationLogoSize());
        }

        return brandingResponse;
    }

}

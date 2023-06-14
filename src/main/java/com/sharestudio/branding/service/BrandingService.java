package com.sharestudio.branding.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharestudio.branding.entity.*;
import com.sharestudio.branding.exception.ResourceNotFoundException;
import com.sharestudio.branding.repository.BrandingRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class BrandingService {
    @Value("${file.upload.path}")
    private String uploadDir;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${attendees.microservice.url}")
    private String attendeesUrl;

    private final BrandingRepository brandingRepository;
    private final AmazonS3 amazonS3;
    private final RestTemplateBuilder restTemplateBuilder;
    private final NoBotMailSrevice noBotMailSrevice;
    private final BrandingServiceHelper brandingServiceHelper;

    public BrandingService(BrandingRepository brandingRepository, AmazonS3 amazonS3, RestTemplateBuilder restTemplateBuilder, NoBotMailSrevice noBotMailSrevice,BrandingServiceHelper brandingServiceHelper) {
        this.brandingRepository = brandingRepository;
        this.amazonS3 = amazonS3;
        this.restTemplateBuilder = restTemplateBuilder;
        this.noBotMailSrevice = noBotMailSrevice;
        this.brandingServiceHelper = brandingServiceHelper;
    }

    public Response create(String clientId, Branding branding, MultipartFile multipartFileLogo, MultipartFile multipartFileBackgroundImage, MultipartFile multipartRegistrationLogo) throws IOException {
        Response response = new Response();
        try {
            branding.setClientId(clientId);
            branding.setCreatedAt(LocalDateTime.now());
            branding.setPublished(false);
            branding.setLiveActive(true);
            branding.setAgendaActive(true);
            branding.setOndemandActive(true);
            branding.setResourceActive(true);
            Branding brandingSave = brandingRepository.save(branding);

            Branding brandingById = brandingRepository.findById(brandingSave.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Not found Branding with id = " + brandingSave.getId()));
            if (multipartFileLogo != null) {
                String folderName = brandingById.getOrganizationId() + "/" + brandingById.getClientId() + "/" + brandingById.getId() + "/portal-logo/";
                createFolder(bucketName, folderName, amazonS3);
                final File logoFile = convertMultiPartFileToFile(multipartFileLogo);
                String logo = uploadFileToS3Bucket(bucketName, logoFile, folderName, brandingById.getId(), "logoFile");
                brandingById.setLogo(uploadDir + logo);
                brandingById.setLogoName(multipartFileLogo.getOriginalFilename());
                brandingById.setLogoSize(multipartFileLogo.getSize());
            }
            if (multipartFileBackgroundImage != null) {
                String folderName = brandingById.getOrganizationId() + "/" + brandingById.getClientId() + "/" + brandingById.getId() + "/portal-backgroundImage/";
                final File backgroundImageFile = convertMultiPartFileToFile(multipartFileBackgroundImage);
                String backgroundImageUploaded = uploadFileToS3Bucket(bucketName, backgroundImageFile, folderName, brandingById.getId(), "backgroundImage");
                brandingById.setBackgroundImage(uploadDir + backgroundImageUploaded);
                brandingById.setBackgroundImageName(multipartFileBackgroundImage.getOriginalFilename());
                brandingById.setBackgroundImageSize(multipartFileBackgroundImage.getSize());
            }
            if (multipartRegistrationLogo != null) {
                String folderName = brandingById.getOrganizationId() + "/" + brandingById.getClientId() + "/" + brandingById.getId() + "/portal-registrationLogo/";
                final File registrationLogoFile = convertMultiPartFileToFile(multipartRegistrationLogo);
                String registrationLogoUploaded = uploadFileToS3Bucket(bucketName, registrationLogoFile, folderName, brandingById.getId(), "registrationLogo");
                brandingById.setRegistrationLogo(uploadDir + registrationLogoUploaded);
                brandingById.setRegistrationLogoName(multipartRegistrationLogo.getOriginalFilename());
                brandingById.setRegistrationLogoSize(multipartRegistrationLogo.getSize());
            }
            Branding brandingByIdSave = brandingRepository.save(brandingById);
            log.info("BRANDING CREATED SUCCESSFULLY ID {}", brandingByIdSave.getId());
            response.setData(brandingByIdSave);
            response.setMessage("Portal Created With Branding Successfully");
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG DURING BRANDING CREATION WITH ->" + e.getMessage(), e);
            response.setError("Something went wrong! " + e.getMessage());
        }
        return response;
    }

    public Response updateBranding(String id, Branding branding, MultipartFile multipartFileLogo, MultipartFile multipartFileBackgroundImage, MultipartFile multipartRegistrationLogo) {
        Response response = new Response();
        Branding brandingById = brandingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Branding with id = " + id));
        BrandingAccessibility previousAccessibility = null;
        Boolean noBotStatus = brandingById.getNoBotChatEnable();
        if (brandingById.getPortalAccessRules() != null) {
            previousAccessibility = brandingById.getPortalAccessRules();
        }
        try {
            if (branding.getOrganizationId() != null) {
                brandingById.setOrganizationId(branding.getOrganizationId());
            }

            if (branding.getClientId() != null) {
                brandingById.setClientId(branding.getClientId());
            }

            if (branding.getBackgroundColor() != null) {
                brandingById.setBackgroundColor(branding.getBackgroundColor());
            }

            if (branding.getPrimaryColor() != null) {
                brandingById.setPrimaryColor(branding.getPrimaryColor());
            }

            if (branding.getSecondaryColor() != null) {
                brandingById.setSecondaryColor(branding.getSecondaryColor());
            }

            if (branding.getFont() != null) {
                brandingById.setFont(branding.getFont());
            }

            if (branding.getFontColor() != null) {
                brandingById.setFontColor(branding.getFontColor());
            }

            if (branding.getPortalAccessRules() != null) {
                brandingById.setPortalAccessRules(branding.getPortalAccessRules());
            }

            if (branding.getPublished() != null) {
                brandingById.setPublished(branding.getPublished());
            }

            if (branding.getTemplateType() != null) {
                brandingById.setTemplateType(branding.getTemplateType());
            }

            if(branding.getSpeakerActive() != null){
                brandingById.setSpeakerActive(branding.getSpeakerActive());
            }
            if(branding.getHeaderBgColor() != null){
                brandingById.setHeaderBgColor(branding.getHeaderBgColor());
            }

            if (multipartFileLogo != null) {
                String logoName = "";
                if (brandingById.getLogo() != null) {
                    logoName = getImageName(brandingById.getLogo());
                }
                if (logoName != null) {
                    String folderName = brandingById.getOrganizationId() + "/" + brandingById.getClientId() + "/" + brandingById.getId() + "/portal-logo/";
                    String previousImage = folderName + logoName;
                    amazonS3.deleteObject(bucketName, previousImage);
                    final File logoFile = convertMultiPartFileToFile(multipartFileLogo);
                    String logo = uploadFileToS3Bucket(bucketName, logoFile, folderName, brandingById.getId(), "logoFile");
                    brandingById.setLogo(uploadDir + logo);
                    brandingById.setLogoName(multipartFileLogo.getOriginalFilename());
                    brandingById.setLogoSize(multipartFileLogo.getSize());
                }
            }

            if (multipartFileBackgroundImage != null) {
                String backgroundImageName = "";
                if (brandingById.getBackgroundImage() != null) {
                    backgroundImageName = getImageName(brandingById.getBackgroundImage());
                }

                if (backgroundImageName != null) {
                    String folderName = brandingById.getOrganizationId() + "/" + brandingById.getClientId() + "/" + brandingById.getId() + "/portal-backgroundImage/";
                    String previousImage = folderName + backgroundImageName;
                    amazonS3.deleteObject(bucketName, previousImage);
                    final File backgroundImageFile = convertMultiPartFileToFile(multipartFileBackgroundImage);
                    String backgroundImageUploaded = uploadFileToS3Bucket(bucketName, backgroundImageFile, folderName, brandingById.getId(), "backgroundImage");
                    brandingById.setBackgroundImage(uploadDir + backgroundImageUploaded);
                    brandingById.setBackgroundImageName(multipartFileBackgroundImage.getOriginalFilename());
                    brandingById.setBackgroundImageSize(multipartFileBackgroundImage.getSize());
                }
            }

            if (multipartRegistrationLogo != null) {
                String registrationLogoName = "";
                if (brandingById.getRegistrationLogo() != null) {
                    registrationLogoName = getImageName(brandingById.getRegistrationLogo());
                }

                if (registrationLogoName != null) {
                    String folderName = brandingById.getOrganizationId() + "/" + brandingById.getClientId() + "/" + brandingById.getId() + "/portal-registrationLogo/";
                    String previousImage = folderName + registrationLogoName;
                    amazonS3.deleteObject(bucketName, previousImage);
                    final File registrationLogoFile = convertMultiPartFileToFile(multipartRegistrationLogo);
                    String registrationLogoUploaded = uploadFileToS3Bucket(bucketName, registrationLogoFile, folderName, brandingById.getId(), "registrationLogo");
                    brandingById.setRegistrationLogo(uploadDir + registrationLogoUploaded);
                    brandingById.setRegistrationLogoName(multipartRegistrationLogo.getOriginalFilename());
                    brandingById.setRegistrationLogoSize(multipartRegistrationLogo.getSize());
                }
            }


            if (branding.getLiveActive() != null) {
                brandingById.setLiveActive(branding.getLiveActive());
            }

            if (branding.getAgendaActive() != null) {
                brandingById.setAgendaActive(branding.getAgendaActive());
            }

            if (branding.getOndemandActive() != null) {
                brandingById.setOndemandActive(branding.getOndemandActive());
            }

            if (branding.getResourceActive() != null) {
                brandingById.setResourceActive(branding.getResourceActive());
            }
            if(branding.getNoBotChatEnable() != null){
                brandingById.setNoBotChatEnable(branding.getNoBotChatEnable());
            }

            if (branding.getLoginPageBackgroundColor() != null) {
                brandingById.setLoginPageBackgroundColor(branding.getLoginPageBackgroundColor());
            }

            if (branding.getImageActive() != null) {
                brandingById.setImageActive(branding.getImageActive());
            }

            if(branding.getPrivacyPolicyUrl() != null){
                brandingById.setPrivacyPolicyUrl(branding.getPrivacyPolicyUrl());
            }

            brandingById.setUpdatedAt(LocalDateTime.now());

            Branding brandingUpdate = this.brandingRepository.save(brandingById);

            log.info("portal updated successfully with id: {}" , brandingUpdate.getId());
            if (previousAccessibility != null &&
                    (previousAccessibility.getAuthType() == AuthenticationType.PASSWORD_ONLY || previousAccessibility.getAuthType() == AuthenticationType.PASSWORD_ONLY_WITHOUT_REGISTRATION) &&
                    (brandingUpdate.getPortalAccessRules().getAuthType() != AuthenticationType.PASSWORD_ONLY && brandingUpdate.getPortalAccessRules().getAuthType() != AuthenticationType.PASSWORD_ONLY_WITHOUT_REGISTRATION)) {
                this.deleteSinglePassword(brandingUpdate.getId());
            }
            if (previousAccessibility != null && checkAccessRuleForRemovePreviousPortalUser(previousAccessibility, brandingUpdate.getPortalAccessRules())) {
                this.deleteAllPortalUser(brandingUpdate.getId());
            }
            if (brandingUpdate.getPortalAccessRules() != null &&
                    (brandingUpdate.getPortalAccessRules().getAuthType() == AuthenticationType.PASSWORD_ONLY || brandingUpdate.getPortalAccessRules().getAuthType() == AuthenticationType.PASSWORD_ONLY_WITHOUT_REGISTRATION )) {
                String password = getPortalLoginPassword(brandingUpdate.getId());
                BrandingAccessibility brandingAccessibility = brandingUpdate.getPortalAccessRules();
                brandingAccessibility.setPassword(password);
                brandingUpdate.setPortalAccessRules(brandingAccessibility);
            }
            if (brandingUpdate.getNoBotChatEnable() != null && Boolean.TRUE.equals(brandingUpdate.getNoBotChatEnable())) {
                noBotMailSendingCondition(noBotStatus, branding);
            }
            response.setData(brandingUpdate);
            response.setMessage("Portal Updated With Branding Successfully");
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG WITH ->" + e.getMessage(), e);
            response.setError("Something went wrong! " + e.getMessage());
        }
        return response;
    }
    public ResponseEntity<BrandingResponse> getBrandingById(String id) {
        Branding branding = brandingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Branding with id = " + id));
        if (branding.getPortalAccessRules() != null && (branding.getPortalAccessRules().getAuthType() == AuthenticationType.PASSWORD_ONLY || branding.getPortalAccessRules().getAuthType() == AuthenticationType.PASSWORD_ONLY_WITHOUT_REGISTRATION )) {
            String password = getPortalLoginPassword(branding.getId());
            if (password == null) log.warn("Found password null for this portal {}" , branding.getId());
            BrandingAccessibility brandingAccessibility = branding.getPortalAccessRules();
            brandingAccessibility.setPassword(password);
            branding.setPortalAccessRules(brandingAccessibility);
        }
        return new ResponseEntity<>(this.brandingServiceHelper.getSingleHomePageData(branding), HttpStatus.OK);
    }

    public Page<BrandingResponse> getAllPortalByClient(String clientId, Integer pageNo, Integer pageSize, String status) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Branding> brandingList;
        if (status.equalsIgnoreCase("publish")) {
            brandingList = brandingRepository.findByClientIdAndPublishedTrue(clientId, true, paging);
            log.debug("FOUND NUMBER OF BRANDING WITH PUBLISH AND CLIENT ID {} :{}", clientId , brandingList.getTotalElements());
        } else if (status.equalsIgnoreCase("unpublish")) {
            brandingList = brandingRepository.findByClientIdAndPublishedFalse(clientId, false, paging);
            log.debug("FOUND NUMBER OF BRANDING WITH UNPUBLISH AND CLIENT ID {} :{}", clientId , brandingList.getTotalElements());
        } else {
            brandingList = brandingRepository.findByClientId(clientId, paging);
            log.debug("FOUND NUMBER OF BRANDING WITH CLIENT ID {} :{}" , clientId , brandingList.getTotalElements());
        }
        return getHomePageData(paging, brandingList.getContent(), brandingList.getTotalElements());
    }

    public Integer getTotalPortalByOrganization(String organizationId, String status) {
        if (status.equalsIgnoreCase("publish")) {
            List<Branding> brandingList = brandingRepository.findByOrganizationIdAndPublishedTrue(organizationId, true);
            return brandingList.size();
        } else if (status.equalsIgnoreCase("unpublish")) {
            List<Branding> brandingList = brandingRepository.findByOrganizationIdAndPublishedFalse(organizationId, false);
            return brandingList.size();
        } else {
            List<Branding> brandingList = brandingRepository.findByOrganizationId(organizationId);
            return brandingList.size();
        }
    }

    public Page<BrandingResponse> getAllPortal(Integer pageNo, Integer pageSize, String status) {
        Page<BrandingResponse> homePageDataList = null;
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
            Page<Branding> brandingList;
            if (status.equalsIgnoreCase("publish")) {
                brandingList = brandingRepository.findAllByPublishedTrue(true, paging);
            } else if (status.equalsIgnoreCase("unpublish")) {
                brandingList = brandingRepository.findAllByPublishedFalse(false, paging);
            } else {
                brandingList = brandingRepository.findAll(paging);
            }
            homePageDataList = getHomePageData(paging, brandingList.getContent(), brandingList.getTotalElements());
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG DURING GET PORTAL WITH STATUS " + status + "WITH ->" + e.getMessage(), e);
        }
        return homePageDataList;
    }

    public Page<BrandingResponse> getAllPortalByOrganizationId(String organizationId, Integer pageNo, Integer pageSize, String status) {
        Page<BrandingResponse> homePageDataList = null;
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
            Page<Branding> brandingList;
            if (status.equalsIgnoreCase("publish")) {
                brandingList = brandingRepository.findByOrganizationIdAndPublishedTrue(organizationId, true, paging);
            } else if (status.equalsIgnoreCase("unpublish")) {
                brandingList = brandingRepository.findByOrganizationIdAndPublishedFalse(organizationId, false, paging);
            } else {
                brandingList = brandingRepository.findByOrganizationId(organizationId, paging);
            }
            homePageDataList = getHomePageData(paging, brandingList.getContent(), brandingList.getTotalElements());
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG DURING GET PORTAL WITH STATUS " + status + "Organization Id" + organizationId + "WITH ->" + e.getMessage(), e);
        }
        return homePageDataList;
    }

    public Integer getTotalPortalCount(String status) {
        List<Branding> brandingList = new ArrayList<>();
        try {
            if (status.equalsIgnoreCase("publish")) {
                brandingList = brandingRepository.findAllByPublishedTrue(true);
            } else if (status.equalsIgnoreCase("unpublish")) {
                brandingList = brandingRepository.findAllByPublishedFalse(false);
            } else {
                brandingList = brandingRepository.findAll();
            }
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG WITH->" + e.getMessage(), e);
        }
        return brandingList.size();
    }

    public Integer getTotalPortalCountByClientId(String clientId, String status) {
        List<Branding> brandingList = new ArrayList<>();
        try {
            if (status.equalsIgnoreCase("publish")) {
                brandingList = brandingRepository.findAllByClientIdAndPublishedTrue(clientId, true);
            } else if (status.equalsIgnoreCase("unpublish")) {
                brandingList = brandingRepository.findAllByClientIdAndPublishedFalse(clientId, false);
            } else {
                brandingList = brandingRepository.findAllByClientId(clientId);
            }
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG WITH->" + e.getMessage(), e);
        }
        return brandingList.size();
    }

    //find all branding by organization id list
    public Page<BrandingResponse> getAllPortalByOrganizationIdList(List<String> organizationIdList, Integer pageNo, Integer pageSize, String status) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Branding> brandingList = null;

        if (status.equalsIgnoreCase("publish")) {
                brandingList = this.brandingRepository.findAllByOrganizationIdListWithStatus(organizationIdList, true, paging);
        } else if (status.equalsIgnoreCase("unpublish")) {
            brandingList = this.brandingRepository.findAllByOrganizationIdListWithStatus(organizationIdList, false , paging);
        } else {
            brandingList = this.brandingRepository.findAllByOrganizationIdList(organizationIdList , paging);
        }
        return getHomePageData(paging, brandingList.getContent(), brandingList.getTotalElements());
    }

    public Page<BrandingResponse> getAllPortalByClientIdList(String[] clientIdList, Integer pageNo, Integer pageSize, String status) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Branding> brandingList = Page.empty();

        if (status.equalsIgnoreCase("publish")) {
            brandingList = this.brandingRepository.findAllByClientIdListAndPublishedStatus(clientIdList,true ,paging);
        } else if (status.equalsIgnoreCase("unpublish")) {
            brandingList = this.brandingRepository.findAllByClientIdListAndPublishedStatus(clientIdList,false ,paging);
        } else {
            brandingList = this.brandingRepository.findAllByClientIdList(clientIdList,paging);
        }
        return getHomePageData(paging, brandingList.getContent(), brandingList.getTotalElements());
    }
    public Page<BrandingResponse> getPortalListByIdList(String[] portalIdList, Integer pageNo, Integer pageSize, String status) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Branding> brandingList = Page.empty();
        if (status.equalsIgnoreCase("publish")) {
            brandingList = this.brandingRepository.findAllByBrandingIdListAndPublishedStatus(portalIdList,true,paging);
        } else if (status.equalsIgnoreCase("unpublish")) {
            brandingList = this.brandingRepository.findAllByBrandingIdListAndPublishedStatus(portalIdList,false,paging);
        } else {
            brandingList = this.brandingRepository.findAllByBrandingIdList(portalIdList,paging);
        }
        return getHomePageData(paging, brandingList.getContent(), brandingList.getTotalElements());
    }

    // custom functions
    public static void createFolder(final String bucketName, String folderName, AmazonS3 client) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, emptyContent, metadata);
        client.putObject(putObjectRequest);
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            log.warn("SOMETHING WENT WRONG DURING MULTIPART FILE TO FILE CONVERSION WITH ->" + ex.getMessage(), ex);
        }
        return file;
    }

    private String uploadFileToS3Bucket(final String bucketName, final File file, String folderName, String brandingId, String type) {
        String uniqueFileName = null;
        try {
            if (type.equalsIgnoreCase("logoFile")) {
                uniqueFileName = folderName + "branding_logo_" + brandingId + "_" + getUniqueFileName(file.getName());
            } else if (type.equalsIgnoreCase("backgroundImage")) {
                uniqueFileName = folderName + "branding_background_image_" + brandingId + "_" + file.getName();
            } else {
                uniqueFileName = folderName + "branding_registration_logo_" + brandingId + "_" + file.getName();
            }
            uniqueFileName = uniqueFileName.replaceAll("\\s+", "_").trim();
            final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            log.warn("Something went wrong during upload file to the s3bucket with-> {}",e.getMessage());
        }
        return uniqueFileName;
    }

    private String getUniqueFileName(String name) {
        String[] fileName = name.split("\\.");
        String suffixName = fileName[0];
        String extensionName = fileName[1];
        return suffixName + "_" + System.currentTimeMillis() + "." + extensionName;
    }

    private String getImageName(String imageUrl) {
        String[] parts = imageUrl.split("/", 8);
        return parts[7];
    }

    private Page<BrandingResponse> getHomePageData(Pageable paging, List<Branding> brandingList, long totalElements) {
        List<BrandingResponse> brandingResponseList = new ArrayList<>();
        brandingResponseList = this.brandingServiceHelper.getAllHomePageData(brandingList);
        return new PageImpl<>(brandingResponseList, paging, totalElements);
    }


    private String getPortalLoginPassword(String portalId) {
        String password = null;
        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(attendeesUrl + "/password-only-info/" + portalId))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Attendees Api call finish and the status code is => {}" ,response.statusCode());
            if (response.statusCode() == 200 && response.body() != null) {
                ObjectMapper oMapper = new ObjectMapper();
                HashMap<String, Object> responseData = oMapper.readValue(response.body(), new TypeReference<HashMap<String, Object>>() {
                });
                password = (String) responseData.get("password");
            }
        } catch (Exception e) {
            log.error("Fail to fetch password from attendees for single password with -> {}" , e.getMessage());
        }
        return password;
    }

    private void deleteSinglePassword(String portalId) {
        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(attendeesUrl + "/password-only/" + portalId))
                    .DELETE()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Attendees Api call finish and the status code is => {}", response.statusCode());
            if (response.statusCode() == 200 && response.body() != null) {
                log.info("deleted the single password successfully");
            } else {
                log.warn("Fail to delete single password info or not found");
            }
        } catch (Exception e) {
            log.error("Fail to delete single password to attendees -> {}", e.getMessage());
        }
    }

    private void deleteAllPortalUser(String portalId) {
        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(attendeesUrl + "/delete-all-portal-users/" + portalId))
                    .DELETE()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Attendees Api call finish and the status code is => {}", response.statusCode());
            if (response.statusCode() == 200 && response.body() != null) {
                log.info("deleted all portal user successfully");
            } else {
                log.warn("Fail to delete portal users or not found");
            }
        } catch (Exception e) {
            log.error("Fail to delete portal users to attendees -> {}", e.getMessage());
        }
    }


    private boolean checkAccessRuleForRemovePreviousPortalUser(BrandingAccessibility previousRule, BrandingAccessibility currentRule) {

        if (Boolean.FALSE.equals(previousRule.getInvited()) && Boolean.TRUE.equals(currentRule.getInvited()))
            return true;

        if (Boolean.TRUE.equals(previousRule.getInvited()) && Boolean.FALSE.equals(currentRule.getInvited()))
            return true;
        if((previousRule.getAuthType() == AuthenticationType.PASSWORD_ONLY || previousRule.getAuthType() == AuthenticationType.PASSWORD_ONLY_WITHOUT_REGISTRATION) && currentRule.getAuthType() == AuthenticationType.EMAIL_AND_PASSWORD)
            return true;

        return previousRule.getAuthType() == AuthenticationType.EMAIL_ONLY && currentRule.getAuthType() == AuthenticationType.EMAIL_AND_PASSWORD;
    }

    private void noBotMailSendingCondition(Boolean noBotStatus, Branding branding) {
        if (Boolean.TRUE.equals(branding.getNoBotChatEnable()) && branding.getMailSenderInfo() == null) {
            log.error("Missing the noBoatChat necessary information");
        }
        if (Boolean.FALSE.equals(noBotStatus) && Boolean.TRUE.equals(branding.getNoBotChatEnable()) && branding.getMailSenderInfo() != null) {
            try {
                noBotMailSrevice.sendEmailToNoBotAdmin(branding.getMailSenderInfo());
            } catch (Exception e) {
                log.error("Fail to send mail => {}", e.getMessage());
            }
        }
    }
}

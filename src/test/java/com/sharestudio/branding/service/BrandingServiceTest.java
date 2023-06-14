package com.sharestudio.branding.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import com.sharestudio.branding.entity.Branding;
import com.sharestudio.branding.entity.BrandingResponse;
import com.sharestudio.branding.entity.Response;
import com.sharestudio.branding.repository.BrandingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BrandingServiceTest {

    @Mock
    private AmazonS3 amazonS3;


    @Mock
    BrandingRepository brandingRepository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @InjectMocks
    BrandingService brandingService;

    Branding branding;
    List<Branding> brandingList;

    Integer pageNo=0;
    Integer pageSize=5;

    Page<Branding> brandingPage;
    Pageable pageable;

    @BeforeEach
    void setUp(){
        branding = new Branding();
        branding.setClientId("9754dsf45sdf5sdf44");
        branding.setAgendaActive(false);
        branding.setId("9744df54sdf4f5sdf45");
        branding.setLiveActive(true);
        branding.setPublished(true);
        branding.setOndemandActive(true);
        branding.setResourceActive(true);
        branding.setOrganizationId("9754554sdf45sdfsafg45");

        Branding branding_1 = new Branding();
        branding_1.setClientId("9754dsf45sdf5ds5f4s4d");
        branding_1.setAgendaActive(true);
        branding_1.setId("9744df54sdf4f54d5f4");
        branding_1.setLiveActive(true);
        branding_1.setPublished(true);
        branding_1.setOndemandActive(true);
        branding_1.setResourceActive(true);
        branding_1.setOrganizationId("9754554sdf45sdf5f4sd");

        Branding branding_2 = new Branding();
        branding_2.setClientId("9754dsf45sdf5ds5ffg54");
        branding_2.setAgendaActive(false);
        branding_2.setId("9744df54sdf4f54dfg45");
        branding_2.setLiveActive(false);
        branding_2.setPublished(false);
        branding_2.setOndemandActive(true);
        branding_2.setResourceActive(true);
        branding_2.setOrganizationId("9754554sdf45sdf5ffgh44");

        brandingList = List.of(branding,branding_1,branding_2);

        pageable = PageRequest.of(pageNo,pageSize, Sort.by("createdAt").descending());

        brandingPage = new PageImpl<>(brandingList,pageable,brandingList.size());

    }

    @Test
    void getBrandingById(){
        given(brandingRepository.findById(any())).willReturn(Optional.of(branding));
        ResponseEntity response = brandingService.getBrandingById(branding.getId());

        assertNotNull(response);
        //assertEquals(((Branding)response.getBody()).getId(),branding.getId());

    }

    @Test
    void create() throws IOException {
        given(brandingRepository.save(any())).willReturn(branding);
        Response response = brandingService.create(branding.getClientId(),branding,null,null , null);
        assertNotNull(response);
    }

    @Test
    void updateBranding() throws IOException {
        given(brandingRepository.findById(any())).willReturn(Optional.of(branding));
        given(brandingRepository.save(any())).willReturn(branding);
        Response response = brandingService.updateBranding(branding.getId(),branding,null,null, null);
        assertNotNull(response);
    }

    @RepeatedTest(2)
    void getAllPortalByClient(RepetitionInfo rep){
        String status="";
        if(rep.getCurrentRepetition()==1){
            status="publish";
            given(brandingRepository.findByClientIdAndPublishedTrue(branding.getClientId(),true,pageable)).willReturn(brandingPage);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortalByClient(branding.getClientId(),pageNo,pageSize,status);
            assertNotNull(brandingResponses);
            assertTrue(brandingResponses.getContent().get(0).isPublished());
        }
        if(rep.getCurrentRepetition()==2){
            status="unpublish";
            given(brandingRepository.findByClientIdAndPublishedFalse(branding.getClientId(),false,pageable)).willReturn(brandingPage);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortalByClient(branding.getClientId(),pageNo,pageSize,status);
            assertNotNull(brandingResponses);
        }

    }

    @Test
    void getTotalPortalByOrganization(){
        String status="publish";
        given(brandingRepository.findByOrganizationIdAndPublishedTrue(branding.getOrganizationId(),true)).willReturn(brandingList);
        Integer responseSize = brandingService.getTotalPortalByOrganization(branding.getOrganizationId(),status);
        assertEquals(responseSize,brandingList.size());

    }

    @RepeatedTest(2)
    void getAllPortal(RepetitionInfo rep){
        String status="";
        if(rep.getCurrentRepetition()==1){
            status="publish";
            given(brandingRepository.findAllByPublishedTrue(true,pageable)).willReturn(brandingPage);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortal(pageNo,pageSize,status);
            assertNotNull(brandingResponses);
            assertTrue(brandingResponses.getContent().get(0).isPublished());
        }
        if(rep.getCurrentRepetition()==2){
            status="unpublish";
            given(brandingRepository.findAllByPublishedFalse(false,pageable)).willReturn(brandingPage);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortal(pageNo,pageSize,status);
            assertNotNull(brandingResponses);
        }

    }

    @RepeatedTest(2)
    void getAllPortalByOrganizationId(RepetitionInfo rep){
        String status="";
        if(rep.getCurrentRepetition()==1){
            status="publish";
            given(brandingRepository.findByOrganizationIdAndPublishedTrue(branding.getOrganizationId(),true,pageable)).willReturn(brandingPage);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortalByOrganizationId(branding.getOrganizationId(),pageNo,pageSize,status);
            assertNotNull(brandingResponses);
            assertTrue(brandingResponses.getContent().get(0).isPublished());
        }
        if(rep.getCurrentRepetition()==2){
            status="unpublish";
            given(brandingRepository.findByOrganizationIdAndPublishedFalse(branding.getOrganizationId(),false,pageable)).willReturn(brandingPage);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortalByOrganizationId(branding.getOrganizationId(),pageNo,pageSize,status);
            assertNotNull(brandingResponses);
        }

    }

    @Test
    void getTotalPortalCount(){
        String status="publish";
        given(brandingRepository.findAllByPublishedTrue(true)).willReturn(brandingList);
        Integer responseSize = brandingService.getTotalPortalCount(status);
        assertEquals(responseSize,brandingList.size());

    }

    @Test
    void getTotalPortalCountByClientId(){
        String status="publish";
        given(brandingRepository.findAllByClientIdAndPublishedTrue(branding.getClientId(),true)).willReturn(brandingList);
        Integer responseSize = brandingService.getTotalPortalCountByClientId(branding.getClientId(),status);
        assertEquals(responseSize,brandingList.size());

    }

    @RepeatedTest(2)
    void getAllPortalByClientIdList(RepetitionInfo rep){
        String status="";
        if(rep.getCurrentRepetition()==1){
            status="publish";
            String [] clientList = {branding.getClientId()};
            given(brandingRepository.findAllByClientIdAndPublishedTrue(branding.getClientId(),true)).willReturn(brandingList);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortalByClientIdList(clientList,pageNo,pageSize,status);
            assertNotNull(brandingResponses);
            assertTrue(brandingResponses.getContent().get(0).isPublished());
        }
        if(rep.getCurrentRepetition()==2){
            String [] clientList = {branding.getClientId()};
            status="unpublish";
            given(brandingRepository.findAllByClientIdAndPublishedFalse(branding.getClientId(),false)).willReturn(brandingList);
            Page<BrandingResponse> brandingResponses = brandingService.getAllPortalByClientIdList(clientList,pageNo,pageSize,status);
            assertNotNull(brandingResponses);
        }

    }




    public MockMultipartFile createMultipart(String filePath) throws Exception {

        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("logo",
                file.getName(), "image/png", IOUtils.toByteArray(fis));

        return multipartFile;
    }




}
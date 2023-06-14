package com.sharestudio.branding.controller;

import com.sharestudio.branding.entity.Branding;
import com.sharestudio.branding.entity.BrandingResponse;
import com.sharestudio.branding.service.BrandingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandingController.class)
class BrandingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BrandingService brandingService;
    List<BrandingResponse> brandingList;

    Integer pageNo = 0;
    Integer pageSize = 5;

    Page<BrandingResponse> brandingPage;
    Pageable pageable;
    BrandingResponse brandingResponse;

    @BeforeEach
    void setUp() {
        brandingResponse = new BrandingResponse();
        brandingResponse.setClientId("9754dsf45sdf5ds5f4s4d");
        brandingResponse.setAgendaActive(true);
        brandingResponse.setId("9744df54sdf4f54d5f4");
        brandingResponse.setLiveActive(true);
        brandingResponse.setPublished(true);
        brandingResponse.setOndemandActive(true);
        brandingResponse.setResourceActive(true);
        brandingResponse.setOrganizationId("9754554sdf45sdf5f4sd");

        BrandingResponse brandingResponse1 = new BrandingResponse();
        brandingResponse1.setClientId("9754dsf45sdf5ds5ffg54");
        brandingResponse1.setAgendaActive(false);
        brandingResponse1.setId("9744df54sdf4f54dfg45");
        brandingResponse1.setLiveActive(false);
        brandingResponse1.setPublished(false);
        brandingResponse1.setOndemandActive(true);
        brandingResponse1.setResourceActive(true);
        brandingResponse1.setOrganizationId("9754554sdf45sdf5ffgh44");

        brandingList = List.of(brandingResponse, brandingResponse1);

        pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());

        brandingPage = new PageImpl<>(brandingList, pageable, brandingList.size());

    }

    @Test
    void getBrandingById() throws Exception {
        BrandingResponse branding;
        branding = new BrandingResponse();
        branding.setClientId("9754dsf45sdf5sdf44");
        branding.setAgendaActive(false);
        branding.setId("9744df54sdf4f5sdf45");
        branding.setLiveActive(true);
        branding.setPublished(true);
        branding.setOndemandActive(true);
        branding.setResourceActive(true);
        branding.setOrganizationId("9754554sdf45sdfsafg45");
        given(brandingService.getBrandingById(any())).willReturn(new ResponseEntity<>(branding, HttpStatus.OK));

        mockMvc.perform(get("/api/v1/branding/" + branding.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(branding.getId())))
                .andReturn();
    }

    @Test
    void getAllPortalByClient() throws Exception {
        given(brandingService.getAllPortalByClient(any(), any(), any(), any())).willReturn(brandingPage);

        mockMvc.perform(get("/api/v1/branding/client/" + brandingResponse.getClientId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(brandingResponse.getId())))
                .andReturn();
    }

    @Test
    void getAllPortal() throws Exception {
        given(brandingService.getAllPortal(any(), any(), any())).willReturn(brandingPage);

        mockMvc.perform(get("/api/v1/branding")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(brandingResponse.getId())))
                .andReturn();
    }

    @Test
    void getTotalPortalByOrganization() throws Exception {
        given(brandingService.getTotalPortalByOrganization(any(), any())).willReturn(brandingList.size());

        mockMvc.perform(get("/api/v1/branding/organization/" + brandingResponse.getOrganizationId() + "/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getAllPortalByOrganizationId() throws Exception {
        given(brandingService.getAllPortalByOrganizationId(any(), any(), any(), any())).willReturn(brandingPage);

        mockMvc.perform(get("/api/v1/branding/organization/" + brandingResponse.getOrganizationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(brandingResponse.getId())))
                .andReturn();
    }


    @Test
    void getAllPortalByClientIdList() throws Exception {

        String[] clientList = {brandingResponse.getClientId()};
        given(brandingService.getAllPortalByClientIdList(any(), any(), any(), any())).willReturn(brandingPage);

        mockMvc.perform(get("/api/v1/branding/clientIdList")
                        .param("clientIdList", clientList)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(brandingResponse.getId())))
                .andReturn();
    }


    @Test
    void getTotalPortalCount() throws Exception {
        given(brandingService.getTotalPortalCount(any())).willReturn(brandingList.size());

        mockMvc.perform(get("/api/v1/branding/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getTotalPortalCountByClientId() throws Exception {
        given(brandingService.getTotalPortalCountByClientId(any(), any())).willReturn(brandingList.size());

        mockMvc.perform(get("/api/v1/branding/client/" + brandingResponse.getClientId() + "/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }


}
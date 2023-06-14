package com.sharestudio.branding.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharestudio.branding.entity.Branding;
import com.sharestudio.branding.entity.BrandingResponse;
import com.sharestudio.branding.entity.Response;
import com.sharestudio.branding.service.BrandingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@Tag(name = "Branding service",description = "Service for create ,update , delete , get branding")
public class BrandingController {

    private final BrandingService brandingService;

    public BrandingController(BrandingService brandingService) {
        this.brandingService = brandingService;
    }
    @Operation(summary = "Create Branding", description = "Endpoint for create branding", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @PostMapping("/branding/{clientId}")
    public Response createBranding(
            @Parameter(description = "Client Id for create branding under it")
            @PathVariable("clientId") String clientId,
            @Parameter(description = "Create date of branding")
            @RequestParam(value = "data") String data,
            @Parameter(description = "image file of the branding logo")
            @RequestParam(value = "logoFile") MultipartFile logoFile,
            @Parameter(description = "image file for branding background logo")
            @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
            @Parameter(description = "image file for material template registration logo")
            @RequestParam(value = "registrationLogo" , required = false) MultipartFile registrationLogo
    ) throws IOException {
        Branding branding = new ObjectMapper().readValue(data, Branding.class);
        return this.brandingService.create(clientId, branding, logoFile, backgroundImage, registrationLogo);
    }
    @Operation(summary = "Update Branding", description = "Endpoint for update branding", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @PutMapping("/branding/{id}")
    public Response updateBranding(
            @Parameter(description = "branding id to be update",required = true)
            @PathVariable("id") String id,
            @Parameter(description = "update date")
            @RequestParam(value = "data") String data,
            @Parameter(description = "updated image file for branding")
            @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
            @Parameter(description = "updated image file for branding background")
            @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
            @Parameter(description = "image file for material template registration logo")
            @RequestParam(value = "registrationLogo" , required = false) MultipartFile registrationLogo
    ) throws IOException {
        Branding branding = new ObjectMapper().readValue(data, Branding.class);
        return this.brandingService.updateBranding(id, branding, logoFile, backgroundImage, registrationLogo);
    }
    @Operation(summary = "Find branding by id", description = "Find branding by branding id", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/{id}")
    public ResponseEntity<BrandingResponse> getBrandingById(
            @Parameter(description = "branding id",required = true)
            @PathVariable("id") String id
    ) {
        return this.brandingService.getBrandingById(id);
    }
    @Operation(summary = "Find branding/portal by client id", description = "Find branding/client under the provided client id", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/client/{clientId}")
    public Page<BrandingResponse> getAllPortalByClient(
            @Parameter(description = "Client Id",required = true)
            @PathVariable("clientId") String clientId,
            @Parameter(description = "page number for pagination")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(description = "page size for pagination",required = true)
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "status of the branding which need to find")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getAllPortalByClient(clientId, pageNo, pageSize, status);
    }
    @Operation(summary = "Find number of portal/branding", description = "Find number of portal/branding using organization id", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/organization/{organizationId}/count")
    public Integer getTotalPortalByOrganization(
            @Parameter(description = "Organization Id",required = true)
            @PathVariable("organizationId") String organizationId,
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getTotalPortalByOrganization(organizationId, status);
    }
    @Operation(summary = "Find All portal/branding", description = "Find all existing portal using status but default status all", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding")
    public Page<BrandingResponse> getAllPortal(
            @Parameter(description = "page number for pagination")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(description = "page size for pagination")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getAllPortal(pageNo, pageSize, status);
    }
    @Operation(summary = "Find All portal/branding by organization Id", description = "Find all portal under the organization id", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/organization/{organizationId}")
    public Page<BrandingResponse> getAllPortalByOrganizationId(
            @Parameter(description = "organization id")
            @PathVariable("organizationId") String organizationId,
            @Parameter(description = "page number for pagination")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(description = "page size for pagination")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getAllPortalByOrganizationId(organizationId, pageNo, pageSize, status);
    }
    @Operation(summary = "Find total number of portal/branding", description = "Find total number of portal/branding using", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/count")
    public Integer getTotalPortalCount(
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getTotalPortalCount(status);
    }
    @Operation(summary = "Find total number of portal/branding by client id", description = "Find total number of portal/branding under given client id", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/client/{clientId}/count")
    public Integer getTotalPortalCountByClientId(
            @Parameter(description = "Client Id")
            @PathVariable("clientId") String clientId,
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getTotalPortalCountByClientId(clientId, status);
    }
    @Operation(summary = "Find portal/branding by client id list", description = "Find all portal/branding under given client id list", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/clientIdList")
    public Page<BrandingResponse> getAllPortalByClientIdList(
            @Parameter(description = "Client Id List to find portal/branding under of it")
            @RequestParam("clientIdList") String[] clientIdList,
            @Parameter(description = "page number for pagination")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(description = "page size for pagination")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getAllPortalByClientIdList(clientIdList, pageNo, pageSize, status);
    }

    @Operation(summary = "Find portal/branding by organization id list", description = "Find all portal/branding under given org id list", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/orgIdList")
    public Page<BrandingResponse> getAllPortalByOrganizationIdList(
            @Parameter(description = "Organization Id List to find portal/branding under of it")
            @RequestParam("orgIdList") List<String> organizationIdList,
            @Parameter(description = "page number for pagination")
            @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,
            @Parameter(description = "page size for pagination")
            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
            @Parameter(description = "status of the branding")
            @RequestParam(name = "status",defaultValue = "all") String status
    ) {
        return this.brandingService.getAllPortalByOrganizationIdList(organizationIdList, pageNo, pageSize, status);
    }

    @Operation(summary = "Find portal by portal id list", description = "Find all portal/branding under given portal/branding id list", tags = { "Branding service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/branding/portalIdList")
    public Page<BrandingResponse> getPortalListByIdList(
            @Parameter(description = "portal Id List to find portal/branding under of it")
            @RequestParam("portalIdList") String[] portalIdList,
            @Parameter(description = "page number for pagination")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(description = "page size for pagination")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "status of the branding")
            @RequestParam(defaultValue = "all") String status
    ) {
        return this.brandingService.getPortalListByIdList(portalIdList, pageNo, pageSize, status);
    }
}

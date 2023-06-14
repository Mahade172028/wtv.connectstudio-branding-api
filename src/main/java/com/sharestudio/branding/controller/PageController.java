package com.sharestudio.branding.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sharestudio.branding.entity.PageModels.PageModel;
import com.sharestudio.branding.entity.PageModels.Views;
import com.sharestudio.branding.service.PageHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PageController {

    private final PageHandlerService pageHandlerService;

    @PostMapping("/branding/createPage")
    public ResponseEntity createPage(@RequestBody PageModel pageModel){
        return pageHandlerService.createPage(pageModel);
    }

    @PutMapping("/branding/updatePage/{id}")
    public ResponseEntity updatePage(@PathVariable("id") String id,@RequestBody PageModel pageModel){
        return pageHandlerService.updatePage(id,pageModel);
    }

    @GetMapping("/branding/getPageById/{id}")
    public ResponseEntity getPageById(@PathVariable("id") String id){
        return pageHandlerService.getPageById(id);
    }

    @GetMapping("/branding/portal/getPageById/{id}")
    public ResponseEntity getPageByIdForPortal(@PathVariable("id") String id , @RequestParam(value = "password" , required = false) String password){
        return pageHandlerService.getPageByIdForPortal(id , password);
    }
    @GetMapping("/branding/getPageByPortalId/{portalId}")
    public ResponseEntity getPageByPortalId(@PathVariable("portalId") String portalId){
        return pageHandlerService.getPagesByPortalId(portalId);
    }
    @JsonView(Views.Public.class)
    @GetMapping("/branding/portal/getPageByPortalId/{portalId}")
    public ResponseEntity getPageByPortalIdForPortal(@PathVariable("portalId") String portalId){
        return pageHandlerService.getPagesByPortalId(portalId);
    }

    @DeleteMapping("/branding/deletePage/{id}")
    public ResponseEntity deletePage(@PathVariable("id") String id){
        return pageHandlerService.deletePage(id);
    }
}

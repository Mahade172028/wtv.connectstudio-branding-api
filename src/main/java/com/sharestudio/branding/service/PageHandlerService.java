package com.sharestudio.branding.service;

import com.sharestudio.branding.entity.PageModels.PageModel;
import com.sharestudio.branding.exception.GenericException;
import com.sharestudio.branding.exception.ResourceNotFoundException;
import com.sharestudio.branding.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageHandlerService {
  private final PageRepository pageRepository;

    public ResponseEntity createPage(PageModel pageModel) {
      try {
        if(!pageValidate(pageModel)) throw new Exception("please fill all the fields");
        PageModel pageModelResponse = pageRepository.save(pageModel);
       return ResponseEntity.ok(pageModelResponse);
      }catch (Exception e){
        log.error(e.getMessage());
        throw new ResourceNotFoundException(e.getMessage());
      }
    }

    public ResponseEntity updatePage(String id , PageModel pageModel) {
      PageModel previousPage = pageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Not found by id"));
      if(pageModel.getPageName() != null){
        previousPage.setPageName(pageModel.getPageName());
      }
      if(pageModel.getIframeUrl() != null){
        previousPage.setIframeUrl(pageModel.getIframeUrl());
      }
      if(pageModel.getDimensions() != null){
        previousPage.setDimensions(pageModel.getDimensions());
      }
      if(pageModel.getActive() != null){
        previousPage.setActive(pageModel.getActive());
      }
      if(Objects.equals(pageModel.getRestricted(),false)){
        previousPage.setRestricted(false);
        previousPage.setPassword(null);
        previousPage.setAccessMessage(null);
      }
      if(Objects.equals(pageModel.getRestricted(),true)){
        previousPage.setRestricted(true);
        if(pageModel.getPassword() == null) throw new GenericException(HttpStatus.BAD_REQUEST , "Please provide the password");
        previousPage.setPassword(pageModel.getPassword());
        if(pageModel.getAccessMessage() != null)
          previousPage.setAccessMessage(pageModel.getAccessMessage());
      }
      try {
        PageModel pageModelResponse = pageRepository.save(previousPage);
        return ResponseEntity.ok(pageModelResponse);
      }catch (Exception e){
        log.error(e.getMessage());
        throw new ResourceNotFoundException(e.getMessage());
      }
    }

    public ResponseEntity getPageById(String id){
      try {
        Optional<PageModel> pageModelResponse = pageRepository.findPageModelById(id);
        if(pageModelResponse.isPresent()){
          return ResponseEntity.ok(pageModelResponse.get());
        }else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry didn't found with the id");
        }
      }catch (Exception e){
        log.error(e.getMessage());
        throw new ResourceNotFoundException(e.getMessage());
      }
    }

    public ResponseEntity getPageByIdForPortal(String id, String password) {
      try {
        Optional<PageModel> pageModelResponse = pageRepository.findPageModelById(id);
        if(pageModelResponse.isPresent()){
          PageModel pageModel = pageModelResponse.get();
          if(Objects.equals(pageModel.getRestricted(),true)){
            if(password == null)
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Please provide the password");
            if(!Objects.equals(pageModel.getPassword(),password))
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sorry password is wrong. please try again");
          }
          return ResponseEntity.ok(pageModelResponse.get());
        }else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry didn't found with the id");
        }
      }catch (Exception e){
        log.error(e.getMessage());
        throw new ResourceNotFoundException(e.getMessage());
      }
    }
    public ResponseEntity getPagesByPortalId(String portalId){
      try {
        List<PageModel> pageModelList = pageRepository.findPageModelByPortalId(portalId);
        return ResponseEntity.ok(pageModelList);
      }catch (Exception e){
        log.error(e.getMessage());
        throw new ResourceNotFoundException(e.getMessage());
      }
    }
    public ResponseEntity deletePage(String id){
      try {
           pageRepository.deleteById(id);
        return ResponseEntity.ok("page deleted successfully");
      }catch (Exception e){
        log.error(e.getMessage());
        throw new ResourceNotFoundException(e.getMessage());
      }
    }
    public boolean pageValidate(PageModel pageModel){
      return pageModel.getPortalId() != null && pageModel.getClientId() != null && pageModel.getOrgId() != null;
    }
}

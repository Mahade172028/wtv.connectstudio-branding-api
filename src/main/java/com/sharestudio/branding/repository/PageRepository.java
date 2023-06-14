package com.sharestudio.branding.repository;

import com.sharestudio.branding.entity.PageModels.PageModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends MongoRepository<PageModel , String> {
    Optional<PageModel> findPageModelById(String id);

    List<PageModel> findPageModelByPortalId(String portalId);
}

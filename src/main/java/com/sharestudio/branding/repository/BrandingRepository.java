package com.sharestudio.branding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sharestudio.branding.entity.Branding;

public interface BrandingRepository extends MongoRepository<Branding, String>{

	public Page<Branding> findByClientId(String clientId, Pageable paging);
	public Page<Branding> findByClientIdAndPublishedTrue(String clientId, boolean published, Pageable paging);
	public Page<Branding> findByClientIdAndPublishedFalse(String clientId, boolean published, Pageable paging);
	public List<Branding> findByOrganizationId(String organizationId);
	public List<Branding> findByOrganizationIdAndPublishedTrue(String organizationId, boolean published);
	public List<Branding> findByOrganizationIdAndPublishedFalse(String organizationId, boolean published);
	public Page<Branding> findAll(Pageable paging);
	public Page<Branding> findAllByPublishedTrue(boolean published, Pageable paging);
	public Page<Branding> findAllByPublishedFalse(boolean published, Pageable paging);
	public Page<Branding> findByOrganizationId(String organizationId, Pageable paging);
	public Page<Branding> findByOrganizationIdAndPublishedTrue(String organizationId, boolean published, Pageable paging);
	public Page<Branding> findByOrganizationIdAndPublishedFalse(String organizationId, boolean published, Pageable paging);
	public List<Branding> findAll();
	public List<Branding> findAllByPublishedTrue(boolean published);
	public List<Branding> findAllByPublishedFalse(boolean published);
	public List<Branding> findAllByClientId(String clientId);
	public List<Branding> findAllByClientIdAndPublishedTrue(String clientId, boolean published);
	public List<Branding> findAllByClientIdAndPublishedFalse(String clientId, boolean published);
	@Query(value = "{'clientId' : {$in: ?0},'published': ?1}")
	public Page<Branding> findAllByClientIdListAndPublishedStatus(String [] idList, boolean published, Pageable pageable);
	@Query(value = "{'clientId' : {$in: ?0} }")
	public Page<Branding> findAllByClientIdList(String [] idList, Pageable pageable);
	@Query(value = "{'organizationId' : {$in: ?0},'published' : ?1}")
	Page<Branding> findAllByOrganizationIdListWithStatus(List<String> orgList,boolean published , Pageable pageable);
	@Query(value = "{'organizationId' : {$in: ?0}}")
	Page<Branding> findAllByOrganizationIdList(List<String> orgList , Pageable pageable);
	public Branding findByIdAndPublishedTrue(String id, boolean published);
	public Branding findByIdAndPublishedFalse(String id, boolean published);
	@Query(value = "{'_id' : {$in: ?0},'published': ?1}")
	public Page<Branding> findAllByBrandingIdListAndPublishedStatus(String [] idList, boolean published, Pageable pageable);
	@Query(value = "{'_id' : {$in: ?0}}")
	public Page<Branding> findAllByBrandingIdList(String [] idList, Pageable pageable);
}

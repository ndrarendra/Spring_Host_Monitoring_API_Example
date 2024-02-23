package test.demo.demo.repository;

import test.demo.demo.model.AuditTrail;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ATrailRepository extends MongoRepository<AuditTrail, String> {
    
};
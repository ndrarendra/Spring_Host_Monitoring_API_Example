package test.demo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import test.demo.demo.model.ERole;
import test.demo.demo.model.Role;


public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);

  Optional<Role> findById(String id);
}
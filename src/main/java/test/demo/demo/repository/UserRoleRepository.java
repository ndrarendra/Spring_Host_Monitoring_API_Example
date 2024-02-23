package test.demo.demo.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import test.demo.demo.model.User;

public interface UserRoleRepository extends MongoRepository<User, String> {
  User findByUsername(String username);


}
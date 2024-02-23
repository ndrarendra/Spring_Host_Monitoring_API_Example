package test.demo.demo.repository;

import test.demo.demo.model.IPaddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPRepository extends MongoRepository<IPaddress, String> {

    List<IPaddress> findByName(String name);

    List<IPaddress> findByIpaddress(String ipaddress);
}
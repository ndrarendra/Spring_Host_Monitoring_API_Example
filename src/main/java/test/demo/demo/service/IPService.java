package test.demo.demo.service;

import test.demo.demo.dto.IPRequestDTO;
import test.demo.demo.dto.IPUpdateRequestDTO;
import test.demo.demo.model.IPaddress;

import java.util.List;

public interface IPService {

    IPaddress createNew(IPRequestDTO ipRequestDTO);

    IPaddress updateIPaddress(IPUpdateRequestDTO ipUpdateRequestDTO);

    Boolean deleteById(String id);

    List<IPaddress> getByIPaddress(String ip_address);

    List<IPaddress> getByName(String name);

    IPaddress getById(String ipId);

}
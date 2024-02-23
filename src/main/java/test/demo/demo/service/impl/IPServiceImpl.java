package test.demo.demo.service.impl;

import test.demo.demo.dto.IPRequestDTO;
import test.demo.demo.dto.IPUpdateRequestDTO;
import test.demo.demo.model.IPaddress;
import test.demo.demo.repository.IPRepository;
import test.demo.demo.service.IPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IPServiceImpl implements IPService {

    @Autowired
    IPRepository ipRepository;

    @Override
    public IPaddress createNew(IPRequestDTO ipRequestDTO) {
        return ipRepository.save(
                IPaddress.builder()
                        .name(ipRequestDTO.getName())
                        .ipaddress(ipRequestDTO.getIpaddress())
                        .author(ipRequestDTO.getAuthorName())
                        .createdAt(new Date(System.currentTimeMillis()))
                        .lastUpdatedAt(new Date(System.currentTimeMillis()))
                        .build()
        );
    }

    @Override
    public IPaddress updateIPaddress(IPUpdateRequestDTO ipUpdateRequestDTO) {
        Optional<IPaddress> existingIP = ipRepository.findById(ipUpdateRequestDTO.getId());
        if(existingIP.isEmpty())
            throw new RuntimeException(
                    String.format("No IP found for id: %s", ipUpdateRequestDTO.getId()));
        existingIP.get().setName(ipUpdateRequestDTO.getName());
        existingIP.get().setIpaddress(ipUpdateRequestDTO.getIpaddress());
        existingIP.get().setAuthor(ipUpdateRequestDTO.getAuthorName());
        return ipRepository.save(existingIP.get());
    }

    @Override
    public Boolean deleteById(String id) {
        ipRepository.deleteById(id);
        if(ipRepository.findById(id).isEmpty())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Override
    public List<IPaddress> getByIPaddress(String ip_address) {
        return ipRepository.findByIpaddress(ip_address);
    }

    @Override
    public List<IPaddress> getByName(String name) {
        return ipRepository.findByName(name);
    }

    @Override
    public IPaddress getById(String ipId) {
        return ipRepository.findById(ipId).orElseThrow(
                () -> new RuntimeException(String.format("No IP found for id: %s", ipId))
        );
    }
}
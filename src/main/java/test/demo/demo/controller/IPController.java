package test.demo.demo.controller;

import test.demo.demo.dto.IPRequestDTO;
import test.demo.demo.dto.IPUpdateRequestDTO;
import test.demo.demo.model.AuditTrail;
import test.demo.demo.model.IPaddress;
import test.demo.demo.model.Role;
import test.demo.demo.model.User;
import test.demo.demo.repository.ATrailRepository;
import test.demo.demo.repository.IPRepository;
import test.demo.demo.repository.RoleRepository;
import test.demo.demo.repository.UserRepository;
import test.demo.demo.repository.UserRoleRepository;
import test.demo.demo.service.IPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/ip_list")
public class IPController {
    @Value("${demo.app.jwtSecret}")
    private String jwtSecret;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    @Autowired
    IPService ipService;

    @Autowired
    IPRepository ipRepository;

    @Autowired
    ATrailRepository aTrailRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository uRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping
    public Map<String, String> createNew(@RequestBody IPRequestDTO IPRequestDTO,HttpServletRequest request) throws Exception {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Cookie[] list = request.getCookies();
        String result = new String();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
                System.out.println(result);

            }
        }
        AuditTrail entity = AuditTrail.builder().username(result).activity("Create IP").createdAt(timeStamp).build();
        aTrailRepository.insert(entity);

        User users = uRepository.findByUsername(result);
        Set<Role> roles1 = users.getRoles();
        String ids = new String();
        for (Role item : roles1) {
            ids = item.getId();
        }
        Role roles3 = roleRepository.findById(ids).get();
        String roles4 = roles3.getName().name();
        if (roles4 != "ROLE_ADMIN") {
            throw new Exception("User is not Admin");
        }


        List<IPaddress> total_data = ipRepository.findAll();
        System.err.println(total_data);
        if (total_data.size() == 100) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Status", "Could not inserted more than 100 data");
            return map;
        }

        String ipaddress = IPRequestDTO.getIpaddress();
        List<IPaddress> result1 = ipService.getByIPaddress(ipaddress);

        String names = IPRequestDTO.getName();
        List<IPaddress> result_names = ipService.getByName(names);
        int size_names = result_names.size();
        int size = result1.size();

        if ((size == 0) && (size_names == 0)) {
            IPaddress ip_data = ipService.createNew(IPRequestDTO);
            HashMap<String, String> map = new HashMap<>();
            map.put("ipId", ip_data.getId());
            return map;
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("Status", "Duplicate Data");
            return map;
        }

    }

    @GetMapping
    public IPaddress getById(@RequestParam String ipId,HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Cookie[] list = request.getCookies();
        String result = new String();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
                System.out.println(result);

            }
        }
        AuditTrail entity = AuditTrail.builder().username(result).activity("see IP by ID").createdAt(timeStamp).build();
        aTrailRepository.insert(entity);

        
        return ipService.getById(ipId);
    }

    @GetMapping("getByName")
    public List<IPaddress> getByName(@RequestParam String name,HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Cookie[] list = request.getCookies();
        String result = new String();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
                System.out.println(result);

            }
        }
        AuditTrail entity = AuditTrail.builder().username(result).activity("see IP names ").createdAt(timeStamp).build();
        aTrailRepository.insert(entity);
        return ipService.getByName(name);

    }

    @GetMapping("getByIPaddress")
    public List<IPaddress> getByIPaddresses(@RequestParam String ipaddress,HttpServletRequest request) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Cookie[] list = request.getCookies();
        String result = new String();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
                System.out.println(result);

            }
        }
        AuditTrail entity = AuditTrail.builder().username(result).activity("Create IP").createdAt(timeStamp).build();
        aTrailRepository.insert(entity);

        return ipService.getByIPaddress(ipaddress);
    }

    @PutMapping
    public IPaddress updateIPaddress(@RequestBody IPUpdateRequestDTO ipUpdateRequestDTO,HttpServletRequest request) throws Exception {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Cookie[] list = request.getCookies();
        String result = new String();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
                System.out.println(result);

            }
        }
        AuditTrail entity = AuditTrail.builder().username(result).activity("Update IP").createdAt(timeStamp).build();
        aTrailRepository.insert(entity);

        User users = uRepository.findByUsername(result);
        Set<Role> roles1 = users.getRoles();
        String ids = new String();
        for (Role item : roles1) {
            ids = item.getId();
        }
        Role roles3 = roleRepository.findById(ids).get();
        String roles4 = roles3.getName().name();
        if (roles4 != "ROLE_ADMIN") {
            throw new Exception("User is not Admin");
        }
        return ipService.updateIPaddress(ipUpdateRequestDTO);
    }

    @DeleteMapping
    public Boolean deleteById(@RequestParam String ipId,HttpServletRequest request) throws Exception {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Cookie[] list = request.getCookies();
        String result = new String();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
                System.out.println(result);

            } else {
                // pass
            }
        }
        AuditTrail entity = AuditTrail.builder().username(result).activity("Delete IP").createdAt(timeStamp).build();
        aTrailRepository.insert(entity);

        User users = uRepository.findByUsername(result);
        Set<Role> roles1 = users.getRoles();
        String ids = new String();
        for (Role item : roles1) {
            ids = item.getId();
        }
        ;
        Role roles3 = roleRepository.findById(ids).get();
        String roles4 = roles3.getName().name();
        if (roles4 != "ROLE_ADMIN") {
            throw new Exception("User is not Admin");
        }
        return ipService.deleteById(ipId);
    }

}
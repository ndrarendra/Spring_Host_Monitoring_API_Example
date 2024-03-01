package test.demo.demo.controller;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

@RestController
@RequestMapping("/api/v1/ip_list")
public class IPController {

    @Value("${demo.app.jwtSecret}")
    private String jwtSecret;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    };

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

    public String AuditTrailInsert(int activity, String username)
    {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String auditDetail = new String();

        if (activity == 1)
        {
            auditDetail = "Creating New IP or Host Data";
        }
        else if (activity == 2)
        {
            auditDetail = "Updating IP or Host Data";
        }
        else if (activity == 3)
        {
            auditDetail = "Deleting IP or Host Data";
        }
        else if (activity == 4)
        {
            auditDetail = "Checking or Selecting the IP or Host Data";
        };

        AuditTrail entity = AuditTrail.builder().username(username).activity(auditDetail).createdAt(timeStamp).build();
        aTrailRepository.insert(entity);
        return "Success";
    };

    public boolean CheckAdmin(String username) throws Exception {
        User users = uRepository.findByUsername(username);
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
        return true;
    };

    public String getCookieData(HttpServletRequest request) {
        String result = new String();
        Cookie[] list = request.getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("demotest")) {
                String cookie_value = cookie.getValue();
                result = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(cookie_value).getBody()
                        .getSubject();
            }
        }
        return result;
    }

    @PostMapping
    public Map<String, String> createNew(@RequestBody IPRequestDTO IPRequestDTO, HttpServletRequest request)
            throws Exception {

        String ipaddress = IPRequestDTO.getIpaddress();
        HashMap<String, String> map = new HashMap<>();
        String result = getCookieData(request);
        
        AuditTrailInsert(1, result);
        boolean checkadm = CheckAdmin(result);
        if (checkadm == true) {
            List<IPaddress> total_data = ipRepository.findAll();
            System.err.println(total_data);
            if (total_data.size() == 100) {
                map.put("Status", "Could not inserted more than 100 data");
                return map;
            }
            List<IPaddress> result1 = ipService.getByIPaddress(ipaddress);

            String names = IPRequestDTO.getName();
            List<IPaddress> result_names = ipService.getByName(names);
            int size_names = result_names.size();
            int size = result1.size();

            if ((size == 0) && (size_names == 0)) {
                IPaddress ip_data = ipService.createNew(IPRequestDTO);
                map.put("ipId", ip_data.getId());
                return map;
            } else {
                map.put("Status", "Duplicate IP or Host Data");
                return map;
            }
        }
        map.put("Status", "User is not Admin or Function Error");
        return map;
    };

    @GetMapping
    public IPaddress getById(@RequestParam String ipId, HttpServletRequest request) {
        String result = getCookieData(request);
        AuditTrailInsert(4, result);
        return ipService.getById(ipId);
    }

    @GetMapping("getByName")
    public List<IPaddress> getByName(@RequestParam String name, HttpServletRequest request) {
        String result = getCookieData(request);
        AuditTrailInsert(4, result);
        return ipService.getByName(name);

    };

    @GetMapping("getByIPaddress")
    public List<IPaddress> getByIPaddresses(@RequestParam String ipaddress, HttpServletRequest request) {
        String result = getCookieData(request);
        AuditTrailInsert(4, result);
        return ipService.getByIPaddress(ipaddress);
    };

    @PutMapping
    public IPaddress updateIPaddress(@RequestBody IPUpdateRequestDTO ipUpdateRequestDTO, HttpServletRequest request)
            throws Exception {
        String result = getCookieData(request);
        AuditTrailInsert(2, result);

        boolean checkadm = CheckAdmin(result);
        if (checkadm == true) {
            return ipService.updateIPaddress(ipUpdateRequestDTO);
        } else {
            throw new Exception("User is not Admin or Function Error");
        }
    };

    @DeleteMapping
    public Boolean deleteById(@RequestParam String ipId, HttpServletRequest request) throws Exception {
        String result = getCookieData(request);
        AuditTrailInsert(3, result);

        boolean checkadm = CheckAdmin(result);
        if (checkadm == true) {
            return ipService.deleteById(ipId);
        } else {
            throw new Exception("User is not Admin or Function Error");
        }
    }
}
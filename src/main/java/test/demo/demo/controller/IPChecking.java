package test.demo.demo.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/ip_checking")
public class IPChecking {
    @Value("${demo.app.jwtSecret}")
    private String jwtSecret;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    @Autowired
    ATrailRepository aTrailRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    IPService ipService;

    @Autowired
    IPRepository ipRepository;

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


    @GetMapping
    public Boolean checkOneConnection(@RequestParam String ipId, HttpServletRequest request) throws Exception {
        String result = getCookieData(request);
        
        AuditTrailInsert(1, result);
        boolean checkadm = CheckAdmin(result);
        if (checkadm == true)
        {
            IPaddress ip_data = ipService.getById(ipId);
            String ipadd = ip_data.ipaddress;
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName(ipadd);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                throw new Exception("Error Unknown Host");
            }
    
            try {
                if (inetAddress.isReachable(1000)) { // timeout 1 second
    
                    System.out.println("Reachable");
                    return true;
    
                } else {
                    System.out.println("Unreachable");
                    return false;
    
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("Error IO");
            }
        }
        return false;
  
    }

    @GetMapping("checkall")
    public List<Map<String, String>> checkAllConnection(HttpServletRequest request) throws Exception {
        List<Map<String, String>> ip_list = new ArrayList<Map<String, String>>();
        String result = getCookieData(request);
        AuditTrailInsert(1, result);
        boolean checkadm = CheckAdmin(result);
        if (checkadm == true)
        {
            for (IPaddress iPaddress1 : ipRepository.findAll()) {
                String ipadd = iPaddress1.ipaddress;
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getByName(ipadd);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    if (inetAddress.isReachable(1000)) { // timeout 1 second
                        HashMap<String, String> map = new HashMap<>();
                        map.put("ipadd", ipadd);
                        map.put("result", "Reachable");
                        System.out.println("Reachable");
                        ip_list.add(map);

                    } else {
                        System.out.println("Unreachable");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("ipadd", ipadd);
                        map.put("result", "unreachable");
                        ip_list.add(map);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("ipadd", ipadd);
                    map.put("result", "iOError");
                    ip_list.add(map);
                    continue;
                }
            }
        }
        return ip_list;
    }
}

package test.demo.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "ip_list")
public class IPaddress {

    @Id
    private String id;

    private String name;

    public String ipaddress;

    private String author;

    private Date createdAt;

    private Date lastUpdatedAt;

}
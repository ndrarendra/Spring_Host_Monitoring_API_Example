package test.demo.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "audit_trail")
public class AuditTrail {

    @Id
    private String id;

    private String username;

    public String activity;

    private String createdAt;

    private Date lastUpdatedAt;

}
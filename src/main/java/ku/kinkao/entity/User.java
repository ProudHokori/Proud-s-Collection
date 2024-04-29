package ku.kinkao.entity;


import jakarta.persistence.*;
import ku.kinkao.enums.UserRole;
import lombok.Data;


import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Instant createdAt;

}

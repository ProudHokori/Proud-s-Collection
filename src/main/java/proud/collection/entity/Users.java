package proud.collection.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Users {

    @Id
    @GeneratedValue
    private UUID id;

    private String username;
    private String password;
    private String email;
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;


    private boolean isEnabled;
    private String resetPasswordToken;

    private Instant createdAt;

    private boolean isAcceptConsent;

}

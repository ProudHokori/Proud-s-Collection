package proud.collection.entity;

import jakarta.persistence.*;
import proud.collection.enums.UserRole;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class Rating {

    @Id
    @GeneratedValue
    private UUID id;

    private float score;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne
    private Book book;

    private Instant createdAt;

}

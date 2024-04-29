package proud.collection.entity;

import jakarta.persistence.*;
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
    private String role;

    @ManyToOne
    private Book book;

    private Instant createdAt;

}

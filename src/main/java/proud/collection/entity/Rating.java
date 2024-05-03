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

    @ManyToOne
    private Users user;

    @ManyToOne
    private Book book;

    private String role;


    private Instant createdAt;

}

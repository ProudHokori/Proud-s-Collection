package proud.collection.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "book_id"})})
public class Rating {

    @Id
    @GeneratedValue
    private UUID id;

    private float score;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users user;

    @ManyToOne
    private Book book;

    private String role;

//    @JoinColumn(name = "book_id")

    private Instant createdAt;

}

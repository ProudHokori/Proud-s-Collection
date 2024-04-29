package ku.kinkao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue
    private UUID id;

    private String titleTH;
    private String titleEN;
    private String author;
    private String publisher;
    private String isbn;
    private String category;
    @OneToMany(mappedBy = "book")
    List<Rating> ratings;

    private Instant createdAt;

}

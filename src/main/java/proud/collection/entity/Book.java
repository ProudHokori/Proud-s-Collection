package proud.collection.entity;

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
    private String titleTh;
    private String titleEn;
    private String author;
    private String publisher;
    private String isbn;
    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "book")
    List<Rating> ratings;

    private Instant createdAt;

}

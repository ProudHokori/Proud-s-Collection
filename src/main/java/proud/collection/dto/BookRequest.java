package proud.collection.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class BookRequest {

    @NotBlank
    private String titleTH;
    @NotBlank
    private String titleEN;
    @NotBlank
    private String author;
    @NotBlank
    private String publisher;
    @NotBlank
    private String isbn;
    @NotBlank
    private String category;
}

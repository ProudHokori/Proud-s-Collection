package proud.collection.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class BookRequest {

    @NotBlank
    private String titleTh;
    @NotBlank
    private String titleEn;
    @NotBlank
    private String author;
    @NotBlank
    private String publisher;
    @NotBlank
    private String isbn;
    @Min(0)
    @Max(5)
    private float rating;
}

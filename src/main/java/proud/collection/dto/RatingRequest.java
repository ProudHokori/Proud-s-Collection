package proud.collection.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;


@Data
public class RatingRequest {

    @Min(0)
    @Max(5)
    private float score;

    @NotNull
    private String userRole;

    @NotNull
    private UUID bookId;
}

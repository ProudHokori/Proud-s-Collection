package proud.collection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import proud.collection.validation.ResetPasswordMatches;
import proud.collection.validation.ResetValidPassword;

@Data
@ResetPasswordMatches
public class ResetPasswordRequest {

    @NotBlank
    @ResetValidPassword
    @Size(min = 12, max = 128, message = "Password must have at least 12 characters")
    private String password;

    @NotBlank
    private String confirmPassword;

}

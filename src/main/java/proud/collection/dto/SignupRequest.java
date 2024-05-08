package proud.collection.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import proud.collection.validation.PasswordMatches;
import proud.collection.validation.ValidPassword;
import lombok.Data;

@Data
@PasswordMatches
public class SignupRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 4, message = "Username must have at least 4 characters")
    private String username;

    @NotBlank
    @ValidPassword
    @Size(min = 12, max = 128, message = "Password must have at least 12 characters")
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotNull
    private boolean isAcceptConsent;

    private String captcha;
    private String hidden;
    private String image;


}

package proud.collection.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import proud.collection.dto.ResetPasswordRequest;
import proud.collection.validation.ResetPasswordMatches;

public class ResetPasswordMatchesValidator implements ConstraintValidator<ResetPasswordMatches, ResetPasswordRequest> {

    @Override
    public void initialize(ResetPasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(ResetPasswordRequest resetPasswordRequest, ConstraintValidatorContext context) {
        boolean result = resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword());

        if (result) {
            return result;
        }

        context.buildConstraintViolationWithTemplate("Passwords don't match")
                .addPropertyNode("confirmPassword")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}


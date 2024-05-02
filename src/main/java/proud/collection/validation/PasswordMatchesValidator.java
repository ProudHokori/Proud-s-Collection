package proud.collection.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import proud.collection.dto.SignupRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SignupRequest> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(SignupRequest signupRequest, ConstraintValidatorContext context) {
        boolean result = signupRequest.getPassword().equals(signupRequest.getConfirmPassword());

        if(result){
            return result;
        }

        context.buildConstraintViolationWithTemplate("Passwords don't match")
                .addPropertyNode("confirmPassword")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}

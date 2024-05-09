package proud.collection.controller;

import cn.apiclub.captcha.Captcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proud.collection.dto.SignupRequest;
import proud.collection.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import proud.collection.service.UserService;
import proud.collection.utils.CaptchaUtil;
import proud.collection.validation.CompromisedPasswordValidator;

import java.security.NoSuchAlgorithmException;

@Controller
public class SignupController {

    Logger logger = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private SignupService signupService;

    @Autowired
    private UserService userService;

    private void setupCaptcha(SignupRequest signupRequest) {
        Captcha captcha = CaptchaUtil.createCaptcha(200, 50);
        signupRequest.setHidden(captcha.getAnswer());
        signupRequest.setCaptcha("");
        signupRequest.setImage(CaptchaUtil.encodeBase64(captcha));
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        SignupRequest signupRequest = new SignupRequest();
        setupCaptcha(signupRequest);
        model.addAttribute("signupRequest", signupRequest);
        return "signup"; // return signup.html
    }


    @PostMapping("/signup")
    public String signupUser(@Valid SignupRequest member,
                             BindingResult result, Model model) throws NoSuchAlgorithmException {

        CompromisedPasswordValidator compromisedPasswordValidator = new CompromisedPasswordValidator();

        if (result.hasErrors()) {
            setupCaptcha(member);
            return "signup";
        }

        if (compromisedPasswordValidator.isPasswordCompromised(member.getPassword())) {
            model.addAttribute("signupError", "Password is compromised");
            logger.info("User tried to sign up with a compromised password");
            setupCaptcha(member);
            return "signup";
        }
        if(member.getCaptcha() == null || !member.getCaptcha().equals(member.getHidden())) {
            model.addAttribute("signupError", "Captcha is incorrect");
            logger.info("User tried to sign up with an incorrect captcha");
            setupCaptcha(member);
            return "signup";
        }

        if (!member.isAcceptConsent()) {
            model.addAttribute("signupError", "Please accept the terms and conditions");
            setupCaptcha(member);
            return "signup";
        }


        if (!signupService.isEmailAvailable(member.getEmail())) {
            model.addAttribute("signupError", "Email not available");
            setupCaptcha(member);
        } else if (!signupService.isUsernameAvailable(member.getUsername())) {
            model.addAttribute("signupError", "Username not available");
            setupCaptcha(member);
        } else {
            signupService.createMember(member);
            model.addAttribute("signupSuccess", true);
        }
        model.addAttribute("signupRequest", new SignupRequest());

        logger.info("User: "+ member.getUsername() + "signed up successfully");
        return "signup";
    }

}

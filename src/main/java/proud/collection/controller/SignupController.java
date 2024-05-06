package proud.collection.controller;

import org.springframework.web.client.RestTemplate;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@Controller
public class SignupController {

    private static final Logger logger = Logger.getLogger(SignupController.class.getName());

    @Autowired
    private SignupService signupService;

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup"; // return signup.html
    }


    @PostMapping("/signup")
    public String signupUser(@Valid SignupRequest member,
                             BindingResult result, Model model) throws NoSuchAlgorithmException {
        if (result.hasErrors())
            return "signup";

        if (isPasswordCompromised(member.getPassword())) {
            model.addAttribute("signupError", "Password is compromised");
            logger.info("User tried to sign up with a compromised password");
            return "signup";
        }

        if (!signupService.isEmailAvailable(member.getEmail())) {
            model.addAttribute("signupError", "Email not available");
        } else if (!signupService.isUsernameAvailable(member.getUsername())) {
            model.addAttribute("signupError", "Username not available");
        } else {
            signupService.createMember(member);
            model.addAttribute("signupSuccess", true);
        }
        model.addAttribute("signupRequest", new SignupRequest());

        logger.info("User signed up successfully");
        return "signup";
    }

    public boolean isPasswordCompromised(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        String sha1 = sb.toString();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.pwnedpasswords.com/range/" + sha1.substring(0, 5);
        String response = restTemplate.getForObject(url, String.class);
        if (response != null && response.contains(sha1.substring(5).toUpperCase())) {
            return true;
        }
        return false;
    }
}

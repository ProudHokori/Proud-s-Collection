package proud.collection.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import proud.collection.dto.ResetPasswordRequest;
import proud.collection.entity.Users;
import proud.collection.service.EmailService;
import proud.collection.service.UserService;
import proud.collection.utils.UrlUtil;
import proud.collection.validation.CompromisedPasswordValidator;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         Authentication auth) {

        if (auth != null){
            new SecurityContextLogoutHandler()
                    .logout(request, response, auth);

            // logging out user who log out
                logger.info("User id: " + auth.getName() + " has logged out");
             }

        logger.info("User has logged out");
        // You can redirect wherever you want, but generally it's a good
        // practice to show the login screen again.
        return "redirect:/login?logout";
    }


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        logger.info("User requested forgot password form");
        return "forgot-password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = UrlUtil.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
            logger.info("Reset password link sent to email: " + email);

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgot-password";
    }

    public void sendEmail(String email, String link) throws MessagingException, UnsupportedEncodingException {

        String content = "<p>Dear User,</p>"
                + "<p>We are writing to <strong>formally notify</strong> you that a request to reset your password has been received.</p>"
                + "<p>Please <strong>proceed promptly</strong> to change your password by clicking the link below:</p>"
                + "<p><a href=\"" + link + "\">Reset Password</a></p>"
                + "<br>"
                + "<p>If you <strong>did not initiate this request</strong> or you remember your password, please <strong>disregard this message</strong>.</p>";

        MimeMessage mailMessage = emailService.createEmail(email, "Here's the link to reset your password", content);
        emailService.sendEmail(mailMessage);
        logger.info("Reset password link sent to email: " + email);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Users user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        logger.info("User requested reset password form");

        return "reset-password-form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(@Valid ResetPasswordRequest member, BindingResult result, Model model,HttpServletRequest request) throws NoSuchAlgorithmException {
        String token = request.getParameter("token");
        String password = member.getPassword();
        Users user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
        CompromisedPasswordValidator compromisedPasswordValidator = new CompromisedPasswordValidator();

        if (result.hasErrors()) {
            model.addAttribute("token", token);
            return "reset-password-form";
        }

        if (compromisedPasswordValidator.isPasswordCompromised(password)) {
            model.addAttribute("token", token);
            model.addAttribute("message", "Password is compromised");
            return "reset-password-form";
        }

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password.");
            logger.info("User has successfully changed password");
        }

        return "message";
    }
}
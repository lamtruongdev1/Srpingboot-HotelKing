package com.hotelking.routes;

import com.hotelking.entity.User;
import com.hotelking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RouteClient {
    @Autowired
    UserRepository userRepository;

    @GetMapping({"/", "/home", "/dashboard"})
    public String index() {
        return "index";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "auth/login";
    }

    @GetMapping("/login")
    public String logout() {
        return "auth/login";
    }

    @GetMapping("/signup-form")
    public String signUpForm() {
        return "auth/signup";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Principal: " + authentication.getPrincipal());
            System.out.println("Authorities: " + authentication.getAuthorities());
        } else {
            System.out.println("No authentication found.");
        }

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByEmail(username).orElse(null);

            if (user != null) {
                model.addAttribute("user", user);
                return "auth/profile";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/checkout")
    public String getCheckout() {
        return "checkout/checkout";
    }

    @GetMapping("/rooms")
    public String room() {
        return "room/rooms";
    }

    @GetMapping("/room/room-details/{slug}")
    public String roomDetails(@PathVariable("slug") String slug) {
        return "room/room-details";
    }

    @GetMapping("/room/{slug}")
    public String getRoomByCategory(@PathVariable("slug") String slug) {
        return "room/rooms";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

}

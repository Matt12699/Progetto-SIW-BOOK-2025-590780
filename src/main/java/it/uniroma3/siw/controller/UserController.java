package it.uniroma3.siw.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	BookService bookService;
	@Autowired
	CredentialsService credentialsService;

	// Get mapping della pagina che mostra la user-Area
	@GetMapping(value = "/userArea")
	public String userArea(@ModelAttribute("currentUser") User user, Model model) {

		// Calcolo per ottenere il voto medio
		if (user.getWrittenReviews() != null && !user.getWrittenReviews().isEmpty()) {
			double sum = 0.0;
			for (Review review : user.getWrittenReviews()) {
				sum += review.getRating();
			}
			model.addAttribute("avgRating", sum / user.getWrittenReviews().size());
		} else {
			model.addAttribute("avgRating", 0.0);
		}

		Set<Book> reviewedBooks = new HashSet<>();
		for (Review review : user.getWrittenReviews()) {
			reviewedBooks.add(review.getBook());
		}
		model.addAttribute("books", reviewedBooks);
		return "userArea.html";
	}

	// Get mapping dell'account details
	@GetMapping(value = "/accountDetails")
	public String accountDetails() {
		return "accountDetails.html";
	}

	// Get mapping dell'account details
	@GetMapping(value = "/changePassword")
	public String changePassword() {
		return "changePassword.html";
	}
	
	@PostMapping(value="/changedPassword")
	public String changedPassword(@RequestParam String oldPassword, 
								  @RequestParam String newPassword, 
								  @RequestParam String confirmPassword,
								  @ModelAttribute("currentUser") User user,
								  RedirectAttributes redirectAttributes,
								  HttpServletRequest request,
								  HttpServletResponse response,
								  Model model) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean match = passwordEncoder.matches(oldPassword, user.getCredentials().getPassword());
        
        if(!match) {
        	model.addAttribute("oldPasswordError", "Old password is wrong");
        }
        
        if(newPassword.equals(oldPassword)) {
        	model.addAttribute("newPasswordError", "Old password and New password can't be the same");
        }
        
        if(!newPassword.equals(confirmPassword)) {
        	model.addAttribute("confirmPasswordError", "New password and confirm password must be the same");
        }
        
        if(model.containsAttribute("oldPasswordError") || model.containsAttribute("newPasswordError") || model.containsAttribute("confirmPasswordError")) {
        	return "changePassword.html";
        }
        
        credentialsService.updatePassword(user.getCredentials(), newPassword);
        
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        
        redirectAttributes.addFlashAttribute("changedPassword", true);
        
		return "redirect:/login";
	}

}

package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.UserService;


@Controller
public class UserController {

	@Autowired UserService userService;
	@Autowired BookService bookService;

	//Get mapping della pagina che mostra la user-Area
	@GetMapping(value = "/userArea") 
	public String userArea(@ModelAttribute("currentUser") User user, Model model) {
		
		if(user.getWrittenReviews()!=null && !user.getWrittenReviews().isEmpty()) {
			double sum = 0.0;
			for(Review review : user.getWrittenReviews()) {
				sum += review.getRating();
			}
			model.addAttribute("avgRating", sum/user.getWrittenReviews().size());
		}else {
			model.addAttribute("avgRating", 0.0);
		}
		
		Set<Book> reviewedBooks = new HashSet<>();
		for(Review review : user.getWrittenReviews()) {
			reviewedBooks.add(review.getBook());
		}
		model.addAttribute("books", reviewedBooks);
		return "/userArea.html";
	}


}

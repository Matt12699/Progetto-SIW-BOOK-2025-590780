package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired ReviewService reviewService;
	
	// Post mapping per la cancellazione di una review
	@PostMapping("/admin/deleteReview/{bookId}/{reviewId}")
	public String deleteReview(@PathVariable("reviewId") Long reviewId, @PathVariable("bookId") Long bookId, Model model) {
		
		this.reviewService.deleteById(reviewId);
		return "redirect:/admin/manageBook/" + bookId;
	}

}
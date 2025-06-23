package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	
	// Salva la review
	public void save(Review review) {
		this.reviewRepository.save(review);
	}
	
	// Conta il numero di review
	public Long countReviews() {
		return this.reviewRepository.count();
	}
	
	// Cancella la review facendo attenzione a cancellarla sia dalla lista di review dello user
	// Che dalla lista di review del libro
	public void deleteById(Long reviewId) {
		
		Review review = this.reviewRepository.findById(reviewId).get();
		User user = review.getUser();
		Book book = review.getBook();
		if (user != null) user.getWrittenReviews().remove(review);
	    if (book != null) book.getReviews().remove(review);
	    
		this.reviewRepository.deleteById(reviewId);
	}
}
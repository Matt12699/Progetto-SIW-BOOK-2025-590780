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
	
	public void save(Review review) {
		this.reviewRepository.save(review);
	}
	
	public Long countReviews() {
		return this.reviewRepository.count();
	}
	
	public void deleteById(Long reviewId) {
		
		Review review = this.reviewRepository.findById(reviewId).get();
		User user = review.getUser();
		Book book = review.getBook();
		if (user != null) user.getWrittenReviews().remove(review);
	    if (book != null) book.getReviews().remove(review);
	    
		this.reviewRepository.deleteById(reviewId);
	}
}
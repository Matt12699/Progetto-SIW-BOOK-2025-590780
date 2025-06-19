package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;
	
	public Iterable<Book> getAllBooks(){
		return bookRepository.findAll();
	}
	
	public Book save(Book book) {
		return bookRepository.save(book);
	}
	
	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}
	
	public void addAuthorToBook(Long bookId, Long authorId) {
		bookRepository.addAuthorToBook(bookId, authorId);
	}
	
	public List<Book> findByTitleContaining(String keyword) {
		return bookRepository.findByTitleContaining(keyword);
	}
	
	public Long countBooks() {
		return bookRepository.count();
	}
	
	public void deleteById(Long bookId) {
		bookRepository.deleteById(bookId);
	}
	
}
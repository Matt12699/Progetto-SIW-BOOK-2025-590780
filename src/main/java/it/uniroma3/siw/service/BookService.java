package it.uniroma3.siw.service;

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
}
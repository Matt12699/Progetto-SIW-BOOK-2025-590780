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
	
	// Trova tutti i libri
	public Iterable<Book> getAllBooks(){
		return bookRepository.findAll();
	}
	
	// Salva il libro
	public Book save(Book book) {
		return bookRepository.save(book);
	}
	
	// Trova il libro dato l'id
	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}
	
	// Trova il libro che contiene la parola chiave nel titolo
	public List<Book> findByTitleContaining(String keyword) {
		return bookRepository.findByTitleContaining(keyword);
	}
	
	// Conta il numero di libri
	public Long countBooks() {
		return bookRepository.count();
	}
	
	// Cancella il libro dato l'id
	public void deleteById(Long bookId) {
		bookRepository.deleteById(bookId);
	}
	
	// Controlla se esiste un libro con quel titolo
	public boolean existsByTitle(String title) {
		return this.bookRepository.existsByTitle(title);
	}
}
package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Author;

import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	
	// Ottiene tutti gli autori
	public Iterable<Author> getAllAuthors(){
		return this.authorRepository.findAll();
	}

	// Salva l'autore
	public Author save(Author author) {
		return authorRepository.save(author);
		
	}
	
	// Trova l'autore dato l'id
	public Author findById(Long id) {
		return authorRepository.findById(id).get();
	}
	
	// Data una lista di id trova tutti gli autori
	public Iterable<Author> findAllById(List<Long> id) {
		return authorRepository.findAllById(id);
	}
	
	// Trova gli autori che hanno la parola chiave nel nome o nel cognome
	public List<Author> findByNameOrSurnameContaining(String keyword) {
		return authorRepository.findByNameOrSurnameContaining(keyword);
	}
	
	// Conta il numero di autori
	public Long countAuthors() {
		return authorRepository.count();
	}
	
	// Cancella l'autore dato l'id
	public void deleteById(Long authorId) {
		this.authorRepository.deleteById(authorId);
	}
	
	// Controlla se esiste un autore con lo stesso nome e cognome
	public boolean existsByNameAndSurname(String name, String surname) {
		return this.authorRepository.existsByNameAndSurname(name, surname);
	}
}
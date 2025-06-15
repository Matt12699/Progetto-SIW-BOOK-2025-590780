package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Author;

import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	
	public Iterable<Author> getAllAuthors(){
		return this.authorRepository.findAll();
	}

	public Author save(Author author) {
		return authorRepository.save(author);
		
	}
	
	public Author findById(Long id) {
		return authorRepository.findById(id).get();
	}
}
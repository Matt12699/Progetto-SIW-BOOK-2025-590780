package it.uniroma3.siw.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Book;


public interface BookRepository extends CrudRepository<Book, Long> {
	
	// Query per trovare i libri che hanno la parola chiave nel titolo
	// ILIKE per rendere la query non case sensitive
	@Query(value = "SELECT * FROM book WHERE title ILIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	public List<Book> findByTitleContaining(@Param("keyword") String keyword);
	
	// Controlla se esiste un libro con quel titolo
	public boolean existsByTitle(String title);
}
package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
	
	//Utilizzo ILIKE che mi permette di fare un confronto con maiuscole e minuscole
	@Query(value = "SELECT * FROM author WHERE name ILIKE CONCAT('%', :keyword, '%') OR surname ILIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	public List<Author> findByTitleContaining(String keyword);

}
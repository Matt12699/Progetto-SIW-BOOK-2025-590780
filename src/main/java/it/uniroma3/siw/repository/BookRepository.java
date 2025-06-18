package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Book;
import jakarta.transaction.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO book_authors VALUES (:authorId, :bookId)", nativeQuery = true)
	public void addAuthorToBook(Long bookId, Long authorId);
	
	
	@Query(value = "SELECT * FROM book WHERE title ILIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	public List<Book> findByTitleContaining(@Param("keyword") String keyword);
}
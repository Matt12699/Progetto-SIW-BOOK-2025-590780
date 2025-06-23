package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Controller
public class BookController {

	@Autowired BookService bookService;
	@Autowired AuthorService authorService;
	@Autowired ReviewService reviewService;

	// Inserimento immagine come copertina
	@Transactional
	@GetMapping("/book/image/{id}")
	public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
		Book book = bookService.findById(id);
		if (book == null || book.getImage() == null) {
			return ResponseEntity.notFound().build();
		}
		byte[] imageBytes = book.getImage().getImage();  
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg") 
				.body(imageBytes);
	}

	// Get mapping della pagina di un solo libro
	@GetMapping(value = "/book/{id}") 
	public String showBook(@PathVariable Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		// Questa è la review che verrà riempita in caso l'utente autenticato
		// Decida di scriverne una
		model.addAttribute("review", new Review());
		return "book.html";
	}

	// Get mapping della pagina che mostra i libri
	@GetMapping(value = "/books") 
	public String showBooks(@RequestParam(required=false) String search, Model model) {

		// Se la ricerca non è nulla allora mostro i libri che contengono nella parola chiave la ricerca
		if(search!=null && !search.trim().isEmpty()) {
			model.addAttribute("books", this.bookService.findByTitleContaining(search));
			model.addAttribute("searchQuery", search);
		}else {
			model.addAttribute("books", this.bookService.getAllBooks());
		}

		return "books.html";
	}

	// Get mapping della pagina che mostra il manage dei libri
	@GetMapping(value = "/admin/manageBooks") 
	public String manageBooks(Model model) {

		// Per le stats dell'admin
		model.addAttribute("reviewNumber", this.reviewService.countReviews());
		model.addAttribute("authorNumber", this.authorService.countAuthors());
		model.addAttribute("bookNumber", this.bookService.countBooks());
		model.addAttribute("books", this.bookService.getAllBooks());


		return "/admin/manageBooks.html";
	}

	// Get mapping per la modifica di un singolo libro
	@GetMapping(value = "/admin/manageBook/{bookId}")
	public String manageBook(@PathVariable("bookId") Long bookId, Model model) {
		model.addAttribute("book", this.bookService.findById(bookId));
		return "/admin/manageBook.html";
	}

	// Get mapping per il form di modifica del libro
	@GetMapping(value = "/admin/formEditBook/{bookId}")
	public String formEditBook(@PathVariable("bookId") Long bookId, Model model) {
		model.addAttribute("book", this.bookService.findById(bookId));
		// Questo serve a mostrarli
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "/admin/formEditBook.html";
	}

	// Post mapping per l'edit di un libro
	@PostMapping("/admin/editBook/{bookId}")
	public String editBook(@Valid @ModelAttribute("book") Book formBook, 
						   BindingResult bindingResult, 
						   @RequestParam("imageFile") MultipartFile imageFile, 
						   @RequestParam(value = "authorIds", required = false) List<Long> authorIds, 
						   @PathVariable("bookId") Long bookId, Model model) throws IOException {

		// Se non è stato spuntato nulla delle checkbox
		if (authorIds == null || authorIds.isEmpty()) {

			model.addAttribute("authorError", "Book must have at least one author");
		}
		if(imageFile.isEmpty()) {
			model.addAttribute("imageError", "Book must have an image");
		}

		if (bindingResult.hasErrors() || model.containsAttribute("authorError") || model.containsAttribute("imageError")) { // sono emersi errori nel binding
			// Sennò gli autori non si vedrebbero
			model.addAttribute("book", formBook);
			model.addAttribute("authors", this.authorService.getAllAuthors());
			return "/admin/formEditBook.html";
		} else { // NON sono emersi errori nel binding

			Book existingBook = this.bookService.findById(bookId);
			
			// Aggiorna i campi del libro esistente
	        existingBook.setTitle(formBook.getTitle());
	        existingBook.setYear(formBook.getYear());
	        existingBook.setDescription(formBook.getDescription());

			Image image = new Image();
			image.setImage(imageFile.getBytes());
			existingBook.setImage(image);


			// Setto gli autori selezionati
			Iterable<Author> authorsIterable = authorService.findAllById(authorIds);
			Set<Author> authors = new HashSet<>();
			authorsIterable.forEach(authors::add);
			existingBook.setAuthors(authors);


			// Salva il libro con autori e immagine
			bookService.save(existingBook);

			// Reindirizza alla lista dei libri
			return "redirect:/admin/manageBooks";

		}

	}

	// Get mapping del form per l'ineserimento di un libro
	@GetMapping("/admin/formNewBook")
	public String formNewBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "/admin/formNewBook.html";
	}

	// Post mapping per la cancellazione di un libro
	@PostMapping("/admin/bookDelete/{bookId}")
	public String bookDelete(@PathVariable("bookId") Long bookId) {
		this.bookService.deleteById(bookId);
		return "redirect:/admin/manageBooks";
	}

	// Post mapping per l'inserimento di un libro
	@PostMapping("/admin/addBook")
	public String addBook(@Valid @ModelAttribute("book") Book book, 
						  BindingResult bindingResult, 
						  @RequestParam("imageFile") MultipartFile imageFile, 
						  @RequestParam(value = "authorIds", required = false) List<Long> authorIds, 
						  Model model) throws IOException {

		// Se non hai spuntato nessuna checkbox
		if (authorIds == null || authorIds.isEmpty()) {

			model.addAttribute("authorError", "Book must have at least one author");
		}
		if(imageFile.isEmpty()) {
			model.addAttribute("imageError", "Book must have an image");
		}

		if(bookService.existsByTitle(book.getTitle())) {
			model.addAttribute("existingBook", "This book already exists");
		}
		
		if (bindingResult.hasErrors() || model.containsAttribute("authorError") || model.containsAttribute("imageError") || model.containsAttribute("existingBook")) { // sono emersi errori nel binding
			// Sennò gli autori non si vedrebbero
			model.addAttribute("authors", this.authorService.getAllAuthors());
			return "/admin/formNewBook.html";
		} else {                         // NON sono emersi errori nel binding



			Image image = new Image();
			image.setImage(imageFile.getBytes());
			book.setImage(image);


			// Setto gli autori selezionati
			Iterable<Author> authorsIterable = authorService.findAllById(authorIds);
			Set<Author> authors = new HashSet<>();
			authorsIterable.forEach(authors::add);
			book.setAuthors(authors);


			// Salva il libro con autori e immagine
			bookService.save(book);

			// Reindirizza alla lista dei libri
			return "redirect:/admin/manageBooks";

		}

	}

	// Post mapping per l'inserimento di una review
	@PostMapping("/book/{bookId}/addReview")
	public String addReview(@Valid @ModelAttribute("review") Review review, 
							BindingResult bindingResult, 
							@ModelAttribute("currentUser") User user, 
							@PathVariable("bookId") Long bookId ,Model model) throws IOException {

		// Sono emersi errori nel binding, ritorno alla pagina del libro con il modal della recensione
		if (bindingResult.hasErrors()) { 
			model.addAttribute("book", bookService.findById(bookId));
			return "book.html";
		} else {  // NON sono emersi errori nel binding

			// Salva la review
			Book book = bookService.findById(bookId);
			review.setUser(user);
			review.setBook(book);
			reviewService.save(review);

			// Reindirizza alla lista dei libri
			return "redirect:/book/{bookId}";

		}

	}




}

package it.uniroma3.siw.controller;

import java.io.IOException;
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

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import jakarta.validation.Valid;


@Controller
public class BookController {
	
	@Autowired BookService bookService;
	@Autowired AuthorService authorService;
	
	@GetMapping("/book/image/{id}")
	public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
	    Book book = bookService.findById(id);
	    if (book == null || book.getImage() == null) {
	        return ResponseEntity.notFound().build();
	    }
	    byte[] imageBytes = book.getImage().getImage();  
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // o image/png se necessario
	            .body(imageBytes);
	}
	
	@GetMapping(value = "/books") 
	public String showBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
        return "books.html";
	}
	
	@GetMapping("/formNewBook")
	public String formNewBook(Model model) {
	    model.addAttribute("book", new Book());
	    model.addAttribute("authors", this.authorService.getAllAuthors());
	    return "formNewBook.html";
	}

	@PostMapping("/addBook")
	public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {

		if (bindingResult.hasErrors()) { // sono emersi errori nel binding
			
			return "formNewBook.html";
		} else {                         // NON sono emersi errori nel binding
			

			if (!imageFile.isEmpty()) {
				Image image = new Image();
				image.setImage(imageFile.getBytes());
				book.setImage(image);
			}
			this.bookService.save(book);
			return "redirect:/books";
		}

	}
	 

}

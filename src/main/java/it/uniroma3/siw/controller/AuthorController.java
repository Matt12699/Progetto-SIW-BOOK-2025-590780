package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;


@Controller
public class AuthorController {

	@Autowired AuthorService authorService;
	@Autowired BookService bookService;
	@Autowired ReviewService reviewService;

	// Get mapping per il caricamento delle immagini
	@GetMapping("/author/image/{id}")
	public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
		Author author = authorService.findById(id);
		if (author == null || author.getImage() == null) {
			return ResponseEntity.notFound().build();
		}
		byte[] imageBytes = author.getImage().getImage();  
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg") 
				.body(imageBytes);
	}

	// Get mapping della pagina che mostra tutti gli autori
	@GetMapping(value = "/authors") 
	public String showAuthors(@RequestParam(required=false) String search, Model model) {

		// Se è stato cercato qualcosa, mostro solo gli autori che contengono nel nome o
		// nel cognome la parola chiave
		if(search!=null && !search.trim().isEmpty()) {
			model.addAttribute("authors", this.authorService.findByNameOrSurnameContaining(search));
			model.addAttribute("searchQuery", search);
		}else
		{	// Senno li mostro tutti
			model.addAttribute("authors", this.authorService.getAllAuthors());
		}

		return "authors.html";
	}

	// Pagina che mostra un autore in particolare
	@GetMapping(value = "/author/{id}") 
	public String showAuthor(@PathVariable Long id, Model model) {
		model.addAttribute("author", this.authorService.findById(id));
		return "author.html";
	}

	// Get mapping del form per l'aggiunta di un autore
	@GetMapping("/admin/formNewAuthor")
	public String formNewAuthor(Model model) {
		model.addAttribute("author", new Author());
		return "/admin/formNewAuthor.html";
	}

	// Get mapping della pagina che mostra il manage degli autori
	@GetMapping(value = "/admin/manageAuthors") 
	public String manageAuthors(Model model) {

		// Stats per l'admin
		model.addAttribute("reviewNumber", this.reviewService.countReviews());
		model.addAttribute("authorNumber", this.authorService.countAuthors());
		model.addAttribute("bookNumber", this.bookService.countBooks());
		model.addAttribute("authors", this.authorService.getAllAuthors());


		return "/admin/manageAuthors.html";
	}

	// Get mapping per la modifica di un singolo autore
	@GetMapping(value = "/admin/manageAuthor/{authorId}")
	public String manageAuthor(@PathVariable("authorId") Long authorId, Model model) {
		model.addAttribute("author", this.authorService.findById(authorId));
		return "/admin/manageAuthor.html";
	}

	// Post mapping per la cancellazione di un autore, 
	// cancella a cascata anche i libri i quali a loro volta cancellano le recensioni
	@PostMapping("/admin/authorDelete/{authorId}")
	public String authorDelete(@PathVariable("authorId") Long authorId) {
		this.authorService.deleteById(authorId);
		return "redirect:/admin/manageAuthors";
	}

	// Get mapping per il form della modifica di un autore
	@GetMapping(value = "/admin/formEditAuthor/{authorId}")
	public String formEditAuthor(@PathVariable("authorId") Long authorId, Model model) {
		model.addAttribute("author", this.authorService.findById(authorId));
		return "/admin/formEditAuthor.html";
	}

	// Post mapping per l'edit di un autore
	@PostMapping("/admin/editAuthor/{authorId}")
	public String editAuthor(@Valid @ModelAttribute("author") Author formAuthor, 
							BindingResult bindingResult,
							@RequestParam("imageFile") MultipartFile imageFile, 
							@PathVariable("authorId") Long authorId, Model model) throws IOException {

		if(imageFile.isEmpty()) {
			model.addAttribute("imageError", "Author must have an image");
		}

		if (bindingResult.hasErrors() || model.containsAttribute("imageError")) { // sono emersi errori nel binding
			// Sennò gli autori non si vedrebbero
			model.addAttribute("author", formAuthor);
			return "/admin/formEditAuthor.html";
		} else {   // NON sono emersi errori nel binding

			Author existingAuthor = this.authorService.findById(authorId);

			// Vengono risettati manualmente tutti gli attributi dell'autore
			existingAuthor.setName(formAuthor.getName());
			existingAuthor.setSurname(formAuthor.getSurname());
			existingAuthor.setDateOfBirth(formAuthor.getDateOfBirth());
			existingAuthor.setDateOfDeath(formAuthor.getDateOfDeath());
			existingAuthor.setNationality(formAuthor.getNationality());

			Image image = new Image();
			image.setImage(imageFile.getBytes());
			existingAuthor.setImage(image);


			authorService.save(existingAuthor);


			return "redirect:/admin/manageAuthors";

		}

	}

	// Post mapping per aggiungere un autore
	@PostMapping("/admin/addAuthor")
	public String addAuthor(@Valid @ModelAttribute("author") Author author, BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {

		if(imageFile.isEmpty()) {
			model.addAttribute("imageError", "Author must have an image");
		}
		
		if(authorService.existsByNameAndSurname(author.getName(), author.getSurname())) {
			model.addAttribute("existingAuthor", "This author already exists");
		}
		if (bindingResult.hasErrors() || model.containsAttribute("imageError") || model.containsAttribute("existingAuthor")) { // sono emersi errori nel binding

			return "/admin/formNewAuthor.html";
			
		} else {    // NON sono emersi errori nel binding


			Image image = new Image();
			image.setImage(imageFile.getBytes());
			author.setImage(image);

			this.authorService.save(author);
			return "redirect:/admin/manageAuthors";
		}

	}



}

package it.uniroma3.siw.controller;

import java.io.IOException;

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
import jakarta.validation.Valid;


@Controller
public class AuthorController {

	@Autowired AuthorService authorService;
	
	@GetMapping("/author/image/{id}")
	public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
	    Author author = authorService.findById(id);
	    if (author == null || author.getImage() == null) {
	        return ResponseEntity.notFound().build();
	    }
	    byte[] imageBytes = author.getImage().getImage();  
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // o image/png se necessario
	            .body(imageBytes);
	}

	@GetMapping(value = "/authors") 
	public String showAuthors(Model model) {
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "authors.html";
	}

	@GetMapping("/formNewAuthor")
	public String formNewAuthor(Model model) {
	    model.addAttribute("author", new Author());
	    return "formNewAuthor.html";
	}

	@PostMapping("/addAuthor")
	public String addAuthor(@Valid @ModelAttribute("author") Author author, BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {

		if (bindingResult.hasErrors()) { // sono emersi errori nel binding
			
			return "formNewAuthor.html";
		} else {                         // NON sono emersi errori nel binding
			

			if (!imageFile.isEmpty()) {
				Image image = new Image();
				image.setImage(imageFile.getBytes());
				author.setImage(image);
			}
			this.authorService.save(author);
			return "redirect:/authors";
		}

	}



}

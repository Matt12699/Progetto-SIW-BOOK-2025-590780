package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.AuthorService;
import jakarta.validation.Valid;


@Controller
public class AuthorController {

	@Autowired AuthorService authorService;

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

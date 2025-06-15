package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.service.BookService;


@Controller
public class BookController {
	
	@Autowired BookService bookService;
	
	@GetMapping(value = "/books") 
	public String showBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
        return "books.html";
	}
	
	@GetMapping(value = "/formNewBook") 
	public String formNewBook(Model model) {
		model.addAttribute("book", new Book());
        return "formNewBook.html";
	}
	 

}

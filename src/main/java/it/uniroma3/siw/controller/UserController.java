package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.service.UserService;


@Controller
public class UserController {
	
	@Autowired UserService userService;
	 

}

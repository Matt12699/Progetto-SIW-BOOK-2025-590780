package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;

@ControllerAdvice
public class GlobalController {
	
	@Autowired CredentialsService credentialsService;
    
	@ModelAttribute("userDetails")
    public UserDetails getUserDetails() {
        UserDetails user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }
	
	//Metodo per ottenere lo user attuale
	@ModelAttribute("currentUser")
    public User getCurrentUser() {
		
		UserDetails userDetails = getUserDetails();
		User user = null;
		if(userDetails!=null) {
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		user = credentials.getUser();
		}
		return user;
        
    }
    
    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() 
               && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
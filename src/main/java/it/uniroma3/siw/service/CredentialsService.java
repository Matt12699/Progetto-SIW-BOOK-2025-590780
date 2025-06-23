package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    // Ritorna le credenziali dato l'id
    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    // Ritorna le credenziali dato lo username
    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    // Salva le credenziali, ricordandosi di criptare la password
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    // Vede se esistono credenziali con quello username
	public boolean existsByUsername(String username) {
		boolean result = this.credentialsRepository.existsByUsername(username);
		return result;
	}
	
	// Aggiorna la password
	public void updatePassword(Credentials credentials, String newPassword) {
		credentials.setPassword(this.passwordEncoder.encode(newPassword));
		this.credentialsRepository.save(credentials);
	}
}

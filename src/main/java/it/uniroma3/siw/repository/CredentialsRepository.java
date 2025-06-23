package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	// Trova le credenziali per uno username
	public Optional<Credentials> findByUsername(String username);
	
	// Controlla se esiste quello username
	public boolean existsByUsername(String username);

}
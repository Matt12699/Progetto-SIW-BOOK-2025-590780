package it.uniroma3.siw.model;

import java.time.LocalDate;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	

	@Column(nullable = false)
	@NotBlank
	@Size(min=2, max=20)
	private String name;
	
	@Column(nullable = false)
	@NotBlank
	@Size(min=2, max=20)
	private String surname;
	
	@Column(nullable = false)
	@NotNull
	@Past
	private LocalDate dateOfBirth;
	
	@PastOrPresent
	private LocalDate dateOfDeath;
	
	@Column(nullable = false)
	@NotBlank
	private String nationality;

	@Lob
	@OneToOne(cascade = CascadeType.ALL)
	private Image image;
	
	@ManyToMany(mappedBy = "authors" ,cascade = CascadeType.REMOVE)
	private List<Book> books;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, dateOfDeath, name, nationality, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(dateOfDeath, other.dateOfDeath)
				&& Objects.equals(name, other.name) && Objects.equals(nationality, other.nationality)
				&& Objects.equals(surname, other.surname);
	}
	
	
	
}

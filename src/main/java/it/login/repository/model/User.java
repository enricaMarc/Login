package it.login.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String username;

	@Column(nullable = false, length = 60)
	private String password;

	@Column(name = "nome", nullable = false, length = 20)
	private String nome;
	
	@Column(name = "cognome", nullable = false, length = 20)
	private String cognome;

	@Column(name = "codice_fiscale", nullable = false, unique = true, length = 16)
	private String codiceFiscale;

	@Column(name = "file_path", nullable = true, length = 64)
	private String file;

	@Column(name = "notifica", nullable = true)
	private String notifica;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNotifica() {
		return notifica;
	}

	public void setNotifica(String notifica) {
		this.notifica = notifica;
	}
}

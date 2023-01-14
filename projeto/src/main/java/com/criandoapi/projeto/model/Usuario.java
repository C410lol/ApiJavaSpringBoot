package com.criandoapi.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data

@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@NotBlank(message = "Nome não pode estar vazio!")
	@Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres!")
	@Column(name = "nome", length = 100, nullable  = false)
	private String nome;
	
	@Email(message = "Insira um e-mail válido")
	@NotBlank(message = "E-mail não pode estar vazio!")
	@Column(name = "email", length = 50, nullable  = false)
	private String email;
	
	@NotBlank(message = "Senha não pode estar vazia!")
	@Column(name = "senha", columnDefinition = "TEXT", nullable  = false)
	private String senha;
	
	@NotBlank(message = "Telefone não pode estar vazio!")
	@Column(name = "telefone", length = 15, nullable  = false)
	private String telefone;
}

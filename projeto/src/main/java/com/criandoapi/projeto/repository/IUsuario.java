package com.criandoapi.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.criandoapi.projeto.model.Usuario;

public interface IUsuario extends JpaRepository<Usuario, Integer> {

	public Usuario findByNomeOrEmail(String nome, String email);
}

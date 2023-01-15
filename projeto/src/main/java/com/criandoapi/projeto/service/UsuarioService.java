package com.criandoapi.projeto.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.criandoapi.projeto.dto.UsuarioDto;
import com.criandoapi.projeto.model.Usuario;
import com.criandoapi.projeto.repository.IUsuario;
import com.criandoapi.projeto.security.Token;
import com.criandoapi.projeto.security.TokenUtil;

import jakarta.validation.Valid;

@Service
public class UsuarioService {

	private final IUsuario repository;
	private final PasswordEncoder encoder;
	private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	
	public UsuarioService(IUsuario repository) {
		
		this.repository = repository;
		this.encoder = new BCryptPasswordEncoder();
	}
	
	public List<Usuario> listaUsuarios() {

		logger.info("Usuário: " + getLogado() + " Listando usuários");

		return repository.findAll();
	}
	
	public Usuario salvaUsuario(Usuario usuario) {
		
		String senhaCriptografada = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);

		logger.info("Usuário: " + getLogado() + " Criando usuário");

		return repository.save(usuario);
	}
	
	public Usuario editaUsuario(Usuario usuario) {
		
        String senhaCriptografada = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);

		logger.info("Usuário: " + getLogado() + " Editando " + usuario);

		return repository.save(usuario);
	}
	
	public Boolean deletaUsuario(Integer id) {
		
		repository.deleteById(id);

		logger.info("Usuário: " + getLogado() + " Excluindo usuário");

		return true;
	}
	
	public Token gerarToken(@Valid UsuarioDto usuario) {
		
		Usuario user = repository.findByNomeOrEmail(usuario.getNome(), usuario.getEmail());
		
		if(user != null) {
			
			boolean valid = encoder.matches(usuario.getSenha(), user.getSenha());
			
			if(valid) {
				
				return new Token(TokenUtil.createToken(user));
			}
		}
		
		return null;
	}

	private String getLogado() {

		Authentication userLogado = SecurityContextHolder.getContext().getAuthentication();

		if (userLogado instanceof AnonymousAuthenticationToken) {

			return userLogado.getName();
		}

		return null;
	}
}

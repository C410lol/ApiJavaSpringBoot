package com.criandoapi.projeto.service;

import java.util.List;

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

	private IUsuario repository;
	private PasswordEncoder encoder;
	
	public UsuarioService(IUsuario repository) {
		
		this.repository = repository;
		this.encoder = new BCryptPasswordEncoder();
	}
	
	public List<Usuario> listaUsuarios() {
		
		List<Usuario> todos = repository.findAll();
		
		return todos;
	}
	
	public Usuario salvaUsuario(Usuario usuario) {
		
		String senhaCriptografada = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
		Usuario novoUsuario = repository.save(usuario);
		
		return novoUsuario;
	}
	
	public Usuario editaUsuario(Usuario usuario) {
		
        String senhaCriptografada = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
		Usuario usuarioEditado = repository.save(usuario);
		
		return usuarioEditado;
	}
	
	public Boolean deletaUsuario(Integer id) {
		
		repository.deleteById(id);
		
		return true;
	}

	public Boolean validarSenha(Usuario usuario) {
		
		String senha = repository.getById(usuario.getId()).getSenha();
		
		Boolean valid = encoder.matches(usuario.getSenha(), senha);
		
		return valid;
	}
	
	public Token gerarToken(@Valid UsuarioDto usuario) {
		
		Usuario user = repository.findByNomeOrEmail(usuario.getNome(), usuario.getEmail());
		
		if(user != null) {
			
			Boolean valid = encoder.matches(usuario.getSenha(), user.getSenha());
			
			if(valid) {
				
				return new Token(TokenUtil.createToken(user));
			}
		}
		
		return null;
	}
}

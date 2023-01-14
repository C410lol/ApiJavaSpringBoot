package com.criandoapi.projeto.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.criandoapi.projeto.dto.UsuarioDto;
import com.criandoapi.projeto.model.Usuario;
import com.criandoapi.projeto.security.Token;
import com.criandoapi.projeto.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private UsuarioService service;
	
	public UsuarioController(UsuarioService service) { 
		
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> listaUsuarios() {
		
		return ResponseEntity.status(200).body(service.listaUsuarios());
	}
	
	@PostMapping
	public ResponseEntity<Usuario> adicionaUsuario(@Valid @RequestBody Usuario usuario) {
		
		return ResponseEntity.status(201).body(service.salvaUsuario(usuario));
	}
	
	@PutMapping
    public ResponseEntity<Usuario> editaUsuario(@Valid @RequestBody Usuario usuario) {
		
		return ResponseEntity.status(201).body(service.editaUsuario(usuario));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletaUsuario(@PathVariable Integer id) {
		
		service.deletaUsuario(id);
		
		return ResponseEntity.status(204).build();
	}
	
	@PostMapping("/login")
	public ResponseEntity<Token> logar(@RequestBody UsuarioDto usuario) {
		
		Token token = service.gerarToken(usuario);
		
		if(token != null) {
			
			return ResponseEntity.ok(token);
		}
		
		return ResponseEntity.status(403).build();
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
		
		Map<String, String> errors = new HashMap<>();
		
		exception.getBindingResult().getAllErrors().forEach((error) -> {
			
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			
			errors.put(fieldName, errorMessage);
		});
		
		return errors;
	}
}

package com.daw.controllers;

import java.sql.SQLException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.services.Servicio;

@RestController
public class AplicacionSQLController {
	
	@Autowired
	private Servicio servicio;
	
	@GetMapping("/consulta_autores")
	public ResponseEntity<?> buscarAutores(@RequestParam String nombre){
		try {
			return ResponseEntity.ok().body(servicio.buscarAutores(nombre));
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/consulta_libros")
	public ResponseEntity<?> buscarLibros(
	        @RequestParam(defaultValue = "") String titulo,
	        @RequestParam(defaultValue = "") String descripcion,
	        @RequestParam(defaultValue = "false") Boolean favorite) {
	    try {
	        // Print out the parameters for debugging
	        System.out.println("Buscando libros - Titulo: " + titulo + 
	                           ", Descripcion: " + descripcion + 
	                           ", Favorito: " + favorite);
	        
	        return ResponseEntity.ok().body(servicio.buscarLibros(titulo, descripcion, favorite));
	    } catch (SQLException e) {
	        System.err.println("Error en búsqueda de libros: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
	    }
	}
	
	@GetMapping("/crear_autor")
	public ResponseEntity<?> crearAutor(@RequestParam String nombre,
			@RequestParam String dni,
			@RequestParam String direccion,
			@RequestParam Date fechaNacimiento){
		try {
			return ResponseEntity.ok().body(servicio.crearAutor(nombre, dni, direccion, fechaNacimiento));
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
		}
	}
	
	@GetMapping("/crear_libro")
	public ResponseEntity<?> crearLibro(@RequestParam String titulo,
			@RequestParam String descripcion,			
			@RequestParam Boolean favorite,
			@RequestParam Long idCategoria,
			@RequestParam Long idEditorial,
			@RequestParam Date fechaPublicacion,
			@RequestParam String isbn,
			@RequestParam String idioma){
		try {
			return ResponseEntity.ok().body(servicio.crearLibro(titulo, descripcion, favorite, fechaPublicacion, isbn, idioma, idCategoria, idEditorial));
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
		}
	}
	
	@GetMapping("/modificar_autor")
	public ResponseEntity<?> modificarAutor(@RequestParam Long idAutor,
			@RequestParam String nombre,
			@RequestParam String dni,
			@RequestParam String direccion,
			@RequestParam Date fechaNacimiento){
		try {
			return ResponseEntity.ok().body(servicio.modificarAutor(idAutor, nombre, dni, direccion, fechaNacimiento));
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
		}
	}
	@GetMapping("/eliminarAutor")
	public ResponseEntity<?> eliminarAutor(@RequestParam Long idAutor){
		try {
			return ResponseEntity.ok().body(servicio.eliminarAutor(idAutor));
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
		}
	}
	
	@GetMapping("/buscar.editoriales")
	public ResponseEntity<?> buscarEditorial(){
		try {
			return ResponseEntity.ok().body(servicio.buscarEditorial());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
		}
	}
	
	
	@GetMapping("/buscar.categorias")
	public ResponseEntity<?> buscarCategorias() throws SQLException{
		return ResponseEntity.ok().body(servicio.buscarCategoria());
	}
	
	
	

}

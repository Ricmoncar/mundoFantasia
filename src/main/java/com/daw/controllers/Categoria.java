package com.daw.controllers;

public class Categoria {

	private Long id;
	private String nombre;
	
	
	public Categoria(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}

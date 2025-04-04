package com.daw.controllers;
import java.util.Date;

public class Autor {
	
	private Long id;
	private String nombre;
	private String direccion;
	private String dni;
	private Date fechaNacimiento;
	
	public Autor(Long id, String nombre, String direccion, String dni, Date fechaNacimiento) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
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
	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}	

}

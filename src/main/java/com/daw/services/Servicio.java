package com.daw.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controllers.Autor;
import com.daw.controllers.Categoria;
import com.daw.controllers.Editorial;
import com.daw.controllers.Libro;

@Service
public class Servicio {
	
	static final String url = "jdbc:mysql://localhost:3306/base_datos_biblioteca?user=usuario&password=usuario";	
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Autor> buscarAutores(String nombreBusqueda) throws SQLException {
		Connection conn = DriverManager.getConnection(url);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM autor WHERE nombre LIKE '%" + nombreBusqueda + "%'");
		List<Autor> autores = new ArrayList<Autor>();
		while (rs.next()) {
			Long id = rs.getLong("id");
			String nombre = rs.getString("nombre");
			String direccion = rs.getString("direccion");
			String dni = rs.getString("dni");
			Date fecha = rs.getDate("fecha_nacimiento");
			Autor autor = new Autor(id, nombre, direccion, dni, fecha);
			autores.add(autor);
		}
		rs.close();
		stmt.close();
		conn.close();
		return autores;
	}
	

public List<Libro> buscarLibros(String titulo, String descripcion, Boolean favorite) throws SQLException {
    Connection conn = DriverManager.getConnection(url);
    
    // Modify the query to be more flexible with empty search parameters
    String sql = "SELECT l.id, l.titulo, l.descripcion, l.favorite, " +
                 "ia.fecha_publicacion, ia.isbn, ia.idioma " +
                 "FROM libro l " +
                 "LEFT JOIN info_adicional ia ON l.id = ia.libro " +
                 "WHERE (l.titulo LIKE ? OR ? = '') " +
                 "AND (l.descripcion LIKE ? OR ? = '') " +
                 "AND (l.favorite = ? OR ? IS NULL)";
    
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, "%" + titulo + "%");
    stmt.setString(2, titulo);
    stmt.setString(3, "%" + descripcion + "%");
    stmt.setString(4, descripcion);
    stmt.setBoolean(5, favorite);
    stmt.setObject(6, favorite);

    ResultSet rs = stmt.executeQuery();
    List<Libro> libros = new ArrayList<Libro>();
    while (rs.next()) {
        Long id = rs.getLong("id");
        String tit = rs.getString("titulo");
        String desc = rs.getString("descripcion");
        Boolean fav = rs.getBoolean("favorite");
        String isbn = rs.getString("isbn");
        String idioma = rs.getString("idioma");
        Date fechaPublicacion = rs.getDate("fecha_publicacion");
        
        Libro libro = new Libro(id, desc, tit, fav, fechaPublicacion, isbn, idioma);
        libros.add(libro);
    }
    
    rs.close();
    stmt.close();
    conn.close();
    
    return libros;
}
	
	public Autor crearAutor(String nombre, String dni, String direccion, Date fechaNacimiento) throws SQLException {
		Connection conn = DriverManager.getConnection(url);
		
		String sql = "INSERT INTO autor (nombre, dni, direccion, fecha_nacimiento) VALUES(?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, nombre);
		ps.setString(2, dni);
		ps.setString(3, direccion);
		ps.setDate(4, fechaNacimiento);

		int respuesta = ps.executeUpdate();
		if(respuesta==1) {
			System.out.println("Insercion correcta.");
		} else {
			throw new SQLException("No se ha podido insertar el nuevo autor.");
		}
		ps.close();
		conn.close();
		return new Autor(null, nombre, dni, direccion, fechaNacimiento);
	}
	
	public Libro crearLibro(String titulo,
			String descripcion,			
			Boolean favorite,
			Date fechaPublicacion,
			String isbn,
			String idioma,
			Long idCategoria,
			Long idEditorial) throws SQLException {

		Connection conn = DriverManager.getConnection(url);
		conn.setAutoCommit(false);
		
		try {
			String sql = "INSERT INTO libro (titulo, descripcion, favorite, categoria_id, editorial) VALUES(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, titulo);
			ps.setString(2, descripcion);
			ps.setBoolean(3, favorite);
			ps.setLong(4, idCategoria);
			ps.setLong(5, idEditorial);
	
			int respuesta = ps.executeUpdate();
			if(respuesta==1) {
				System.out.println("Insercion correcta.");
			} else {
				throw new SQLException("No se ha podido insertar el nuevo autor.");
			}
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM libro WHERE titulo = '"+titulo+"' AND descripcion='"+descripcion+"' AND favorite = " + favorite);
			Long id_libro = null;
			while (rs.next()) {
				id_libro = rs.getLong("id");
				break;
			}
			rs.close();
			stmt.close();
			
			sql = "INSERT INTO info_adicional (fecha_publicacion, idioma, isbn, libro) VALUES(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setDate(1, fechaPublicacion);
			ps.setString(2, idioma);
			ps.setString(3, isbn);
			ps.setLong(4, id_libro);
	
			respuesta = ps.executeUpdate();
			if(respuesta==1) {
				System.out.println("Insercion correcta.");
			} else {
				throw new SQLException("No se ha podido insertar la nueva información adicional.");
			}
			
			conn.commit();
			
			ps.close();
			conn.close();
			
			return new Libro(id_libro, descripcion, titulo, favorite, fechaPublicacion, isbn, idioma);
			
		} catch(SQLException ex) {
			conn.rollback();
			conn.close();
			throw ex;
		}
		
	}	
	
	public Autor modificarAutor(Long idAutor, String nombre, String dni, String direccion, Date fechaNacimiento) throws SQLException {
		Connection conn = DriverManager.getConnection(url);
		
		String sql = "UPDATE autor SET nombre=?, dni=?, direccion=?, fecha_nacimiento=? WHERE id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, nombre);
		ps.setString(2, dni);
		ps.setString(3, direccion);
		ps.setDate(4, fechaNacimiento);
		ps.setLong(5, idAutor);

		int respuesta = ps.executeUpdate();
		if(respuesta==1) {
			System.out.println("Actualización correcta.");
		} else {
			throw new SQLException("No se ha podido actualizar el autor.");
		}
		ps.close();
		conn.close();
		return new Autor(idAutor, nombre, dni, direccion, fechaNacimiento);
	}


	public boolean eliminarAutor(Long idAutor) throws SQLException {
	
	    
	    try (Connection conn = DriverManager.getConnection(url)) {
	        conn.setAutoCommit(false);

	        String sqlLibrosUnicoAutor = """
	                SELECT al.libro_id 
	        		FROM autor_libro al
	        		WHERE al.autor_id = ?
	        		AND NOT EXISTS (
	        		SELECT 1 
	        		FROM autor_libro al2 
	        		WHERE al2.libro_id = al.libro_id 
	        		AND al2.autor_id <> al.autor_id
	        		)
	        		GROUP BY al.libro_id;
	        """;

	        String sqlBorrarRelacion = "DELETE FROM autor_libro WHERE autor_id = ?";
	        String sqlBorrarInfoAdicional = "DELETE FROM info_adicional WHERE libro = ?";
	        String sqlBorrarLibro = "DELETE FROM libro WHERE id = ?";
	        String sqlBorrarAutor = "DELETE FROM autor WHERE id = ?";

	        List<Long> librosAEliminar = new ArrayList<>();

	        try (PreparedStatement psLibros = conn.prepareStatement(sqlLibrosUnicoAutor)) {
	            psLibros.setLong(1, idAutor);
	            try (ResultSet rs = psLibros.executeQuery()) {
	                while (rs.next()) {
	                    librosAEliminar.add(rs.getLong("libro_id"));
	                }
	            }
	        }

	        try (PreparedStatement psRelacion = conn.prepareStatement(sqlBorrarRelacion)) {
	            psRelacion.setLong(1, idAutor);
	            psRelacion.executeUpdate();
	        }

	        try (PreparedStatement psBorrarInfo = conn.prepareStatement(sqlBorrarInfoAdicional);
	             PreparedStatement psBorrarLibro = conn.prepareStatement(sqlBorrarLibro)) {
	            
	            for (Long idLibro : librosAEliminar) {
	                psBorrarInfo.setLong(1, idLibro);
	                psBorrarInfo.executeUpdate();
	                
	                psBorrarLibro.setLong(1, idLibro);
	                psBorrarLibro.executeUpdate();
	            }
	        }
	        
	        try (PreparedStatement psAutor = conn.prepareStatement(sqlBorrarAutor)) {
	            psAutor.setLong(1, idAutor);
	            int filasAfectadas = psAutor.executeUpdate();
	            if (filasAfectadas == 0) {
	                conn.rollback();
	                return false;
	            }
	        }

	        conn.commit();
	        return true;
	    } catch (SQLException e) {
	        throw e;
	    }
	}


	public List<Editorial> buscarEditorial() throws SQLException {
		
		Connection conn = DriverManager.getConnection(url);
	    
	    String sql = "SELECT * from editorial";
	    List<Editorial> editoriales = new ArrayList<Editorial>();

	    
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
	        Long id = rs.getLong("id");
	        String nombre = rs.getString("nombre");
	        
	        Editorial editorial = new Editorial(id, nombre);
	        editoriales.add(editorial);
	    }
	    
	    rs.close();
	    stmt.close();
	    conn.close();
	    
	    return editoriales;
	}



	public List<Categoria> buscarCategoria() throws SQLException {
		
		Connection conn = DriverManager.getConnection(url);
	    
	    String sql = "SELECT * from categoria";
	    List<Categoria> categorias = new ArrayList<Categoria>();

	    
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
	        Long id = rs.getLong("id");
	        String nombre = rs.getString("nombre");
	        
	        Categoria categoria = new Categoria(id, nombre);
	        categorias.add(categoria);
	    }
	    
	    rs.close();
	    stmt.close();
	    conn.close();
	    
	    return categorias;
	}
	
}

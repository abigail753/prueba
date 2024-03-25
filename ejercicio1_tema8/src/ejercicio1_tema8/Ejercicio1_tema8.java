package ejercicio1_tema8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Ejercicio1_tema8 {

	static void imprimirMenu() {
		System.out.println("1. Mostrar todo el contenido de la tabla.");
		System.out.println("2. Mostrar los nombres de las personas mayores de edad");
		System.out.println("3. Mostrar la edad de una persona con un id tecleado por el usuario");
		System.out.println("4. Insertar una nueva persona");
		System.out.println("5. Eliminar una persona");
		System.out.println("6. Actualizar la edad de una persona");
	}

	public static void main(String[] args) {
		String url = "jdbc:mysql://127.0.0.1:3307/prueba";
		// ?useSSL=false
		String user = "alumno";
		String password = "alumno";

		Scanner s = new Scanner(System.in);

		int opcion;

		do {
			imprimirMenu();
			System.out.println("Selecciona la operacion que se desea realizar");
			opcion = s.nextInt();

			try {
				Connection con = DriverManager.getConnection(url, user, password);

				Statement stmt = con.createStatement();
				ResultSet rs;
				PreparedStatement sel_pstmt;
			

				switch (opcion) {
				case 1:
					rs = stmt.executeQuery("SELECT * FROM persona");

					while (rs.next()) {
						int idp = rs.getInt("id");
						String nomp = rs.getString("nombre");
						int edadp = rs.getInt("edad");

						System.out.println("ID: " + idp + ", Nombre: " + nomp + ", Edad: " + edadp);

					}
					rs.close();
					break;

				case 2:
					rs = stmt.executeQuery("SELECT nombre FROM persona WHERE edad >= 18");
					while (rs.next()) {
						String nomp = rs.getString("nombre");

						System.out.println("Nombre: " + nomp);

					}

					rs.close();
				case 3:
					System.out.println("De cual id quieres saber su edad: ");
					int idIntroducida = s.nextInt();
					
					sel_pstmt = con.prepareStatement("SELECT edad FROM persona WHERE id=?");
					sel_pstmt.setInt(1,idIntroducida); 

					ResultSet rs_sel = sel_pstmt.executeQuery();
					while (rs_sel.next()) {
						int edadp = rs_sel.getInt("edad");
						System.out.println("Edad: " + edadp);
					}
					rs_sel.close();
					sel_pstmt.close();
					
					break;
					
				case 4:
					System.out.println("Introduce el id de la persona: ");
					int id = s.nextInt();
					
					System.out.println("Introduce el nombre de la persona: ");
					String nombre = s.next();
					
					System.out.println("Introduce la edad de la persona: ");
					int edad = s.nextInt();
					
					sel_pstmt = con.prepareStatement("INSERT INTO persona (id,nombre,edad) VALUES (?,?,?)");
					
					sel_pstmt.setInt(1, id);
					sel_pstmt.setString(2, nombre);
					sel_pstmt.setInt(3, edad);
					
					int rowsInserted = sel_pstmt.executeUpdate();
					
					sel_pstmt.close();
					
					break;
					
				case 5:
					System.out.println("Introduce la id de la persona que quieras eliminar: ");
					int idaEliminar = s.nextInt();

					sel_pstmt = con.prepareStatement("DELETE FROM persona WHERE id = ?");
					sel_pstmt.setInt(1, idaEliminar);

					int rowsDeleted =sel_pstmt.executeUpdate();
					sel_pstmt.close();

					break;

				case 6:
					System.out.println("Introduce la id de la persona que quieras modificar: ");
					int idaActualizar = s.nextInt();
					
					System.out.println("Introduce la nueva edad: ");
					int edadaActualizar = s.nextInt();
					
					sel_pstmt = con.prepareStatement("UPDATE persona SET edad = ? WHERE id = ?");
					
					sel_pstmt.setDouble(1, edadaActualizar);
					sel_pstmt.setInt(2, idaActualizar);
					
					int rowsUpdated = sel_pstmt.executeUpdate();
					sel_pstmt.close();
					
					
					break;
				}

				stmt.close();
				con.close();

			} catch (SQLException e) {
				System.out.println("Fallo de conexion");
			}

		} while (opcion < 7);

	}
}

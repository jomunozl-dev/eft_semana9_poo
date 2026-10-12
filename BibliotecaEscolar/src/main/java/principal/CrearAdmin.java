
package principal;

import dao.UsuarioDAO;
import modelo.Usuario;

public class CrearAdmin {

    public static void main(String[] args) {

        try {

            // Datos del bibliotecario inicial
            String nombre = "Administrador Biblioteca";
            String rut = "12345678-9";
            String correo = "admin@biblioteca.cl";
            String contrasena = "Admin2026*";

            Usuario administrador = new Usuario();

            administrador.setNombre(nombre);
            administrador.setRut(rut);
            administrador.setCorreo(correo);
            administrador.setContrasena(contrasena);
            administrador.setRol("BIBLIOTECARIO");

            UsuarioDAO dao = new UsuarioDAO();

            if (dao.guardar(administrador)) {

                System.out.println(
                    "Bibliotecario creado correctamente."
                );

                System.out.println(
                    "Correo: " + correo
                );

            } else {

                System.out.println(
                    "No se pudo crear el bibliotecario."
                );
            }

        } catch (Exception e) {

            System.out.println(
                "Error al crear bibliotecario: " + e.getMessage()
            );
        }
    }
}

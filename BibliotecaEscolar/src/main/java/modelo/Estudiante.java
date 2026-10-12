
package modelo;

public class Estudiante extends Usuario {

    private int idEstudiante;
    private int idUsuario;
    private String curso;

    public Estudiante() {
        super();
    }

    public Estudiante(int idEstudiante, int idUsuario,
                      String nombre, String rut, String curso,
                      String correo, String contrasena) {

        super(idUsuario, nombre, rut, correo,
              contrasena, "ESTUDIANTE");

        this.idEstudiante = idEstudiante;
        this.idUsuario = idUsuario;
        this.curso = curso;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
        super.setIdUsuario(idUsuario);
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}

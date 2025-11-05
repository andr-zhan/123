package server;

public class Utilizador {
    private int id;
    private String nome;
    private String email;
    private String tipo;
    private String estadoOperacional;
    private String estadoAdmin;

    public Utilizador(int id, String nome, String email, String tipo,
                      String estadoOperacional, String estadoAdmin) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.estadoOperacional = estadoOperacional;
        this.estadoAdmin = estadoAdmin;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTipo() { return tipo; }
    public String getEstadoOperacional() { return estadoOperacional; }
    public String getEstadoAdmin() { return estadoAdmin; }

    @Override
    public String toString() {
        return "Utilizador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estadoOperacional='" + estadoOperacional + '\'' +
                ", estadoAdmin='" + estadoAdmin + '\'' +
                '}';
    }
}

package server;

import java.sql.Date;

public class Emprestimo {
    private int id;
    private int utilizadorId;
    private int livroId;
    private Date dataEmprestimo;
    private Date dataDevolucao;
    private String estado;

    public Emprestimo(int id, int utilizadorId, int livroId,
                      Date dataEmprestimo, Date dataDevolucao, String estado) {
        this.id = id;
        this.utilizadorId = utilizadorId;
        this.livroId = livroId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.estado = estado;
    }

    public int getId() { return id; }
    public int getUtilizadorId() { return utilizadorId; }
    public int getLivroId() { return livroId; }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public Date getDataDevolucao() { return dataDevolucao; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", utilizadorId=" + utilizadorId +
                ", livroId=" + livroId +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucao=" + dataDevolucao +
                ", estado='" + estado + '\'' +
                '}';
    }
}

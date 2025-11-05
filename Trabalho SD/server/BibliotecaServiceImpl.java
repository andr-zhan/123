package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaServiceImpl extends UnicastRemoteObject implements BibliotecaService {

    public BibliotecaServiceImpl() throws RemoteException {
        super();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/biblioteca",
                "admin",
                "admin123"
        );
    }

    // ------------- CLIENTE GERAL ------------------

    @Override
    public boolean registarUtilizador(String nome, String email, String tipo) throws RemoteException {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO utilizadores (nome, email, tipo, estado_operacional, estado_admin) " +
                         "VALUES (?, ?, ?, 'ativo', 'nao_aprovado')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, tipo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERRO] registarUtilizador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean registarLivro(String titulo, String autor, String categoria) throws RemoteException {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO livros (titulo, autor, categoria, estado_operacional, estado_admin) " +
                         "VALUES (?, ?, ?, 'disponivel', 'nao_aprovado')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, categoria);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERRO] registarLivro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean realizarEmprestimo(int utilizadorId, int livroId) throws RemoteException {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO emprestimos (utilizador_id, livro_id, data_emprestimo, estado) " +
                         "VALUES (?, ?, CURRENT_DATE, 'ativo')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, utilizadorId);
            stmt.setInt(2, livroId);
            stmt.executeUpdate();

            // Atualiza estado do livro
            String sql2 = "UPDATE livros SET estado_operacional='emprestado' WHERE id=?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, livroId);
            stmt2.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("[ERRO] realizarEmprestimo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Livro> listarLivrosDisponiveis(String categoriaFiltro, String autorFiltro) throws RemoteException {
        List<Livro> lista = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM livros WHERE estado_operacional='disponivel' AND estado_admin='aprovado'";
            if (categoriaFiltro != null && !categoriaFiltro.isBlank()) sql += " AND categoria ILIKE '%" + categoriaFiltro + "%'";
            if (autorFiltro != null && !autorFiltro.isBlank()) sql += " AND autor ILIKE '%" + autorFiltro + "%'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                lista.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("categoria"),
                        rs.getString("estado_operacional"),
                        rs.getString("estado_admin")
                ));
            }

        } catch (SQLException e) {
            System.out.println("[ERRO] listarLivrosDisponiveis: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public String consultarEstadoLivro(int livroId) throws RemoteException {
        try (Connection conn = getConnection()) {
            String sql = "SELECT estado_operacional FROM livros WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("estado_operacional");
        } catch (SQLException e) {
            System.out.println("[ERRO] consultarEstadoLivro: " + e.getMessage());
        }
        return "Livro não encontrado";
    }

    @Override
    public List<String> consultarHistoricoLivro(int livroId) throws RemoteException {
        List<String> historico = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT e.id, u.nome, e.data_emprestimo, e.data_devolucao, e.estado " +
                         "FROM emprestimos e JOIN utilizadores u ON e.utilizador_id = u.id " +
                         "WHERE livro_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, livroId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                historico.add(
                        "Empréstimo " + rs.getInt("id") +
                        " | Utilizador: " + rs.getString("nome") +
                        " | Estado: " + rs.getString("estado") +
                        " | Data: " + rs.getString("data_emprestimo") +
                        " | Devolvido: " + rs.getString("data_devolucao")
                );
            }

        } catch (SQLException e) {
            System.out.println("[ERRO] consultarHistoricoLivro: " + e.getMessage());
        }
        return historico;
    }

    // ------------- CLIENTE ADMIN ------------------

    @Override
    public List<Utilizador> listarUtilizadoresPorEstado(String estado) throws RemoteException {
        List<Utilizador> lista = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM utilizadores WHERE estado_admin=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Utilizador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("estado_operacional"),
                        rs.getString("estado_admin")
                ));
            }

        } catch (SQLException e) {
            System.out.println("[ERRO] listarUtilizadoresPorEstado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Livro> listarLivrosPorEstado(String estado) throws RemoteException {
        List<Livro> lista = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM livros WHERE estado_admin=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("categoria"),
                        rs.getString("estado_operacional"),
                        rs.getString("estado_admin")
                ));
            }

        } catch (SQLException e) {
            System.out.println("[ERRO] listarLivrosPorEstado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean aprovarUtilizador(int utilizadorId) throws RemoteException {
        return updateSimple("UPDATE utilizadores SET estado_admin='aprovado' WHERE id=?", utilizadorId);
    }

    @Override
    public boolean aprovarLivro(int livroId) throws RemoteException {
        return updateSimple("UPDATE livros SET estado_admin='aprovado' WHERE id=?", livroId);
    }

    @Override
    public boolean alterarEstadoLivro(int livroId, String novoEstado) throws RemoteException {
        return updateSimple("UPDATE livros SET estado_operacional=? WHERE id=?", livroId, novoEstado);
    }

    @Override
    public boolean alterarEstadoUtilizador(int utilizadorId, String novoEstado) throws RemoteException {
        return updateSimple("UPDATE utilizadores SET estado_operacional=? WHERE id=?", utilizadorId, novoEstado);
    }

    @Override
    public boolean atualizarDadosUtilizador(int utilizadorId, String nome, String email, String tipo) throws RemoteException {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE utilizadores SET nome=?, email=?, tipo=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setObject(1, nome);
            stmt.setObject(2, email);
            stmt.setObject(3, tipo);
            stmt.setInt(4, utilizadorId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERRO] atualizarDadosUtilizador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizarDadosLivro(int livroId, String titulo, String autor, String categoria) throws RemoteException {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE livros SET titulo=?, autor=?, categoria=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setObject(1, titulo);   // se titulo = null → valor na BD será apagado
            stmt.setObject(2, autor);
            stmt.setObject(3, categoria);
            stmt.setInt(4, livroId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERRO] atualizarDadosLivro: " + e.getMessage());
            return false;
        }
    }

    // ------------- MÉTODOS AUXILIARES ------------------

    private boolean updateSimple(String sql, int id) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERRO] updateSimple: " + e.getMessage());
            return false;
        }
    }

    private boolean updateSimple(String sql, int id, String valor) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, valor);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERRO] updateSimple: " + e.getMessage());
            return false;
        }
    }
}

package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BibliotecaService extends Remote {

    // CLIENTE GERAL
    boolean registarUtilizador(String nome, String email, String tipo) throws RemoteException;
    boolean registarLivro(String titulo, String autor, String categoria) throws RemoteException;
    boolean realizarEmprestimo(int utilizadorId, int livroId) throws RemoteException;
    List<Livro> listarLivrosDisponiveis(String categoriaFiltro, String autorFiltro) throws RemoteException;
    String consultarEstadoLivro(int livroId) throws RemoteException;
    List<String> consultarHistoricoLivro(int livroId) throws RemoteException;

    // CLIENTE ADMIN
    List<Utilizador> listarUtilizadoresPorEstado(String estado) throws RemoteException;
    List<Livro> listarLivrosPorEstado(String estado) throws RemoteException;

    boolean aprovarUtilizador(int utilizadorId) throws RemoteException;
    boolean aprovarLivro(int livroId) throws RemoteException;

    boolean alterarEstadoLivro(int livroId, String novoEstado) throws RemoteException;
    boolean alterarEstadoUtilizador(int utilizadorId, String novoEstado) throws RemoteException;

    boolean atualizarDadosUtilizador(int utilizadorId, String nome, String email, String tipo) throws RemoteException;
    boolean atualizarDadosLivro(int livroId, String titulo, String autor, String categoria) throws RemoteException;
}

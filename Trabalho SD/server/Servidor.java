package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class Servidor {
    public static void main(String[] args) {
        try {
            // Instancia o serviço
            BibliotecaService service = new BibliotecaServiceImpl();

            // Cria o registry RMI na porta 1099 (padrão)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Regista o serviço com o nome "BibliotecaService"
            registry.rebind("BibliotecaService", service);

            System.out.println("✅ Servidor RMI iniciado e serviço registrado...");
        } catch (RemoteException e) {
            System.out.println("❌ Erro ao iniciar servidor: " + e.getMessage());
        }
    }
}

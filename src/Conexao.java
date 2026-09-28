import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/gestao_artesanato";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado com sucesso!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
    
    public static Connection getConexao() {
        return conectar();
    }
}

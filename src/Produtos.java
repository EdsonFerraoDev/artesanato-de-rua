import java.sql.*;
import java.util.Scanner;

public class Produtos {

    private static final String URL = "jdbc:mysql://localhost:3306/Artesanatos_de_Rua";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado com sucesso no MySQL!");
            
            int opcao;
            do {
                System.out.println("\n=== GESTÃO DE ARTESANATO ===");
                System.out.println("1 - Listar Produtos");
                System.out.println("2 - Cadastrar Produto");
                System.out.println("3 - Sair");
                System.out.print("Escolha: ");
                opcao = sc.nextInt();
                sc.nextLine(); // limpar buffer
                
                if(opcao == 1) {
                    listarProdutos(conn);
                }
                if(opcao == 2) {
                    cadastrarProduto(conn, sc);
                }
                
            } while(opcao != 3);
            
            conn.close();
            System.out.println("Sistema encerrado!");
            
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    public static void listarProdutos(Connection conn) throws SQLException {
        String sql = "SELECT * FROM artesanato";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        System.out.println("\n=== LISTA DE ARTESANATOS ===");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " - " + rs.getString("nome") + 
                               " | R$" + rs.getDouble("preco") + " | Qtd: " + rs.getInt("quantidade"));
        }
    }
public static void cadastrarProduto(Connection conn, Scanner sc) throws SQLException {
    System.out.print("Nome: ");
    String nome = sc.nextLine();
    System.out.print("Preço: ");
    double preco = sc.nextDouble();
    System.out.print("Quantidade: ");
    int qtd = sc.nextInt();
    sc.nextLine(); // limpar buffer
    
    String sql = "INSERT INTO artesanato (nome, preco, quantidade) VALUES (?, ?, ?)";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, nome);
    stmt.setDouble(2, preco);
    stmt.setInt(3, qtd);
    stmt.executeUpdate();
    
    System.out.println("Produto cadastrado com sucesso!");
}
}
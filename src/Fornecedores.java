import java.sql.*;
import java.util.Scanner;

public class Fornecedores {

    // Altera aqui com teus dados do MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/artesanatos_de_rua";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Conectado com sucesso no MySQL!");

            int opcao;
            do {
                System.out.println("\n=== GESTÃO DE FORNECEDORES ===");
                System.out.println("1. Cadastrar Fornecedor");
                System.out.println("2. Listar Fornecedores");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                opcao = sc.nextInt();
                sc.nextLine(); // limpa o buffer

                switch (opcao) {
                    case 1:
                        cadastrarFornecedor(conn, sc);
                        break;
                    case 2:
                        listarFornecedores(conn);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }

            } while (opcao != 0);

        } catch (SQLException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }
        sc.close();
    }

    // MÉTODO PARA CADASTRAR
    public static void cadastrarFornecedor(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Digite o nome do fornecedor: ");
        String nome = sc.nextLine();

        System.out.print("Digite o telefone: ");
        String telefone = sc.nextLine();

        // 2 colunas pra preencher
        String sql = "INSERT INTO fornecedores(nome_fornecedores, telefone) VALUES(?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, telefone);

            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Fornecedor cadastrado com sucesso!");
            }
        }
    }

    // MÉTODO PARA LISTAR
    public static void listarFornecedores(Connection conn) throws SQLException {
        String sql = "SELECT * FROM fornecedores";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- LISTA DE FORNECEDORES ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome_fornecedores");
                String telefone = rs.getString("telefone");

                System.out.println("ID: " + id + 
                                   " | Nome: " + nome + 
                                   " | Telefone: " + telefone);
            }
        }
    }
}
import java.sql.*;
import java.util.Scanner;

public class Cliente {
    private static final String URL = "jdbc:mysql://localhost:3306/artesanatos_de_rua";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conectado com sucesso no MySQL!");

            int opcao;
            do {
                System.out.println("\n=== GESTÃO DE CLIENTES ===");
                System.out.println("1 - Listar Clientes");
                System.out.println("2 - Cadastrar Cliente");
                System.out.println("3 - Sair");
                System.out.print("Escolha: ");
                opcao = Integer.parseInt(sc.nextLine());

                switch (opcao) {
                    case 1 -> listarClientes(conn);
                    case 2 -> cadastrarCliente(conn, sc);
                    case 3 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } while (opcao != 3);

            conn.close();
            sc.close();

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void cadastrarCliente(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Digite o nome: ");
        String nome = sc.nextLine();
        System.out.print("Digite a idade: ");
        int idade = Integer.parseInt(sc.nextLine());
        System.out.print("Digite o telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Digite o email: ");
        String email = sc.nextLine();
        System.out.print("Digite as preferencias: ");
        String preferencias = sc.nextLine();

        String sql = "INSERT INTO clientes(nome_cliente, idade, telefone, email, preferencias) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nome);
        pstmt.setInt(2, idade);
        pstmt.setString(3, telefone);
        pstmt.setString(4, email);
        pstmt.setString(5, preferencias);
        pstmt.executeUpdate();

        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void listarClientes(Connection conn) throws SQLException {
        String sql = "SELECT * FROM clientes";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- LISTA DE CLIENTES ---");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + 
                " | Nome: " + rs.getString("nome_cliente") + 
                " | Idade: " + rs.getInt("idade") + 
                " | Tel: " + rs.getString("telefone") + 
                " | Email: " + rs.getString("email") +
                " | Preferencias: " + rs.getString("preferencias"));
        }
    }
}
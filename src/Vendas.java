import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Vendas {

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
                System.out.println("\n=== GESTÃO DE VENDAS ===");
                System.out.println("1. Registrar Venda");
                System.out.println("2. Listar Vendas");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                opcao = sc.nextInt();

                switch (opcao) {
                    case 1:
                        registrarVenda(conn, sc);
                        break;
                    case 2:
                        listarVendas(conn);
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

    // MÉTODO PARA CADASTRAR VENDA
    public static void registrarVenda(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Digite o ID do cliente: ");
        int idCliente = sc.nextInt();

        System.out.print("Digite o ID do artesanato: ");
        int idArtesanato = sc.nextInt();

        System.out.print("Digite a quantidade: ");
        int quantidade = sc.nextInt();

        System.out.print("Digite o valor total: ");
        double valorTotal = sc.nextDouble();

        String data = LocalDate.now().toString(); // pega data de hoje automatico

        // AQUI ESTÃO OS 5 ? CERTINHOS
        String sql = "INSERT INTO vendas(id_cliente, id_artesanato, data_venda, quantidade, valor_total) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            pstmt.setInt(2, idArtesanato);
            pstmt.setString(3, data);
            pstmt.setInt(4, quantidade);
            pstmt.setDouble(5, valorTotal);

            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Venda registrada com sucesso!");
            }
        }
    }

    // MÉTODO PARA LISTAR VENDAS
    public static void listarVendas(Connection conn) throws SQLException {
        String sql = "SELECT * FROM vendas";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- LISTA DE VENDAS ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                int idCliente = rs.getInt("id_cliente");
                int idArtesanato = rs.getInt("id_artesanato");
                String data = rs.getString("data_venda");
                int qtd = rs.getInt("quantidade");
                double valor = rs.getDouble("valor_total");

                System.out.println("ID Venda: " + id + 
                                   " | ID Cliente: " + idCliente + 
                                   " | ID Artesanato: " + idArtesanato + 
                                   " | Data: " + data + 
                                   " | Qtd: " + qtd + 
                                   " | Valor: R$" + valor);
            }
        }
    }
}
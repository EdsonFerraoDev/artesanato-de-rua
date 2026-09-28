import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Gastos {
    private static final String URL = "jdbc:mysql://localhost:3306/artesanatos_de_rua";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Conectado com sucesso no MySQL!");
            int opcao;
            do {
                System.out.println("\n=== GESTÃO DE GASTOS ===");
                System.out.println("1. Registrar Gasto");
                System.out.println("2. Listar Gastos");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                opcao = sc.nextInt(); sc.nextLine();
                if(opcao == 1) registrarGasto(conn, sc);
                else if(opcao == 2) listarGastos(conn);
            } while (opcao != 0);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        sc.close();
    }

    public static void registrarGasto(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Digite o ID do fornecedor: ");
        int idFornecedor = sc.nextInt(); sc.nextLine();
        System.out.print("Digite a descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Digite o valor: ");
        double valor = Double.parseDouble(sc.nextLine().replace(",", "."));
        String data = LocalDate.now().toString();

        String sql = "INSERT INTO gastos(id_fornecedor, descrição, valor, date_gasto) VALUES(?, ?, ?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFornecedor);
            pstmt.setString(2, descricao);
            pstmt.setDouble(3, valor);
            pstmt.setString(4, data);
            pstmt.executeUpdate();
            System.out.println("Gasto registrado com sucesso!");
        }
    }

    public static void listarGastos(Connection conn) throws SQLException {
        String sql = "SELECT g.id, f.nome_fornecedores, g.descrição, g.valor, g.date_gasto FROM gastos g JOIN fornecedores f ON g.id_fornecedor = f.id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- LISTA DE GASTOS ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Fornecedor: %s | Desc: %s | Valor: R$%.2f | Data: %s\n",
                        rs.getInt("id"), rs.getString("nome_fornecedores"), rs.getString("descrição"), rs.getDouble("valor"), rs.getString("date_gasto"));
            }
        }
    }
}




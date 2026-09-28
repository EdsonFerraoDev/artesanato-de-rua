import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;

public class Contas {

    // ====== DADOS DA CONEXÃO - TROCA AQUI ======
    private static final String URL = "jdbc:mysql://localhost:3306/artesanatos_de_rua"; 
    private static final String USUARIO = "root"; 
    private static final String SENHA = "123456"; // COLOCA TUA SENHA DO MYSQL AQUI
    // ============================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n========== CONTAS A PAGAR / RECEBER ==========");
            System.out.println("1. Cadastrar Nova Conta");
            System.out.println("2. Listar Contas a PAGAR Pendentes");
            System.out.println("3. Listar Contas a RECEBER Pendentes");
            System.out.println("4. Marcar Conta como PAGA/RECEBIDA");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine(); // limpa buffer

            switch (opcao) {
                case 1: cadastrarConta(sc); break;
                case 2: listarContas("Pagar"); break;
                case 3: listarContas("Receber"); break;
                case 4: marcarComoPago(sc); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
        sc.close();
    }

    // CONECTAR NO BANCO
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    // 1. CADASTRAR CONTA - 5 COLUNAS CERTINHO
    private static void cadastrarConta(Scanner sc) {
        try (Connection conn = conectar()) {
            System.out.print("Tipo [Pagar/Receber]: ");
            String tipo = sc.nextLine();
            System.out.print("Descrição: ");
            String descricao = sc.nextLine();
            System.out.print("Valor: ");
            double valor = sc.nextDouble();
            sc.nextLine();

            // DATA DE HOJE AUTOMÁTICA
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dataVencimento = hoje.format(formato);

            String status = "Pendente"; // sempre começa como pendente

            System.out.println("Data Vencimento: " + dataVencimento + " [automático]");

            // 5 COLUNAS E 5 ?
            String sql = "INSERT INTO contas(tipo, descricao, valor, data_vencimento, status) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipo);           // 1
            pstmt.setString(2, descricao);      // 2
            pstmt.setDouble(3, valor);          // 3
            pstmt.setString(4, dataVencimento); // 4
            pstmt.setString(5, status);         // 5
            pstmt.executeUpdate();
            System.out.println(">>> Conta cadastrada com sucesso! <<<");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // 2. LISTAR CONTAS PENDENTES
    private static void listarContas(String tipo) {
        try (Connection conn = conectar()) {
            String sql = "SELECT * FROM contas WHERE tipo = ? AND status = 'Pendente' ORDER BY data_vencimento ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- CONTAS A " + tipo.toUpperCase() + " PENDENTES ---");
            double total = 0;
            boolean temConta = false;
            while (rs.next()) {
                temConta = true;
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | " + rs.getString("descricao") + 
                                   " | R$ " + String.format("%.2f", rs.getDouble("valor")) + 
                                   " | Vence: " + rs.getDate("data_vencimento"));
                total += rs.getDouble("valor");
            }
            if(!temConta) System.out.println("Nenhuma conta pendente.");
            System.out.println("-------------------------------------------");
            System.out.println("TOTAL PENDENTE: R$ " + String.format("%.2f", total));
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    // 3. MARCAR COMO PAGO
    private static void marcarComoPago(Scanner sc) {
        try (Connection conn = conectar()) {
            System.out.print("Digite o ID da conta para BAIXAR: ");
            int id = sc.nextInt();

            String sql = "UPDATE contas SET status = 'Pago' WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int linhas = pstmt.executeUpdate();

            if (linhas > 0) System.out.println(">>> Conta marcada como PAGA/RECEBIDA! <<<");
            else System.out.println("ID não encontrado!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}

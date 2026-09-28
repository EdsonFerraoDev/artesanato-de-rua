import java.sql.*;
import java.util.Scanner;

public class Deletar {

    private static final String URL = "jdbc:mysql://localhost:3306/artesanatos_de_rua"; 
    private static final String USUARIO = "root"; 
    private static final String SENHA = "123456"; 

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA)) {
            
            System.out.println("========== DELETAR DO SISTEMA ==========");
            System.out.println("1. Deletar CONTA");
            System.out.println("2. Deletar FORNECEDOR");
            System.out.println("3. Deletar GASTO");
            System.out.println("4. Deletar CLIENTE");
            System.out.print("Escolha o que quer deletar: ");
            opcao = sc.nextInt();
            sc.nextLine();

            System.out.print("Digite o ID para deletar: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Tem CERTEZA? S/N: ");
            String confirma = sc.nextLine();

            if (confirma.equalsIgnoreCase("S")) {
                String tabela = "";
                if(opcao == 1) tabela = "contas";
                else if(opcao == 2) tabela = "fornecedores";
                else if(opcao == 3) tabela = "gastos";
                else if(opcao == 4) tabela = "clientes";

                String sql = "DELETE FROM " + tabela + " WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                int linhas = pstmt.executeUpdate();

                if (linhas > 0) System.out.println(">>> Deletado com sucesso da tabela " + tabela + "! <<<");
                else System.out.println("ID não encontrado!");
            } else {
                System.out.println("Operação cancelada.");
            }
            
        } catch (SQLException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        sc.close();
    }
}

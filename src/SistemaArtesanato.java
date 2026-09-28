import java.util.Scanner;

public class SistemaArtesanato {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n====== SISTEMA LOJA ARTESANATOS DE RUA ======");
            System.out.println("1 - CLIENTE");
            System.out.println("2 - CONTAS");
            System.out.println("3 - FORNECEDORES");
            System.out.println("4 - GASTOS");
            System.out.println("5 - PRODUTOS");
            System.out.println("6 - VENDAS");
            System.out.println("7 - DELETAR");
            System.out.println("8 - SAIR");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine(); // limpa buffer

            switch (opcao) {
                case 1:
                    Cliente.main(args); // chama a classe Cliente
                    break;
                case 2:
                    Contas.main(args); // chama a classe Contas
                    break;
                case 3:
                    Fornecedores.main(args); // chama a classe Fornecedores
                    break;
                case 4:
                    Gastos.main(args); // chama a classe Gastos
                    break;
                case 5:
                    Produtos.main(args); // chama a classe Produtos
                    break;
                case 6:
                    Vendas.main(args); // chama a classe Vendas
                    break;
                case 7:
                    Deletar.main(args); // chama a classe Deletar
                    break;
                case 8:
                    System.out.println("Saindo do sistema... Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida! Tenta de novo.");
            }
        } while (opcao != 8);

        sc.close();
    }
}

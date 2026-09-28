import com.sun.net.httpserver.*;
import java.sql.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Servidor {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/produtos", (exchange) -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1); return;
            }
            try {
                Connection con = Conexao.conectar();
                if ("GET".equals(exchange.getRequestMethod())) {
                    ResultSet rs = con.createStatement().executeQuery("SELECT * FROM produtos");
                    StringBuilder json = new StringBuilder("[");
                    boolean first = true;
                    while(rs.next()){
                        if(!first) json.append(",");
                        json.append(String.format("{\"id\":%d,\"nome\":\"%s\",\"categoria\":\"%s\",\"custo\":%s,\"preco\":%s,\"quantidade\":%d,\"imagem\":\"%s\"}",
                            rs.getInt("id"), rs.getString("nome"), rs.getString("categoria"),
                            rs.getString("custo"), rs.getString("preco"), rs.getInt("quantidade"),
                            rs.getString("imagem").replace("\"","'").replace("\n","")));
                        first = false;
                    }
                    json.append("]");
                    byte[] resp = json.toString().getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, resp.length);
                    exchange.getResponseBody().write(resp);
                } else if ("POST".equals(exchange.getRequestMethod())) {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    String nome = body.split("\"nome\":\"")[1].split("\"")[0];
                    String categoria = body.split("\"categoria\":\"")[1].split("\"")[0];
                    String custo = body.split("\"custo\":")[1].split(",")[0].replace("\"","");
                    String preco = body.split("\"preco\":")[1].split(",")[0].replace("\"","");
                    String qtd = body.split("\"quantidade\":")[1].split("[,}]")[0].replace("\"","");
                    String imagem = body.split("\"imagem\":\"")[1].split("\"")[0];
                    PreparedStatement ps = con.prepareStatement("INSERT INTO produtos(nome,categoria,custo,preco,quantidade,imagem) VALUES(?,?,?,?,?,?)");
                    ps.setString(1, nome); ps.setString(2, categoria);
                    ps.setDouble(3, Double.parseDouble(custo)); ps.setDouble(4, Double.parseDouble(preco));
                    ps.setInt(5, Integer.parseInt(qtd)); ps.setString(6, imagem);
                    ps.executeUpdate();
                    String resp = "{\"status\":\"ok\"}";
                    exchange.sendResponseHeaders(200, resp.length());
                    exchange.getResponseBody().write(resp.getBytes());
                }
                exchange.close(); con.close();
            } catch(Exception e){ e.printStackTrace(); }
        });
        server.start();
        System.out.println("SERVIDOR RODANDO EM http://localhost:8080");
    }
}

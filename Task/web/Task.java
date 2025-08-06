//javac -cp "../lib/mysql-connector-j-9.3.0.jar" Task.java
// java -cp ".;../lib/mysql-connector-j-9.3.0.jar" Task
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.*;

public class Task {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/candidateDB";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "jasmin";

    public static void main(String[] args) throws IOException {
        try{
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/create", new CandidateHandler("CREATE"));
        server.createContext("/read", new CandidateHandler("READ"));
        server.createContext("/update", new CandidateHandler("UPDATE"));
        server.createContext("/delete", new CandidateHandler("DELETE"));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }catch(Exception e){System.out.println(e.getMessage());}
}

    static class CandidateHandler implements HttpHandler {
    private String operation;

    public CandidateHandler(String operation) {
        this.operation = operation;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers for all responses
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // Handle preflight (OPTIONS) requests
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1); // No content
            return;
        }

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();

        String response = performOperation(sb.toString(), operation);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String performOperation(String json, String op) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            Map<String, String> map = parseJson(json);
            String id = map.get("id");
            String name = map.get("name");
            String email = map.get("email");

            switch (op) {
                case "CREATE":
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO Candidates(name, email) VALUES (?, ?)");
                    ps1.setString(1, name);
                    ps1.setString(2, email);
                    int rows1 = ps1.executeUpdate();
                    return rows1 > 0 ? "Candidate created." : "Failed to create.";

                case "READ":
                    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM Candidates");
                    ResultSet rs = ps2.executeQuery();
                    StringBuilder sb = new StringBuilder();
                    while (rs.next()) {
                        sb.append("ID: ").append(rs.getInt("id"))
                          .append(", Name: ").append(rs.getString("name"))
                          .append(", Email: ").append(rs.getString("email"))
                          .append("\n");
                    }
                    return sb.toString().isEmpty() ? "No candidates found." : sb.toString();

                case "UPDATE":
                    PreparedStatement ps3 = conn.prepareStatement("UPDATE Candidates SET name=?, email=? WHERE id=?");
                    ps3.setString(1, name);
                    ps3.setString(2, email);
                    ps3.setInt(3, Integer.parseInt(id));
                    int rows3 = ps3.executeUpdate();
                    return rows3 > 0 ? "Candidate updated." : "Candidate not found.";

                case "DELETE":
                    PreparedStatement ps4 = conn.prepareStatement("DELETE FROM Candidates WHERE id=?");
                    ps4.setInt(1, Integer.parseInt(id));
                    int rows4 = ps4.executeUpdate();
                    return rows4 > 0 ? "Candidate deleted." : "Candidate not found.";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "Invalid operation";
    }

    private static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.replaceAll("[{}\"]", "");
        String[] parts = json.split(",");
        for (String part : parts) {
            String[] kv = part.split(":");
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }
}

}

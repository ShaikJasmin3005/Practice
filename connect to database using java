import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    // JDBC URL, username, and password
    private static final String URL = "jdbc:mysql://localhost:3306/CandidateDB";
    private static final String USER = "root";
    private static final String PASSWORD = "jasmin";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);alta

        System.out.println("Enter Candidate Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Candidate Email: ");
        String email = scanner.nextLine();

        System.out.println("Enter Candidate Phone: ");
        String phone = scanner.nextLine();

        System.out.println("Enter Candidate Experience (in years): ");
        int experience = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter Candidate Skills (comma-separated): ");
        String skills = scanner.nextLine();

        String insertQuery = "INSERT INTO Candidate (name, email, phone, experience, skills) VALUES (?, ?, ?, ?, ?)";
        scanner.close();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setInt(4, experience);
            preparedStatement.setString(5, skills);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Candidate data inserted successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
}

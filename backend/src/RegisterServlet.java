import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "INSERT INTO users " +
                     "(name, username, email, password, role) " +
                     "VALUES (?, ?, ?, ?, 'USER')";

        response.setContentType("text/html");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password);

            ps.executeUpdate();

            response.getWriter().println(
                "<h2>Registration Successful!</h2>" +
                "<p>Your account has been created.</p>" +
                "<a href='login.html'>Go to Login</a>"
            );

        } catch (Exception e) {

            response.getWriter().println(
                "<h2>Registration Failed</h2>" +
                "<p>" + e.getMessage() + "</p>" +
                "<a href='register.html'>Go Back</a>"
            );
        }
    }
}

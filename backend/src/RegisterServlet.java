import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String requestedRole = request.getParameter("role");
        String adminCode = request.getParameter("adminCode");

        // Normal registration can NEVER assign an admin role.
        String role = "USER";

        if ("ADMIN".equals(requestedRole)) {

            String secret = System.getenv("ADMIN_INVITE_CODE");

            if (secret == null || secret.isBlank()
                    || adminCode == null
                    || !java.security.MessageDigest.isEqual(
                        secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                        adminCode.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println(
                    "<h2>Admin registration denied</h2>" +
                    "<p>A valid admin invitation code is required.</p>" +
                    "<a href='register.html'>Go Back</a>"
                );
                return;
            }

            role = "ADMIN";
        }

        if (name == null || username == null || email == null
                || password == null || name.isBlank()
                || username.isBlank() || email.isBlank()
                || password.length() < 12) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(
                "<h2>Invalid registration details</h2>" +
                "<p>Complete every field and use a password " +
                "with at least 12 characters.</p>" +
                "<a href='register.html'>Go Back</a>"
            );
            return;
        }

        String sql = "INSERT INTO users " +
                     "(name, username, email, password, role) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, username.trim());
            ps.setString(3, email.trim());
            ps.setString(4, password);
            ps.setString(5, role);

            ps.executeUpdate();

            response.getWriter().println(
                "<h2>Registration Successful!</h2>" +
                "<p>Your account has been created.</p>" +
                "<a href='login.html'>Go to Login</a>"
            );

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {

            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().println(
                "<h2>Registration Failed</h2>" +
                "<p>That username or email may already be registered.</p>" +
                "<a href='register.html'>Try Again</a>"
            );

        } catch (Exception e) {

            getServletContext().log("Registration failed", e);
            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.getWriter().println(
                "<h2>Registration failed</h2>" +
                "<p>Please try again later.</p>" +
                "<a href='register.html'>Go Back</a>"
            );
        }
    }
}

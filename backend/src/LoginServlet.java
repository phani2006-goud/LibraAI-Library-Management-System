import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String sql = "SELECT user_id, name, role FROM users " +
                     "WHERE username=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession();

                session.setAttribute("user_id", rs.getInt("user_id"));
                session.setAttribute("name", rs.getString("name"));
                session.setAttribute("role", rs.getString("role"));

                if ("ADMIN".equals(rs.getString("role"))) {
                    response.sendRedirect("admin.html");
                } else {
                    response.sendRedirect("user.html");
                }

            } else {
                response.setContentType("text/html");
                response.getWriter().println(
                    "<h2>Invalid username or password</h2>"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println(
                "<h2>Login Error: " + e.getMessage() + "</h2>"
            );
        }
    }
}

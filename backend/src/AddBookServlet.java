import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String category = request.getParameter("category");
        String rackNo = request.getParameter("rack_no");
        String shelfNo = request.getParameter("shelf_no");
        String quantityText = request.getParameter("quantity");

        response.setContentType("text/html;charset=UTF-8");

        try {
            int quantity = Integer.parseInt(quantityText);

            String sql = "INSERT INTO books " +
                         "(title, author, category, rack_no, shelf_no, quantity) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, title);
                ps.setString(2, author);
                ps.setString(3, category);
                ps.setString(4, rackNo);
                ps.setString(5, shelfNo);
                ps.setInt(6, quantity);

                ps.executeUpdate();
            }

            response.getWriter().println(
                "<h2>Book Added Successfully!</h2>" +
                "<p><strong>Title:</strong> " + title + "</p>" +
                "<p><strong>Author:</strong> " + author + "</p>" +
                "<p><strong>Category:</strong> " + category + "</p>" +
                "<p><strong>Rack:</strong> " + rackNo + "</p>" +
                "<p><strong>Shelf:</strong> " + shelfNo + "</p>" +
                "<p><strong>Quantity:</strong> " + quantity + "</p>" +
                "<br><a href='add-book.html'>Add Another Book</a>" +
                "<br><a href='admin.html'>Back to Admin Dashboard</a>"
            );

        } catch (NumberFormatException e) {

            response.getWriter().println(
                "<h2>Invalid Quantity</h2>" +
                "<p>Please enter a valid number.</p>" +
                "<a href='add-book.html'>Go Back</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Unable to Add Book</h2>" +
                "<p>Database error occurred.</p>" +
                "<a href='add-book.html'>Go Back</a>"
            );
        }
    }
}

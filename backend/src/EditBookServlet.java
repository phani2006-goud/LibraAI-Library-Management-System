import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            int bookId = Integer.parseInt(request.getParameter("book_id"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String category = request.getParameter("category");
            String rackNo = request.getParameter("rack_no");
            String shelfNo = request.getParameter("shelf_no");

            String sql = "UPDATE books SET title=?, author=?, category=?, " +
                         "rack_no=?, shelf_no=?, quantity=? WHERE book_id=?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, title);
                ps.setString(2, author);
                ps.setString(3, category);
                ps.setString(4, rackNo);
                ps.setString(5, shelfNo);
                ps.setInt(6, quantity);
                ps.setInt(7, bookId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    response.getWriter().println(
                        "<h2>Book Updated Successfully!</h2>" +
                        "<p>Book ID: " + bookId + "</p>" +
                        "<a href='edit-book.html'>Edit Another Book</a><br>" +
                        "<a href='admin.html'>Back to Dashboard</a>"
                    );
                } else {
                    response.getWriter().println(
                        "<h2>Book Not Found</h2>" +
                        "<p>No book exists with ID " + bookId + ".</p>" +
                        "<a href='edit-book.html'>Go Back</a>"
                    );
                }
            }

        } catch (NumberFormatException e) {

            response.getWriter().println(
                "<h2>Invalid Input</h2>" +
                "<p>Book ID and Quantity must be numbers.</p>" +
                "<a href='edit-book.html'>Go Back</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Unable to Update Book</h2>" +
                "<p>Database error occurred.</p>" +
                "<a href='edit-book.html'>Go Back</a>"
            );
        }
    }
}

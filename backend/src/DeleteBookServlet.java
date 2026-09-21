import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            int bookId = Integer.parseInt(
                request.getParameter("book_id")
            );

            String sql = "DELETE FROM books WHERE book_id=?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, bookId);

                int rows = ps.executeUpdate();

                if (rows > 0) {

                    response.getWriter().println(
                        "<h2>Book Deleted Successfully!</h2>" +
                        "<p>Book ID " + bookId +
                        " has been deleted.</p>" +
                        "<br><a href='delete-book.html'>Delete Another Book</a>" +
                        "<br><a href='admin.html'>Back to Dashboard</a>"
                    );

                } else {

                    response.getWriter().println(
                        "<h2>Book Not Found</h2>" +
                        "<p>No book exists with ID " +
                        bookId + ".</p>" +
                        "<br><a href='delete-book.html'>Go Back</a>"
                    );
                }
            }

        } catch (NumberFormatException e) {

            response.getWriter().println(
                "<h2>Invalid Book ID</h2>" +
                "<a href='delete-book.html'>Go Back</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Unable to Delete Book</h2>" +
                "<p>Database error occurred.</p>" +
                "<a href='delete-book.html'>Go Back</a>"
            );
        }
    }
}

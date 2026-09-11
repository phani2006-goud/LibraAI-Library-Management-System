import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        PrintWriter out =
                response.getWriter();


        String sql =
                "SELECT " +
                "COUNT(*) AS total_books, " +
                "COALESCE(SUM(quantity), 0) AS available_copies, " +
                "COUNT(DISTINCT rack_no) AS total_racks " +
                "FROM books";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {

                int totalBooks =
                        rs.getInt("total_books");

                int availableCopies =
                        rs.getInt("available_copies");

                int totalRacks =
                        rs.getInt("total_racks");


                String json =
                        "{"
                        + "\"totalBooks\":"
                        + totalBooks
                        + ","
                        + "\"availableCopies\":"
                        + availableCopies
                        + ","
                        + "\"totalRacks\":"
                        + totalRacks
                        + "}";


                out.print(json);
            }


        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print(
                    "{\"error\":\"" +
                    e.getMessage()
                    + "\"}"
            );
        }
    }
}

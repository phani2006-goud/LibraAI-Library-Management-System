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

@WebServlet("/books")
public class BookServlet extends HttpServlet {

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
                "SELECT book_id, title, author, category, " +
                "rack_no, shelf_no, quantity " +
                "FROM books " +
                "ORDER BY book_id";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            StringBuilder json =
                    new StringBuilder();

            json.append("[");

            boolean first = true;


            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;


                json.append("{");

                json.append("\"book_id\":")
                        .append(rs.getInt("book_id"))
                        .append(",");

                json.append("\"title\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("title")
                                )
                        )
                        .append("\",");

                json.append("\"author\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("author")
                                )
                        )
                        .append("\",");

                json.append("\"category\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("category")
                                )
                        )
                        .append("\",");

                json.append("\"rack_no\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("rack_no")
                                )
                        )
                        .append("\",");

                json.append("\"shelf_no\":\"")
                        .append(
                                escapeJson(
                                        rs.getString("shelf_no")
                                )
                        )
                        .append("\",");

                json.append("\"quantity\":")
                        .append(rs.getInt("quantity"));

                json.append("}");
            }


            json.append("]");

            out.print(json.toString());


        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print(
                    "{\"error\":\"" +
                    escapeJson(e.getMessage()) +
                    "\"}"
            );
        }
    }


    private String escapeJson(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}

package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.format.HtmlFormat;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {
    public GetProductsServlet(final SqlRequestService sqlRequestService) {
         super(sqlRequestService);
    }

/*
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
                response.getWriter().println("<html><body>");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    response.getWriter().println(name + "\t" + price + "</br>");
                }
                response.getWriter().println("</body></html>");

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
*/

    @Override
    protected void doGetImpl(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String sql = "SELECT * FROM PRODUCT";
        final List<Map<String, Object>> table = sqlRequestService.executeQuery(sql);
        response.getWriter().println(HtmlFormat.newPage(HtmlFormat.productsToString(table)));
    }
}

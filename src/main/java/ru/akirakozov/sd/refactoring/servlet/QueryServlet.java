package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.format.HtmlFormat;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import javax.servlet.http.HttpServlet;
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
public class QueryServlet extends AbstractServlet {
    public QueryServlet(final SqlRequestService sqlRequestService) {
        super(sqlRequestService);
    }

/*
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h1>Product with max price: </h1>");

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
        } else if ("min".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h1>Product with min price: </h1>");

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
        } else if ("sum".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("Summary price: ");

                    if (rs.next()) {
                        response.getWriter().println(rs.getInt(1));
                    }
                    response.getWriter().println("</body></html>");

                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("Number of products: ");

                    if (rs.next()) {
                        response.getWriter().println(rs.getInt(1));
                    }
                    response.getWriter().println("</body></html>");

                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
*/

    @Override
    protected void doGetImpl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String command = request.getParameter("command");
        String sql;
        String header;
        boolean product;
        if ("max".equals(command)) {
            sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
            header = "Product with max price";
            product = true;
        } else if ("min".equals(command)) {
            sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
            header = "Product with min price";
            product = true;
        } else if ("sum".equals(command)) {
            sql = "SELECT SUM(price) FROM PRODUCT";
            header = "Summary price";
            product = false;
        } else if ("count".equals(command)) {
            sql = "SELECT COUNT(*) FROM PRODUCT";
            header = "Number of products";
            product = false;
        } else {
            response.getWriter().println("Unknown command: " + command);
            return;
        }

        final List<Map<String, Object>> table = sqlRequestService.executeQuery(sql);
        String htmlContent;
        if (product) {
            htmlContent = HtmlFormat.productsToString(header, table);
        } else {
            htmlContent = HtmlFormat.valuesToString(header, table);
        }
        response.getWriter().println(HtmlFormat.newPage(htmlContent));
    }
}

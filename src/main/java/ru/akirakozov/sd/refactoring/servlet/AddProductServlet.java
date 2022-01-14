package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.entity.Product;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {
    public AddProductServlet(final SqlRequestService sqlRequestService) {
        super(sqlRequestService);
    }

/*
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
*/

    @Override
    protected void doGetImpl(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Product product = new Product(request);
        final String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES " + product.toSqlFormat();
        sqlRequestService.executeUpdate(sql);

        response.getWriter().println("OK");
    }
}

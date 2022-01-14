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

    @Override
    protected void doGetImpl(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String sql = "SELECT * FROM PRODUCT";
        final List<Map<String, Object>> table = sqlRequestService.executeQuery(sql);
        response.getWriter().println(HtmlFormat.newPage(HtmlFormat.productsToString(table)));
    }
}

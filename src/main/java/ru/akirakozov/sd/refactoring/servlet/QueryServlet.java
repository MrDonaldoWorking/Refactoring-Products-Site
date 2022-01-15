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
        header = header + ": ";
        if (product) {
            htmlContent = HtmlFormat.productsToString(header, table);
        } else {
            htmlContent = HtmlFormat.valuesToString(header, table);
        }
        response.getWriter().println(HtmlFormat.newPage(htmlContent));
    }
}

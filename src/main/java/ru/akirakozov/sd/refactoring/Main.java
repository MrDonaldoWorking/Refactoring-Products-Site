package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(final String[] args) throws Exception {
        final SqlRequestService sqlRequestService = new SqlRequestService("jdbc:sqlite:test.db");

        final Server server = new Server(8081);

        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(sqlRequestService)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(sqlRequestService)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(sqlRequestService)),"/query");

        server.start();
        server.join();
    }
}

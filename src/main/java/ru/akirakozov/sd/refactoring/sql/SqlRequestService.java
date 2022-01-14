package ru.akirakozov.sd.refactoring.sql;

import java.sql.*;
import java.util.*;

public class SqlRequestService {
    private final String jdbcUrl;
    private Connection connection;

    public SqlRequestService(final String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;

    }

    private void createProductsIfNotExists() {
        final String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        executeUpdate(sql);
    }

    private void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(jdbcUrl);
            }
        } catch (final SQLException e) {
            throw new RuntimeException("Couldn't connect to SQL" + e.getMessage());
        }
    }

    public List<Map<String, Object>> executeQuery(final String query) {
        checkConnection();

        try (final Statement stmt = connection.createStatement()) {
            final ResultSet resultSet = stmt.executeQuery(query);

            final List<Map<String, Object>> table = new ArrayList<>();
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            final int columns = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                final Map<String, Object> row = new HashMap<>();
                for (int i = 0; i < columns; ++i) {
                    row.put(resultSetMetaData.getColumnName(i + 1).toLowerCase(Locale.ROOT), resultSet.getObject(i + 1));
                }
                table.add(row);
            }
            return table;
        } catch (final SQLException e) {
            throw new RuntimeException("Unexpected SQLException in executeQuery(): " + e.getMessage());
        }
    }

    public void executeUpdate(final String query) {
        checkConnection();

        try (final Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (final SQLException e) {
            throw new RuntimeException("Unexpected SQLException in executeUpdate(): " + e.getMessage());
        }
    }
}

package ru.akirakozov.sd.refactoring.format;

import ru.akirakozov.sd.refactoring.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HtmlFormat {
    private static final String CONTENT_TYPE = "text/html";
    private static final String NEW_LINE = "</br>";

    public static String getContentType() {
        return CONTENT_TYPE;
    }

    public static String getNewLine() {
        return NEW_LINE;
    }

    public static String newPage(final String content) {
        return String.format("<html><bode>%s</body></html>", content);
    }

    public static String withNewLine(final String content) {
        return content + NEW_LINE;
    }

    private static String products(final List<Product> products) {
        return products.stream()
                .map(Product::toHtmlFormat)
                .collect(Collectors.joining());
    }

    public static String productsToString(final List<Map<String, Object>> table) {
        return products(table.stream()
                .map(Product::new)
                .collect(Collectors.toList())
        );
    }

    public static String productsToString(final String header, final List<Map<String, Object>> table) {
        return withNewLine(header) + productsToString(table);
    }

    private static String valuesToString(final List<Map<String, Object>> table) {
        return table.stream()
                .map(row -> {
                    if (row.size() != 1) {
                        throw new RuntimeException("Expected table with 1 element row, found column size = " + row.size());
                    } else {
                        return row.values().toArray()[0].toString();
                    }
                })
                .collect(Collectors.joining());
    }

    public static String valuesToString(final String header, final List<Map<String, Object>> table) {
        return withNewLine(header) + valuesToString(table);
    }
}

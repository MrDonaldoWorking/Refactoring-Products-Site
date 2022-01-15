package ru.akirakozov.sd.refactoring.entity;

import ru.akirakozov.sd.refactoring.format.HtmlFormat;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Product {
    private static final String NAME_LABEL = "name";
    private static final String PRICE_LABEL = "price";

    private final String name;
    private final long price;

    private static <T> T getField(final Map<String, Object> row, final String label, Class<T> clazz) {
        final Object field = row.get(label);
        if (field == null) {
            throw new RuntimeException("Given row doesn't contain label " + label);
        }
        return clazz.cast(field);
    }

    public Product(final Map<String, Object> row) {
        this.name = getField(row, NAME_LABEL, String.class);

        long fromField;
        try {
            fromField = (long) getField(row, PRICE_LABEL, Integer.class);
        } catch (final ClassCastException e) {
            try {
                fromField = getField(row, PRICE_LABEL, Long.class);
            } catch (final ClassCastException ee) {
                throw new RuntimeException("Cannot get price class, expected int or long: " + ee.getMessage());
            }
        }
        this.price = fromField;
    }

    public Product(final HttpServletRequest request) {
        this.name = request.getParameter(NAME_LABEL);
        this.price = Long.parseLong(request.getParameter(PRICE_LABEL));
    }

    public String toHtmlFormat() {
        return HtmlFormat.withNewLine(name + "\t" + price);
    }

    public String toSqlFormat() {
        return "(\"" + name + "\", " + price + ")";
    }
}

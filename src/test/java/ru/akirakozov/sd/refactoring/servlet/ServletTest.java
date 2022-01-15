package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class ServletTest {
    private SqlRequestService sqlRequestService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    private final String OK_RESPONSE = "OK\n";

    @Before
    public void prepare() {
        sqlRequestService = new SqlRequestService("jdbc:sqlite:test.db");
        sqlRequestService.executeUpdate("DELETE FROM PRODUCT WHERE 1 = 1");
    }

    private void prepare2() throws IOException {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        writer = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    private String add(final String name, final String price) throws IOException {
        prepare2();
        Mockito.when(request.getParameter("name")).thenReturn(name);
        Mockito.when(request.getParameter("price")).thenReturn(price);

        new AddProductServlet(sqlRequestService).doGet(request, response);

        return writer.toString();
    }

    private String get() throws IOException {
        prepare2();
        new GetProductsServlet(sqlRequestService).doGet(request, response);
        return writer.toString();
    }

    private String query(final String command) throws IOException {
        prepare2();
        Mockito.when(request.getParameter("command")).thenReturn(command);

        new QueryServlet(sqlRequestService).doGet(request, response);

        return writer.toString();
    }

    @Test
    public void single() throws IOException {
        assertEquals(OK_RESPONSE, add("iphone", "300"));

        final String responseMin = query("min");
        assertTrue(responseMin.contains("300"));
        assertTrue(responseMin.contains("min"));
        assertTrue(responseMin.contains("body"));

        final String responseCount = query("count");
        assertTrue(responseCount.contains("1"));
        assertFalse(responseCount.contains("price"));
        assertTrue(responseCount.contains("html"));
    }

    @Test
    public void errors() throws IOException {
        assertThrows(NumberFormatException.class, () -> add("iphone", "steve"));
        assertTrue(query("bla-bla").contains("Unknown command"));
    }

    private String getRandString(final int length) {
        byte[] arr = new byte[length];
        final Random rand = new Random();
        for (int i = 0; i < length; ++i) {
            arr[i] = (byte) (rand.nextInt('z' - 'a' + 1) + 'a');
        }
        return new String(arr, StandardCharsets.UTF_8);
    }

    private String getRandString() {
        return getRandString(new Random().nextInt(255) + 1);
    }

    private String getRandInt() {
        return Long.toString(new Random().nextLong());
    }

    @Test
    public void randomGet() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");

        for (int test = 0; test < 10; ++test) {
            final String name = getRandString();
            final String price = getRandInt();
            assertEquals(OK_RESPONSE, add(name, price));

            sb.append(name).append("\t").append(price).append("</br>");
        }

        sb.append("</body></html>").append(System.lineSeparator());
        assertEquals(sb.toString(), get());
    }
}

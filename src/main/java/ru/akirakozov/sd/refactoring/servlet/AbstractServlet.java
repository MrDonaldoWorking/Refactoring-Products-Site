package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.format.HtmlFormat;
import ru.akirakozov.sd.refactoring.sql.SqlRequestService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractServlet extends HttpServlet {
    protected final SqlRequestService sqlRequestService;

    public AbstractServlet(final SqlRequestService sqlRequestService) {
        this.sqlRequestService = sqlRequestService;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        doGetImpl(request, response);
        response.setContentType(HtmlFormat.getContentType());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract void doGetImpl(final HttpServletRequest request, final HttpServletResponse response) throws IOException;
}

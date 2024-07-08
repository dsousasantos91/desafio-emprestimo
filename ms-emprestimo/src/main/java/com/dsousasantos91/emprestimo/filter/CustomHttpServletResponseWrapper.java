package com.dsousasantos91.emprestimo.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
    private final ServletOutputStream servletOutputStream;

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(javax.servlet.WriteListener writeListener) {
            }

            @Override
            public void write(int b) throws IOException {
                byteArrayOutputStream.write(b);
            }
        };
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        printWriter.flush();
        if (servletOutputStream != null) {
            servletOutputStream.flush();
        }
    }

    public byte[] getContentAsByteArray() throws IOException {
        flushBuffer();
        return byteArrayOutputStream.toByteArray();
    }
}





package com.danjitalk.danjitalk.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;

public class ResponseUtil {

    private ResponseUtil() {}

    public static void createResponseBody(HttpServletResponse response, HttpStatus status, Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(status.value());
    }

    public static void createResponseBody(HttpServletResponse response, HttpStatus status) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
    }
}

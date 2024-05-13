package com.learn.euphy.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class MainController {

    @Value("/WEB-INF/jsp/index.jsp")
    private String INDEX_PATH;

    @GetMapping("/hello")
    protected void hello(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String name = req.getParameter("name");
        if (name == null) name = "World";

        PrintWriter out = resp.getWriter();
        out.print("Hello " + name + "!");
    }

    @GetMapping("/")
    public void home(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(INDEX_PATH).forward(req, resp);
    }
}

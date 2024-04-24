package com.learn.euphy.controller;

import com.learn.euphy.model.User;
import com.learn.euphy.model.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Value("${path.view.index}")
    private String INDEX_PATH;

    @Value("${path.view.user}")
    private String USER_PATH;

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public void getUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var username = req.getParameter("username");
        if (!isValid(username)) {
            handleError(req, resp, "請輸入使用者名稱");
            return;
        }

        Optional<User> user = userService.accountBy(username);
        if (user.isPresent()) {
            req.setAttribute("user", user.get());
            req.getRequestDispatcher(USER_PATH).forward(req, resp);
        } else {
            handleError(req, resp, "查無資料");
        }
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("errors", List.of(msg));
        req.getRequestDispatcher(INDEX_PATH).forward(req, resp);
    }

    private boolean isValid(String username) {
        return username != null && !username.isEmpty();
    }
}

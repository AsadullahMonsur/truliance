package org.web.truliance.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error_page_view")
    private String handleError(Model model) {
        return "error_page";
    }

    public String getErrorPath() {
        return "/error_page_view";
    }
}
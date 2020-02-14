package be.wouterversyck.shoppinglistapi.admin.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin")
public class AdminController {

    @GetMapping
    public String test() {
        return "test";
    }

    @GetMapping("test")
    public String test2() {
        return "test2";
    }
}

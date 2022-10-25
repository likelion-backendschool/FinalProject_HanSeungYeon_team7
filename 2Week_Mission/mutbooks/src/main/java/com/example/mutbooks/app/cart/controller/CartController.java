package com.example.mutbooks.app.cart.controller;

import com.example.mutbooks.app.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {
    private final CartService cartService;
}

package com.powersupply.PES.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ManageController {

    @GetMapping("/management")
    public String getManagePage() {
        return "management";
    }
}

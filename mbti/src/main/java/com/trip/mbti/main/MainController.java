package com.trip.mbti.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
 
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest req, HttpServletResponse res, Model model){
        
        return "main";
    }

}

package com.example.demo.component;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    public ModelAndView defaultErrorHandler(String message, String view) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message",message);
        mav.setViewName(view);
        return mav;
    }

}

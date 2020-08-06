package com.artarkatesoft.controllers;

import com.artarkatesoft.exceptions.RecipeIdMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNumberFormatException(Exception exception, Model model) {
        log.error("Handling NumberFormatException." + exception.getMessage());
        model.addAttribute("exception", exception);
        return "400error";
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWebExchangeBindException(Exception exception, Model model) {
        log.error("Handling WebExchangeBindException." + exception.getMessage());
        model.addAttribute("exception", exception);
        return "400error";
    }

    @ExceptionHandler(RecipeIdMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRecipeIdMismatchException(Exception exception, Model model) {
        log.error("Handling RecipeIdMismatchException." + exception.getMessage());
        model.addAttribute("exception", exception);
        return "400error";
    }

}

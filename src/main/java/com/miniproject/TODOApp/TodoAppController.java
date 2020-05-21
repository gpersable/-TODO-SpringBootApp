package com.miniproject.TODOApp;

import com.miniproject.TODOApp.models.TODOList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
public class TodoAppController {
    @GetMapping("/")
    public String index(@ModelAttribute TODOList todo, Model model) {

        model.addAttribute("todoListsToday", TODOList.getTodoListsToday());
        model.addAttribute("todoListsTodayLength", TODOList.getTodoListsToday().size());

        return "index";
    }
    
    @GetMapping("/add")
    public String addTodo(Model model) {
        model.addAttribute("todo", new TODOList());
        return "addTodo";
    }

    @PostMapping("/add")
    public String submitAddTodo(@ModelAttribute TODOList todo) {
        TODOList.addTodo(todo);

        if (todo.isDueToday()) {
            TODOList.addTodoToday(todo);
        }
        
        return "addedTodo";
    }


}
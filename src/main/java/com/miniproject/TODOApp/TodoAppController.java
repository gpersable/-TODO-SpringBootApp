package com.miniproject.TODOApp;

import com.miniproject.TODOApp.models.TODOList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;


@Controller
public class TodoAppController {
    @GetMapping("/")
    public String index(@ModelAttribute TODOList todo, Model model) {
        LocalDate tgl = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd");
        String date = tgl.format(formatter);

        try{
            String quote = TODOList.randomQuote();
            model.addAttribute("quote", quote);
        }
        catch(FileNotFoundException ex){}
        catch(IOException x){}

        model.addAttribute("todaysDate", date);
        model.addAttribute("nearestDeadlines", TODOList.getNearestDeadlines());
        return "index";
    }

    @GetMapping("/today")
    public String today(@ModelAttribute TODOList todo, Model model) {

        model.addAttribute("todo", todo);
        model.addAttribute("todoListsToday", TODOList.getTodoListsToday());
        model.addAttribute("todoListsTodayLength", TODOList.getTodoListsToday().size());

        return "today";
    }

    @PostMapping("/today")
    public String submitTodoToday(@ModelAttribute TODOList todo, Model model) {
        TODOList.deleteTodo(todo);

        model.addAttribute("todo", todo);
        model.addAttribute("todoListsToday", TODOList.getTodoListsToday());
        model.addAttribute("todoListsTodayLength", TODOList.getTodoListsToday().size());
        return "today";
    }

    @GetMapping("/all")
    public String allTodos(@ModelAttribute TODOList todo, Model model) {

        model.addAttribute("todo", todo);
        model.addAttribute("todoLists", TODOList.getTodoLists());
        model.addAttribute("todoListsLength", TODOList.getTodoLists().size());

        return "allTodos";
    }

    @PostMapping("/all")
    public String submitTodo(@ModelAttribute TODOList todo, Model model) {
        TODOList.deleteTodo(todo);

        model.addAttribute("todo", todo);
        model.addAttribute("todoLists", TODOList.getTodoLists());
        model.addAttribute("todoListsLength", TODOList.getTodoLists().size());
        return "allTodos";
    }
    
    @GetMapping("/add")
    public String addTodo(Model model) {
        model.addAttribute("todo", new TODOList());
        return "addTodo";
    }

    @PostMapping("/add")
    public String submitAddTodo(@ModelAttribute TODOList todo) {
        TODOList.addTodo(todo);

        // if (todo.isDueToday()) {
        //     TODOList.addTodoToday(todo);
        // }
        
        return "addedTodo";
    }

    @GetMapping("/about")
    public String aboutTodo(Model model){
        return "about";
    }


}
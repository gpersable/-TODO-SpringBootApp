package com.miniproject.TODOApp.models;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TODOList {
    private static ArrayList<TODOList> todoLists = new ArrayList<>();
    private static ArrayList<TODOList> todoListsToday;
    private String name;
    private String description;
    private String dueDate;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String date) {
        this.dueDate = date;
    }

    public static ArrayList<TODOList> getTodoLists() {
        ArrayList<TODOList> newTodoLists = new ArrayList<>();

        for (TODOList todo : todoLists){
            if (!hasPassed(todo.getDueDate())){
                newTodoLists.add(todo);
            }
        }
        Collections.sort(newTodoLists, new Comparator<TODOList>() {
            @Override
            public int compare(TODOList o1, TODOList o2) {
                int value1 = LocalDate.parse(o1.getDueDate()).compareTo(LocalDate.parse(o2.getDueDate()));
                if (value1 == 0) {
                    return o1.getName().compareTo(o2.getName());
                }
                return value1;
            }
        });
        return newTodoLists;
    }

    public static ArrayList<TODOList> getTodoListsToday() {
        ArrayList<TODOList> todoListsToday = new ArrayList<>();

        for (TODOList todo : todoLists){
            if (todo.isDueToday()){
                todoListsToday.add(todo);
            }
        }
        Collections.sort(todoListsToday, new Comparator<TODOList>() {
            @Override
            public int compare(TODOList o1, TODOList o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return todoListsToday;
    }

    public static void addTodo(TODOList todo) {
        todoLists.add(todo);

        // for ((Iterator<TODOList> iterator = todoLists.iterator(); iterator.hasNext(); ) {
        //     TODOList t = iterator.next();
        //     if (t.isDueToday()) {
        //         todoListsToday.add(t);
        //         iterator.remove();
        //     })
        // }
    }

    public static void deleteTodo(TODOList todo) {
        todoLists.remove(todo);
    }

    public static void addTodoToday(TODOList todo){
        todoListsToday.add(todo);
    }

    public Boolean isDueToday() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateNow = date.format(formatter);

        if (dueDate.equals(dateNow)) {
            return true;
        }

        return false;
    } 

    public static Boolean hasPassed(String other) {
        LocalDate date = LocalDate.now();
        if (date.compareTo(LocalDate.parse(other)) > 0) {
            return true;
        }
        return false;
    }
    
}
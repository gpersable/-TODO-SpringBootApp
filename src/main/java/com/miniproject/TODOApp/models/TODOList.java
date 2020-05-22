package com.miniproject.TODOApp.models;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;


public class TODOList {
    private static final String CSV_PATH = "./TODOList.csv";
    // private static ArrayList<TODOList> todoLists = getTodoLists();
    // private static ArrayList<TODOList> todoListsToday = getTodoListsToday();
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
        ArrayList<TODOList> listTodo = new ArrayList<>();
        ArrayList<TODOList> newListTodo = new ArrayList<>();
        try (
            Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ) {
            for (CSVRecord csvRecord : csvParser) {
                TODOList todo = new TODOList();

                todo.name = csvRecord.get(0);
                todo.dueDate = csvRecord.get(1);
                todo.description = csvRecord.get(2);

                listTodo.add(todo);
            }
        } catch (Exception e){}

        for (TODOList todo : listTodo){
            if (!hasPassed(todo.getDueDate())){
                newListTodo.add(todo);
            }
        }
        Collections.sort(newListTodo, new Comparator<TODOList>() {
            @Override
            public int compare(TODOList o1, TODOList o2) {
                int value1 = LocalDate.parse(o1.getDueDate()).compareTo(LocalDate.parse(o2.getDueDate()));
                if (value1 == 0) {
                    return o1.getName().compareTo(o2.getName());
                }
                return value1;
            }
        });
        return newListTodo;
    }

    public static ArrayList<TODOList> getTodoListsToday() {
        ArrayList<TODOList> newListsToday = new ArrayList<>();

        for (TODOList todo : getTodoLists()){
            if (todo.isDueToday()){
                newListsToday.add(todo);
            }
        }
        Collections.sort(newListsToday, new Comparator<TODOList>() {
            @Override
            public int compare(TODOList o1, TODOList o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return newListsToday;
    }

    public static void addTodo(TODOList todo){
        try (
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_PATH), StandardCharsets.UTF_8, 
                StandardOpenOption.APPEND);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ) {
            csvPrinter.printRecord(todo.getName(), todo.getDueDate(), todo.getDescription());
            csvPrinter.flush();            
        } catch (Exception e){}
    }

    // public static void deleteTodo(TODOList todo) {
    //     todoLists.remove(todo);
    // }


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
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
import com.opencsv.CSVReader; 
// import com.opencsv.CSVWriter;


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

    public String parseDueDate(String date) {
        // buat dipake di templates, biar tanggalnya lebih human readable
        // contoh: input user 2020-04-04, di viewnya April 4, 2020
        LocalDate tgl = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, uuuu");
        String date2 = tgl.format(formatter);
        return date2;
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
            csvParser.close();
        } catch (Exception e){}

        for (TODOList todo : listTodo){
            if (!hasPassed(todo.getDueDate())){
                // kalau belum lewat hari ini
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

        // agar todoListsToday ter-update tiap di-get
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
            csvPrinter.close();      
        } catch (Exception e){}
    }

    public static void deleteTodo(TODOList todo) {
        // ArrayList<TODOList> newList = new ArrayList<>();

        for (TODOList t : getTodoLists()){
            if (t.equals(todo)){
                getTodoLists().remove(todo);
            }
        }

        for (TODOList t : getTodoListsToday()) {
            if (t.equals(todo)) {
                getTodoListsToday().remove(todo);
            }
        }

        try (
            Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            CSVReader csvReader = new CSVReader(reader);
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_PATH), StandardCharsets.UTF_8, 
                StandardOpenOption.APPEND);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ) {
            // list ini isinya semua row yang ada di file csv nya
            List<String[]> allElements = csvReader.readAll();
            for (CSVRecord csvRecord : csvParser) {
                // si objek todo gak masuk ke kondisi ini
                // jadinya dia gak ngeremove row yg dianya
                // mungkin masalahnya di form html-nya (thymeleaf) ?
                // help gais
                if (todo.getName().equals(csvRecord.get(0)) && todo.getDueDate().equals(csvRecord.get(1))
                    && todo.getDescription().equals(csvRecord.get(2))) {
                        allElements.remove((int)csvParser.getCurrentLineNumber()); // atau ada masalah di sini??
                        csvPrinter.printRecords(allElements);
                        csvPrinter.flush();
                }
            }
            csvParser.close();
            csvPrinter.close();
        } catch (Exception e){}
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

    public static TODOList[] getNearestDeadlines() {
        if (getTodoLists().size() != 0) {
            if (getTodoLists().size() == 1) {
                TODOList[] nearestDeadlines = new TODOList[1];
                nearestDeadlines[0] = getTodoLists().get(0);
                return nearestDeadlines;
            }
            else if (getTodoLists().size() == 2) {
                TODOList[] nearestDeadlines = new TODOList[2];
                for(int i = 0; i < 2; i++) {
                    nearestDeadlines[i] = getTodoLists().get(i);
                }
                return nearestDeadlines;
            }
            else if (getTodoLists().size() >= 3) {
                TODOList[] nearestDeadlines = new TODOList[3];
                for(int i = 0; i < 3; i++) {
                    nearestDeadlines[i] = getTodoLists().get(i);
                }
                return nearestDeadlines;
            }
        }
        TODOList[] nearestDeadlines = {};
        return nearestDeadlines;
    }

    public static String randomQuote() throws FileNotFoundException, IOException {
        // File quotes = new File("./src/main/resources/static/text/quotes.txt");
        // Scanner sc = new Scanner(quotes);
        Random random = new Random();
        
        int randomLine = random.nextInt(9); //for now, ada 10 quotes di quotes.txt
        String quote = Files.readAllLines(
                        Paths.get("./src/main/resources/static/text/quotes.txt")
                        ).get(randomLine);

        // for (int i = 0; i < randomLine; i++) {
        //     quote = sc.nextLine();
        // }

        // sc.close();
        return quote;
    }
    
}
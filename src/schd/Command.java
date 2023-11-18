package schd;

import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Map;

public class Command {
    private int course = -1;
    private int group = -1;
    private String day;

    public Command(int course, int group, String day) {
        this.course = course;
        this.group = group;
        this.day = day;
    }

    public Command(String day) {
        this.day = day;
    }

    public Command(int course, int group) {
        this.course = course;
        this.group = group;
    }

    public Command(int course) {
        this.course = course;
    }

    public Command(int course, String day) {
        this.course = course;
        this.day = day;
    }

    public Command() {
    }

    public void execute(Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary){
        int c = 0;
        if(day.equals("Суббота") || day.equals("Воскресенье")) {
            System.out.println("пар нет");
            return;
        }
        for(Table.Cell<Integer, Integer, Map<String, ArrayList<String>>> cell : itinerary.cellSet()){
            for(Map.Entry<String, ArrayList<String>> entry : cell.getValue().entrySet()){
                if(cell.getRowKey() == course && cell.getColumnKey() == group && entry.getKey().equals(day)) {
                    System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + entry.getKey() + " " + entry.getValue().toString());
                    c = 1;
                }
                else if(entry.getKey().equals(day) && cell.getRowKey() == course && group == -1){
                    System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + entry.getKey() + " " + entry.getValue().toString());
                    c = 1;
                }
                else if(entry.getKey().equals(day) && course == -1 && group == -1){
                    System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + entry.getKey() + " " + entry.getValue().toString());;
                    c = 1;
                }
            }
            if(cell.getRowKey() == course && cell.getColumnKey() == group && c == 0){
                System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + cell.getValue().toString());
            }
            else if(cell.getRowKey() == course && c == 0 && group == -1){
                System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + cell.getValue().toString());
            }
        }
    }
    public void printAllSchedule(Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary){
        for(Table.Cell<Integer, Integer, Map<String, ArrayList<String>>> cell : itinerary.cellSet()){
            System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + cell.getValue().toString());
        }
    }
}
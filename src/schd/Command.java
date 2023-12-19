package schd;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Table<Integer, Integer, Map<String, ArrayList<String>>> execute(Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary){
        Table<Integer, Integer, Map<String, ArrayList<String>>> it = HashBasedTable.create();
        if(day.equals("Суббота") || day.equals("Воскресенье")) {
            return it;
        }
        for(Table.Cell<Integer, Integer, Map<String, ArrayList<String>>> cell : itinerary.cellSet()){
            for(Map.Entry<String, ArrayList<String>> entry : cell.getValue().entrySet()){
                if(cell.getRowKey() == course && cell.getColumnKey() == group && entry.getKey().equals(day)) {
                    Map<String, ArrayList<String>> temp = new HashMap<>();
                    temp.put(entry.getKey(), entry.getValue());
                    it.put(cell.getRowKey(), cell.getColumnKey(), temp);
                    //System.out.println("курс: " + cell.getRowKey() + " " + "Группа: " + cell.getColumnKey() + " " + entry.getKey() + " " + entry.getValue().toString());
                    return it;
                }
                else if(entry.getKey().equals(day) && cell.getRowKey() == course && group == -1){
                    Map<String, ArrayList<String>> temp = new HashMap<>();
                    temp.put(entry.getKey(), entry.getValue());
                    it.put(cell.getRowKey(), cell.getColumnKey(), temp);
                    return it;
                }
                else if(entry.getKey().equals(day) && course == -1 && group == -1){
                    Map<String, ArrayList<String>> temp = new HashMap<>();
                    temp.put(entry.getKey(), entry.getValue());
                    it.put(cell.getRowKey(), cell.getColumnKey(), temp);
                    return it;
                }
            }
            if(cell.getRowKey() == course && cell.getColumnKey() == group){
                it.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
                return it;
            }
            else if(cell.getRowKey() == course && group == -1){
                it.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
                return it;
            }
        }
        return it;
    }
    public Table<Integer, Integer, Map<String, ArrayList<String>>> printAllSchedule(Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary){
        Table<Integer, Integer, Map<String, ArrayList<String>>> it = HashBasedTable.create();
        it.putAll(itinerary);
        return it;
    }
}
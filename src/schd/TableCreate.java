package schd;

import com.google.common.collect.Table;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TableCreate {
    public static DefaultTableModel createModel(Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary, String day){
        DefaultTableModel model = new DefaultTableModel();
        if(itinerary.isEmpty()){
            model.addColumn(null, new String[]{"пар нет"});
            return model;
        }
        List<String> crss = new ArrayList<>(List.of(new String[]{"",""}));
        List<String> grps = new ArrayList<>(List.of(new String[]{"",""}));

        if(day.isEmpty()){
            String[] days = new String[]{
                    "Понедельник",
                    "Вторник",
                    "Среда",
                    "Четверг",
                    "Пятница"
            };
            List<String> time = new ArrayList<>(List.of(new String[]{"8:00 - 9:35",
                    "9:45 - 11:20",
                    "11:30 - 13:05",
                    "13:25 - 15:00"}));

            List<String> t1 = new ArrayList<>();
            for(String ignored : days) {
                t1.addAll(time);
            }
            int c = 0;
            int i = 0;
            List<String> day1 = new ArrayList<>();
            for(String str: t1){
                if(c == 0){
                    day1.add(days[i]);
                    i++;
                }
                else {
                    day1.add("");
                }
                c++;
                if(c == time.size()) c = 0;
            }
            model.addColumn(null, day1.toArray());
            model.addColumn(null, t1.toArray());
            fillModel(model, itinerary, time, days);
        }
        else{
            String[] days = {day};
            List<String> time = new ArrayList<>(List.of(new String[]{"8:00 - 9:35",
                    "9:45 - 11:20",
                    "11:30 - 13:05",
                    "13:25 - 15:00"}));
            int t = 0;

            List<String> t1 = new ArrayList<>();
            for(String ignored : days) {
                t1.addAll(time);
            }
            model.addColumn(null, days);
            model.addColumn(null, t1.toArray());
            fillModel(model, itinerary, time, days);
        }
        int t = 1;
        for(Table.Cell<Integer, Integer, Map<String, ArrayList<String>>> cell: itinerary.cellSet()){
            if(t ==  Math.round((float) itinerary.size() / 2)){
                crss.add(String.valueOf(cell.getRowKey()) + " курс");
            } else {
                crss.add("");
            }
            grps.add(String.valueOf(cell.getColumnKey()) + " группа");
            t++;
        }

        model.insertRow(0, crss.toArray());
        model.insertRow(1, grps.toArray());
        return model;
    }

    private static void fillModel(DefaultTableModel model, Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary, List<String> time, String[] days){
        int t = 0;
        List<String> sbj = new ArrayList<>();
        for(Table.Cell<Integer, Integer, Map<String, ArrayList<String>>> cell: itinerary.cellSet()){
            for(String day: days){
            for(Map.Entry<String, ArrayList<String>> entry: cell.getValue().entrySet()){
                if(day.equals(entry.getKey())){
                for(String str: entry.getValue()){
                    t++;
                    sbj.add(str);
                }
                while(t < time.size()){
                    sbj.add("");
                    t++;
                }
                t = 0;
                }
            }
        }
            model.addColumn(null, sbj.toArray());
            sbj.clear();
        }
    }
}

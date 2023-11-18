package schd;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.cli.Options;
import org.checkerframework.checker.units.qual.N;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

public class Main {
    static ArrayList<Lesson> curriculum;
    static ArrayList<Professor> professors;
    static Map<String, ArrayList<Lesson>> schedule;
    static Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary;
    static Map<Integer, ArrayList<Group>> courseMap;
    public static void main(String[] args) throws IOException {
        ArrayList<String> week = new ArrayList<>();
        week.add("Понедельник");
        week.add("Вторник");
        week.add("Среда");
        week.add("Четверг");
        week.add("Пятница");

        courseMap = new HashMap<>();
        courseMap.put(1, new ArrayList<>());
        for(int i = 0; i < 7; i++){
            courseMap.get(1).add(new Group(i));
        }

        schedule = new HashMap<>();
        for(String day: week){
            schedule.put(day, new ArrayList<>());
        }
        curriculum = new ArrayList<>();
        curriculum.add(new Lesson(1 ,1, "предмет_1" ));
        curriculum.add(new Lesson(1 ,2, "предмет_2" ));
        curriculum.add(new Lesson(1 ,2, "предмет_3" ));
        curriculum.add(new Lesson(1 ,4, "предмет_4" ));
        curriculum.add(new Lesson(1 ,1, "предмет_5" ));
        curriculum.add(new Lesson(1 ,1, "предмет_6" ));
        curriculum.add(new Lesson(1 ,2, "предмет_7" ));
        professors = new ArrayList<>();
        professors.add(new Professor("преп_1", "предмет_1"));
        professors.add(new Professor("преп_2", "предмет_2"));
        professors.add(new Professor("преп_3", "предмет_3"));
        professors.add(new Professor("преп_4", "предмет_4"));
        professors.add(new Professor("преп_5", "предмет_5"));
        professors.add(new Professor("преп_6", "предмет_6"));
        professors.add(new Professor("преп_7", "предмет_7"));
        itinerary = makeSchedule(courseMap, schedule, professors, curriculum);
        ArgumentsReader ar = new ArgumentsReader();
        ar.read(curriculum,
                professors,
                schedule,
                courseMap);
        //handleCommands();
    }

    public static Table<Integer, Integer, Map<String, ArrayList<String>>> makeSchedule(Map<Integer, ArrayList<Group>> course, Map<String, ArrayList<Lesson>> schedule, ArrayList<Professor> professors, ArrayList<Lesson> curriculum){
        Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary = HashBasedTable.create();

        for(Map.Entry<Integer, ArrayList<Group>> crsEntry: course.entrySet()){
            int crs = crsEntry.getKey();
            ArrayList<Group> groups = crsEntry.getValue();
            for(Group group: groups){
                int grp = group.getNumeric();
                clearLessons(schedule);
                assignLessons(schedule, curriculum, crs);
                optimize(schedule);
                Map<String, ArrayList<String>> map = new HashMap<>();
                for(Map.Entry<String, ArrayList<Lesson>> schd: schedule.entrySet()){
                    Collections.shuffle(schd.getValue());
                    ArrayList<String> list = new ArrayList<>();
                    ArrayList<Lesson> lessons = schd.getValue();
                    map.put(schd.getKey(), list);
                    for(Lesson lesson: lessons){
                        for(Professor professor: professors) {
                            if(professor.getSubject().equals(lesson.getName())) map.get(schd.getKey()).add(lesson.getName() + " " + professor.getSurname());
                        }
                    }
                }
                itinerary.put(crs, grp, map);
            }
        }
        return itinerary;
    }

    public static void clearLessons(Map<String, ArrayList<Lesson>> schedule){
        for(Map.Entry<String, ArrayList<Lesson>> entry: schedule.entrySet()){
            entry.getValue().clear();
        }
    }
    public static void assignLessons(Map<String, ArrayList<Lesson>> schedule, ArrayList<Lesson> lessons, int course){
        int lessonIndex = 0;
        int maxPairs = schedule.size();
        while(lessonIndex < lessons.size()){
            Lesson lesson = lessons.get(lessonIndex);
            int currentHours = lesson.getAcademicHour();
            for(Map.Entry<String, ArrayList<Lesson>> entry : schedule.entrySet()){
                ArrayList<Lesson> dayLessons = entry.getValue();
                if(dayLessons.size() >= maxPairs) continue;
                if(currentHours > 0  && lesson.getCourse() == course){
                    dayLessons.add(lesson);
                    currentHours--;
                }
                if(currentHours == 0) break;
            }
            lessonIndex++;
        }
    }
    public static void optimize(Map<String, ArrayList<Lesson>> schedule){
        int left = 0;
        int right = schedule.size() - 1;
        while(left < right){
            int max = 0;
            int min = 0;
            int dif = Math.abs(schedule.get(getDayByIndex(schedule, left)).size() - schedule.get(getDayByIndex(schedule, right)).size());
            if(schedule.get(getDayByIndex(schedule, left)).size() > schedule.get(getDayByIndex(schedule, right)).size()){
                max = left;
                min = right;
            }
            if(schedule.get(getDayByIndex(schedule, left)).size() < schedule.get(getDayByIndex(schedule, right)).size()) {
                max = right;
                min = left;
            }
            if(dif >= 2) {
                for(int i = 0; i < Math.round(dif / 2); i++){
                    Collections.shuffle(schedule.get(getDayByIndex(schedule, max)));
                    Lesson temp = schedule.get(getDayByIndex(schedule, max)).remove(0);
                    schedule.get(getDayByIndex(schedule, min)).add(temp);
                }
            }
            left++;
            right--;
        }
    }
    private static String getDayByIndex(Map<String, ArrayList<Lesson>> schedule, int index) {
        int i = 0;
        for (String day : schedule.keySet()) {
            if (i == index) {
                return day;
            }
            i++;
        }
        return null;
    }

    public static ArrayList<Lesson> getCurriculum() throws IOException {
        BufferedReader brd = new BufferedReader(new InputStreamReader(System.in));

        ArrayList<Lesson> curriculum = new ArrayList<>();

        while (true){
            if(curriculum.isEmpty()) System.out.println("нету учебного плана\nВведите предметы");
            else {
                System.out.println("\nучебный план");
                for(Lesson subject: curriculum){
                    System.out.println(subject.getCourse() + " курс " + subject.getName() + " " + subject.getAcademicHour() + " часов");
                }
            }
            String str = brd.readLine();
            if(str.equals("Показать расписание")) break;
            try{
                String[] split = str.split(" ");
                curriculum.add(new Lesson(Integer.parseInt(split[0]), Math.round(Integer.parseInt(split[2]) / 20), split[1]));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return curriculum;
    }

    public static ArrayList<Professor> getProfessors()throws IOException{
        BufferedReader brd = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Professor> professors = new ArrayList<>();

        while (true){
            if(professors.isEmpty()) System.out.println("нету преподовательского состава\nВведите преподов");
            for(Professor professor: professors){
                System.out.println(professor.getSurname() + " преподает " + professor.getSubject());
            }
            String str = brd.readLine();
            if(str.equals("Конец")) break;
            try{
                String[] split = str.split(" ");
                professors.add(new Professor(split[0], split[1]));
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return professors;
    }
}
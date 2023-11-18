package schd;

import com.google.common.collect.Table;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class ArgumentsReader {
    public void read(ArrayList<Lesson> curriculum,
                     ArrayList<Professor> professors,
                     Map<String, ArrayList<Lesson>> schedule,
                     Map<Integer, ArrayList<Group>> courseMap)
    {
        Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary;
        Options options = new Options();
        Option show = Option.builder("s")
                .longOpt("show")
                .desc("show schedule for <course number> <group number> <day name>/ show full schedule(-s all)")
                .hasArgs()
                .build();
        show.setRequired(false);
        options.addOption(show);

        Option delete = Option.builder("d")
                .longOpt("delete")
                .desc("delete subj in curriculum(-d all)/ <course number> <group number> <day name>(с большой буквы) <subj name>")
                .hasArgs()
                .build();
        delete.setRequired(false);
        options.addOption(delete);

        Option add = Option.builder("a")
                .longOpt("add")
                .desc("add subj in curriculum(<course number> <academic hour> <subj name> <professor name>/ <course number> <group number> " +
                        "<day name>(с большой буквы) <subj name> <professor name>")
                .hasArgs()
                .build();
        add.setRequired(false);
        options.addOption(add);

        options.addOption(new Option("h", "help", false, "print help message"));
        options.addOption(new Option("b", "break", false, "stop program"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("[OPTIONS]...", options);
        while(true){
            itinerary = Main.makeSchedule(courseMap, schedule, professors, curriculum);
        try{
            String args = reader.readLine();
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, Arrays.stream(args.split(" ")).toArray(String[]::new));
            if(cmd.hasOption("help")) {
                formatter.printHelp("[OPTIONS]...", options);
                continue;
            }
            if(cmd.hasOption("break")) break;
            String[] s = cmd.getOptionValues("show");
            String[] a = cmd.getOptionValues("add");
            String[] d = cmd.getOptionValues("delete");
            if(!(s == null)){
                if(s[0].equals("all")){
                    Command command = new Command();
                    command.printAllSchedule(itinerary);
                    continue;
                }
                int course = Integer.parseInt(s[0]);
                int group = s.length > 1 ? Integer.parseInt(s[1]) : -1;
                String day = s.length > 2 ? s[2] : "";
                if(day.equals("сегодня")){
                    day = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
                    String replacement = day.substring(0,1).toUpperCase();
                    day = replacement + day.substring(1);
                }

                Command command = new Command(course, group, day);
                command.execute(itinerary);
            }
            else if (!(a == null)) {
                if(a.length == 4){
                    curriculum.add(new Lesson(Integer.parseInt(a[0]), Integer.parseInt(a[1]), a[2]));
                    professors.add(new Professor(a[3], a[2]));
                }
                else itinerary.get(Integer.parseInt(a[0]),Integer.parseInt(a[1])).get(a[2]).add(a[3] + " " + a[4]);
            }
            else if (!(d == null)) {
                if(d[0].equals("all")) {
                    itinerary.clear();
                    professors.clear();
                    curriculum.clear();
                }
                else {
                    int course = Integer.parseInt(d[0]);
                    int group = Integer.parseInt(d[1]);
                    String day = d[2];
                    String subjName = d[3];
                    for(Table.Cell<Integer, Integer, Map<String, ArrayList<String>>> cell : itinerary.cellSet()){
                        for(Map.Entry<String, ArrayList<String>> entry : cell.getValue().entrySet()){
                            if(entry.getKey().equals(day)) {
                                entry.getValue().removeIf(str -> str.contains(subjName));
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParseException e){
            e.printStackTrace();
            formatter.printHelp("[OPTIONS]...", options);
            }
        }
    }
}

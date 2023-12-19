package schd;

import com.google.common.collect.Table;
import org.apache.commons.cli.*;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import javax.swing.text.*;

public class ArgumentsReader implements ButtonPress {
    Options options;
    Form form;

    public ArgumentsReader(){
        setOptions();
    }
    private void setOptions(){
        options = new Options();
        Option show = Option.builder("s")
                .longOpt("show")
                .desc("show schedule for <course number> <group number> <day name>/ show full schedule(-s all)/" +
                        " on week for group(<course num> <group num>)/ on week for course" +
                        "<course num>")
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
    }
    private void handle(ArrayList<Lesson> curriculum,
                        ArrayList<Professor> professors,
                        Map<String, ArrayList<Lesson>> schedule,
                        Map<Integer, ArrayList<Group>> courseMap,
                        Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary,
                        Form form){
        try{
            help(form, options);
            String args = String.valueOf(form.getTextField());
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, Arrays.stream(args.split(" ")).toArray(String[]::new));
            if(cmd.hasOption("help")) {
                help(form, options);
                form.getLabel().setForeground(Color.RED);
            }
            if(cmd.hasOption("break")) {
                form.dispose();
            }
            String[] s = cmd.getOptionValues("show");
            String[] a = cmd.getOptionValues("add");
            String[] d = cmd.getOptionValues("delete");
            if(!(s == null)){
                if(s[0].equals("all")){
                    Command command = new Command();
                    form.updateTable(TableCreate.createModel(command.printAllSchedule(itinerary), ""));
                    //continue;
                }
                else{
                int course = Integer.parseInt(s[0]);
                int group = s.length > 1 ? Integer.parseInt(s[1]) : -1;
                String day = s.length > 2 ? s[2] : "";
                if(day.equals("сегодня")){
                    day = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
                    String replacement = day.substring(0,1).toUpperCase();
                    day = replacement + day.substring(1);
                }
                Command command = new Command(course, group, day);
                form.updateTable(TableCreate.createModel(command.execute(itinerary), day));
                }
            }
            else if (!(a == null)) {
                if(a.length == 4){
                    curriculum.add(new Lesson(Integer.parseInt(a[0]), Integer.parseInt(a[1]), a[2]));
                    professors.add(new Professor(a[3], a[2]));
                    Main.itinerary = Main.makeSchedule(courseMap, schedule, professors, curriculum);
                }
                else Main.itinerary.get(Integer.parseInt(a[0]),Integer.parseInt(a[1])).get(a[2]).add(a[3] + " " + a[4]);
            }
            else if (!(d == null)) {
                if(d[0].equals("all")) {
                    itinerary.clear();
                    professors.clear();
                    curriculum.clear();
                    form.updateTable(TableCreate.createModel(itinerary, ""));
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
            else{
                help(form, options);
                form.setStyle(Color.RED);
                String str = "Wrong command: " + Arrays.toString(cmd.getArgs());
                form.getTextPane().getDocument().insertString(form.getTextPane().getDocument().getLength(), str, null);
                form.getTextPane().getStyledDocument().setCharacterAttributes(form.getTextPane().getDocument().getLength() - str.length(), str.length(), form.getStyle(), false);
                form.setStyle(Color.BLACK);
            }
        } catch (ParseException | BadLocationException exception){
            help(form, options);
            exceptionMessage(form, (ParseException) exception);
        }
    }

    private void exceptionMessage(Form form, ParseException exception){
        try {
            form.setStyle(Color.RED);
            String msg = exception.getMessage();
            form.getTextPane().getDocument().insertString(form.getTextPane().getDocument().getLength(), msg, null);
            form.getTextPane().getStyledDocument().setCharacterAttributes(form.getTextPane().getDocument().getLength() - msg.length(), msg.length(), form.getStyle(), false);
            form.setStyle(Color.BLACK);
        } catch (BadLocationException ignored){
        }
    }

    private void help(Form form, Options options){
        HelpFormatter formatter = new HelpFormatter();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        formatter.printHelp(printWriter, 80, "[OPTIONS]...", null, options, 2, 2, null);


        form.getTextPane().setText(stringWriter.toString());
    }

    public void read(ArrayList<Lesson> curriculum,
                     ArrayList<Professor> professors,
                     Map<String, ArrayList<Lesson>> schedule,
                     Map<Integer, ArrayList<Group>> courseMap){
        form = new Form();
        help(form, options);
        Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary;
        itinerary = Main.makeSchedule(courseMap, schedule, professors, curriculum);
        form.setPressed(ArgumentsReader.this);
    }

    @Override
    public void buttonPressed(ArrayList<Lesson> curriculum,
                              ArrayList<Professor> professors,
                              Map<String, ArrayList<Lesson>> schedule,
                              Map<Integer, ArrayList<Group>> courseMap,
                              Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary,
                              Form form){
        handle(curriculum, professors, schedule, courseMap,itinerary, form);
    }
}

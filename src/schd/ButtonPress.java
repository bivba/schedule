package schd;

import com.google.common.collect.Table;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface ButtonPress {

    void buttonPressed(ArrayList<Lesson> curriculum,
                       ArrayList<Professor> professors,
                       Map<String, ArrayList<Lesson>> schedule,
                       Map<Integer, ArrayList<Group>> courseMap,
                       Table<Integer, Integer, Map<String, ArrayList<String>>> itinerary,
                       Form form);
}

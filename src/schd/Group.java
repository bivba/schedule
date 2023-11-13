package schd;

import java.util.ArrayList;

public class Group {
    private int numeric;
    private ArrayList<Student> students;

    public int getNumeric() {
        return numeric;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setNumeric(int numeric) {
        this.numeric = numeric;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
    public void addStudent(Student student){
        this.students.add(student);
    }

    public Group(int numeric) {
        this.numeric = numeric;
    }
    public Group(){}

    public Group(int numeric, ArrayList<Student> students) {
        this.numeric = numeric;
        this.students = students;
    }
}

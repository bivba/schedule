package schd;

public class Lesson {
    private int course;
    private int academicHour;
    private String name;

    public int getCourse() {
        return course;
    }

    public int getAcademicHour() {
        return academicHour;
    }

    public String getName() {
        return name;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public void setAcademicHour(int academicHour) {
        this.academicHour = academicHour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lesson(int course, int academicHour, String name) {
        this.course = course;
        this.academicHour = academicHour;
        this.name = name;
    }
    public Lesson(){}
    public boolean compare(Lesson lesson){
        return this.name.equals(lesson.getName()) && this.course == lesson.getCourse() && this.academicHour == lesson.getAcademicHour();
    }

}

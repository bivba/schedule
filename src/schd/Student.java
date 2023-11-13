package schd;

public class Student {
    private String name;
    private String surname;
    private String studentCard;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getStudentCard() {
        return studentCard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setStudentCard(String studentCard) {
        this.studentCard = studentCard;
    }

    public Student(String name, String surname, String studentCard) {
        this.name = name;
        this.surname = surname;
        this.studentCard = studentCard;
    }
    public Student(){}
}

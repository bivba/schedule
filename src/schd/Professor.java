package schd;

public class Professor {
    private String surname;
    private String subject;
    private String post;


    public String getSurname() {
        return surname;
    }

    public String getSubject() {
        return subject;
    }

    public String getPost() {
        return post;
    }


    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Professor(String surname, String subject) {
        this.surname = surname;
        this.subject = subject;
    }
    public Professor(){}
}

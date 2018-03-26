package service;

public class Exam {
    String name;
    int id;
    String lu;

    public Exam(String name, int id, String lu) {
        this.name = name;
        this.id = id;
        this.lu = lu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLu() {
        return lu;
    }

    public void setLu(String lu) {
        this.lu = lu;
    }

    @Override
    public String toString() {
        return name;
    }
}

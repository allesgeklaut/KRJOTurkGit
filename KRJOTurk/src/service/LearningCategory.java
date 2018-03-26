package service;

public class LearningCategory extends LearningInstance {

    int la_id;

    public LearningCategory(int id, String name, String description, int la_id) {
        super(id, name, description);
        this.la_id = la_id;
    }

    public int getLa_id() {
        return la_id;
    }

    public void setLa_id(int la_id) {
        this.la_id = la_id;
    }

    @Override
    public String toString() {
        return name;
    }
}

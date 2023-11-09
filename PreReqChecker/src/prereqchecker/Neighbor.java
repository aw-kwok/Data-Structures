package prereqchecker;

public class Neighbor {
    private String course;
    private Neighbor next;
    private int index;
    
    public Neighbor() {
    }

    public Neighbor(String course, Neighbor next, int index) {
        this.course = course;
        this.next = next;
        this.index = index;
    }
    
    public String getCourse() {
        return this.course;
    }

    public Neighbor getNext() {
        return this.next;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}

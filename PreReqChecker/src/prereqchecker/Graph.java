package prereqchecker;

import java.util.*;

public class Graph {
    String[] courseList;
    Neighbor[] adjList;
    int size;

    public Graph(String input) {
        build(input);
    }

    public int searchCourse(String target) {
        for (int i = 0; i < courseList.length; i++) {
            if (courseList[i].equals(target)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    public void build(String input) {
        StdIn.setFile(input);
        size = StdIn.readInt();
        courseList = new String[size];
        adjList = new Neighbor[size];
        for (int i = 0; i < size; i++) {
            courseList[i] = StdIn.readString();
        }
        int numPrereq = StdIn.readInt();
        for (int i = 0; i < numPrereq; i++) {
            String course = StdIn.readString();
            String prereq = StdIn.readString();
            int courseIndex = this.searchCourse(course);
            int prereqIndex = this.searchCourse(prereq);
            adjList[courseIndex] = new Neighbor(prereq, adjList[courseIndex], prereqIndex);
        }
    }

    public String[] getCourseList() {
        return this.courseList;
    }

    public Neighbor[] getAdjList() {
        return this.adjList;
    }

    public int getSize() {
        return this.size;
    }

    public Neighbor getAdj(int index) {
        return adjList[index];
    }

    public Neighbor getAdj(String course) {
        return getAdj(searchCourse(course));
    }

    public String getCourse(int index) {
        return courseList[index];
    }

    public boolean[] DFS(Neighbor neighbor, boolean[] mark) {
        if (neighbor != null) {
            mark[neighbor.getIndex()] = true;
            int index = this.searchCourse(neighbor.getCourse());
            Neighbor current = this.getAdj(index);
            while (current != null) {
                if (!mark[current.getIndex()]) {
                    mark = this.DFS(current, mark);
                }
                current = current.getNext();
            }
        }
        return mark;
    }

    public int[] maxDistDFS(Neighbor neighbor, int[] distArray, boolean[] mark, int dist) {
        dist++;
        if (neighbor != null && !mark[neighbor.getIndex()]) {
            if (dist > distArray[neighbor.getIndex()]) {
                distArray[neighbor.getIndex()] = dist;
            }
            int index = this.searchCourse(neighbor.getCourse());
            Neighbor current = this.getAdj(index);
            while (current != null) {
                distArray = this.maxDistDFS(current, distArray, mark, dist);
                current = current.getNext();
            }
        }
        return distArray;
    }

}

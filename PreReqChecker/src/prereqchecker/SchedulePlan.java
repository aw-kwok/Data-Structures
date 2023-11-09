package prereqchecker;

import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
            return;
        }

        Graph graph = new Graph(args[0]);
        
        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);

        String target = StdIn.readString();
        int taken = StdIn.readInt();
        boolean[] mark = new boolean[graph.getSize()];

        for (int i = 0; i < taken; i++) {
            String course = StdIn.readString();
            mark[graph.searchCourse(course)] = true;
            Neighbor current = graph.getAdj(course);
            while (current != null) {
                mark = graph.DFS(current, mark);
                current = current.getNext();
            }
        }

        int[] distArray = new int[graph.getSize()];
        Neighbor current = graph.getAdj(target);

        while (current != null) {
            distArray = graph.maxDistDFS(current, distArray, mark, 0);
            current = current.getNext();
        }

        int maxSem = 0;

        for (int i = 0; i < graph.getSize(); i++) {
            if (distArray[i] > maxSem) {
                maxSem = distArray[i];
            }
        }
        
        StdOut.println(maxSem);

        while (maxSem > 0) {
            for (int j = 0; j < graph.getSize(); j++) {
                if (!mark[j] && distArray[j] == maxSem) {
                    StdOut.print(graph.getCourse(j) + " ");
                }
            }
            StdOut.println();
            maxSem--;
        }
    }
}

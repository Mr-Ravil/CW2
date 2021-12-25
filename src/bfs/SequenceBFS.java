package bfs;

import java.util.*;

public class SequenceBFS implements BFS {
    @Override
    public List<Distance> compute(int[][] graph, int start) {
        List<Distance> distances = new ArrayList<>(Collections.nCopies(graph.length, null));
        Queue<Distance> queue = new LinkedList<>();

        distances.set(start, new Distance(0, -1));
        queue.add(new Distance(1, start));

        while (!queue.isEmpty()) {
            Distance current = queue.remove();

            for (int next : graph[current.Parent]) {
                if (distances.get(next) == null) {
                    distances.set(next, current);
                    queue.add(new Distance(current.Distance + 1, next));
                }
            }
        }

        return distances;
    }

}

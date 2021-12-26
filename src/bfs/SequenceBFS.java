package bfs;

import simulator.graph.GraphSimulator;

import java.util.*;

public class SequenceBFS implements BFS {
    @Override
    public List<Distance> compute(GraphSimulator graph, int start) {
//        List<Distance> distances = new ArrayList<>(Collections.nCopies(graph.getSize(), null));
        int[] dist = new int[graph.getSize()];
        Queue<Distance> queue = new LinkedList<>();
        List<Boolean> used = new ArrayList<>(Collections.nCopies(graph.getSize(), false));
//        Set<Integer> used = new HashSet<>();
//        used.add(start);

        dist[start] = 0;
//        distances.set(start, new Distance(0, -1));
        queue.add(new Distance(1, start));

        while (!queue.isEmpty()) {
            Distance current = queue.remove();

            for (int next : graph.getNeighbours(current.Parent)) {
                if (!used.get(next)) {
                    used.set(next, true);
                    dist[next] = current.Distance + 1;
//                if (distances.get(next) == null) {
//                if (!used.contains(next)) {
//                    used.add(next);
//                    distances.set(next, current);
                    queue.add(new Distance(current.Distance + 1, next));
                }
            }
        }

        return null;
    }

}

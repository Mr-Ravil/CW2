package bfs;

import simulator.graph.GraphSimulator;

import java.util.*;

public class SequenceBFS implements BFS {
    @Override
    public int[] compute(int[][] graph, int start) {
        int[] dist = new int[graph.length];
        Queue<Distance> queue = new LinkedList<>();
        Set<Integer> used = new HashSet<>();
        used.add(start);

        dist[start] = 0;
        queue.add(new Distance(1, start));

        while (!queue.isEmpty()) {
            Distance current = queue.remove();

            for (int next : graph[current.Parent]) {
                if (!used.contains(next)) {
                    dist[next] = current.Distance;
                    used.add(next);
                    queue.add(new Distance(current.Distance + 1, next));
                }
            }
        }

        return dist;
    }

}

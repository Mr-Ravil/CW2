package bfs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SequenceBFS implements BFS {
    @Override
    public List<Distance> compute(List<List<Integer>> graph, int start) {
        List<Distance> distances = Stream.generate(Distance::new).limit(graph.size()).collect(Collectors.toList());
        Queue<Distance> queue = new LinkedList<>();

        distances.set(start, new Distance(0, -1));
        queue.add(new Distance(1, start));

        while (!queue.isEmpty()) {
            Distance current = queue.remove();

            for (int next : graph.get(current.Parent)) {
                if (distances.get(next).Distance > current.Distance) {
                    distances.set(next, current);
                    queue.add(new Distance(current.Distance + 1, next));
                }
            }
        }

        return distances;
    }

}

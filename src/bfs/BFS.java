package bfs;

import java.util.List;

public interface BFS {
    List<Distance> compute(List<List<Integer>> graph, int start);

    class Distance {
        public int Distance;
        public int Parent;

        public Distance() {
            Distance = Integer.MAX_VALUE;
            Parent = -1;
        }

        public Distance(int distance, int parent) {
            Distance = distance;
            Parent = parent;
        }
    }
}

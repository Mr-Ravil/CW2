package bfs;

import parallel.util.ParallelUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelBFS implements BFS {
    private int BLOCK_SIZE = 1000;

    public ParallelBFS() {
    }

    public ParallelBFS(int BLOCK_SIZE) {
        this.BLOCK_SIZE = BLOCK_SIZE;
    }

    @Override
    public List<Distance> compute(List<List<Integer>> graph, int start) {
        ParallelUtil parallelUtils = new ParallelUtil(BLOCK_SIZE);

        List<Distance> distances = new ArrayList<>(Collections.nCopies(graph.size(), null));
        List<AtomicBoolean> flag = Stream.generate(AtomicBoolean::new).limit(graph.size()).collect(Collectors.toList());

        int[] frontier = new int[1];
        int[] startBlock = new int[1];

        distances.set(start, new Distance(0, -1));
        frontier[0] = start;
        flag.get(start).set(true);

        int distance = 1;
        int nextFrontierSize = graph.get(start).size();

        while (nextFrontierSize != 0) {
            int[] nextFrontier = new int[nextFrontierSize];
            int[] nextDeg = new int[nextFrontierSize];

            parallelUtils.parallelFor(nextFrontierSize, i -> nextFrontier[i] = -1);

            int finalDistance = distance;
            int[] finalFrontier = frontier;
            int[] finalStartBlock = startBlock;

            parallelUtils.parallelFor(frontier.length, currentIndex -> {
                int current = finalFrontier[currentIndex];
                if (current != -1) {
                    for (int i = 0; i < graph.get(current).size(); i++) {
                        int next = graph.get(current).get(i);
                        if (flag.get(next).compareAndSet(false, true)) {
                            distances.set(next, new Distance(finalDistance, current));

                            nextFrontier[finalStartBlock[currentIndex] + i] = next;
                            nextDeg[finalStartBlock[currentIndex] + i] = graph.get(next).size();
                        }
                    }
                }
            });

            frontier = nextFrontier;
            startBlock = parallelUtils.parallelScan(nextDeg);

            nextFrontierSize = startBlock[startBlock.length - 1];
            distance++;
        }

        return distances;
    }
}

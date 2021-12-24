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
        List<Integer> frontier = new ArrayList<>();
        List<Integer> startBlock = new ArrayList<>();
        List<Integer> nextFrontier = new ArrayList<>(Collections.nCopies(graph.get(start).size(), -1));
        List<Integer> nextDeg = new ArrayList<>(Collections.nCopies(graph.get(start).size(), 0));

        distances.set(start, new Distance(0, -1));
        frontier.add(start);
        startBlock.add(0);
        flag.get(start).set(true);

        int distance = 1;

        while (!nextFrontier.isEmpty()) {

            List<Integer> finalFrontier = frontier;
            List<Integer> finalStartBlock = startBlock;
            List<Integer> finalNextFrontier = nextFrontier;
            List<Integer> finalNextDeg = nextDeg;
            int finalDistance = distance;

            parallelUtils.parallelFor(finalFrontier.size(), currentIndex -> {
                int current = finalFrontier.get(currentIndex);
                if (current != -1) {
                    for (int i = 0; i < graph.get(current).size(); i++) {
                        int next = graph.get(current).get(i);
                        if (flag.get(next).compareAndSet(false, true)) {
                            distances.set(next, new Distance(finalDistance, current));

                            finalNextFrontier.set(finalStartBlock.get(currentIndex) + i, next);
                            finalNextDeg.set(finalStartBlock.get(currentIndex) + i, graph.get(next).size());
                        }
                    }
                }
            });

            frontier = nextFrontier;
            startBlock = parallelUtils.parallelScan(nextDeg);

            int nextFrontierSize = startBlock.get(startBlock.size() - 1);
            nextFrontier = new ArrayList<>(Collections.nCopies(nextFrontierSize, -1));
            nextDeg = new ArrayList<>(Collections.nCopies(nextFrontierSize, 0));

            distance++;
        }

        return distances;
    }
}

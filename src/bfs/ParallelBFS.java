package bfs;

import parallel.util.ParallelUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ParallelBFS implements BFS {
    private int P_FOR_BLOCK_SIZE = 1000;
    private int P_SCAN_BLOCK_SIZE = 10000;

    public int getP_FOR_BLOCK_SIZE() {
        return P_FOR_BLOCK_SIZE;
    }

    public void setP_FOR_BLOCK_SIZE(int P_FOR_BLOCK_SIZE) {
        this.P_FOR_BLOCK_SIZE = P_FOR_BLOCK_SIZE;
    }

    public int getP_SCAN_BLOCK_SIZE() {
        return this.P_SCAN_BLOCK_SIZE;
    }

    public void setP_SCAN_BLOCK_SIZE(int P_SCAN_BLOCK_SIZE) {
        this.P_SCAN_BLOCK_SIZE = P_SCAN_BLOCK_SIZE;
    }

    @Override
    public List<Distance> compute(int[][] graph, int start) {
        ParallelUtil parallelUtils = new ParallelUtil();
        parallelUtils.setP_FOR_BLOCK_SIZE(P_FOR_BLOCK_SIZE);
        parallelUtils.setP_SCAN_BLOCK_SIZE(P_SCAN_BLOCK_SIZE);

        List<Distance> distances = new ArrayList<>(Collections.nCopies(graph.length, null));
        AtomicBoolean[] flag = new AtomicBoolean[graph.length];
        parallelUtils.parallelFor(graph.length, i -> flag[i] = new AtomicBoolean());

        AtomicReference<int[]> frontier = new AtomicReference<>(new int[1]);
        AtomicReference<int[]> startBlock = new AtomicReference<>(new int[1]);

        distances.set(start, new Distance(0, -1));
        frontier.get()[0] = start;
        flag[start].set(true);

        int distance = 1;
        int nextFrontierSize = graph[start].length;

        while (nextFrontierSize != 0) {
            int[] nextFrontier = new int[nextFrontierSize];
            int[] nextDeg = new int[nextFrontierSize];
            parallelUtils.parallelFor(nextFrontierSize, i -> nextFrontier[i] = -1);

            int finalDistance = distance;

            parallelUtils.parallelFor(frontier.get().length, currentIndex -> {
                int current = frontier.get()[currentIndex];
                if (current != -1) {
                    for (int i = 0; i < graph[current].length; i++) {
                        int next = graph[current][i];
                        if (flag[next].compareAndSet(false, true)) {
                            distances.set(next, new Distance(finalDistance, current));

                            nextFrontier[startBlock.get()[currentIndex] + i] = next;
                            nextDeg[startBlock.get()[currentIndex] + i] = graph[next].length;
                        }
                    }
                }
            });

            frontier.set(nextFrontier);
            startBlock.set(parallelUtils.parallelScan(nextDeg));

            nextFrontierSize = startBlock.get()[startBlock.get().length - 1];
            distance++;
        }
        return distances;
    }
}

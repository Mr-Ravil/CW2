package bfs;

import simulator.graph.GraphSimulator;
import parallel.util.ParallelUtil;

import java.util.concurrent.atomic.AtomicBoolean;

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
    public int[] compute(int[][] graph, int start) {
        ParallelUtil parallelUtils = new ParallelUtil();
        parallelUtils.setP_FOR_BLOCK_SIZE(P_FOR_BLOCK_SIZE);
        parallelUtils.setP_SCAN_BLOCK_SIZE(P_SCAN_BLOCK_SIZE);

        int[] dist = new int[graph.length];
        AtomicBoolean[] flag = new AtomicBoolean[graph.length];
        parallelUtils.parallelFor(graph.length, i -> flag[i] = new AtomicBoolean());

        /**
            vertexes start from 0,
            so to safe time (the standard variable is 0)
            the null vertex will be indicated by 0
            and vertex will be increased by one
        */
        int[] frontier = new int[1];
        int[] deg = new int[1];

        dist[start] = 0;
        frontier[0] = start + 1;
        deg[0] = graph[start].length;
        flag[start].set(true);

        int distance = 1;

        while (frontier.length != 0) {

            int[] startBlock = parallelUtils.parallelScan(deg);

            int nextFrontierSize = startBlock[startBlock.length - 1];
            int[] nextFrontier = new int[nextFrontierSize];
            deg = new int[nextFrontierSize];

            int[] finalDeg = deg;
            int[] finalFrontier = frontier;
            int finalDistance = distance;

            parallelUtils.parallelFor(frontier.length, currentIndex -> {
                int current = finalFrontier[currentIndex] - 1;
                if (current != -1) {
                    int[] neighbours = graph[current];
                    for (int i = 0; i < neighbours.length; i++) {
                        int next = neighbours[i];
                        if (flag[next].compareAndSet(false, true)) {
                            dist[next] = finalDistance;
                            nextFrontier[startBlock[currentIndex] + i] = next + 1;
                            finalDeg[startBlock[currentIndex] + i] = graph[next].length;
                        }
                    }
                }
            });

            frontier = nextFrontier;

            distance++;
        }

        return dist;
    }
}

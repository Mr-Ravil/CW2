package bfs;

import simulator.graph.GraphSimulator;
import parallel.util.ParallelUtil;
import unsafe.UnsafeIntArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

//                                            public List<Long> createDegList = new ArrayList<>(); //TODO
                                            public List<Long> createStartBlockList = new ArrayList<>(); //TODO
//                                            public List<Long> createNextFrontierList = new ArrayList<>(); //TODO
//                                            public List<Long> copyFinalFrontierList = new ArrayList<>(); //TODO
                                            public List<Long> doP_FORList = new ArrayList<>(); //TODO
//                                            public List<Long> setFrontierList = new ArrayList<>(); //TODO
//                                            public List<Long> createNewDegList = new ArrayList<>(); //TODO



    @Override
    public int[] compute(GraphSimulator graph, int start) {
                                            long startTime; //TODO
                                            long endTime; //TODO
//                                            long createDeg = 0; //TODO
                                            long createStartBlock = 0; //TODO
//                                            long createNextFrontier = 0; //TODO
//                                            long copyFinalFrontier = 0; //TODO
                                            long doP_FOR = 0; //TODO
//                                            long setFrontier = 0; //TODO
//                                            long createNewDeg = 0; //TODO




        ParallelUtil parallelUtils = new ParallelUtil();
        parallelUtils.setP_FOR_BLOCK_SIZE(P_FOR_BLOCK_SIZE);
        parallelUtils.setP_SCAN_BLOCK_SIZE(P_SCAN_BLOCK_SIZE);

//                                                startTime = System.nanoTime(); //TODO
//        List<Distance> distances = new ArrayList<>(Collections.nCopies(graph.getSize(), null));
        int[] dist = new int[graph.getSize()];
        AtomicBoolean[] flag = new AtomicBoolean[graph.getSize()];
        parallelUtils.parallelFor(graph.getSize(), i -> flag[i] = new AtomicBoolean());

//        AtomicReference<int[]> frontier = new AtomicReference<>(new int[1]);
        /**
            vertexes start from 0,
            so to safe time (the standard variable is 0)
            the null vertex will be indicated by 0
            and vertex will be increased by one
        */
//        int[] frontier = new int[1];
        UnsafeIntArray frontier = new UnsafeIntArray(1);
        UnsafeIntArray deg = new UnsafeIntArray(1);
//        int[] deg = new int[1];

        dist[start] = 0;
//        distances.set(start, new Distance(0, -1));
//        frontier[0] = start + 1;
//        deg[0] = graph.getNeighbours(start).length;
        frontier.set(0, start + 1);
        deg.set(0, graph.getNeighbours(start).length);
        flag[start].set(true);

        int distance = 1;

//                                                endTime = System.nanoTime(); //TODO
//                                                System.out.println("Create time: " + TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS)); //TODO

        while (frontier.size() != 0) {
//                                                    startTime = System.nanoTime(); //TODO

//            int[] finalFrontier = frontier;

//                                                    endTime = System.nanoTime(); //TODO
//                                                    copyFinalFrontier += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO
//                                                    startTime = System.nanoTime(); //TODO

            // put into big p_for
//            int[] deg = new int[frontier.length];
//            parallelUtils.parallelFor(frontier.length,
//                    i -> deg[i] = finalFrontier[i] == -1 ? 0 : graph.getNeighbours(finalFrontier[i]).length);
//
//                                                    endTime = System.nanoTime(); //TODO
//                                                    createDeg += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO
                                                    startTime = System.nanoTime(); //TODO

//            int[] startBlock = parallelUtils.parallelScan(deg);
            UnsafeIntArray startBlock = parallelUtils.parallelScan(deg);

                                                    endTime = System.nanoTime(); //TODO
                                                    createStartBlock += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO
//                                                    startTime = System.nanoTime(); //TODO

            int nextFrontierSize = startBlock.get(startBlock.size() - 1);


//            int[] nextFrontier = new int[nextFrontierSize];
            UnsafeIntArray nextFrontier = new UnsafeIntArray(nextFrontierSize);

//            parallelUtils.parallelFor(nextFrontierSize, i -> nextFrontier.set(i, -1));

//                                                        endTime = System.nanoTime(); //TODO
//                                                        createNextFrontier += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO

            int finalDistance = distance;

//                                                    startTime = System.nanoTime(); //TODO

//            int[] newDeg = new int[nextFrontierSize];
//            UnsafeIntArray newDeg = new UnsafeIntArray(nextFrontierSize);


//                                                    endTime = System.nanoTime(); //TODO
//                                                    createNewDeg += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO

                                                    startTime = System.nanoTime(); //TODO
            deg.close();
            deg = new UnsafeIntArray(nextFrontierSize);


            UnsafeIntArray finalFrontier = frontier;
            UnsafeIntArray finalDeg = deg;

            parallelUtils.parallelFor(nextFrontierSize, i -> {
                finalDeg.set(i, 0);
                nextFrontier.set(i, 0);
            });

            parallelUtils.parallelFor(finalFrontier.size(), currentIndex -> {
                int current = finalFrontier.get(currentIndex) - 1;
                if (current != -1) {
                    int[] neighbours = graph.getNeighbours(current);
                    for (int i = 0; i < neighbours.length; i++) {
                        int next = neighbours[i];
                        if (flag[next].compareAndSet(false, true)) {
//                            distances.set(next, new Distance(finalDistance, current));
                            dist[next] = finalDistance;
                            nextFrontier.set(startBlock.get(currentIndex) + i, next + 1);
                            finalDeg.set(startBlock.get(currentIndex) + i, graph.getNeighbours(next).length);
                        }
//                        else {
//                            nextFrontier.set(startBlock.get(currentIndex) + i, 0);
//                            finalDeg.set(startBlock.get(currentIndex) + i, 0);
//                        }
                    }
                }
//                else {
//                    for (int i = startBlock.get(currentIndex); i < startBlock.get(currentIndex + 1); i++) {
//                        nextFrontier.set(i, 0);
//                        finalDeg.set(i, 0);
//                    }
//                }
            });

                                                    endTime = System.nanoTime(); //TODO
                                                    doP_FOR += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO
//                                                    startTime = System.nanoTime(); //TODO

            frontier.close();

            frontier = nextFrontier;
//            deg = newDeg;

//                                                    endTime = System.nanoTime(); //TODO
//                                                    setFrontier += TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS); //TODO

//            nextFrontier.close();
//            newDeg.close();
            startBlock.close();

            distance++;
        }

//                                                createDegList.add(createDeg); //TODO
                                                createStartBlockList.add(createStartBlock); //TODO
//                                                createNextFrontierList.add(createNextFrontier); //TODO
//                                                copyFinalFrontierList.add(copyFinalFrontier); //TODO
//                                                createNewDegList.add(createNewDeg); //TODO
                                                doP_FORList.add(doP_FOR); //TODO
//                                                setFrontierList.add(setFrontier); //TODO

        frontier.close();
        deg.close();

        return dist;
    }
}

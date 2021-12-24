import bfs.BFS;
import bfs.ParallelBFS;
import bfs.SequenceBFS;
import cube.Cube;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    private static int BLOCK_SIZE = 1000;
    private static final int CUBE_SIZE = 250;
    private static int ATTEMPTS_COUNT = 5;

    private static String timesToString(List<Long> times) {
        String timesString = times.stream().map(String::valueOf).collect(Collectors.joining(" ms, "));
        return timesString + " ms";
    }

    public static void main(String[] args) {
        System.out.println("Block size: " + BLOCK_SIZE);
        System.out.println("Cube size: " + CUBE_SIZE);
        System.out.println("Attempts count: " + ATTEMPTS_COUNT + "\n\n");

        long allSeqTime = 0;
        long allParTime = 0;

        List<Long> seqTimes = new ArrayList<>();
        List<Long> parTimes = new ArrayList<>();


        BFS sequenceBFS = new SequenceBFS();
        BFS parallelBFS = new ParallelBFS();

        List<List<Integer>> cubeGraph = new Cube().generateCubeGraph(CUBE_SIZE);

        for (int i = 0; i < ATTEMPTS_COUNT; i++) {
            long seqTime = launchBFS(sequenceBFS, cubeGraph, "Sequence");
            allSeqTime += seqTime;
            seqTimes.add(seqTime);

            long parTime = launchBFS(parallelBFS, cubeGraph, "Parallel");
            allParTime += parTime;
            parTimes.add(parTime);
        }

        long aveParTime = allParTime / ATTEMPTS_COUNT;
        long aveSeqTime = allSeqTime / ATTEMPTS_COUNT;

        System.out.println("Parallel BFS times: " + timesToString(parTimes) + ".");
        System.out.println("Sequence BFS times: " + timesToString(seqTimes) + ".");

        System.out.println("\nAverage parallel BFS time: " + aveParTime + " ms.");
        System.out.println("Average sequence BFS time: " + aveSeqTime + " ms.");
        System.out.println("\nParallel BFS " + ((double) aveSeqTime / (double) aveParTime) + " times faster than Sequence BFS.");
    }

    private static long launchBFS(BFS bfs, List<List<Integer>> cubeGraph, String typeBFS) {
        long startTime;
        long endTime;
        startTime = System.nanoTime();
        List<BFS.Distance> distances = bfs.compute(cubeGraph, 0);
        endTime = System.nanoTime();
        long time = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);

        if (!new Cube().checkBFSResult(distances, CUBE_SIZE)) {
            System.err.println(typeBFS + " BFS is not correct.");
        }

        return time;
    }
}

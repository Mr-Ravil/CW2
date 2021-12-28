package cube;

import bfs.BFS;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    int getPosition(int size, int i, int j, int k) {
        return (i * size + j) * size + k;
    }

    void tryAdd(List<Integer> neighbours, int size, int i, int j, int k) {
        if (i >= 0 && j >= 0 && k >= 0
                && i < size && j < size && k < size) {
            neighbours.add(getPosition(size, i, j, k));
        }
    }

    public int[][] generateCubeGraph(int size) {
        int[][] graph = new int[size*size*size][];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    List<Integer> neighbours = new ArrayList<>();
                    tryAdd(neighbours, size, i - 1, j, k);
                    tryAdd(neighbours, size, i, j - 1, k);
                    tryAdd(neighbours, size, i, j, k - 1);
                    tryAdd(neighbours, size, i + 1, j, k);
                    tryAdd(neighbours, size, i, j + 1, k);
                    tryAdd(neighbours, size, i, j, k + 1);
                    graph[getPosition(size, i, j, k)] = neighbours.stream().mapToInt(ii -> ii).toArray();
                }
            }
        }

        return graph;
    }

    public boolean checkBFSResult(List<BFS.Distance> distances, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    int position = getPosition(size, i, j, k);
                    if (distances.get(position).Distance != i + j + k
                            || (i == 0 && j == 0 && k == 0 && distances.get(position).Parent != -1)
                            || ((i > 0 && distances.get(position).Parent != getPosition(size, i - 1, j, k))
                                && (j > 0 && distances.get(position).Parent != getPosition(size, i, j - 1, k))
                                && (k > 0 && distances.get(position).Parent != getPosition(size, i, j, k - 1)))
                    ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkBFSResult(int[] distances, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    int position = getPosition(size, i, j, k);
                    if (distances[position] != i + j + k) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
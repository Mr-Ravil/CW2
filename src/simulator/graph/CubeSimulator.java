package simulator.graph;

import bfs.BFS;

import java.util.ArrayList;
import java.util.List;

public class CubeSimulator implements GraphSimulator {
    private final int size;

    public CubeSimulator(int size) {
        this.size = size;
    }

    int getVertex(int i, int j, int k) {
        return (i * size + j) * size + k;
    }

    int[] getPosition(int vertex) {
        int[] positions = new int[3];
        positions[2] = vertex % size;
        vertex /= size;
        positions[1] = vertex % size;
        vertex /= size;
        positions[0] = vertex % size;

        return positions;
    }

    void tryAdd(List<Integer> neighbours, int i, int j, int k) {
        if (i >= 0 && j >= 0 && k >= 0
                && i < size && j < size && k < size) {
            neighbours.add(getVertex(i, j, k));
        }
    }

    @Override
    public int[] getNeighbours(int vertex) {
        int[] positions = getPosition(vertex);
        int i = positions[0];
        int j = positions[1];
        int k = positions[2];

        List<Integer> neighbours = new ArrayList<>();
        tryAdd(neighbours, i - 1, j, k);
        tryAdd(neighbours, i, j - 1, k);
        tryAdd(neighbours, i, j, k - 1);
        tryAdd(neighbours, i + 1, j, k);
        tryAdd(neighbours, i, j + 1, k);
        tryAdd(neighbours, i, j, k + 1);

        return neighbours.stream().mapToInt(ii -> ii).toArray();
    }

    @Override
    public int getSize() {
        return size * size * size;
    }

    public boolean checkBFSResult(List<BFS.Distance> distances) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    int position = getVertex(i, j, k);
                    if (distances.get(position).Distance != i + j + k
                            || (i == 0 && j == 0 && k == 0 && distances.get(position).Parent != -1)
                            || ((i > 0 && distances.get(position).Parent != getVertex(i - 1, j, k))
                            && (j > 0 && distances.get(position).Parent != getVertex(i, j - 1, k))
                            && (k > 0 && distances.get(position).Parent != getVertex(i, j, k - 1)))
                    ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

package parallel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class ParallelUtil {
    private int BLOCK_SIZE = 1000;

    public ParallelUtil() {
    }

    public ParallelUtil(int BLOCK_SIZE) {
        this.BLOCK_SIZE = BLOCK_SIZE;
    }

    public void parallelFor(int threadCount, Consumer<Integer> lambda) {
//        for (int i = 0; i < threadCount; i++) {            lambda.accept(i);        }

        ParallelForAction parallelForAction = new ParallelForAction(lambda, 0, threadCount - 1);
        parallelForAction.fork();
        parallelForAction.join();
    }

    private class ParallelForAction extends RecursiveAction {
        private final Consumer<Integer> lambda;
        private final int left;
        private final int right;

        public ParallelForAction(Consumer<Integer> lambda, int left, int right) {
            this.lambda = lambda;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < BLOCK_SIZE) {
                for (int i = left; i <= right; i++) {
                    lambda.accept(i);
                }
                return;
            }

            int m = (left + right) / 2;
            ParallelForAction leftAction = new ParallelForAction(lambda, left, m);
            ParallelForAction rightAction = new ParallelForAction(lambda, m + 1, right);

            leftAction.fork();
            rightAction.fork();

            leftAction.join();
            rightAction.join();
        }
    }

    public int[] parallelScan(int[] data) {
//        int[] sums = new int[data.length + 1];        for (int i = 0; i < data.length; i++) {            sums[i + 1] = sums[i] + data[i];        }        return sums;
        return new ParallelScan(data).compute();
        // problem with scan
    }

    private class ParallelScan {
        private final int[] data;
        private final int[] sums;
        private final int[] result; // result[0] = 0

        public ParallelScan(int[] data) {
            this.data = data;
            this.sums = new int[data.length * 2 / BLOCK_SIZE + BLOCK_SIZE];
            this.result = new int[data.length + 1];
        }

        public int[] compute() {
            ParallelScanUpAction parallelScanUpAction = new ParallelScanUpAction(data);
            parallelScanUpAction.fork();
            parallelScanUpAction.join();

            ParallelScanDownAction parallelScanDownAction = new ParallelScanDownAction(data);
            parallelScanDownAction.fork();
            parallelScanDownAction.join();

            return result;
        }


        private class ParallelScanUpAction extends RecursiveAction {
            private final int[] data;
            private final int left;
            private final int right;
            private final int index;

            public ParallelScanUpAction(int[] data) {
                this.data = data;
                this.left = 0;
                this.right = data.length - 1;
                this.index = 1;
            }

            public ParallelScanUpAction(int[] data, int left, int right, int index) {
                this.data = data;
                this.left = left;
                this.right = right;
                this.index = index;
            }

            @Override
            protected void compute() {
                if (right - left < BLOCK_SIZE) {
                    int sum = 0;
                    for (int i = left; i <= right; i++) {
                        sum += data[i];
                    }
                    sums[index] = sum;
                    return;
                }

                int m = (left + right) / 2;
                ParallelScanUpAction leftAction = new ParallelScanUpAction(data, left, m, index * 2);
                ParallelScanUpAction rightAction = new ParallelScanUpAction(data, m + 1, right, index * 2 + 1);

                leftAction.fork();
                rightAction.fork();

                leftAction.join();
                rightAction.join();

                sums[index] = sums[index * 2] + sums[index * 2 + 1];
            }
        }

        private class ParallelScanDownAction extends RecursiveAction {
            private final int[] data;
            private final int left;
            private final int right;
            private final int index;
            private final int sumLeft;

            public ParallelScanDownAction(int[] data) {
                this.data = data;
                this.left = 0;
                this.right = data.length - 1;
                this.index = 1;
                this.sumLeft = 0;
            }

            public ParallelScanDownAction(int[] data, int left, int right, int index, int sumLeft) {
                this.data = data;
                this.left = left;
                this.right = right;
                this.index = index;
                this.sumLeft = sumLeft;
            }

            @Override
            protected void compute() {
                if (right - left < BLOCK_SIZE) {
                    result[left + 1] = sumLeft + data[left];
                    for (int i = left + 1; i <= right; i++) {
                        result[i + 1] = result[i] + data[i];
                    }
                    return;
                }

                int m = (left + right) / 2;
                ParallelScanDownAction leftAction = new ParallelScanDownAction(data, left, m,
                        index * 2, sumLeft);
                ParallelScanDownAction rightAction = new ParallelScanDownAction(data, m + 1, right,
                        index * 2 + 1, sumLeft + sums[index * 2]);

                leftAction.fork();
                rightAction.fork();

                leftAction.join();
                rightAction.join();
            }
        }

    }
}

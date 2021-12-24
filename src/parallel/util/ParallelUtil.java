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

    public List<Integer> parallelScan(List<Integer> data) {
//        List<Integer> sums = new ArrayList<>(data.size() + 1);        sums.add(0);        for (int a : data) {            sums.add(sums.get(sums.size() - 1) + a);        }        return sums;
        return new ParallelScan(data).compute();
        // problem with scan
    }

    private class ParallelScan {
        private final List<Integer> data;
        private final List<Integer> sums;
        private final List<Integer> result; // result[0] = 0

        public ParallelScan(List<Integer> data) {
            this.data = data;
            this.sums = new ArrayList<>(Collections.nCopies(data.size() * 2 / BLOCK_SIZE + BLOCK_SIZE, 0));
            this.result = new ArrayList<>(Collections.nCopies(data.size() + 1, 0));
        }

        public List<Integer> compute() {
            ParallelScanUpAction parallelScanUpAction = new ParallelScanUpAction(data);
            parallelScanUpAction.fork();
            parallelScanUpAction.join();

            ParallelScanDownAction parallelScanDownAction = new ParallelScanDownAction(data);
            parallelScanDownAction.fork();
            parallelScanDownAction.join();

            return result;
        }


        private class ParallelScanUpAction extends RecursiveAction {
            private final List<Integer> data;
            private final int left;
            private final int right;
            private final int index;

            public ParallelScanUpAction(List<Integer> data) {
                this.data = data;
                this.left = 0;
                this.right = data.size() - 1;
                this.index = 1;
            }

            public ParallelScanUpAction(List<Integer> data, int left, int right, int index) {
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
                        sum += data.get(i);
                    }
                    sums.set(index, sum);
                    return;
                }

                int m = (left + right) / 2;
                ParallelScanUpAction leftAction = new ParallelScanUpAction(data, left, m, index * 2);
                ParallelScanUpAction rightAction = new ParallelScanUpAction(data, m + 1, right, index * 2 + 1);

                leftAction.fork();
                rightAction.fork();

                leftAction.join();
                rightAction.join();

                sums.set(index, sums.get(index * 2) + sums.get(index * 2 + 1));
            }
        }

        private class ParallelScanDownAction extends RecursiveAction {
            private final List<Integer> data;
            private final int left;
            private final int right;
            private final int index;
            private final int sumLeft;

            public ParallelScanDownAction(List<Integer> data) {
                this.data = data;
                this.left = 0;
                this.right = data.size() - 1;
                this.index = 1;
                this.sumLeft = 0;
            }

            public ParallelScanDownAction(List<Integer> data, int left, int right, int index, int sumLeft) {
                this.data = data;
                this.left = left;
                this.right = right;
                this.index = index;
                this.sumLeft = sumLeft;
            }

            @Override
            protected void compute() {
                if (right - left < BLOCK_SIZE) {
                    result.set(left + 1, sumLeft + data.get(left));
                    for (int i = left + 1; i <= right; i++) {
                        result.set(i + 1, result.get(i) + data.get(i));
                    }
                    return;
                }

                int m = (left + right) / 2;
                ParallelScanDownAction leftAction = new ParallelScanDownAction(data, left, m,
                        index * 2, sumLeft);
                ParallelScanDownAction rightAction = new ParallelScanDownAction(data, m + 1, right,
                        index * 2 + 1, sumLeft + sums.get(index * 2));

                leftAction.fork();
                rightAction.fork();

                leftAction.join();
                rightAction.join();
            }
        }

    }
}

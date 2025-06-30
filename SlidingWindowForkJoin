package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * A parallel sliding window pattern matcher using Java's Fork/Join framework.
 * Designed to efficiently scan massive sequences (e.g., DNA) for exact pattern matches
 * using fixed-size windows equal to the target pattern length.
 */
public class SlidingWindowIndexer {

    /**
     * Recursive task that divides the input string into chunks and searches for pattern matches in parallel.
     */
    static class SlidingWindowTask extends RecursiveTask<List<Integer>> {
        private final String input;
        private final int start, end;
        private final int windowSize;
        private final String target;
        private static final int THRESHOLD = 100_000; // Chunk size threshold for sequential processing

        public SlidingWindowTask(String input, int start, int end, int windowSize, String target) {
            this.input = input;
            this.start = start;
            this.end = end;
            this.windowSize = windowSize;
            this.target = target;
        }

        @Override
        protected List<Integer> compute() {
            if ((end - start) <= THRESHOLD) {
                // If chunk is small enough, scan sequentially
                return scanSequentially(input, start, end, windowSize, target);
            } else {
                // Divide the input range into two overlapping chunks
                int mid = (start + end) / 2;
                int overlap = target.length() - 1;

                SlidingWindowTask left = new SlidingWindowTask(input, start, mid + overlap, windowSize, target);
                SlidingWindowTask right = new SlidingWindowTask(input, mid, end, windowSize, target);

                left.fork(); // Process left half asynchronously
                List<Integer> rightResult = right.compute(); // Process right half now
                List<Integer> leftResult = left.join(); // Wait for left half to finish

                leftResult.addAll(rightResult); // Merge results
                return leftResult;
            }
        }
    }

    /**
     * Pure 1-loop version of the sliding window matcher.
     * Compares each window of length = target length with the pattern using substring().
     */
    private static List<Integer> scanSequentially(String input, int start, int end, int windowSize, String target) {
        List<Integer> result = new ArrayList<>();
        int maxStart = Math.min(end - windowSize, input.length() - windowSize);

        for (int i = start; i <= maxStart; i++) {
            if (input.substring(i, i + windowSize).equals(target)) {
                result.add(i); // Exact match found at index i
            }
        }

        return result;
    }

    /**
     * Utility to generate a long random DNA-like sequence using A, C, G, T characters.
     */
    public static String generateRandomSequence(int length) {
        Random random = new Random();
        char[] bases = {'A', 'C', 'G', 'T'};
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(bases[random.nextInt(4)]);
        }
        return sb.toString();
    }

    /**
     * Entry point for testing the parallel sliding window matcher.
     */
    public static void main(String[] args) {
        int dataSize = 10_000_000;
        String target = "ACGTACGT";
        int windowSize = target.length();

        System.out.println("Generating large input...");
        String input = generateRandomSequence(dataSize);
//        String input = "ACGTACGT"; // For debugging small input

        System.out.println("Running parallel sliding window search...");
        long startTime = System.currentTimeMillis();

        ForkJoinPool pool = ForkJoinPool.commonPool();
        SlidingWindowTask task = new SlidingWindowTask(input, 0, input.length(), windowSize, target);
        List<Integer> result = pool.invoke(task);

        long endTime = System.currentTimeMillis();

        System.out.println("Found " + result.size() + " matches");
        System.out.println("Time taken (parallel): " + (endTime - startTime) + " ms");
    }
}

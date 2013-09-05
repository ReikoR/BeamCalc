import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BeamCalc {

    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    private static class IndexCalc implements Runnable {
        private String path = "";

        public IndexCalc(String path) {
            this.path = path;
        }

        public void run() {
            BeamTest.calcIndex(path);
        }
    }

    public static void main(String[] args) {
        int MAX_THREADS = 16;

        Runtime runtime = Runtime.getRuntime();

        if (args.length > 0) {
            try {
                int userMaxThreads = Integer.parseInt(args[0]);
                MAX_THREADS = userMaxThreads;
            } catch (NumberFormatException e) {
                System.out.print(e.getMessage());
            }
        }

        System.out.print(runtime.maxMemory() / (1024 * 1024) + " MiB");
        //String filePath = args[0];
        List<Thread> threads = new ArrayList<Thread>();

        File[] dirs = new File(".").listFiles();
        for (int i=0; i < dirs.length; i++) {
            File dir = dirs[i];
            //if (dir.isFile() && dir.getName().matches("MER_FR__2P.*\\.N1")) {
            //System.out.println(dir.getName());
            //if (dir.isFile() && dir.getName().matches("([^\\s]+(\\.(?i)(N1))$)")) {
            if (dir.isFile() && dir.getName().matches(".*\\.N1")) {
                //System.out.println(dir.getAbsolutePath());
                //BeamTest.calcIndex(dir.getAbsolutePath());
                Thread t = new Thread(new IndexCalc(dir.getName()));
                threads.add(t);
                //t.start();
                //System.out.println(t.getState());
                //Thread.State.NEW
            }
        }
        //threadMessage("Waiting for MessageLoop thread to finish");
        long startTime = System.currentTimeMillis();
        while (true) {
            int activeThreadCount = 0;
            int doneCount = 0;
            boolean allFinished = true;
            for (Thread thread : threads) {
                long memoryLeft = runtime.maxMemory() - runtime.totalMemory();
                //System.out.print("\nFree " + memoryLeft / (1024 * 1024) + " MiB");
                if (thread != null) {
                    if (thread.getState() == Thread.State.NEW && activeThreadCount < MAX_THREADS && memoryLeft > 500 * 1024 * 1024) {
                        System.out.print("\nStarting new thread");
                        thread.start();
                        activeThreadCount++;
                        allFinished = false;
                    } else if (thread.getState() == Thread.State.RUNNABLE) {
                        allFinished = false;
                        activeThreadCount++;
                    }  else if (thread.getState() == Thread.State.TERMINATED) {
                        doneCount++;
                        thread = null;
                    }
                } else {
                    doneCount++;
                }

            }
            System.out.print("\n" + activeThreadCount + " running, " + doneCount + " of " + threads.size() + " done");
            System.out.print("\n" + runtime.totalMemory() / (1024 * 1024) + " MiB");
            if (doneCount >= threads.size()) {
                break;
            }
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.gc();
        }
        System.out.println("\nAll done in " + (System.currentTimeMillis() - startTime) + "ms");
        //threadMessage("All done");
    }
}

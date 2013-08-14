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
        //String filePath = args[0];
        List<Thread> threads = new ArrayList<Thread>();

        File[] dirs = new File(".").listFiles();
        for (int i=0; i < dirs.length; i++) {
            File dir = dirs[i];
            if (dir.isFile() && dir.getName().matches("MER_FR__2P.*\\.N1")) {
                //System.out.println(dir.getName());
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
                if (thread.getState() == Thread.State.NEW && activeThreadCount < MAX_THREADS) {
                    System.out.print("\nStarting new thread");
                    thread.start();
                    activeThreadCount++;
                    allFinished = false;
                } else if (thread.getState() == Thread.State.RUNNABLE) {
                    allFinished = false;
                    activeThreadCount++;
                }  else if (thread.getState() == Thread.State.TERMINATED) {
                    doneCount++;
                }
            }
            System.out.print("\n" + activeThreadCount + " running, " + doneCount + " of " + threads.size() + " done");
            if (allFinished) {
                break;
            }
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\nAll done in " + (System.currentTimeMillis() - startTime) + "ms");
        //threadMessage("All done");
    }
}

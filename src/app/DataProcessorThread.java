package app;

public class DataProcessorThread extends Thread {
    private String job;

    public DataProcessorThread(String job) {
        this.job = job;
    }

    public void run() {
        System.out.println("Processing: " + job);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("Done: " + job);
    }
}

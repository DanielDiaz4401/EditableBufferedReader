import java.io.*;

public class Cursor implements Runnable {

    private boolean state;

    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}

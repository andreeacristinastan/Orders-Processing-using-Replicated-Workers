import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tema2 {
    /**
     * Metoda care are logica main, preia argumentele din linia de comanda si le prelucreaza,
     * stabileste calupul de bytesi pe care fiecare thread il va avea de prelucrat, porneste
     * thread-urile si le inchide.
     * @param args - argumente din linia de comanda
     */
    public static void main(final String[] args) throws InterruptedException, IOException {
        FileWriter fwo = new FileWriter("orders_out.txt");
        FileWriter fwp = new FileWriter("order_products_out.txt");
        int numberOfThreads = Integer.parseInt(args[1]);

        ExecutorService tpe = Executors.newFixedThreadPool(numberOfThreads);
        Thread[] firstThreads = new Thread[numberOfThreads];
        String ordersFile = args[0] + "/" + "orders.txt";

        File file = new File(ordersFile);
        long size = file.length();
        int sizeOfText = (int) size / numberOfThreads;
        int start = 0;
        int end = sizeOfText;

        for(int i = 0; i < numberOfThreads; i++) {
            firstThreads[i] = new Orders(args[0], tpe, numberOfThreads, i + 1, start, end);
            firstThreads[i].start();
            start += sizeOfText;
            end += sizeOfText;
        }
        for(int i = 0; i < numberOfThreads; i++) {
            firstThreads[i].join();
        }
        tpe.shutdown();
    }
}

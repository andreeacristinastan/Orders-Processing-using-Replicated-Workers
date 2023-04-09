import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class Products implements Runnable {
    Semaphore semaphore;
    ExecutorService tpe;
    String arg;
    StringBuilder nameOfOrder;
    int id;
    int numOfProducts;

    public Products(ExecutorService tpe, String arg, Semaphore semaphore, StringBuilder nameOfOrder, int id, int numOfProducts) throws IOException {
        this.tpe = tpe;
        this.arg = arg;
        this.semaphore = semaphore;
        this.nameOfOrder = nameOfOrder;
        this.id = id;
        this.numOfProducts = numOfProducts;
    }

    /**
     * Functie ce are in spate logica pentru thread-urile de level 2
     * parcurge fisierul linie cu linie pana gaseste comanda cu id-ul
     * potrivit si scrie in fisierul de produse.
     */
    public void run() {
        String ordersFile = arg + "/" + "order_products.txt";
        File file = new File(ordersFile);
        BufferedReader reader;

        int cnt = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.contains(nameOfOrder) && cnt == id) {
                    final FileWriter fileWriter = new FileWriter("order_products_out.txt", true);

                    synchronized (fileWriter) {
                        fileWriter.write(line.concat(",shipped\n"));
                        fileWriter.close();
                    }
                    semaphore.release();
                    reader.close();
                    break;
                } else if (line.contains(nameOfOrder) && cnt != id){
                    cnt++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

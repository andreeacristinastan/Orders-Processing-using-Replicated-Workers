import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class Orders extends Thread{
    String arg;
    ExecutorService tpe;
    int numberOfThreads;
    int nthThread;
    int start;
    int end;

    public Orders(String arg, ExecutorService tpe, int numberOfThreads, int nthThread, int start, int end) {
        this.arg = arg;
        this.tpe = tpe;
        this.numberOfThreads = numberOfThreads;
        this.nthThread = nthThread;
        this.start = start;
        this.end = end;
    }

    /**
     * Functie ce are logica pentru thread-urile de level 1, deschide fisierul din care va citi
     * fiecare thread comenzile, citeste caracter cu caracter din el si, in functie de cazul pe
     * care este, se apeleaza mai departe thread-urile cu produse sau trece mai departe pentru a
     * face scrierea in fisierul de output de comenzi.
     */
    public void run() {
        String ordersFile = arg + "/" + "orders.txt";
        File file = new File(ordersFile);
        BufferedReader reader;
        int cntCommands = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            int character;
            StringBuilder command = new StringBuilder();

            while(cntCommands < start) {
                character = reader.read();
                cntCommands++;
            }

            for(int i = start; i < end; i++) {
                if ((character = reader.read()) != -1) {
                    command.append((char) character);
                    if((char)character == '\n' && !command.toString().contains("o_")) {
                        command = new StringBuilder();
                    } else if(i == end - 1 && !String.valueOf((char) character).equals("\n")) {
                        int check = '\n';
                        while((character = reader.read()) != check) {
                            command.append((char) character);
                        }

                        int cnt = -1;
                        StringBuilder nameOfOrder = new StringBuilder(), numOfOrder = new StringBuilder();
                        int sizeOfCommand = command.length();

                        for(int j = 0; j < sizeOfCommand; j++) {
                            if(command.charAt(j) == ',') {
                                cnt = -2;
                            } else if(cnt == -2) {
                                assert false;
                                numOfOrder.append(command.charAt(j));
                            } else {
                                nameOfOrder.append(command.charAt(j));
                            }
                        }
                        if(Integer.parseInt(String.valueOf(numOfOrder)) > 0) {
                            Semaphore semaphore = new Semaphore(-Integer.parseInt(numOfOrder.toString()) + 1);
                            for (int k = 0; k < Integer.parseInt(numOfOrder.toString()); k++) {
                                tpe.submit(new Products(tpe, arg, semaphore, nameOfOrder, k, Integer.parseInt(numOfOrder.toString())));
                            }
                            semaphore.acquire();
                            final FileWriter fileWriter = new FileWriter("orders_out.txt", true);
                            synchronized (fileWriter) {
                                fileWriter.append(String.valueOf(command.append(",shipped\n")));
                                fileWriter.close();
                            }
                        }
                        command = new StringBuilder();
                    } else if (String.valueOf((char)character).equals("\n") && command.toString().contains("o_")) {
                        int cnt = -1;
                        StringBuilder nameOfOrder = new StringBuilder(), numOfOrder = new StringBuilder();
                        int sizeOfCommand = command.length();

                        for(int j = 0; j < sizeOfCommand - 1; j++) {
                            if(command.charAt(j) == ',') {
                                cnt = -2;
                            } else if(cnt == -2) {
                                numOfOrder.append(command.charAt(j));
                            } else {
                                nameOfOrder.append(command.charAt(j));
                            }
                        }

                        if(Integer.parseInt(String.valueOf(numOfOrder)) > 0) {
                            Semaphore semaphore = new Semaphore(-Integer.parseInt(numOfOrder.toString()) + 1);
                            for (int k = 0; k < Integer.parseInt(numOfOrder.toString()); k++) {
                                tpe.submit(new Products(tpe, arg, semaphore, nameOfOrder, k, Integer.parseInt(numOfOrder.toString())));
                            }
                            semaphore.acquire();
                            FileWriter fileWriter = new FileWriter("orders_out.txt", true);

                            synchronized (fileWriter) {
                                command.deleteCharAt(command.length() - 1);
                                fileWriter.append(command.append(",shipped\n"));
                                fileWriter.close();
                            }
                        }
                        command = new StringBuilder();
                    }
                }
            }
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

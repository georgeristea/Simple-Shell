import java.io.*;

public class InputOutput {

    private String filename;
    private FileSystem fSystem;
    private IFactory myFactory;

    /**
     * Initializeaza o instanta a clasei InputOutput cu numele fisierului primit drept parametru
     * @param filename numele fisierului din care se citesc datele
     */
    public InputOutput(String filename) {
        this.filename = filename;
        this.fSystem = new FileSystem();
        this.myFactory = new CommandFactory();
    }

    /**
     * Metoda se ocupa de citirea si scrierea datelor din/in fisier
     * @param output o reprezentare sub forma de String a rezultatului unei comenzi
     * @param errors o reprezentare sub forma de String a erorilor rezultate in urma executarii unei comenzi
     */
    public void ReadWrite(String output, String errors) {

        ICommand command;
        Directory root = this.fSystem.getRoot(); /* root-ul sistemului de fisiere */

        try (BufferedReader bf = new BufferedReader(new FileReader(filename));
             BufferedWriter outputWriter = new BufferedWriter(new FileWriter(output));
             BufferedWriter errorWriter = new BufferedWriter(new FileWriter(errors))) {

            String input;
            while ((input = bf.readLine()) != null) {

                /* avem comanda creata fara sa stim ce e */
                command = myFactory.createCommand(input, root);

                /* setez in Invoker comanda */
                this.fSystem.setCommand(command);

                /* apelez comanda in Invoker */
                this.fSystem.executeCommand();

                /* scriu in fisier rezultatul comenzii */
                this.fSystem.writeToFILE(outputWriter, errorWriter);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
}
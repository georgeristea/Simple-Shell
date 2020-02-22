import java.io.BufferedWriter;
import java.io.IOException;

public class FileSystem {

    private Directory root; /* root-ul sistemului de fisiere */
    private ICommand command;
    private CommandInfo result;
    private int number = 1;

    /**
     * Initializeaza un sistem de fisiere
     */
    public FileSystem() {
        this.root = new Directory("/", "/", null);
        this.root.setCurrentDirectory(true);
    }

    /**
     * Metoda seteaza o comanda ce urmeaza sa fie executata
     * @param command referinta catre comanda ce trebuie setata
     */
    public void setCommand(ICommand command) {
        this.command = command;
    }

    /**
     * Metoda executa o comanda si salveaza rezultatul obtinut
     */
    public void executeCommand() {
        this.result = this.command.execute();

    }

    /**
     * Metoda scrie in fisiere rezultatele obtinute in urma executarii unei comenzi
     * @param outputWriter bufferul ce se ocupa de scriere rezultatului unei comenzi in fisier
     * @param errorWriter bufferul ce se ocupa de scriere erorilor in fisier
     * @throws IOException
     */
    public void writeToFILE(BufferedWriter outputWriter, BufferedWriter errorWriter) throws IOException {

         errorWriter.write(String.valueOf(this.number) + "\n");
         outputWriter.write(String.valueOf(this.number) + "\n");

         if (this.result.getCommandReturn() != 0) {
             errorWriter.append(this.result.getResult());
         }
         else
             outputWriter.append(this.result.getResult());

         this.number++;
         this.result.resetCommandInfo();
    }

    /**
     * @return returneaza root-ul sistemului de fisiere
     */
    public Directory getRoot() {
        return root;
    }
}

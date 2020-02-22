public class PwdCommand implements ICommand {

    private Directory root;
    static private PwdCommand instance;
    private CommandInfo commandInfo;

    /**
     * Construieste un obiect de tip PwdCommand cu parametrii specificati
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     */
    private PwdCommand(Directory root, String input) {
        this.root = root;
        this.commandInfo = new CommandInfo(0);
    }

    /**
     * Metoda creeaza o instanta a clasei MvCommand sau returneaza instanta, daca aceasta exista deja
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @return metoda returneaza o instanta a clasei MvCommand
     */
    public static PwdCommand getInstance(Directory root, String input) {

        if (instance == null)
            instance = new PwdCommand(root, input);

        return instance;
    }

    /**
     * Metoda executa comanda si returneaza outputul comenzii
     * @return returneaza un obiect ce contine informatii despre statusul comenzii: executarea cu succes sau insucces,
     * respectiv un sir de caractere ce contine rezultatul comenzii executate.
     */
    @Override
    public CommandInfo execute() {
        return root.printWorkingDirectory(this.commandInfo);
        /* returneaza o instanta a clasei CommandInfo ce contine urmatoarele informatii :
            ---> referinta catre directorul curent (in exterior este setat null pentru a nu avea acces la ea
            ---> rezultatul comenzii (calea absoluta a directorului)
            ---> o valoare booleana = true in cazul acestei comenzi
         */
    }

}

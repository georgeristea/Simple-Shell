public class MkdirCommand implements ICommand {

    private Directory root;
    private String input;
    private static MkdirCommand instance;
    private CommandInfo commandInfo;

    /**
     * Construieste un obiect de tip MkdirCommand cu parametrii specificati
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     */
    private MkdirCommand(Directory root, String input) {
        this.root = root;
        this.input = input; /* inputul primit din fisier. Fara asta nu stiu ce trebuie sa execute comanda */
        this.commandInfo = new CommandInfo(0);
    }

    /**
     * Metoda creeaza o instanta a clasei MkdirCommand sau returneaza instanta, daca aceasta exista deja
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @return metoda returneaza o instanta a clasei MkdirCommand
     */
    public static MkdirCommand getInstance(Directory root, String input) {

        /* input-ul o sa fie fara substringul "mkdir " ;
           EXEMPLU : mkdir /exemplu/exemplu2 --> /exemplu/exemplu2
         */
        if (instance == null)
            instance = new MkdirCommand(root, input);
        else
            instance.input = input;

        return instance;
    }

    /**
     * Metoda executa comanda si returneaza outputul comenzii
     * @return returneaza un obiect ce contine informatii despre statusul comenzii: executarea cu succes sau insucces,
     * respectiv un sir de caractere ce contine rezultatul comenzii executate.
     */
    @Override
    public CommandInfo execute() {

        HelperClass help = HelperClass.getInstance();
        /* imi desparte input-ul tinand cont ca ultimul nume este directorul ce trebuie adaugat */
        help.splitSubstring(input);
        String[] pathWords = help.getWords();
        String dirName = help.getName();

        Directory currentDirectory = root.getStartDirectory(input);
        currentDirectory.searchPath(pathWords, dirName, 0, "mkdir", this.commandInfo);

        if (this.commandInfo.getCommandReturn() == 1) {
            String output = "mkdir: " + input.substring(0, input.lastIndexOf('/')) + ": No such directory\n";
            this.commandInfo.setResult(output);
        }
        return this.commandInfo;
    }

}

public class RmCommand implements ICommand {

    private Directory root;
    private String input;
    private static RmCommand instance;
    private CommandInfo commandInfo;

    /**
     * Construieste un obiect de tip RmCommand cu parametrii specificati
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     */
    private RmCommand(Directory root, String input) {
        this.root = root;
        this.input = input; /* inputul primit din fisier. Fara asta nu stiu ce trebuie sa execute comanda */
        this.commandInfo = new CommandInfo(0);
    }

    /**
     * Metoda creeaza o instanta a clasei RmCommand sau returneaza instanta, daca aceasta exista deja
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @return metoda returneaza o instanta a clasei RmCommand
     */
    public static RmCommand getInstance(Directory root, String input) {

        /* input-ul o sa fie fara substringul "mkdir " ;
           EXEMPLU : mkdir /exemplu/exemplu2 --> /exemplu/exemplu2
         */
        if (instance == null)
            instance = new RmCommand(root, input);
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
        help.splitString(input);
        String[] pathWords = help.getWords();

        Directory currentDirectory = root.getStartDirectory(input);
        currentDirectory.searchPath(pathWords, "", 0, "rm", this.commandInfo);

        if (this.commandInfo.getCommandReturn() == 1) {
            String output = "rm: cannot remove " + input +  ": No such file or directory\n";
            this.commandInfo.setResult(output);
        }
        if (this.commandInfo.getResult().toString().contains("succes")) {
            this.commandInfo.resetCommandInfo();
        }
        return this.commandInfo;
    }
}

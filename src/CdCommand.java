public class CdCommand implements ICommand {

    private Directory root;
    private String input;
    private static CdCommand instance;
    private CommandInfo commandInfo;

    /**
     * Construieste un obiect de tip CdCommand cu parametrii specificati
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     */
    private CdCommand(Directory root, String input) {
        this.root = root;
        this.input = input;   /* o sa am doar calea relativa in input */
        this.commandInfo = new CommandInfo(0); /* SUCCES */
    }

    /**
     * Metoda creeaza o instanta a clasei CdCommand sau returneaza instanta, daca aceasta exista deja
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @return metoda returneaza o instanta a clasei CdCommand
     */
    public static CdCommand getInstance(Directory root, String input) {

        if (instance == null)
            instance = new CdCommand(root, input);
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
        currentDirectory.searchPath(pathWords, "", 0, "cd", this.commandInfo);

        if (this.commandInfo.getCommandReturn() == 1) {
            String out = "cd: " + input + ": No such directory\n";
            this.commandInfo.setResult(out);
        } else {
            Directory lastWorkingDir = root.searchWorkingDirectory();
            lastWorkingDir.setCurrentDirectory(false);
            ((Directory)this.commandInfo.getRef()).setCurrentDirectory(true);
            this.commandInfo.setRef(null); /* sa nu poate fi accesata in exterior */
        }

        return this.commandInfo;
    }
}

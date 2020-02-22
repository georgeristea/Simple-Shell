public class LsCommand implements ICommand {

    private Directory root;
    private String input;
    private static LsCommand instance;
    private ICommand auxCommand;
    private CommandInfo commandInfo;

    /**
     * Construieste un obiect de tip LsCommand cu parametrii specificati
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @param auxCommand reprezinta o comanda auxiliara ce poate fi aplicata impreuna cu "ls"
     */
    private LsCommand(Directory root, String input, ICommand auxCommand) {
        this.root = root;
        this.input = input; /* inputul primit din fisier. Fara asta nu stiu ce trebuie sa execute comanda */
        this.auxCommand = auxCommand;
        this.commandInfo = new CommandInfo(0);
    }

    /**
     * Metoda creeaza o instanta a clasei LsCommand sau returneaza instanta, daca aceasta exista deja
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @param auxCommand reprezinta o comanda auxiliara ce poate fi aplicata impreuna cu comanda "ls"
     * @return metoda returneaza o instanta a clasei LsCommand
     */
    public static LsCommand getInstance(Directory root, String input, ICommand auxCommand) {

        if (instance == null)
            instance = new LsCommand(root, input, auxCommand);
        else {
            instance.input = input;
            instance.auxCommand = auxCommand;
        }

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
        String grepSearch = "";
        if (this.auxCommand != null) {
            /* daca nu e null inseamna ca este folosita in conjunctie cu comanda "grep" */
            input = input.substring(0, input.indexOf("|") - 1);
            grepSearch = auxCommand.execute().getResult().toString();
        }
        if (input.equals("ls")) {
            help.splitString(input.substring(2));
        }
        else {
            help.splitString(input.substring(3));
        }
        String[] pathWords = help.getWords();
        Directory currentDirectory;
        int indexOf = input.indexOf("/") - 1; /* o sa dea -1 daca nu exista , deci o sa iasa cu -2 */
        if (indexOf != -2 && input.charAt(indexOf) == ' ') {
            /* inseamna ca / este primul in path, deci cale absoluta */
            currentDirectory = root;
        } else {
            currentDirectory = root.searchWorkingDirectory();
        }
        if (input.contains("-R"))
            currentDirectory.searchPath(pathWords, grepSearch, 0, "ls -R", this.commandInfo);
        else
            currentDirectory.searchPath(pathWords, grepSearch, 0, "ls", this.commandInfo);

        if (this.commandInfo.getCommandReturn() == 1) {
            String output = "ls: " + input.substring(3) + ": No such directory\n";
            this.commandInfo.setResult(output);
        }
        return this.commandInfo;
    }
}
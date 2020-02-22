public class GrepCommand implements ICommand {

    private String input;
    private static GrepCommand instance;

    /**
     * Construieste un obiect de tip GrepCommand cu parametrul specificat
     * @param input reprezinta argumentul comenzii
     */
    private GrepCommand(String input) {
        this.input = input; /* inputul primit din fisier. Fara asta nu stiu ce trebuie sa execute comanda */
    }

    /**
     * Metoda creeaza o instanta a clasei GrepCommand sau returneaza instanta, daca aceasta exista deja
     * @param input reprezinta argumentul comenzii
     * @return metoda returneaza o instanta a clasei GrepCommand
     */
    public static GrepCommand getInstance(String input) {

        if (instance == null)
            instance = new GrepCommand(input);
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

        CommandInfo commandInfo = new CommandInfo(0);
        commandInfo.setResult(this.input);
        return commandInfo;
    }
}
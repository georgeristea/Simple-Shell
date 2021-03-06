public class CpCommand implements ICommand {

    private Directory root;
    private String input;
    private static CpCommand instance;
    private CommandInfo commandInfo;


    /**
     * Construieste un obiect de tip CpCommand cu parametrii specificati
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     */
    private CpCommand(Directory root, String input) {
        this.root = root;
        this.input = input; /* inputul primit din fisier. Fara asta nu stiu ce trebuie sa execute comanda */
        this.commandInfo = new CommandInfo(0);
    }

    /**
     * Metoda creeaza o instanta a clasei CpCommand sau returneaza instanta, daca aceasta exista deja
     * @param root reprezinta root-ul sistemului de fisiere
     * @param input reprezinta argumentul comenzii
     * @return metoda returneaza o instanta a clasei CpCommand
     */
    public static CpCommand getInstance(Directory root, String input) {

        /* input-ul o sa fie fara substringul "mkdir " ;
           EXEMPLU : mkdir /exemplu/exemplu2 --> /exemplu/exemplu2
         */
        if (instance == null)
            instance = new CpCommand(root, input);
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
        String input1 = input.substring(0, input.lastIndexOf(" "));  /* ce se doreste copiat   */
        String input2 = input.substring(input.lastIndexOf(" ") + 1); /* unde se doreste copiat */

        help.splitString(input1);
        String[] pathWords = help.getWords();

        help.splitString(input2);
        String[] pathWords_two = help.getWords();


        Directory currentDirectory = root.getStartDirectory(input1);
        currentDirectory.searchPath(pathWords, "source", 0, "cp", this.commandInfo);
        Component source = this.commandInfo.getRef();

        if (commandInfo.getCommandReturn() == 1) {
            String out =  "cp: cannot copy " + input1 + ": No such file or directory\n";
            commandInfo.setResult(out);
            return commandInfo;
        }
        commandInfo.resetCommandInfo();
        currentDirectory = root.getStartDirectory(input2);
        currentDirectory.searchPath(pathWords_two, "dest_folder", 0, "cp", this.commandInfo);

        if (commandInfo.getCommandReturn() == 1) {
            String out =  "cp: cannot copy into " + input2 + ": No such directory\n";
            commandInfo.setResult(out);
            return commandInfo;
        }
        /* daca ajunge aici inseamna ca ambele referinte la componente sunt diferite de null */
        Directory dest_folder = (Directory)this.commandInfo.getRef();

        try {
            root.applyCommand(source, dest_folder, "cp", this.commandInfo);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (this.commandInfo.getCommandReturn() == 2) {
            String result = "cp: cannot copy " + input1 + ": Node exists at " +
                    "destination\n";
            this.commandInfo.setResult(result);
        }
        return this.commandInfo;
    }

}

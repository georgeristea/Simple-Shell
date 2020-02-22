public class CommandFactory implements IFactory {

    /**
     * Metoda creeaza un obiect de tipul ICommand in functie de parametrii primiti
     * @param input reprezinta argumentul comenzii ce va fi creata
     * @param root reprezinta root-ul sistemului de fisiere
     * @return returneaza un obiect de tipul ICommand
     */
    @Override
    public ICommand createCommand(String input, Directory root) {
        ICommand command = null;
        String name = (input.split(" "))[0];

        if (name.equals("pwd")) {
            command = PwdCommand.getInstance(root, input);
        }
        else if (name.equals("mkdir")) {
            /* --? mkdir /exemplu ---> /exemplu */
            command = MkdirCommand.getInstance(root, input.substring(6));
        }
        else if (name.equals("touch")) {
            /* --? touch /text.txt --> /text.txt */
            command = TouchCommand.getInstance(root, input.substring(6));
        }
        else if (name.equals("cd")) {
            /* --? cd /exemplu/exemplul2 --> /exemplu/exemplul2 */
            command = CdCommand.getInstance(root, input.substring(3));
        }
        else if (name.equals("ls")) {
            ICommand auxCommand = null;
            if (input.contains(" | grep")) {
                auxCommand = GrepCommand.getInstance(input.substring(input.indexOf("\"") + 1,
                        input.length() - 1));
            }
            command = LsCommand.getInstance(root, input, auxCommand);
        }
        else if (name.equals("cp")) {
            command = CpCommand.getInstance(root, input.substring(3));
        }
        else if (name.equals("mv")) {
            command = MvCommand.getInstance(root, input.substring(3));
        }
        else if (name.equals("rm")) {
            command = RmCommand.getInstance(root, input.substring(3));
        }
        return command;
    }
}

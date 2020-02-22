public class CommandInfo {

    private StringBuilder result;
    private int commandReturn;
    private Component ref = null;
    /*
        1 = eroare de tip E1
        2 = eroare de tip E2
        0 = succes;
     */

    /**
     * Initializeaza un obiect de tipul CommandInfo, cu rezultatul comenzii specificat de parametrul "commandReturn"
     * @param commandReturn o valoare int ce reprezinta rezultatul comenzii (0 - succes, 1/2 - insucces)
     */
    public CommandInfo(int commandReturn) {
        this.result = new StringBuilder();
        this.commandReturn = commandReturn;
    }

    /**
     * @return returneaza o referinta catre obiectul de tip Component salvat in urma executarii unei comenzi
     */
    public Component getRef() {
        return ref;
    }

    /**
     * Metoda seteaza valoarea parametrului ref cu referinta primita ca parametru
     * @param ref referinta catre un component din sistemul de fisiere
     */
    public void setRef(Component ref) {
        this.ref = ref;
    }

    /**
     * @return returneaza un obiect de tip StringBuilder ce contine rezultatul comenzii
     */
    public StringBuilder getResult() {
        return result;
    }

    /**
     * Metoda seteaza rezultatul comenzii
     * @param commandReturn o valoarea int ce reprezinta rezultatul comenzii executate
     */
    public void setCommandReturn(int commandReturn) {
        this.commandReturn = commandReturn;
    }

    /**
     * @return returneaza rezultatul comenzii
     */
    public int getCommandReturn() {
        return commandReturn;
    }

    /**
     * Metoda concateneaza rezultatul actual al comenzii cu rezultatele obtinute anterior prin executarea aceleasi
     * comenzi
     * @param info reprezentarea sub forma de string a rezultatului comenzii
     */
    public void setResult(String info) {
        this.result.append(info);
    }

    /**
     * Metoda reseteaza valoarea de return a comenzii (succes sau insucces) si rezultatul comenzii
     */
    public void resetCommandInfo() {
        this.result = new StringBuilder();
        this.commandReturn = 0;
    }
}

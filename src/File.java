public class File extends Component implements Cloneable {

    private final boolean currentDirectory;  /* o sa fie mereu NULL pentru fisier */

    /**
     * Construieste un obiect de tip File cu parametrii specificati
     * @param name numele fisierului
     * @param path path-ul fisierului
     * @param parent directorul parinte al fisierului
     */
    public File(String name, String path, Directory parent) {
        super(name, path, parent);
        this.currentDirectory = false;
    }

    /**
     * Metoda creeaza o copie a unui obiect de tip File prin apelul metodei clone() din clasa Object.
     * @return returneaza o copie a obiectului de tip FIle
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Directory extends Component implements Cloneable{

    private boolean currentDirectory = false;
    private List<Component> components;

    /**
     * Construieste un obiect de tip Directory cu parametrii specificati
     * @param name numele directorului
     * @param path path-ul directorului
     * @param parent parintele directorului (in cazul directorului root, parintele este NULL)
     */
    public Directory(String name, String path, Directory parent) {
        super(name, path, parent);
        this.components = new ArrayList<>();
    }

    /**
     * Metoda aplica o comanda (CpCommand/MvCommand) asupra unui director pe baza parametrilor primiti
     * @param source fisierul/folderul sursa ce va fi copiat/mutat
     * @param dest_folder folderul destinatie in care se face copierea/mutarea daca comanda va fi executat cu succes
     * @param comType tipul comenzii (CpCommand sau MvCommand)
     * @param type un obiect de tip CommandInfo unde va fi retinut rezultatul comenzii (succes/insucces si/sau erori)
     * @throws CloneNotSupportedException
     */
    public void applyCommand(Component source, Directory dest_folder,
                             String comType, CommandInfo type) throws CloneNotSupportedException {

            for (Component temp : dest_folder.components) {
                if (temp.getName().equals(source.getName())) {
                    /* exista deja o componenta cu acelasi nume in folderul destinatie
                       deci nu va avea loc copierea
                     */
                    type.setCommandReturn(2);
                    type.setRef(null); /* sa nu am acces in exterior */
                    return;
                }
            }
            if (source instanceof File) {
                File newFile;
                if (comType.equals("cp")) {
                    newFile = (File) source.clone();
                }
                else {
                    newFile = (File) source;
                    source.getParent().remove(source);
                }
                dest_folder.add(newFile);
                newFile.setParent(dest_folder);
                if (dest_folder.getPath().equals("/")) {
                    newFile.setPath(dest_folder.getPath() + newFile.getName());
                }
                else {
                    newFile.setPath(dest_folder.getPath() + "/" + newFile.getName());
                }
            }
            else {
                Directory newDir;
                if (comType.equals("cp")) {
                    newDir = (Directory) source.clone();
                }
                else {
                    newDir = (Directory) source;
                    /* il elimin din vechiul arrayList, adica din vechiul director */
                    source.getParent().remove(source);
                }
                dest_folder.add(newDir);
                newDir.setParent(dest_folder);
                dest_folder.changeAtributes();
            }
    }

    /**
     * Metoda returneaza directorul de inceput in functie de calea relativa/absoluta a comenzii
     * @param input argumentul unei comenzi
     * @return returneaza directorul de inceput
     */
    public Directory getStartDirectory(String input) {

        Directory startDirectory;
        if (input.startsWith("/"))
            startDirectory = this;
        else
            startDirectory = this.searchWorkingDirectory();

        return startDirectory;

    }

    /**
     * Metoda modifica atributele(path-ul si directorul parinte) unei componente(fisier sau director) recursiv
     */
    public void changeAtributes() {

       for (Component temp : this.components) {
           if (!this.getPath().equals("/"))
               temp.setPath(this.getPath() + "/" + temp.getName());
           else
               temp.setPath(this.getPath() + temp.getName());
           temp.setParent(this);
           temp.changeAtributes();
       }
    }

    /**
     * Metoda listeaza componentele unui director
     * @param str un obiect de tipul StringBuilder
     * @param pattern patternul in functie de care se realizeaza afiseaza componentelor
     */
    @Override
    public void ListComponents(StringBuilder str, String pattern) {

        str.append(this.dirContent(pattern));
        for (Component temp : this.components) {
            temp.ListComponents(str, pattern);
        }
    }

    /**
     * Metoda afiseaza continutul unui director sub forma ---> nume_director + ":" + continut_director
     * @param pattern patternul in functie de care se realizeaza afisarea
     * @return returneaza un obiect de tip StringBuilder reprezentand continutul directorului
     */
    @Override
    public StringBuilder dirContent(String pattern) {

        /* afiseaza numele directorului curent + ":" + continutul directorului */
        Pattern regexChecker = Pattern.compile(pattern);
        StringBuilder out = new StringBuilder();
        out.append(this.getPath()).append(":\n");
        int ok = 1;
        for (Component temp : this.components) {
            if (!pattern.equals("")) {
                Matcher regexMatcher = regexChecker.matcher(temp.getName());
                if (regexMatcher.matches()) {
                    out.append(temp.getPath());
                    out.append(" ");
                    ok = 0;
                }
            } else {
                out.append(temp.getPath());
                out.append(" ");
                ok = 0;
            }
        }
        if (ok == 0) {
            out.deleteCharAt(out.length() - 1);
        }
        out.append("\n").append("\n");
        return out;
    }

    /**
     * Metoda realizeaza o clonare a unui obiect de tipul Directory (deep clone)
     * @return returneaza obiectul clonat
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {

        Directory directory = (Directory) super.clone();
        /* atunci cand copiez directorul trebuie sa ii setez
            currentDirectory = false , sa nu existe doua directoare
            ce reprezinta currentDirectory-ul  actual
         */
        List<Component> deepCopyArray = new ArrayList<>();
        directory.setCurrentDirectory(false);
        /* ii fac deepCopy la arrayList */
        for (Component temp : directory.components) {
            Component cloneComponent = (Component)temp.clone();
            deepCopyArray.add(cloneComponent);
        }
        directory.setComponents(deepCopyArray);
        return directory;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    /**
     * Metoda returneaza directorul curent al sistemului de fisiere
     * @return returneaza o referinta catre directorul curent al sistemului de fisiere sau "null" daca directorul
     * curent este "/".
     */
    @Override
    public Directory searchWorkingDirectory() {

        /* methoda imi cauta directorul curent in care ma aflu */
        Directory wDir;
        if (this.currentDirectory)
            return this;
        else {
            for (Component temp : this.components) {
                wDir = temp.searchWorkingDirectory();
                if (wDir != null) {
                    return wDir;
                }
            }
        }
        return null;
    }

    /**
     * Metoda afiseaza path-ul directorului curent al sistemului de fisiere
     * @param commandInfo referinta catre un obiect de tipul CommandInfo unde va fi retinut statusul comenzii
     * @return returneaza statusul comenzii (incapsulat intr-o instanta a clasei CommandInfo)
     */
    public CommandInfo printWorkingDirectory(CommandInfo commandInfo) {

        String result = this.searchWorkingDirectory().getPath() + "\n";
        commandInfo.setResult(result);
        return commandInfo;
    }


    /**
     * @return returneaza lista de componente ale fisierului
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Seteaza folderul ca fiind directorul curent al sistemului de fisiere
     * @param currentDirectory true - folderul este directorul curent al sis. de fisiere (initial este setat false)
     */
    public void setCurrentDirectory(boolean currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    /**
     * Metoda verifica ca in directorul actual exista deja o componenta cu acelasi nume (in cazul comenzilor "mkdir",
     * "touch"
     * @param newName numele componentei ce trebuie adaugata
     * @param newPath path-ul componentei ce trebuie adaugata
     * @param parent directorul parinte al componentei
     * @param type tipul comenzii
     * @param commandInfo obiect ce retine rezultatul comenzii
     */
    public void checkIt(String newName, String newPath, Directory parent, String type, CommandInfo commandInfo) {

        for (Component temp : this.components) {
            if (temp.getName().equals(newName)) {
                /* exista deja o componenta cu acelasi nume */
                String output;
                if (type.equals("mkdir"))
                    output = type + ": cannot create directory " + newPath + ": Node exists\n";
                else
                    output = type + ": cannot create file " + newPath + ": Node exists\n";
                commandInfo.setCommandReturn(2);
                commandInfo.setResult(output);
                return;
            }
        }
        if (type.equals("mkdir"))
            this.add(new Directory(newName, newPath, parent));
        else
            this.add(new File(newName, newPath, parent));

        /* comanda a fost executata cu SUCCES */
    }

    /**
     * Metoda adauga un component (fisier sau director) in directorul curent
     * @param component componentul ce trebuie adaugat
     */
    @Override
    public void add(Component component) {
        this.components.add(component);
        Collections.sort(this.components);
    }

    /**
     * Metoda sterge un component din directorul curent
     * @param component componentul ce trebuie sters
     */
    @Override
    public void remove(Component component) {
        this.components.remove(component);
    }
}

public abstract class Component implements Cloneable, Comparable<Component> {

    private String name;
    private String path;
    private boolean curentDirectory = false;
    private Directory parent;

    /**
     * Constructorul unei clase abstracte
     * @param name numele componentei
     * @param path path-ul componentei
     * @param parent directorul parinte al componentei
     */
    public Component(String name, String path, Directory parent) {
        this.name = name;
        this.path = path;
        this.parent = parent;
    }

    /**
     * Metoda cauta componenta(fisier/director) sau componentele si aplica comanda ceruta asupra acesteia/acestora
     * ** In functie de path-ul primit ca parametru prin vectorul de Stringuri, metoda cauta un component sau mai multe
     * si aplica comanda ceruta asupra componentei/componentelor, salvand informatiile rezultate in urma comenzii **
     * @param pathWords vector ce contine numele componentelor din path
     * @param componentName numele componentei(fisier/director) ce va fi adaugata in cazul comenzilor "mkdir"/"touch"
     * @param index pozitia actuala din vectorul cu numele componentelor din path : "pathWords"
     * @param commandType tipul comenzii ce va fi executata sub forma de String
     * @param commandInfo obiect ce va salva rezultul unei comenzi (succes/insucces si/sau erori)
     */
    public void searchPath(String[] pathWords,String componentName, int index,
                       String commandType, CommandInfo commandInfo) {

        /* in cazul in care avem cale absoluta, primul element din vector va fi "" si il ignoram */
        if (index == 0 && pathWords[0].equals("")) {
            this.searchPath(pathWords, componentName, ++index, commandType, commandInfo);
            return;
        }

        Component dirr = null;
        int ok = 0;

        if (index < pathWords.length) { /* mai am de cautat */

            if (pathWords[index].equals(".")) {
                if (index == 0)
                    dirr = this.searchWorkingDirectory();
                else
                    dirr = this;
            } else if (pathWords[index].equals("..")) {
                if (index == 0)
                    dirr = this.searchWorkingDirectory().getParent();
                else
                    dirr = this.getParent();
            } else {
                Directory current = ((Directory)this);
                int i = 0;
                while (i < current.getComponents().size()) {
                    Component temp = current.getComponents().get(i);
                    if (pathWords[index].contains("*")) {
                        boolean isGood = checkStarName(temp.getName(), pathWords[index]);
                        if (isGood) {
                            dirr = temp;
                            ok = 1;
                            if (dirr instanceof Directory) {
                                dirr.searchPath(pathWords, componentName, index + 1, commandType, commandInfo);
                                if (dirr.searchWorkingDirectory() == null && commandType.equals("rm") &&
                                        (index == pathWords.length - 1))
                                    i--;
                            } else {
                                if (commandType.equals("rm")  && index == pathWords.length - 1) {
                                    commandInfo.setCommandReturn(0);
                                    if (dirr.searchWorkingDirectory() == null)
                                        i--;
                                    dirr.searchPath(pathWords, componentName, index + 1, commandType, commandInfo);
                                }
                            }
                        }
                    }
                    else if (temp.getName().equals(pathWords[index])) {
                        dirr = temp;
                    }
                    i++;
                }
            }
            if (ok != 1) {
                commandInfo.setCommandReturn(0);
                if (dirr == null) {
                    /* daca e null intorc 1 -> pt eroare de tip E1 */
                    if (!commandInfo.getResult().toString().equals("succes"))
                        commandInfo.setCommandReturn(1);
                } else {
                    dirr.searchPath(pathWords, componentName, ++index, commandType, commandInfo);
                }
            }
        }
        else {
            String newPath = "";
            if (commandType.equals("mkdir") || commandType.equals("touch")) {
                newPath = this.getPath();
                if (!newPath.equals("/"))
                    newPath += "/" + componentName;
                else
                    newPath += componentName;
            }
            switch (commandType) {
                case "mkdir":
                    if (this instanceof File) {
                        commandInfo.setCommandReturn(1);
                        break;
                    }
                    /* in functia checkIt o sa imi completeze commandInfo cum trebuie */
                    ((Directory)this).checkIt(componentName, newPath,((Directory)this), commandType, commandInfo);
                    break;
                case "touch":
                    ((Directory)this).checkIt(componentName, newPath, ((Directory)this), commandType, commandInfo);
                    break;
                case "cd":
                    commandInfo.setRef(this);
                    break;
                case "ls -R":
                    StringBuilder str = commandInfo.getResult();
                    this.ListComponents(str, componentName);
                    break;
                case "ls":
                    if (this instanceof File) {
                        commandInfo.setCommandReturn(1);
                        break;
                    }
                    StringBuilder str_n = commandInfo.getResult();
                    str_n.append(this.dirContent(componentName));
                    break;
                case "cp":
                    commandInfo.setRef(this);
                    break;
                case "mv":
                    commandInfo.setRef(this);
                    break;
                case "rm":
                    if (this.searchWorkingDirectory() == null) {
                        Directory parent = this.getParent();
                        parent.remove(this);
                        commandInfo.setResult("succes");
                    }
                    break;
            }
        }
    }

    /**
     * Metodata verifica daca numele unui fisier/director contine tokenul "*"
     * @param componentName numele fisierului/directorului verificat
     * @param starName patternul dupa care se face verificarea
     * @return true  - daca numele componentei face match cu patternul specificat prin argumentul "starName"
     *         false - altfel
     */
    public boolean checkStarName(String componentName, String starName) {

        boolean isValid = false;
        if (starName.equals("*"))
            isValid = true;
        else {
            String startSubstring = starName.substring(0, starName.indexOf("*"));
            String endSubstring = starName.substring(starName.indexOf("*") + 1);
            if (componentName.startsWith(startSubstring) &&
                componentName.endsWith(endSubstring)) {

                isValid = true;
            }
        }

        return isValid;
    }

    /**
     * Seteaza path-ul componentei cu parametrul specificat
     * @param path path-ul sub forma de String
     */
    public void setPath(String path) {
        this.path = path;
    }
    public void changeAtributes() {
    }

    /**
     * Seteaza parintele componentei cu parametrul specificat
     * @param parent referinta catre directorul ce contine componenta actuala
     */
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public Directory searchWorkingDirectory() {
        return null;
    }

    public void ListComponents(StringBuilder str, String pattern) {

    }

    public StringBuilder dirContent(String pattern) {
        /* trebuie folosit grepSearch pt grep */
        StringBuilder out = new StringBuilder();
        out.append(this.getPath()).append(":\n");
        return out;
    }
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return returneaza numele componentei
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return returneaza path-ul componentei
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @return returneaza directorul parinte al componentei
     */
    public Directory getParent() {
        return parent;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setCurentDirectory(boolean curentDirectory) {
        this.curentDirectory = curentDirectory;
    }

    /**
     * Metodata compara doua componente in functie de nume
     * @param o fisier/director
     * @return returneaza aceleasi valori ca si metoda String.compareTo sau new IllegalArgumentException daca componenta
     * primita ca parametru are valoarea "null".
     */
    @Override
    public int compareTo(Component o) {

        if (o != null) {
            return this.getName().compareTo(o.getName());
        } else
            throw new IllegalArgumentException("ERROR");

    }
}

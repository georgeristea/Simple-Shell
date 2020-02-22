public class HelperClass {

    private String[] words;
    private String name;
    public static HelperClass instance;

    /* Trebuie facuta SINGLETON pentru ca nu am nevoie de mai multe instante ale clasei */

    /**
     * Initializeaza o instanta a clasei
     */
    private HelperClass() {
    }

    /**
     * Metoda creeaza o instanta a clasei HelperClass sau returneaza instanta, daca aceasta exista deja
     * @return metoda returneaza o instanta a clasei HelperClass
     */
    public static HelperClass getInstance() {

        if (instance == null)
            instance = new HelperClass();

        return instance;
    }

    /*
        metoda care departe inputul in cazul comenzilor "mkdir" , "touch" ..etc
        adica atunci cand am nevoie de path-ul unde trebuie adaugata componenta si de numele componentei
     */

    /**
     * Metoda separa stringul primit ca parametru,in functie de caracterul '/', exceptand ultimul cuvand din string
     * @param input sir format din cuvinte separate prin caracterul '/'
     */
    public void splitSubstring(String input) {

        /*
            aici trebuie sa retin numele directorului/fisierului ce trebuie adaugat daca e posibil
            * inainte trebuie verificat pentru stringuri de forma "doar_un_nume" !
         */
        if (!input.contains("/")) {
            this.name = input;
            this.words = new String[]{""};
        }
        else {
            this.name = input.substring(input.lastIndexOf('/') + 1);
            input = input.substring(0,input.lastIndexOf('/'));
            this.words = input.split("/");
        }
        // iar in cazul in care am doar un cuvant vectorul de stringuri o sa fie NULL
    }

    /*
        folosesc metoda asta in cazul path-ului pentru comenzile "cd" , "cp" .. etc
     */

    /**
     * Metoda separa stringul primit ca parametru,in functie de caracterul '/'
     * @param input sir format din cuvinte separate prin caracterul '/'
     */
    public void splitString(String input) {

        /* inseamna ca incepe cu '/', deci cale abosula
               cand am de genul /ceva/altceva imi pune in vectorul de stringuri pe prima pozitie "" (trebuie verificat
               in functia mea de "search" !!!
        */
        if (input.contains("-R")) {
            input = input.replaceAll("-R", "").replaceAll(" ", "");
            /* nu iese modificat din functie */
        }
        if (!input.contains("/")) {
            this.words = new String[]{input};
        }
        else if (input.length() == 1) {
            this.words = new String[]{""};
            }
        else
            this.words = input.split("/");

    }

    /**
     * @return returneaza un vector ce contine cuvintele din sirul de caractere
     */
    public String[] getWords() {
        return words;
    }

    /**
     * @return returneaza un string ce reprezinta ultimul cuvant din sirul de caractere
     */
    public String getName() {
        return name;
    }
}

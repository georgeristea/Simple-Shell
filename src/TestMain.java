public class TestMain {

    public static void main(String[] args) {


        String input = "*nou";
        String componentName = "folder3nou";

//        HelperClass help = HelperClass.getInstance();
//        help.splitString(input);
//        help.splitSubstring(input);
//        String[] words = help.getWords();
//        for (String i : words) {
//            System.out.print(i + ":");
//        }

        if (input.contains("*")) {
            String startSubstring = input.substring(0, input.indexOf("*"));
            String endSubstring = input.substring(input.indexOf("*") + 1);
            System.out.println(startSubstring);
            System.out.println(endSubstring);
            if (componentName.startsWith(startSubstring)) {
                System.out.println("NICE");
            }
        }

        /*      test2 -> Bonus

            ---> pana la linia 45 merge !

         */




        /* daca inputul incepe cu "/" inseamna ca am cale absoluta, deci incep cautarea de la directorul "root"
           - dimensiunea o sa fie cu 1 mai mare in cazul in care stringul incepe cu "/" pentru ca imi pune "" la inceput

           * cand incepe cu . caut in directorul de la pwd  (deci aplic metoda de la pwd sa gasesc directorul curent
           * si caut in arraylist-ul lui de elemente

           * daca incepe cu .. caut in directorul parinte al directorului curent (daca sunt in root direct. parinte
           * este null deci nu poti adauga

            >>> de forma ceva/altceva atunci ma uit in directorul curent dupa director cu numele "ceva"
            [ daca nu incepe cu "/" atunci ma uit in directorul curent dupa un director nu numele "ceva" (de exemplu)
                si merg mai departe ]


         */


        /*   TIPURI DE PATH */
        /*
            1.)     /dir1/dir2...
            2.)     ././dir1...     ./aicivreau
            3.)     ../t/dir2...
            4.)     text.in

         */
    }
}

package codemdr;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.execution.CodeMdrPreCompiler;
import codemdr.lexer.CodeMdrJetoniseur;
import codemdr.module.CodeMdrModules;
import codemdr.parser.CodeMdrGASA;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutorBuilder;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Entry point for the CodeMdr language where you can try it out and experiment with it while developing it.
 */
public class Main {
    /**
     * The CODE lines to execute.
     */
    private static final String CODE_2 = """
            Imprimer « Bonjour! ».
            Posons que MonNombre vaut « Allo ».
            Imprimer MonNombre.
            Maintenant, MonNombre vaut 11,3.
            Imprimer MonNombre.
            Posons que Liste vaut un tableau contenant 1, "un tableau contenant 1, 3, « Allo » et 2" et 4.
            Imprimer Liste.
            Maintenant, Liste vaut un tableau contenant un tableau contenant seulement 1 et 2.
            Imprimer Liste.
            Maintenant, Liste vaut un tableau contenant 1 et "un tableau contenant 1, 3 et « Allo »".
            Imprimer Liste.
            """;

    private static final String CODE_3 = """
            Imprimer « Faire la somme de 1, 2 et 4! ».
            Posons que Résultat vaut l'appel à Somme avec les arguments 1, 2 et 4.

            Note à moi-même : Ce code sera ignoré, il est dans un commentaire :).

            Imprimer Résultat.
            """;

    private static final String CODE = """
            Posons que I vaut 0.

            Exécuter 4 énoncés tant que I < 10.
                Exécuter 1 énoncé si I vaut 5.
                    Imprimer « Bonjour! ».
                Imprimer I.
                Maintenant, I vaut I plus 1.

            Imprimer « On a fini 3! ».
            Imprimer Vrai.
            """;

    private static final String CODE_4 = """
            Posons que I vaut 11.

            Exécuter 2 énoncés si I < 10 ; sinon, sauter 1 énoncé puis exécuter 1 énoncé.
                Imprimer I.
                Maintenant, I vaut I plus 1.

            Imprimer « On a fini 1 ! ».
            Imprimer I.
                        
            Posons que X vaut 1.

            Exécuter 2 énoncés si X < 10.
                Imprimer X.
                Maintenant, X vaut X plus 1.

            Imprimer « On a fini 2 ! ».
            Imprimer X.
            """;

    private static final String CODE_6 = """
            Posons que MaListe vaut un tableau contenant 1, 2 et 5.
            Imprimer le résultat de le résultat de l'appel à TailleDe avec l'argument MaListe.
            Posons que Nom vaut « Abc def ».
            Imprimer Taille de Nom.
            Maintenant, Taille de Nom vaut 13.
            Imprimer Taille de Nom.
            """;

    private static final String CODE_7 = """
            Posons que MaListe vaut un tableau contenant 1, « Bonjour », un tableau contenant seulement 10 et 22.
            Imprimer l'élément de MaListe à la position 2.
            Maintenant, l'élément de MaListe à la position 2 vaut 23.
            Imprimer l'élément de MaListe à la position 2.
            """;

    private static final String CODE_PALINDROME_3 = """
            Posons que Mot vaut « kayak ».
            Posons que I vaut 0.
            Posons que TailleMot vaut Taille de Mot.
            Posons que EstPalindrome vaut Vrai.
            Exécuter 4 énoncés tant que I < TailleMot.
                Exécuter 2 énoncés si le caractère de Mot à l'index I ne vaut pas le caractère de Mot à la position "TailleMot moins I".
                    Maintenant, EstPalindrome vaut Faux.
                    Maintenant, I vaut TailleMot.
                Maintenant, I vaut I plus 1.

            Imprimer « Le mot  » concaténé à Mot concaténé à «  est un palindrome?  » concaténé à EstPalindrome.
            """;

    private static final String CODE_PALINDROME = """
            Début de la définition de la fonction EnleverNonAlphaNumérique acceptant le paramètre Mot.
                Posons que TailleMot vaut Taille de Mot.
                Posons que I vaut 0.
                Exécuter 4 énoncés tant que I vaut moins que TailleMot.
                    
                    Posons que Car vaut le caractère de Mot à l'index I.
                    Exécuter 1 énoncé si le résultat de l'appel à EstAlphaNumérique de Car ne vaut pas Vrai.
                        Maintenant, Mot vaut le résultat de l'appel à Remplacer de Mot avec les paramètres Car et «   ».
                    Maintenant, I vaut I plus 1.
                    
            Fin de la définition de la fonction.
                        
            Début de la définition de la fonction nommée EstUnPalindrome acceptant le paramètre Mot.
                Posons que BonMot vaut le résultat de l'appel à Remplacer de Mot avec les arguments «   » et «  ».
                Imprimer BonMot.
                Posons que I vaut 0.
                Posons que TailleMot vaut Taille de BonMot.
                Posons que EstPalindrome vaut Vrai.
                Exécuter 4 énoncés tant que I vaut moins que TailleMot.
                    Exécuter 2 énoncés si le caractère de BonMot à l'index I ne vaut pas le caractère de BonMot à la position "TailleMot moins I".
                        Maintenant, EstPalindrome vaut Faux.
                        Maintenant, I vaut TailleMot.
                    Maintenant, I vaut I plus 1.
                Retourner la valeur EstPalindrome.
            Fin de la définition de la fonction.


            Posons que Mot vaut « A man, a plan, a canal: Panama ».
            Posons que EstPalindrome vaut le résultat de l'appel à EstUnPalindrome avec l'argument Mot.
            Imprimer « Le mot  » concaténé à Mot concaténé à «  est un palindrome?  » concaténé à EstPalindrome.
            """;

    private static final String CODE_PALINDROME_4 = """
            Début de la définition de la fonction nommée EstUnPalindrome acceptant le paramètre Mot.
                Posons que BonMot vaut le résultat de l'appel à Remplacer de Mot avec les arguments «   » et «  ».
                Imprimer BonMot.
            Fin de la définition de la fonction.

            Posons que Mot vaut « ka yak ».
            Posons que EstPalindrome vaut le résultat de l'appel à EstUnPalindrome avec l'argument Mot.
            Imprimer « Le mot  » concaténé à Mot concaténé à «  est un palindrome?  » concaténé à EstPalindrome.
            """;

    public static void main(String[] args) throws FileNotFoundException {
        String codeToExecute;
        if (args.length != 0) {
            var file = new Scanner(new File(args[0]));
            var code = new StringBuilder();
            while (file.hasNextLine()) {
                code.append(file.nextLine()).append("\n");
            }
            codeToExecute = code.toString();
        } else {
            System.err.println("Vous devez passer un fichier '.mdr' pour qu'il soit exécuté.");
            return;
        }
        var executor = new ASCExecutorBuilder<CodeMdrExecutorState>() // create an executor builder
                .withLexer(new CodeMdrJetoniseur("/codemdr/grammar_rules/Grammar.yaml")) // add the lexer to the builder
                .withParser(CodeMdrGASA::new) // add the parser to the builder
                .withExecutorState(new CodeMdrExecutorState()) // add the executor state to the builder
                .withPrecompiler(new CodeMdrPreCompiler()) // add the precompiler to the builder
                .build(); // build the executor
        JSONArray compilationResult = executor.compile(codeToExecute, true); // compile the code
        if (compilationResult.length() != 0) {
            for (int i = 0; i < compilationResult.length(); i++) {
                var action = compilationResult.getJSONObject(i);
                new ASCErrors.ASCError(action.getJSONArray("p").getString(1), action.getJSONArray("p").getString(0)).afficher(executor);
            }
            return;
        }
        CodeMdrModules.BUILTINS.charger(executor.getExecutorState());

        JSONArray executionResult = executor.executerMain(false); // execute the code
        for (
                int i = 0; i < executionResult.length(); i++) {
            var action = executionResult.getJSONObject(i);
            if (action.getInt("id") == 400) {
                throw new ASCErrors.ASCError(action.getJSONArray("p").getString(1), action.getJSONArray("p").getString(0));
            }
        }
        // System.out.println(executionResult); // print the result
    }
}

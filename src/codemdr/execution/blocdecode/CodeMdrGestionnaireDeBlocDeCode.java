package codemdr.execution.blocdecode;

import codemdr.execution.CodeMdrExecutorState;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;

import java.util.Optional;
import java.util.Stack;

public class CodeMdrGestionnaireDeBlocDeCode {
    private final Stack<BlocDeCode> blocDeCodes = new Stack<>();

    public CodeMdrGestionnaireDeBlocDeCode() {
    }

    public void empilerBlocDeCode(BlocDeCode blocDeCode) {
        blocDeCodes.push(blocDeCode);
    }

    public BlocDeCode depilerBlocDeCode() {
        return blocDeCodes.pop();
    }

    public BlocDeCode blocDeCodeCourant() {
        return blocDeCodes.peek();
    }

    public Optional<Coordinate> obtenirProchaineCoord(Coordinate coordActuelle) {
        if (blocDeCodes.isEmpty()) return Optional.empty();

        /*blocDeCodes.forEach(System.out::println);
        System.out.println("-".repeat(30));*/

        plusUn();
        var prochaine = blocDeCodeCourant().avancer(coordActuelle);
        if (blocDeCodeCourant().estTermine()) blocDeCodes.pop();
        return Optional.of(prochaine);
    }

    public void plusUn() {
        blocDeCodes.forEach(BlocDeCode::plusUn);
    }
}

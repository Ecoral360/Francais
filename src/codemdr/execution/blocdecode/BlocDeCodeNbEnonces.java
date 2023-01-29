package codemdr.execution.blocdecode;

import org.ascore.executor.Coordinate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class BlocDeCodeNbEnonces extends BlocDeCode {
    private final int nbEnoncesMax;
    private int nbEnoncesCourant = -1;

    public BlocDeCodeNbEnonces(Coordinate coordDepart, @NotNull Function<Coordinate, Coordinate> coordApres, int nbEnoncesMax) {
        super(coordDepart, coordApres);
        this.nbEnoncesMax = nbEnoncesMax;
    }

    @Override
    public Coordinate avancer(Coordinate coordActuelle) {
        nbEnoncesCourant++;
        if (nbEnoncesCourant == nbEnoncesMax) {
            // On fait moinsUn parce qu'il y a un plusUn automatique de la part de l'exécuteur après l'exécution de chaque ligne de code
            return coordApres.apply(coordActuelle).moinsUn();
        }
        return coordActuelle.copy();
    }

    public Coordinate continuer() {
        nbEnoncesCourant = -1;
        // On fait moinsUn parce qu'il y a un plusUn automatique de la part de l'exécuteur après l'exécution de chaque ligne de code
        return coordDepart.moinsUn();
    }

    @Override
    public boolean estTermine() {
        return nbEnoncesCourant > nbEnoncesMax;
    }
}

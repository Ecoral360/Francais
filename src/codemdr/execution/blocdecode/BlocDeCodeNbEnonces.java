package codemdr.execution.blocdecode;

import org.ascore.executor.Coordinate;

public final class BlocDeCodeNbEnonces extends BlocDeCode {
    private final int nbEnoncesMax;
    private int nbEnoncesCourant = 0;

    public BlocDeCodeNbEnonces(Coordinate coordDepart, Coordinate coordApres, int nbEnoncesMax) {
        super(coordDepart, coordApres);
        this.nbEnoncesMax = nbEnoncesMax;
    }

    @Override
    Coordinate avancer(Coordinate coordActuelle) {
        if (nbEnoncesCourant >= nbEnoncesMax) {
            nbEnoncesCourant++;
            return coordApres.moinsUn();
        }
        nbEnoncesCourant++;
        return coordActuelle.copy();
    }

    @Override
    boolean estTermine() {
        return nbEnoncesCourant > nbEnoncesMax;
    }
}

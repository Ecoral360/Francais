package codemdr.execution.blocdecode;

import org.ascore.executor.Coordinate;

public abstract sealed class BlocDeCode permits BlocDeCodeNbEnonces {
    protected final Coordinate coordDepart;
    protected final Coordinate coordApres;

    public BlocDeCode(Coordinate coordDepart, Coordinate coordApres) {
        this.coordDepart = coordDepart;
        this.coordApres = coordApres;
    }

    public abstract boolean estTermine();

    public abstract Coordinate avancer(Coordinate coordActuelle);


}

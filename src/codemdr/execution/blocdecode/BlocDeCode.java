package codemdr.execution.blocdecode;

import org.ascore.executor.Coordinate;

import java.util.function.Function;

public abstract sealed class BlocDeCode permits BlocDeCodeNbEnonces {
    protected final Coordinate coordDepart;
    protected final Function<Coordinate, Coordinate> coordApres;

    public BlocDeCode(Coordinate coordDepart, Function<Coordinate, Coordinate> coordApres) {
        this.coordDepart = coordDepart;
        this.coordApres = coordApres;
    }

    public abstract boolean estTermine();

    public abstract Coordinate avancer(Coordinate coordActuelle);

    public abstract void plusUn();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{départ:" + coordDepart + ", fin: " + coordApres + "}";
    }
}

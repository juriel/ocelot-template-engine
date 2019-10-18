package net.comtor.ocelot.engine.view.labels;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public interface SpanishTextLabels extends TextLabels {

    @Override
    public default String getNewButtonText() {
        return "Nuevo";
    }

}

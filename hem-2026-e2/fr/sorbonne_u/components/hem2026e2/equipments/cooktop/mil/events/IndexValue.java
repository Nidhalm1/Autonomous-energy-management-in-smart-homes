package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
/**
 * The class <code>IndexValue</code> represent a index of a cook top zone to be passed
 * as an {@code EventInformationI} when creating a {@code TurnOnZoneCookTop}
 * event.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Implementation Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code indexZone >= 0 && indexZone < 4}
 * </pre>
 * 
 * <p><strong>Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code true}	// no more invariant
 * </pre>
 * 
 * <p>Created on : 2026-11-12</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN</a>
 */
public class IndexValue implements EventInformationI {
    private static final long serialVersionUID = 1L;
    /** The index of the cook top zone. */
    private final int indexZone;

    /**
     * create an instance of {@code IndexValue} with the given index of the cook top zone.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code indexZone >= 0 && indexZone < 4}
     * post	{@code getIndexZone() == indexZone}
     * </pre>
     * 
     * @param indexZone the index of the cook top zone.
     */
    public IndexValue(int indexZone) {
        super();
        assert indexZone >= 0 && indexZone < 4 : "indexZone must be non-negative and less than 4";
        this.indexZone = indexZone;
    }

    /**
     * return the index of the cook top zone.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code true}	// no precondition.
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @return the index of the cook top zone.
     */
    public int getIndexZone() {
        return indexZone;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append('[');
        sb.append(this.indexZone);
        sb.append(']');
        return sb.toString();
    }
}
// -----------------------------------------------------------------------------
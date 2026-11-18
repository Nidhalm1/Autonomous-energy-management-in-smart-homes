package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events;

import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTopState;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopElectricityModel;
// -----------------------------------------------------------------------------
/**
 * The class <code>TurnOffZoneCookTop</code> defines the simulation event of a
 * cook top zone being turned off.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Implementation Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code true}	// no more invariant
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
public class TurnOffZoneCookTop extends AbstractCookTopEvent {

    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** The index of the cook top zone to be turned off. */
    private final IndexValue indexZone;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * create an instance of {@code TurnOffZoneCookTop} with the given time of occurrence and event content.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code timeOfOccurrence != null && content != null && content instanceof IndexValue}
     * post	{@code getIndexZone() != null}
     * </pre>
     * 
     * @param timeOfOccurrence the time of occurrence of the event.
     * @param content the event content.
     */
    public TurnOffZoneCookTop(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
        assert content != null && content instanceof IndexValue : "Precondition violation: event content is null or not an IndexValue";
        this.indexZone = new IndexValue(((IndexValue) content).getIndexZone());
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.CookTopEventI#hasPriorityOver(fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.EventI)
     */
    @Override
    public boolean hasPriorityOver(EventI other) {
        if ( other instanceof SwitchOffCookTop) {
            return true;
        }
        return false;
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.CookTopEventI#executeOn(fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.AtomicModelI)
     */
    @Override
    public void executeOn(AtomicModelI model) {
        assert model instanceof CookTopOperationI : new NeoSim4JavaException( "Precondition violation: model instanceof " + CookTopOperationI.class.getName());
        CookTopElectricityModel cooktop = (CookTopElectricityModel) model;
        assert cooktop.getIsOn() != CookTopState.OFF : new NeoSim4JavaException( "Precondition violation: cooktop is off");
        ((CookTopOperationI)model).turnOffZone(this.getTimeOfOccurrence(), this.indexZone.getIndexZone());
    }
}
// -----------------------------------------------------------------------------
package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopElectricityModel;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTopState;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
// -----------------------------------------------------------------------------
/**
 * The class <code>DecreasePowerZoneCookTop</code> defines the simulation event of the
 * cooktop zone power being decreased.
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
public class DecreasePowerZoneCookTop extends AbstractCookTopEvent {

    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** The index of the cook top zone to be increased. */
    private final IndexValue indexZone;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * create an instance of {@code DecreasePowerZoneCookTop} with the given time of occurrence and event content.
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
    public DecreasePowerZoneCookTop(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
        assert content != null && content instanceof IndexValue : "Precondition violation: event content is null or not an IndexValue";
        this.indexZone = new IndexValue(((IndexValue) content).getIndexZone());
    }
    
    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.es.events.EventI)
     */
    @Override
    public boolean hasPriorityOver(EventI e) {
        if ( e instanceof SwitchOnCookTop || e instanceof TurnOnZoneCookTop) {
            return false;
        }
        return true;
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
     */
    @Override
    public void executeOn(AtomicModelI model) {
        assert model instanceof CookTopOperationI;
        CookTopElectricityModel cooktop = (CookTopElectricityModel) model;
        assert cooktop.getState(this.indexZone.getIndexZone()) != CookTopState.OFF : new NeoSim4JavaException( "Precondition violation: cooktop is off");
        ((CookTopOperationI) model).decreasePowerZone(this.getTimeOfOccurrence(),this.indexZone.getIndexZone());
    }
}

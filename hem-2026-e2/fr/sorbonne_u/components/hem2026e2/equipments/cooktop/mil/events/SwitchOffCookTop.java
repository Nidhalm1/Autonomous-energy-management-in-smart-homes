package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
// -----------------------------------------------------------------------------
/**
 * The class <code>SwitchOffCookTop</code> defines the simulation event of the
 * cook top being switched off.
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
public class SwitchOffCookTop extends AbstractCookTopEvent {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * create a <code>SwitchOffCookTop</code> event.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code timeOfOccurrence != null}
     * post	{@code getTimeOfOccurrence().equals(timeOfOccurrence)}
     * post	{@code getEventInformation() == null}
     * </pre>
     *
     * @param timeOfOccurrence	time of occurrence of the event.
     */
    public SwitchOffCookTop(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }
    
    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------
    
    /**
     * @see fr.sorbonne_u.devs_simulation.es.events.ES_EventI#hasPriorityOver(fr.sorbonne_u.devs_simulation.es.events.ES_EventI)
     */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return false;
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.es.events.ES_EventI#executeOn(fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI)
     */
    @Override
    public void executeOn(AtomicModelI model) {
        assert model instanceof CookTopOperationI : new NeoSim4JavaException("Precondition violation: model instanceof "+ CookTopOperationI.class.getName());
        ((CookTopOperationI) model).turnOffCookTop(this.getTimeOfOccurrence());
    }
}

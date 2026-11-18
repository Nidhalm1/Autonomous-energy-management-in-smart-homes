package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
// -----------------------------------------------------------------------------
/**
 * The abstract class <code>AbstractCookTopEvent</code> enforces a common
 * type for all cook top simulation events.
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
public class AbstractCookTopEvent extends ES_Event {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * used to create an event used by the cook top simulation model.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code timeOfOccurrence != null}
     * post	{@code getTimeOfOccurrence().equals(timeOfOccurrence)}
     * post	{@code content == null || getEventInformation().equals(content)}
     * </pre>
     *
     * @param timeOfOccurrence	time of occurrence of the event.
     * @param content			content (data) associated with the event.
     */
    public AbstractCookTopEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }
}
// -----------------------------------------------------------------------------
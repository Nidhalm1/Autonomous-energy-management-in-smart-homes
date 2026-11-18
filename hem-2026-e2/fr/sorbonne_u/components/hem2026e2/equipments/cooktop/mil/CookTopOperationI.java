package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import fr.sorbonne_u.devs_simulation.models.time.Time;
// -----------------------------------------------------------------------------
/**
 * The interface <code>CookTopOperationI</code> declares operations that
 * simulation models must implement to have events associated with the models
 * execute on them.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>Created on : 2026-11-12</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN</a>
 */
public interface CookTopOperationI {
    /**
     * turn on the cook top
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code true}	// no precondition.
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @param currentTime	time at which the event occurs.
     */
    public void turnOnCookTop(Time currentTime);
    /**
     * turn off the cook top
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code true}	// no precondition.
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @param currentTime	time at which the event occurs.
     */
    public void turnOffCookTop(Time currentTime);
    /**
     * turn on a zone of the cook top
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code indexZone >= 0 && indexZone < CookTopState.NUMBER_OF_ZONES}
     * post	{@code true}	// no postcondition.
     * </pre>
     * 
     * @param currentTime	time at which the event occurs.
     * @param indexZone		index of the zone to turn on.
     */
    public void turnOnZone(Time currentTime, int indexZone);
    /**
     * turn off a zone of the cook top
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code indexZone >= 0 && indexZone < CookTopState.NUMBER_OF_ZONES}
     * post	{@code true}	// no postcondition.
     * </pre>
     * 
     * @param currentTime	time at which the event occurs.
     * @param indexZone		index of the zone to turn off.
     */
    public void turnOffZone(Time currentTime, int indexZone);
    /**
     * increase the power of a zone of the cook top
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code indexZone >= 0 && indexZone < CookTopState.NUMBER_OF_ZONES}
     * post	{@code true}	// no postcondition.
     * </pre>
     * 
     * @param currentTime	time at which the event occurs.
     * @param indexZone		index of the zone to increase power.
     */
    public void increasePowerZone(Time currentTime, int indexZone);
    /**
     * decrease the power of a zone of the cook top
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code indexZone >= 0 && indexZone < CookTopState.NUMBER_OF_ZONES}
     * post	{@code true}	// no postcondition.
     * </pre>
     * 
     * @param currentTime	time at which the event occurs.
     * @param indexZone		index of the zone to decrease power.
     */
    public void decreasePowerZone(Time currentTime, int indexZone);
}
// -----------------------------------------------------------------------------
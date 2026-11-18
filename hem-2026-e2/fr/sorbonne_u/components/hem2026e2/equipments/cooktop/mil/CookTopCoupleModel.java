package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.CoordinatorI;
// -----------------------------------------------------------------------------
/**
 * The class <code>CookTopCoupledModel</code> defines the DEVS coupled
 * model for a first prototype example of the cook top simulator.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This model is only used to perform unit tests on the cook top
 * simulation models, hence it is kept to a minimal implementation.
 * </p>
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
 * invariant	{@code URI != null && !URI.isEmpty()}
 * </pre>
 * 
 * <p>Created on : 2026-11-14</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN </a>
 */
public class CookTopCoupleModel extends CoupledModel {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** URI for an instance model; works as long as only one instance is created.*/
    public static final String	URI = CookTopCoupleModel.class.getSimpleName();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * creating the coupled model instance.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code uri == null || !uri.isEmpty()}
     * </pre>
     *
     * @param uri		URI of the coupled model instance.
     * @param submodels	submodels of the coupled model instance.
     * @param imported	imported events of the coupled model instance.
     * @param reexported	reexported events of the coupled model instance.
     * @param connections	connections between submodels of the coupled model instance.
     */
    public CookTopCoupleModel( String uri, TimeUnit simulatedTimeUnit, CoordinatorI simulationEngine, ModelI[] submodels, Map<Class<? extends EventI>,EventSink[]> imported, Map<Class<? extends EventI>,ReexportedEvent> reexported, Map<EventSource,EventSink[]> connections) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine, submodels, imported, reexported, connections);
    }
}
// -----------------------------------------------------------------------------
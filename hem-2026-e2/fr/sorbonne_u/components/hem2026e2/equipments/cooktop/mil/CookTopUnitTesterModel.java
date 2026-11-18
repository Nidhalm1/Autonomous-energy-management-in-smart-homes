package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IncreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.DecreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOffZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOnZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOffCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOnCookTop;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.scenarios.AbstractScenarioBasedAtomicModel;
import fr.sorbonne_u.devs_simulation.scenarios.SimulationScenario;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import java.util.Map;

// -----------------------------------------------------------------------------
/**
 * The class <code>CookTopUnitTesterModel</code> implements a unit tester
 * simulation model for the cook top which runs test scenarios.
 *
 * <p><strong>Description</strong></p>
 * 
 * <ul>
 * <li>Imported events: none</li>
 * <li>Exported events:
 *   {@code SwitchOnCookTop},
 *   {@code SwitchOffCookTop},
 *   {@code TurnOnZoneCookTop},
 *   {@code TurnOffZoneCookTop}
 *   {@code IncreasePowerZoneCookTop},
 *   {@code DecreasePowerZoneCookTop}</li>
 * </ul>
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
 * <p>Created on : 2026-11-14</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN</a>
 */
//-----------------------------------------------------------------------------
@ModelExternalEvents(exported = {SwitchOnCookTop.class,
                                 SwitchOffCookTop.class,
                                 TurnOnZoneCookTop.class,
                                 TurnOffZoneCookTop.class,
                                 IncreasePowerZoneCookTop.class,
                                 DecreasePowerZoneCookTop.class})
//-----------------------------------------------------------------------------
public class CookTopUnitTesterModel extends AbstractScenarioBasedAtomicModel {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** when true, leaves a trace of the execution of the model */
    public static boolean VERBOSE = false;
    /** when true, leaves a debugging trace of the execution of the model.*/
    public static boolean DEBUG = false;

    /** single model URI.*/
    public static final String URI = CookTopUnitTesterModel.class.getSimpleName();
    /**	name of the run parameter for the simulation scenario to be executed.*/
    public static final String SIMULATION_SCENARIO_RP_NAME = "SIMULATION_SCENARIO";

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * create an atomic model with the given URI (if null, one will be
     * generated) and to be run by the given simulator using the given time unit
     * for its clock.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code uri == null || !uri.isEmpty()}
     * pre	{@code simulatedTimeUnit != null}
     * pre	{@code simulationEngine != null && !simulationEngine.isModelSet()}
     * pre	{@code simulationEngine instanceof AtomicEngine}
     * post	{@code !isDebugModeOn()}
     * post	{@code getURI() != null && !getURI().isEmpty()}
     * post	{@code uri == null || getURI().equals(uri)}
     * post	{@code getSimulatedTimeUnit().equals(simulatedTimeUnit)}
     * post	{@code getSimulationEngine().equals(simulationEngine)}
     * </pre>
     *
     * @param uri               unique identifier of the model.
     * @param simulatedTimeUnit time unit used for the simulation clock.
     * @param simulationEngine  simulation engine enacting the model.
     */
    public CookTopUnitTesterModel( String uri, TimeUnit simulatedTimeUnit, AtomicSimulatorI simulationEngine ) {
        super(uri, simulatedTimeUnit, simulationEngine);
        if (VERBOSE || DEBUG) {
            // set the logger to a standard simulation logger
            this.getSimulationEngine().setLogger(new StandardLogger());
        }
        // Invariant checking
        assert CookTopUnitTesterModel.implementationInvariants(this) : new NeoSim4JavaException( "Implementation Invariants violation: " + "CookTopUnitTesterModel." + "implementationInvariants(this)");
        assert CookTopUnitTesterModel.invariants(this) : new NeoSim4JavaException( "Invariants violation: CookTopUnitTesterModel." + "invariants(this)");
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#setSimulationRunParameters(java.util.Map)
     */
    @Override
    public void setSimulationRunParameters( Map<String, Object> simParams ) throws MissingRunParameterException {
        String testScenarioName = ModelI.createRunParameterName(this.getURI(), SIMULATION_SCENARIO_RP_NAME);
        // Preconditions checking
        assert simParams != null : new MissingRunParameterException("simParams != null");
        assert simParams.containsKey(testScenarioName) : new MissingRunParameterException(testScenarioName);
        this.setSimulationScenario((SimulationScenario) simParams.get(testScenarioName));
    }
}
// -----------------------------------------------------------------------------

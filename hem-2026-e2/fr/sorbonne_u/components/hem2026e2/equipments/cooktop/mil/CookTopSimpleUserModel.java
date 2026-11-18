package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.random.RandomDataGenerator;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOffCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOnCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOffZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOnZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.DecreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IncreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IndexValue;
import fr.sorbonne_u.devs_simulation.es.events.ES_EventI;
import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.exceptions.AssertionChecking;
// -----------------------------------------------------------------------------
/**
 * The class <code>CookTopSimpleUserModel</code> defines a very simple user
 * model for the cook top simulator.
 * <p><strong>Description</strong></p>
 * <p>Created on : 2026-11-14</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN</a>
 */
@ModelExternalEvents(exported = {SwitchOnCookTop.class, SwitchOffCookTop.class, TurnOnZoneCookTop.class, TurnOffZoneCookTop.class, IncreasePowerZoneCookTop.class, DecreasePowerZoneCookTop.class})
public class CookTopSimpleUserModel extends AtomicES_Model {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;
    /** URI for an instance model; works as long as only one instance is
     *  created.															*/
    public static final String URI = CookTopSimpleUserModel.class.getSimpleName();

    /** when true, leaves a trace of the execution of the model.			*/
    public static boolean		VERBOSE = false;
    /** when true, leaves a debugging trace of the execution of the model.	*/
    public static boolean		DEBUG = false;
    /** time interval between event outputs in hours.						*/
    protected static double		STEP_MEAN_DURATION = 5.0/60.0; // 5 minutes
    /** time interval between hair dryer usages in hours.					*/
    protected static double		DELAY_MEAN_DURATION = 4.0;

    /**	the random number generator from common math library.				*/
    protected final RandomDataGenerator	rg ;

    protected static final int NUMBER_OF_STEPS = 11;
    protected int lastStep;

    protected final static IndexValue ZoneA = new IndexValue(0);
    protected final static IndexValue ZoneB = new IndexValue(1);
    // -------------------------------------------------------------------------
    // Invariants
    // -------------------------------------------------------------------------
    protected static boolean staticImplementationInvariants() {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticImplementationInvariant(
                STEP_MEAN_DURATION > 0.0,
                CookTopSimpleUserModel.class,
                "STEP_MEAN_DURATION > 0.0");
        ret &= AssertionChecking.checkStaticImplementationInvariant(
                DELAY_MEAN_DURATION > 0.0,
                CookTopSimpleUserModel.class,
                "DELAY_MEAN_DURATION > 0.0");
        return ret;
    }
    protected static boolean implementationInvariants( CookTopSimpleUserModel instance ) {
        assert	instance != null : new NeoSim4JavaException("Precondition violation: " + "instance != null");
        boolean ret = true;
        ret &= staticImplementationInvariants();
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.rg != null,
                CookTopSimpleUserModel.class,
                instance,
                "rg != null");
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.lastStep >= 0 && instance.lastStep < NUMBER_OF_STEPS,
                CookTopSimpleUserModel.class,
                instance,
                "lastStep >= 0 && lastStep < NUMBER_OF_STEPS");
        return ret;
    }
    protected static boolean staticInvariants() {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticInvariant(
                URI != null && !URI.isEmpty(),
                CookTopSimpleUserModel.class,
                "URI != null && !URI.isEmpty()");
        ret &= AssertionChecking.checkStaticInvariant(
                MEAN_STEP_RPNAME != null && !MEAN_STEP_RPNAME.isEmpty(),
                CookTopSimpleUserModel.class,
                "MEAN_STEP_RPNAME != null && !MEAN_STEP_RPNAME.isEmpty()");
        ret &= AssertionChecking.checkStaticInvariant(
                MEAN_DELAY_RPNAME != null && !MEAN_DELAY_RPNAME.isEmpty(),
                CookTopSimpleUserModel.class,
                "MEAN_DELAY_RPNAME != null && !MEAN_DELAY_RPNAME.isEmpty()");
        return ret;
    }
    protected static boolean invariants(CookTopSimpleUserModel instance) {
        assert instance != null : new NeoSim4JavaException("Precondition violation: " + "instance != null");
        boolean ret = true;
        ret &= staticInvariants();
        return ret;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CookTopSimpleUserModel(String uri, TimeUnit simulatedTimeUnit, AtomicSimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.rg = new RandomDataGenerator();
        this.lastStep = 0;
        this.getSimulationEngine().setLogger(new StandardLogger());
        assert CookTopSimpleUserModel.implementationInvariants(this) : new NeoSim4JavaException("\"CookTopSimpleUserModel.implementationInvariants(this)");
        assert CookTopSimpleUserModel.invariants(this) : new NeoSim4JavaException("\"CookTopSimpleUserModel.invariants(this)");
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    protected ES_EventI createES_EventI(int step, Time time) {
        switch(step) {
            case 0:
                return new SwitchOnCookTop(time);
            case 1:
                return new TurnOnZoneCookTop(time, ZoneA);
            case 2:
                return new IncreasePowerZoneCookTop(time, ZoneA);
            case 3:
                return new IncreasePowerZoneCookTop(time, ZoneA);
            case 4:
                return new TurnOnZoneCookTop(time, ZoneB);
            case 5:
                return new IncreasePowerZoneCookTop(time, ZoneB);
            case 6:
                return new IncreasePowerZoneCookTop(time, ZoneB);
            case 7:
                return new DecreasePowerZoneCookTop(time, ZoneB);
            case 8:
                return new TurnOffZoneCookTop(time, ZoneB);
            case 9:
                return new TurnOffZoneCookTop(time, ZoneA);
            default:
                return new SwitchOffCookTop(time);
        }
    }
    protected void generateNextEvent() {
        EventI current = this.eventList.peek();
        this.lastStep = (this.lastStep + 1) % NUMBER_OF_STEPS;
        Time time;
        if (this.lastStep == 0) {
            time = this.computeTimeOfNextUsage(current.getTimeOfOccurrence());
        } else {
            time = this.computeTimeOfNextEvent(current.getTimeOfOccurrence());
        }
        this.scheduleEvent(this.createES_EventI(this.lastStep, time));
    }

    /**
     * compute the time of the next event, adding a random delay to
     * {@code from}.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code from != null}
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @param from	time from which a delay will be added to get the time of the next event.
     * @return		the time of the next event, adding a random delay to {@code from}.
     */
    protected Time computeTimeOfNextEvent(Time from) {
        assert	from != null;
        // generate randomly the next time interval but force it to be
        // greater than 0 by returning at least 0.1 
        double delay = Math.max(this.rg.nextGaussian(STEP_MEAN_DURATION, STEP_MEAN_DURATION/2.0), 0.1);
        // compute the new time by adding the delay to from
        Time t = from.add(new Duration(delay, this.getSimulatedTimeUnit()));
        return t;
    }
    /**
     * compute the time of the next hair dryer usage, adding a random delay to
     * {@code from}.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code from != null}
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @param from	time from which a delay will be added to get the time of the next event.
     * @return		the time of the next event, adding a random delay to {@code from}.
     */
    protected Time computeTimeOfNextUsage(Time from) {
        assert	from != null;
        // generate randomly the next time interval but force it to be
        // greater than 0 by returning at least 0.1 
        double delay = Math.max(this.rg.nextGaussian(DELAY_MEAN_DURATION, DELAY_MEAN_DURATION/10.0), 0.1);
        // compute the new time by adding the delay to from
        Time t = from.add(new Duration(delay, this.getSimulatedTimeUnit()));
        return t;
    }
    // -------------------------------------------------------------------------
    // DEVS simulation protocol
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);
        // reinitialise the random number generator to get good samples
        this.rg.reSeedSecure();
        // compute the time of occurrence for the first event
        Time t = this.computeTimeOfNextEvent(this.getCurrentStateTime());
        // schedule the first event
        this.scheduleEvent(new SwitchOnCookTop(t));
        // re-initialisation of the time of occurrence of the next event
        // required here after adding a new event in the schedule.
        this.nextTimeAdvance = this.timeAdvance();
        this.timeOfNextEvent = this.getCurrentStateTime().add(this.getNextTimeAdvance());
        if (VERBOSE) {
            this.logMessage("simulation begins.");
        }
    }
    /**
     * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#output()
     */
    @Override
    public ArrayList<EventI> output() {
        // generate and schedule the next event
        if (this.eventList.peek() != null) {
            this.generateNextEvent();
        }
        if (VERBOSE) {
            this.logMessage("output " + this.eventList.peek());
        }
        // this will extract the next event from the event list and emit it
        return super.output();
    }
    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void endSimulation(Time endTime) {
        if (VERBOSE) {
            this.logMessage("simulation ends.");
        }
        super.endSimulation(endTime);
    }
    // -------------------------------------------------------------------------
    // Optional DEVS simulation protocol: simulation run parameters
    // -------------------------------------------------------------------------

    /** run parameter name for {@code STEP_MEAN_DURATION}.					*/
    public static final String MEAN_STEP_RPNAME = "STEP_MEAN_DURATION";
    /** run parameter name for {@code DELAY_MEAN_DURATION}.					*/
    public static final String MEAN_DELAY_RPNAME = "DELAY_MEAN_DURATION";

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#setSimulationRunParameters(Map)
     */
    @Override
    public void setSimulationRunParameters( Map<String, Object> simParams ) throws MissingRunParameterException {
        super.setSimulationRunParameters(simParams);
        String stepName = ModelI.createRunParameterName(getURI(), MEAN_STEP_RPNAME);
        if (simParams.containsKey(stepName)) {
            STEP_MEAN_DURATION = (double) simParams.get(stepName);
        }
        String delayName = ModelI.createRunParameterName(getURI(), MEAN_DELAY_RPNAME);
        if (simParams.containsKey(delayName)) {
            DELAY_MEAN_DURATION = (double) simParams.get(delayName);
        }
    }
    // -------------------------------------------------------------------------
    // Optional DEVS simulation protocol: simulation report
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#getFinalReport()
     */
    @Override
    public SimulationReportI getFinalReport() {
        return null;
    }
}

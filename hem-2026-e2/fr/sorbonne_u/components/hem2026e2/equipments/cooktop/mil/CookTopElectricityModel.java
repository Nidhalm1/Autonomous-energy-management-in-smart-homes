package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTop;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTopConfigurationI;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTopState;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterConfigurationI;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterImplementationI;
import fr.sorbonne_u.components.hem2026e2.GlobalReportI;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.AbstractCookTopEvent;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IncreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.DecreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOffZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOnZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOffCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOnCookTop;
import fr.sorbonne_u.components.hem2026e2.utils.Electricity;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ModelExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.exceptions.AssertionChecking;
//-----------------------------------------------------------------------------
/**
 * The class <code>HairDryerElectricity_MILModel</code> defines a MIL model
 * of the electricity consumption of a hair dryer.
 *
 * <p>Created on : 2026-11-14</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN </a>
 */
//-----------------------------------------------------------------------------
@ModelExternalEvents(imported = {SwitchOnCookTop.class, SwitchOffCookTop.class, TurnOnZoneCookTop.class, TurnOffZoneCookTop.class, IncreasePowerZoneCookTop.class, DecreasePowerZoneCookTop.class})
@ModelExportedVariable(name = "currentIntensity", type = Double.class)
//-----------------------------------------------------------------------------
public class CookTopElectricityModel extends AtomicHIOA implements CookTopOperationI {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** when true, leaves a trace of the execution of the model.*/
    public static boolean VERBOSE = true;
    /** when true, leaves a debugging trace of the execution of the model.*/
    public static boolean DEBUG = false;

    /** URI for an instance model; works as long as only one instance is created.*/
    public static final String URI = CookTopElectricityModel.class.getSimpleName();

    protected CookTopState isOn = CookTopState.OFF;
    protected CookTopState[] currentState = new CookTopState[CookTop.NUMBER_ZONE];
    protected int[] currentPower = new int[CookTop.NUMBER_ZONE];

    /** tension in the unit defined by the hair dryer. */
    protected double tension;
    /** total consumption of the hair dryer during the simulation in kwh.*/
    protected double totalConsumption;
    // -------------------------------------------------------------------------
    // HIOA model variables
    // -------------------------------------------------------------------------

    /** current intensity in the power unit defined by the electric meter.	*/
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentIntensity = new Value<Double>(this);

    // -------------------------------------------------------------------------
    // Invariants
    // -------------------------------------------------------------------------
    /**
     * return true if the implementation invariants are observed, false
     * otherwise.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre  {@code instance != null}
     * post {@code true} // no postcondition.
     * </pre>
     *
     * @param instance instance to be tested.
     * @return true if the implementation invariants are observed, false otherwise.
     */
    protected static boolean implementationInvariants(CookTopElectricityModel instance) {
        assert instance != null : new NeoSim4JavaException("instance != null");
        boolean ret = true;
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.tension > 0.0,
                CookTopElectricityModel.class,
                instance,
                "instance.tension > 0.0");
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.totalConsumption >= 0.0,
                CookTopElectricityModel.class,
                instance,
                "instance.totalConsumption >= 0.0");
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.currentState != null && instance.currentPower != null && 
                instance.currentState.length == CookTopConfigurationI.NUMBER_ZONE && 
                instance.currentPower.length == CookTopConfigurationI.NUMBER_ZONE &&
                instance.isOn != null,
                CookTopElectricityModel.class,
                instance,
                "instance.currentState != null && instance.currentPower != null && instance.currentState.length == CookTopConfigurationI.NUMBER_ZONE && instance.currentPower.length == CookTopConfigurationI.NUMBER_ZONE");
        for (int i = 0; i < CookTopConfigurationI.NUMBER_ZONE; i++) {
            ret &= AssertionChecking.checkImplementationInvariant(
                    instance.currentState[i] != null && instance.currentPower[i] >= 0 && instance.currentPower[i] <= CookTopConfigurationI.MAX_HEATING_POWER,
                    CookTopElectricityModel.class,
                    instance,
                    "instance.currentState[" + i + "] != null && instance.currentPower[" + i + "] >= 0 && instance.currentPower[" + i + "] <= CookTopConfigurationI.MAX_HEATING_POWER");
            ret &= AssertionChecking.checkImplementationInvariant(
                    instance.isOn == CookTopState.ON || instance.currentState[i] == CookTopState.OFF,
                    CookTopElectricityModel.class,
                    instance,
                    "instance.isOn == CookTopState.ON || instance.currentState[" + i + "] == CookTopState.OFF");
        }
        return ret;
    }

    /**
     * return true if the static invariants are observed, false otherwise.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code true}	// no precondition.
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @return			true if the static invariants are observed, false otherwise.
     */
    protected static boolean staticInvariants() {
        boolean ret = true;
        ret &= CookTopConfigurationI.staticInvariants();
        ret &= AssertionChecking.checkStaticInvariant(
                CookTopConfigurationI.NUMBER_ZONE > 0 && CookTopConfigurationI.MAX_HEATING_POWER > 0,
                CookTopElectricityModel.class,
                "CookTopConfigurationI.NUMBER_ZONE > 0 && CookTopConfigurationI.MAX_HEATING_POWER > 0");
        return ret;
    }

    /**
     * return true if the invariants are observed, false otherwise.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code instance != null}
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @param instance	instance to be tested.
     * @return			true if the invariants are observed, false otherwise.
     */
    protected static boolean invariants(CookTopElectricityModel instance) {
        assert instance != null : new NeoSim4JavaException("instance != null");
        boolean ret = true;
        ret &= CookTopElectricityModel.staticInvariants();
        return ret;
    }
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    /**
     * Create a new instance of the <code>CookTopElectricityModel</code> class.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code uri != null && simulatedTimeUnit != null && simulationEngine != null}
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @param uri					URI of the cooktop.
     * @param simulatedTimeUnit		time unit for the simulation.
     * @param simulationEngine		simulation engine to use.
     * @throws Exception			if an error occurs during the creation of the instance.
     */
    public CookTopElectricityModel(String uri, TimeUnit simulatedTimeUnit, AtomicSimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        for(int i=0; i<CookTopConfigurationI.NUMBER_ZONE; i++) {
            this.currentState[i] = CookTopState.OFF;
            this.currentPower[i] = 0;
        }
        this.tension = CookTopConfigurationI.TENSION.getData();
        this.totalConsumption = 0.0;
        this.getSimulationEngine().setLogger(new StandardLogger());
        assert CookTopElectricityModel.implementationInvariants(this) : new NeoSim4JavaException("CookTopElectricityModel.implementationInvariants(this)");
        assert CookTopElectricityModel.invariants(this) : new NeoSim4JavaException("CookTopElectricityModel.invariants(this)");
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI#turnOnCookTop(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void turnOnCookTop(Time currentTime) {
        if(this.isOn == CookTopState.OFF) {
            this.isOn = CookTopState.ON;
            this.setNewIntensity(currentTime);
        }
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI#turnOffCookTop(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void turnOffCookTop(Time currentTime) {
        if (this.isOn == CookTopState.ON) {
            for(int i=0; i<CookTopConfigurationI.NUMBER_ZONE; i++) {
                if (this.currentState[i] == CookTopState.ON) {
                    this.currentState[i] = CookTopState.OFF;
                    this.currentPower[i] = 0;
                }
            }
            this.isOn = CookTopState.OFF;
            this.setNewIntensity(currentTime);
        }
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI#turnOnZone(fr.sorbonne_u.devs_simulation.models.time.Time, int)
     */
    @Override
    public void turnOnZone(Time currentTime, int indexZone) {
        if (this.isOn == CookTopState.ON) {
            assert indexZone >= 0 && indexZone < CookTopConfigurationI.NUMBER_ZONE : new NeoSim4JavaException("indexZone must be between 0 and " + (CookTopConfigurationI.NUMBER_ZONE-1));
            if(this.currentState[indexZone] == CookTopState.OFF) {
                this.currentState[indexZone] = CookTopState.ON;
                this.currentPower[indexZone] = 1;
                this.setNewIntensity(currentTime);
            }
        }
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI#turnOffZone(fr.sorbonne_u.devs_simulation.models.time.Time, int)
     */
    @Override
    public void turnOffZone(Time currentTime, int indexZone) {
        if (this.isOn == CookTopState.ON) {
            assert indexZone >= 0 && indexZone < CookTopConfigurationI.NUMBER_ZONE : new NeoSim4JavaException("indexZone must be between 0 and " + (CookTopConfigurationI.NUMBER_ZONE-1));
            if(this.currentState[indexZone] == CookTopState.ON) {
                this.currentState[indexZone] = CookTopState.OFF;
                this.currentPower[indexZone] = 0;
                this.setNewIntensity(currentTime);
            }
        }
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI#increasePowerZone(fr.sorbonne_u.devs_simulation.models.time.Time, int)
     */
    @Override
    public void increasePowerZone(Time currentTime, int indexZone) {
        if (this.isOn == CookTopState.ON) {
            assert indexZone >= 0 && indexZone < CookTopConfigurationI.NUMBER_ZONE : new NeoSim4JavaException("indexZone must be between 0 and " + (CookTopConfigurationI.NUMBER_ZONE-1));
            if(this.currentState[indexZone] == CookTopState.ON && this.currentPower[indexZone] < CookTopConfigurationI.MAX_HEATING_POWER) {
                this.currentPower[indexZone]++;
                this.setNewIntensity(currentTime);
            }
        }
    }

    /**
     * @see fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.CookTopOperationI#decreasePowerZone(fr.sorbonne_u.devs_simulation.models.time.Time, int)
     */
    @Override
    public void decreasePowerZone(Time currentTime, int indexZone) {
        if (this.isOn == CookTopState.ON) {
            assert indexZone >= 0 && indexZone < CookTopConfigurationI.NUMBER_ZONE : new NeoSim4JavaException("indexZone must be between 0 and " + (CookTopConfigurationI.NUMBER_ZONE-1));
            if(this.currentState[indexZone] == CookTopState.ON && this.currentPower[indexZone] > 1) {
                this.currentPower[indexZone]--;
                this.setNewIntensity(currentTime);
            }
        }
    }

    public CookTopState getState(int index) {
        assert index >= 0 && index < CookTopConfigurationI.NUMBER_ZONE : new NeoSim4JavaException("indexZone must be between 0 and " + (CookTopConfigurationI.NUMBER_ZONE-1));
        return this.currentState[index];
    }
    public CookTopState getIsOn() {
        return this.isOn;
    }
    /**
     * Calculate the total power consumption of the cooktop.
     * @return the total power consumption
     */
    protected double sommeTotalPower() {
        if (this.isOn == CookTopState.OFF) {
            return 0.0;
        } 
        double totalPower = CookTopConfigurationI.ACTIVATION_MEASURE.getData();
        for(int i=0; i<CookTopConfigurationI.NUMBER_ZONE; i++) {
            if (this.currentState[i] == CookTopState.ON) {
                totalPower += CookTopConfigurationI.getZonePower(i, this.currentPower[i]);
            }
        }
        return totalPower;
    }

    /**
     * Set the new intensity of the cooktop.
     * @param currentTime the current time
     * @param index the index of the zone
     */
    protected void setNewIntensity(Time currentTime) {
        double totalPower = this.sommeTotalPower();
        if (ElectricMeterImplementationI.POWER_UNIT.equals(MeasurementUnit.WATTS)) {
            this.currentIntensity.setNewValue(totalPower, currentTime);
        } else {
            this.currentIntensity.setNewValue(totalPower / this.tension, currentTime);
        }
    }

    // -------------------------------------------------------------------------
    // DEVS simulation protocol
    // -------------------------------------------------------------------------

    /***
     * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void initialiseState(Time startTime) {
        super.initialiseState(startTime);
        this.isOn = CookTopState.OFF;
        for(int i=0; i<CookTop.NUMBER_ZONE; i++) {
            this.currentState[i] = CookTopState.OFF;
            this.currentPower[i] = 0;
        }
        this.totalConsumption = 0.0;
        if ( VERBOSE ) {
            this.logMessage("simultation begins.");
        }
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void initialiseVariables() {
        super.initialiseVariables();
        this.currentIntensity.initialise(0.0);
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#initialise(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public ArrayList<EventI> output(){
        return null;
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#timeAdvance()
     */
    @Override
    public Duration timeAdvance() {
        return Duration.INFINITY;
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
     */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        super.userDefinedExternalTransition(elapsedTime);
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert currentEvents != null && currentEvents.size() == 1 : new NeoSim4JavaException("one and only one event should be stored.");
        EventI ce = (Event) currentEvents.get(0);
        assert ce instanceof AbstractCookTopEvent : new NeoSim4JavaException("event should be a Cooktop event.");
        if (ElectricMeterImplementationI.POWER_UNIT.equals(MeasurementUnit.WATTS)) {
            this.totalConsumption += Electricity.computeConsumption(elapsedTime,this.currentIntensity.getValue());
            } else {
            this.totalConsumption += Electricity.computeConsumption(elapsedTime, this.tension * this.currentIntensity.getValue());
        }
        ce.executeOn(this);
        // Tracing
        if (VERBOSE) {
            StringBuffer message = new StringBuffer("external transition on ");
            message.append(ce.toString());
            message.append(" with new consumption ");
            message.append(this.currentIntensity.getValue());
            message.append(" ");
            message.append(ElectricMeterConfigurationI.POWER_UNIT);
            message.append(" at ");
            message.append(this.currentIntensity.getTime());
            this.logMessage(message.toString());
        }
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void endSimulation(Time endTime) {
        Duration d = endTime.subtract(this.getCurrentStateTime());
        if (ElectricMeterImplementationI.POWER_UNIT.equals(MeasurementUnit.WATTS)) {
            this.totalConsumption += Electricity.computeConsumption(d,this.currentIntensity.getValue());
        } else {
            this.totalConsumption += Electricity.computeConsumption( d, this.tension * this.currentIntensity.getValue());
        }
        if (VERBOSE) {
            this.logMessage("simulation ends.");
        }
        super.endSimulation(endTime);
    }
    /** run parameter name for {@code TENSION}.*/
    public static final String TENSION_RPNAME = "TENSION";

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#setSimulationRunParameters(Map)
     */
    @Override
    public void setSimulationRunParameters( Map<String, Object> simParams) throws MissingRunParameterException {
        super.setSimulationRunParameters(simParams);
        String tensionName = ModelI.createRunParameterName(getURI(), TENSION_RPNAME);
        if (simParams.containsKey(tensionName)) this.tension = (double) simParams.get(tensionName);
        assert CookTopElectricityModel.implementationInvariants(this) : new NeoSim4JavaException( "CookTopElectricityModel.implementationInvariants(" + "this)");
        assert	CookTopElectricityModel.invariants(this) : new NeoSim4JavaException( "CookTopElectricityModel.invariants(this)");
    }
    // -------------------------------------------------------------------------
    // Optional DEVS simulation protocol: simulation run parameters
    // -------------------------------------------------------------------------
    /**
     * The class <code>CookTopElectricityReport</code> implements the
     * simulation report for the <code>CookTopElectricityModel</code>.
     *
     * <p><strong>Description</strong></p>
     * 
     * <p><strong>Glass-box Invariants</strong></p>
     * 
     * <pre>
     * invariant	{@code true}	// no more invariant
     * </pre>
     * 
     * <p><strong>Black-box Invariants</strong></p>
     * 
     * <pre>
     * invariant	{@code true}	// no more invariant
     * </pre>
     * 
     * <p>Created on : 2023-09-29</p>
     * 
     * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
     */
    public static class CookTopElectricityReport implements SimulationReportI, GlobalReportI {
        private static final long serialVersionUID = 1L;
        protected String modelURI;
        protected double totalConsumption; // in kwh

        public CookTopElectricityReport(String modelURI, double totalConsumption) {
            super();
            this.modelURI = modelURI;
            this.totalConsumption = totalConsumption;
        }

        @Override
        public String getModelURI() {
            return this.modelURI;
        }

        @Override
        public String printout(String indent) {
            StringBuffer ret = new StringBuffer(indent);
            ret.append("---\n");
            ret.append(indent);
            ret.append('|');
            ret.append(this.modelURI);
            ret.append(" report\n");
            ret.append(indent);
            ret.append('|');
            ret.append("total consumption in kwh = ");
            ret.append(this.totalConsumption);
            ret.append(".\n");
            ret.append(indent);
            ret.append("---\n");
            return ret.toString();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.printout("");
        }
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#getFinalReport()
     */
    @Override
    public SimulationReportI getFinalReport() {
        return new CookTopElectricityReport(this.getURI(), this.totalConsumption);
    }
}
//-----------------------------------------------------------------------------
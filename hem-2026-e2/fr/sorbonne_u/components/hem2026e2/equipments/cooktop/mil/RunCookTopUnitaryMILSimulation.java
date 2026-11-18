package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOffCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOnCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOffZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOnZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.DecreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IncreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IndexValue;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.scenarios.SimulationScenario;
import fr.sorbonne_u.devs_simulation.scenarios.SimulationStep;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import java.util.ArrayList;
// -----------------------------------------------------------------------------
/**
 * The class <code>RunCookTopUnitaryMILSimulation</code> is the main class used
 * to run simulations on the example models of the cooktop in isolation
 * based on test scenarios.
 *
 * <p><strong>Description</strong></p>
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
 * <p>Created on : 2023-09-29</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class RunCookTopUnitaryMILSimulation {
    
    // -------------------------------------------------------------------------
    // Invariants
    // -------------------------------------------------------------------------

    /**
     * return true if the static invariants are observed, false otherwise.
     * 
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code instance != null}
     * post	{@code true}	// no postcondition.
     * </pre>
     *
     * @return	true if the invariants are observed, false otherwise.
     */
    public static boolean staticInvariants() {
        boolean ret = true;
        ret &= CookTopSimulationConfigurationI.staticInvariants();
        return ret;
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        assert staticInvariants(): new NeoSim4JavaException("staticInvariants()");
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);
        try {
            // map that will contain the atomic model descriptors to construct
            // the simulation architecture
            Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
            // the cooktop model simulating its electricity consumption, an
            // atomic HIOA model hence we use an AtomicHIOA_Descriptor
            atomicModelDescriptors.put(
                    CookTopElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            CookTopElectricityModel.class,
                            CookTopElectricityModel.URI,
                            CookTopSimulationConfigurationI.TIME_UNIT,
                            null));
            // for atomic model, we use an AtomicModelDescriptor
            atomicModelDescriptors.put(
                    CookTopUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            CookTopUnitTesterModel.class,
                            CookTopUnitTesterModel.URI,
                            CookTopSimulationConfigurationI.TIME_UNIT,
                            null));
            // map that will contain the coupled model descriptors to construct
            // the simulation architecture
            Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // the set of submodels of the coupled model, given by their URIs
            Set<String> submodels = new HashSet<String>();
            submodels.add(CookTopElectricityModel.URI);
            submodels.add(CookTopUnitTesterModel.URI);

            // event exchanging connections between exporting and importing
            // models
            Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>();
            connections.put(
                    new EventSource(CookTopUnitTesterModel.URI, SwitchOnCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI,SwitchOnCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopUnitTesterModel.URI, SwitchOffCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, SwitchOffCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopUnitTesterModel.URI, TurnOnZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, TurnOnZoneCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopUnitTesterModel.URI, TurnOffZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, TurnOffZoneCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopUnitTesterModel.URI, IncreasePowerZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, IncreasePowerZoneCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopUnitTesterModel.URI, DecreasePowerZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, DecreasePowerZoneCookTop.class)
                    });

            // coupled model descriptor
            coupledModelDescriptors.put(
                    CookTopCoupleModel.URI,
                    new CoupledModelDescriptor(
                            CookTopCoupleModel.class,
                            CookTopCoupleModel.URI,
                            submodels,
                            null,
                            null,
                            connections,
                            null));

            // simulation architecture
            ArchitectureI architecture =
                    new Architecture(
                            CookTopCoupleModel.URI,
                            atomicModelDescriptors,
                            coupledModelDescriptors,
                            CookTopSimulationConfigurationI.TIME_UNIT);

            // create the simulator from the simulation architecture
            SimulatorI se = architecture.constructSimulator();

            // -----------------------------------------------------------------
            // Simulation runs
            // -----------------------------------------------------------------

            // Tracing configuration

            CookTopElectricityModel.VERBOSE = true;
            CookTopElectricityModel.DEBUG = false;

            // this add additional time at each simulation step in
            // standard simulations (useful when debugging)
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

            // run a CLASSICAL simulation scenario
            SimulationScenario classical = classical();
            Map<String, Object> classicalRunParameters = new HashMap<String, Object>();
            classical.addToRunParameters(classicalRunParameters);
            se.setSimulationRunParameters(classicalRunParameters);
            Time startTime = classical.getStartTime();
            Duration d = classical.getEndTime().subtract(startTime);
            ((Consumer<String>) (m -> { if (m != null) System.out.println(m); })).accept(classical.beginMessage());
            se.doStandAloneSimulation(startTime.getSimulatedTime(), d.getSimulatedDuration());
            SimulationReportI sr = se.getSimulatedModel().getFinalReport();
            System.out.println(sr);
            ((Consumer<String>) (m -> { if (m != null) System.out.println(m); })).accept(classical.endMessage());
            System.exit(0);
        } catch (Throwable e) {
            throw new RuntimeException(e) ;
        }
    }

    // -------------------------------------------------------------------------
    // Test scenarios
    // -------------------------------------------------------------------------

    /** the start time in simulated time.									*/
    protected static Time START_TIME = new Time(0.0, CookTopSimulationConfigurationI.TIME_UNIT);
    /** the start time in simulated time.									*/
    protected static Time END_TIME = new Time(6.0, CookTopSimulationConfigurationI.TIME_UNIT);

    protected final static IndexValue ZoneA = new IndexValue(0);
    protected final static IndexValue ZoneB = new IndexValue(1);
    /** 
     * standard simulation scenario, see Gherkin specification.				 	
     * @throws NeoSim4JavaException
     */
    protected static SimulationScenario	classical() throws NeoSim4JavaException {
        assert	CookTopSimulationConfigurationI.TIME_UNIT.equals(TimeUnit.HOURS) : new NeoSim4JavaException("CookTopSimulationConfigurationI.TIME_UNIT." + "equals(TimeUnit.HOURS)");

        // times at which events occur during the simulation
        Time switchOnTime = START_TIME.add(new Duration(1.0, TimeUnit.HOURS));
        Time turnOnZoneATime = switchOnTime.add(new Duration(0.5, TimeUnit.HOURS));
        Time increasePowerZoneA1Time = turnOnZoneATime.add(new Duration(0.5, TimeUnit.HOURS));
        Time increasePowerZoneA2Time = increasePowerZoneA1Time.add(new Duration(0.5, TimeUnit.HOURS));
        Time turnOnZoneBTime = increasePowerZoneA2Time.add(new Duration(0.5, TimeUnit.HOURS));
        Time increasePowerZoneB1Time = turnOnZoneBTime.add(new Duration(0.5, TimeUnit.HOURS));
        Time increasePowerZoneB2Time = increasePowerZoneB1Time.add(new Duration(0.5, TimeUnit.HOURS));
        Time decreasePowerZoneB1Time = increasePowerZoneB2Time.add(new Duration(0.5, TimeUnit.HOURS));
        Time turnOffZoneBTime = decreasePowerZoneB1Time.add(new Duration(0.5, TimeUnit.HOURS));
        Time turnOffZoneATime = turnOffZoneBTime.add(new Duration(0.5, TimeUnit.HOURS));
        Time switchOffTime = turnOffZoneATime.add(new Duration(0.5, TimeUnit.HOURS));
        return new SimulationScenario(
            "-----------------------------------------------------\n" +
            "Classical\n\n" +
            "  Gherkin specification\n\n" +
            "    Feature: cooktop operation\n\n" +
            "      Scenario: cooktop switched on\n" +
            "        Given a cooktop that is off\n" +
            "        When it is switched on\n" +
            "        Then it consumes only its activation power\n" +
            "      Scenario: zone turned on\n" +
            "        Given a cooktop that is on\n" +
            "        When the zone 0 is turned on\n" +
            "        Then the zone 0 is at level 1 and the consumption increases\n" +
            "      Scenario: zone level increased\n" +
            "        Given a cooktop with the zone 0 on\n" +
            "        When the level of the zone 0 is increased twice\n" +
            "        Then the zone 0 is at level 3 and the consumption increases\n" +
            "      Scenario: second zone turned on and increased\n" +
            "        Given a cooktop with the zone 0 on\n" +
            "        When the zone 1 is turned on and its level is increased twice\n" +
            "        Then the consumption is the sum of the two zones\n" +
            "      Scenario: zone level decreased\n" +
            "        Given a cooktop with the zone 1 at level 3\n" +
            "        When the level of the zone 1 is decreased\n" +
            "        Then the zone 1 is at level 2 and the consumption decreases\n" +
            "      Scenario: zones turned off and cooktop switched off\n" +
            "        Given a cooktop with the zone 0 at level 3 and the zone 1 at level 2\n" +
            "        When the zones are turned off and the cooktop is switched off\n" +
            "        Then the cooktop is off and the consumption is zero\n" +
            "-----------------------------------------------------\n",
            "\n-----------------------------------------------------\n" +
            "End Classical\n" +
            "-----------------------------------------------------",
            START_TIME,
            END_TIME,
            (ts, simParams) -> {
                simParams.put(
                    ModelI.createRunParameterName(
                        CookTopUnitTesterModel.URI,
                        CookTopUnitTesterModel.SIMULATION_SCENARIO_RP_NAME),
                    ts);
            },
            new SimulationStep[]{
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        switchOnTime,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new SwitchOnCookTop(t));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        turnOnZoneATime,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new TurnOnZoneCookTop(t,ZoneA));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        increasePowerZoneA1Time,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new IncreasePowerZoneCookTop(t,ZoneA));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        increasePowerZoneA2Time,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new IncreasePowerZoneCookTop(t,ZoneA));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        turnOnZoneBTime,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new TurnOnZoneCookTop(t, ZoneB));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        increasePowerZoneB1Time,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new IncreasePowerZoneCookTop(t, ZoneB));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        increasePowerZoneB2Time,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new IncreasePowerZoneCookTop(t, ZoneB));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        decreasePowerZoneB1Time,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new DecreasePowerZoneCookTop(t, ZoneB));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        turnOffZoneBTime,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new TurnOffZoneCookTop(t, ZoneB));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        turnOffZoneATime,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new TurnOffZoneCookTop(t, ZoneA));
                            return ret;
                        },
                        (m, t) -> {}),
                new SimulationStep(
                        CookTopUnitTesterModel.URI,
                        switchOffTime,
                        (m, t) -> {
                            ArrayList<EventI> ret = new ArrayList<>();
                            ret.add(new SwitchOffCookTop(t));
                            return ret;
                        },
                        (m, t) -> {})
            });
    }
}
// -----------------------------------------------------------------------------

package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOffZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.TurnOnZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOnCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.SwitchOffCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.DecreasePowerZoneCookTop;
import fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil.events.IncreasePowerZoneCookTop;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

public class RunCookTopSimpleUserMILSimulation {

    public static void	main(String[] args)
    {
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);

        try {
            // map that will contain the atomic model descriptors to construct
            // the simulation architecture
            Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // the cook top model simulating its electricity consumption, an
            // atomic HIOA model hence we use an AtomicHIOA_Descriptor
            atomicModelDescriptors.put(
                    CookTopElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            CookTopElectricityModel.class,
                            CookTopElectricityModel.URI,
                            TimeUnit.HOURS,
                            null));
            // for atomic model, we use an AtomicModelDescriptor
            atomicModelDescriptors.put(
                    CookTopSimpleUserModel.URI,
                    AtomicModelDescriptor.create(
                            CookTopSimpleUserModel.class,
                            CookTopSimpleUserModel.URI,
                            TimeUnit.HOURS,
                            null));

            // map that will contain the coupled model descriptors to construct
            // the simulation architecture
            Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();
            // the set of submodels of the coupled model, given by their URIs
            Set<String> submodels = new HashSet<String>();
            submodels.add(CookTopElectricityModel.URI);
            submodels.add(CookTopSimpleUserModel.URI);

            // event exchanging connections between exporting and importing
            // models
            Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>();

            connections.put(
                    new EventSource(CookTopSimpleUserModel.URI, SwitchOnCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI,SwitchOnCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopSimpleUserModel.URI, SwitchOffCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, SwitchOffCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopSimpleUserModel.URI, TurnOnZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, TurnOnZoneCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopSimpleUserModel.URI, TurnOffZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, TurnOffZoneCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopSimpleUserModel.URI, IncreasePowerZoneCookTop.class),
                    new EventSink[] {
                            new EventSink(CookTopElectricityModel.URI, IncreasePowerZoneCookTop.class)
                    });
            connections.put(
                    new EventSource(CookTopSimpleUserModel.URI, DecreasePowerZoneCookTop.class),
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
                            TimeUnit.HOURS);

            // create the simulator from the simulation architecture
            SimulatorI se = architecture.constructSimulator();

            // -----------------------------------------------------------------
            // Simulation runs
            // -----------------------------------------------------------------

            // Tracing configuration

            CookTopElectricityModel.VERBOSE = true;
            CookTopElectricityModel.DEBUG = false;
            CookTopSimpleUserModel.VERBOSE = true;
            CookTopSimpleUserModel.DEBUG = false;

            // this add additional time at each simulation step in
            // standard simulations (useful when debugging)
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            // run a simulation with the simulation beginning at 0.0 and
            // ending at 24.0
            se.doStandAloneSimulation(0.0, 24.0);
            SimulationReportI sr = se.getSimulatedModel().getFinalReport();
            System.out.println(sr);
            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException(e) ;
        }
    }
}
// -----------------------------------------------------------------------------


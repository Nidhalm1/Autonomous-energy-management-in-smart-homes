package fr.sorbonne_u.components.hem2026e2.equipments.weather_station.mil;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.time.Instant;
import fr.sorbonne_u.components.cyphy.utils.tests.SimulationTestStep;
import fr.sorbonne_u.components.cyphy.utils.tests.TestScenarioWithSimulation;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.exceptions.VerboseException;

// -----------------------------------------------------------------------------
/**
 * The class <code>RunWeatherStationUnitaryMILSimulation</code> creates a
 * simulator for the weather station and then runs a minimal simulation.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The simulation architecture for the weather station contains three atomic
 * models composed under a coupled model:
 * </p>
 * <p><img src="../../../../../../../../images/hem-2026-e2/WeatherStationUnitTestArchitecture.png"/></p>
 * <p>
 * The
 * {@code UnitTestInternalTemperatureModel} simulates the temperature inside.
 * This model simply considers it as constant. A more elaborated model is
 * provided under the heater component that controls it by its heating behaviour.
 * The {@code ExternalTemperatureModel} simulates the temperature outside. This
 * model makes the temperature evolve between a lowest point at sunrise and a
 * highest point at mid-day following a half cosine function (see the class
 * documentation). The third model, {@code WeatherStationModel} merely imports
 * the inside and the outside temperature and is meant to ease the access to
 * the values from the weather station component.
 * </p>
 * <p>
 * The code of the {@code main} method shows how to use simulation model
 * descriptors to create the description of a simulation architecture and then
 * create an instance of this architecture by instantiating and connecting the
 * models. Note how models are described by atomic model descriptors and coupled
 * model descriptors and then the connections between coupled models and their
 * submodels as well as exported events and variables to imported ones are
 * described by different maps. In this example, only connections of events and
 * bindings of variables between models within this architecture are necessary,
 * but when creating coupled models, they can also import and export events and
 * variables consumed and produced by their submodels.
 * </p>
 * <p>
 * The architecture object is the root of this description and it provides
 * the method {@code constructSimulator} that instantiate the models and
 * connect them. This method returns the reference on the simulator attached
 * to the root coupled model in the architecture instance, which is then used
 * to perform simulation runs by calling the method
 * {@code doStandAloneSimulation}.
 * </p>
 * <p>
 * The descriptors and maps can be viewed as kinds of nodes in the abstract
 * syntax tree of an architectural language that does not have a concrete
 * syntax yet.
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
 * invariant	{@code true}	// no more invariant
 * </pre>
 * 
 * <p>Created on : 2026-09-28</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RunWeatherStationUnitaryMILSimulation
{
	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

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
	 * @return	true if the invariants are observed, false otherwise.
	 */
	public static boolean staticInvariants() {
		boolean ret = true;
		ret &= WeatherStationSimulationConfigurationI.staticInvariants();
		return ret;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public static void	main(String[] args)
	{
		try {
			// map that will contain the atomic model descriptors to construct
			// the simulation architecture
			Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
					new HashMap<>();

			// the heater models simulating its electricity consumption, its
			// temperatures and the external temperature are atomic HIOA models
			// hence we use an AtomicHIOA_Descriptor(s)
			atomicModelDescriptors.put(
					ExternalTemperatureModel.URI,
					AtomicHIOA_Descriptor.create(
							ExternalTemperatureModel.class,
							ExternalTemperatureModel.URI,
							WeatherStationSimulationConfigurationI.TIME_UNIT,
							null));
			atomicModelDescriptors.put(
					UnitTestInternalTemperatureModel.URI,
					AtomicHIOA_Descriptor.create(
							UnitTestInternalTemperatureModel.class,
							UnitTestInternalTemperatureModel.URI,
							WeatherStationSimulationConfigurationI.TIME_UNIT,
							null));
			atomicModelDescriptors.put(
					WeatherStationModel.URI,
					AtomicHIOA_Descriptor.create(
							WeatherStationModel.class,
							WeatherStationModel.URI,
							WeatherStationSimulationConfigurationI.TIME_UNIT,
							null));

			// map that will contain the coupled model descriptors to construct
			// the simulation architecture
			Map<String,CoupledModelDescriptor> coupledModelDescriptors =
															new HashMap<>();

			// the set of submodels of the coupled model, given by their URIs
			Set<String> submodels = new HashSet<String>();
			submodels.add(ExternalTemperatureModel.URI);
			submodels.add(UnitTestInternalTemperatureModel.URI);
			submodels.add(WeatherStationModel.URI);

			// variable bindings between exporting and importing models
			Map<VariableSource,VariableSink[]> bindings =
								new HashMap<VariableSource,VariableSink[]>();

			bindings.put(new VariableSource("externalTemperature",
						 					Double.class,
						 					ExternalTemperatureModel.URI),
						 new VariableSink[] {
								 new VariableSink("externalTemperature",
										 		  Double.class,
										 		  WeatherStationModel.URI)
						 });
			bindings.put(new VariableSource("internalTemperature",
											Double.class,
											UnitTestInternalTemperatureModel.URI),
						 new VariableSink[] {
								 new VariableSink("internalTemperature",
										 		  Double.class,
										 		  WeatherStationModel.URI)
						 });

			// coupled model descriptor
			coupledModelDescriptors.put(
					WeatherStationCoupledModel.URI,
					new CoupledHIOA_Descriptor(
							WeatherStationCoupledModel.class,
							WeatherStationCoupledModel.URI,
							submodels,
							null,
							null,
							null,
							null,
							null,
							null,
							bindings));

			// simulation architecture
			ArchitectureI architecture =
					new Architecture(
							WeatherStationCoupledModel.URI,
							atomicModelDescriptors,
							coupledModelDescriptors,
							WeatherStationSimulationConfigurationI.TIME_UNIT);

			// create the simulator from the simulation architecture
			SimulatorI se = architecture.constructSimulator();

			// -----------------------------------------------------------------
			// Simulation runs
			// -----------------------------------------------------------------

			// Tracing configuration

			ExternalTemperatureModel.VERBOSE = false;
			ExternalTemperatureModel.DEBUG = false;
			UnitTestInternalTemperatureModel.VERBOSE = true;
			UnitTestInternalTemperatureModel.DEBUG = false;
			WeatherStationModel.VERBOSE = true;
			WeatherStationModel.DEBUG = false;

			// this add additional time at each simulation step in
			// standard simulations (useful when debugging)
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

			// run a CLASSICAL test scenario
			TestScenarioWithSimulation classical = classical();
			Map<String, Object> classicalRunParameters =
												new HashMap<String, Object>();
			classical.addToRunParameters(classicalRunParameters);
			se.setSimulationRunParameters(classicalRunParameters);
			Time startTime = classical.getStartTime();
			Duration d = classical.getEndTime().subtract(startTime);
			se.doStandAloneSimulation(startTime.getSimulatedTime(),
									  d.getSimulatedDuration());
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------
	// Test scenarios
	// -------------------------------------------------------------------------

	/** the start instant used in the test scenarios.						*/
	protected static Instant	START_INSTANT =
									Instant.parse("2026-10-01T06:00:00.00Z");
	/** the end instant used in the test scenarios.							*/
	protected static Instant	END_INSTANT =
									Instant.parse("2026-10-01T12:00:00.00Z");
	/** the start time in simulated time, corresponding to
	 *  {@code START_INSTANT}.												*/
	protected static Time		START_TIME = new Time(0.0, TimeUnit.HOURS);

	/** standard test scenario, see Gherkin specification.				 	
	 * @throws VerboseException */
	protected static TestScenarioWithSimulation	classical() throws VerboseException
	{
		return new TestScenarioWithSimulation(
			"-----------------------------------------------------\n" +
			"Classical\n\n" +
			"  Gherkin specification\n\n" +
			"    Feature: weather station operation\n\n" +
			"      Scenario: weather data trace \n" +
			"        Given an initialised weather station\n" +
			"        When the simulation is running\n" +
			"        Then the weather station collects and shows the current inside and outside temperatures\n" +
			"-----------------------------------------------------\n",
			"\n-----------------------------------------------------\n" +
			"End Classical\n" +
			"-----------------------------------------------------",
			"fake-clock-URI",	// for simulation only test scenario, no clock needed
			START_INSTANT,
			END_INSTANT,
			WeatherStationCoupledModel.URI,
			START_TIME,
			(ts, simParams) -> {
				simParams.put(
					ModelI.createRunParameterName(
						ExternalTemperatureModel.URI,
						ExternalTemperatureModel.START_INSTANT_RP_NAME
						),
					START_INSTANT);
			},
			new SimulationTestStep[]{});
	}
}
// -----------------------------------------------------------------------------

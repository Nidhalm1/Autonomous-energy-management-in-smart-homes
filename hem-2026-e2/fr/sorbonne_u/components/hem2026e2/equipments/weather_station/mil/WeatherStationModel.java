package fr.sorbonne_u.components.hem2026e2.equipments.weather_station.mil;

import java.util.Formatter;

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

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ModelImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

// -----------------------------------------------------------------------------
/**
 * The class <code>WeatherStationModel</code> implements a simulation model
 * that keeps track of the external and internal temperature for the weather
 * station.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The weather station is meant to measure and show the temperature inside and
 * outside the house. As such, it uses an external temperature model from which
 * it imports a model variable {@code externalTemperature} and that simulates
 * more or less realistically the evolution of the outside temperature over one
 * day using a cosine function basis and repeats it each day if the simulation
 * duration requires it. For the inside temperature, a mock model implementatio
 * is provided from which it imports a model variable {@code internalTemperature}
 * and that is used for unit testing which sets a constant temperature. When
 * integrated to the house, the inside temperature model will be provided by a
 * model provided by the heater appliance exporting the required temperature.
 * </p>
 * 
 * <ul>
 * <li>Imported events: none</li>
 * <li>Exported events: none</li>
 * <li>Imported variables:
 *   name = {@code externalTemperature}, type = {@code Double},
 *   name = {@code internalTemperature}, type = {@code Double}</li>
 * <li>Exported variables: none</li>
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
 * <p>Created on : 2026-09-25</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@ModelImportedVariable(name = "externalTemperature", type = Double.class)
@ModelImportedVariable(name = "internalTemperature", type = Double.class)
public class			WeatherStationModel
extends		AtomicHIOA
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// The following variables should be considered constant but can be changed
	// before the first model instance is created to adapt the simulation
	// scenario.

	/** URI for a model; works when only one instance is created.			*/
	public static String		URI = WeatherStationModel.class.getSimpleName();
	/** when true, leaves a trace of the execution of the model.			*/
	public static boolean		VERBOSE = false;
	/** when true, leaves a debugging trace of the execution of the model.	*/
	public static boolean		DEBUG = false;

	/** tracing step  (assumed in hours).									*/
	protected static double		TRACING_STEP = 300.0/3600.0;	// 300 seconds

	/** current external temperature in Celsius.							*/
	@ImportedVariable(type = Double.class)
	protected Value<Double>		externalTemperature;
	/** current internal temperature in Celsius.							*/
	@ImportedVariable(type = Double.class)
	protected Value<Double>		internalTemperature;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a <code>WeatherStationModel</code> instance.
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
	 * @param uri				URI of the model.
	 * @param simulatedTimeUnit	time unit used for the simulation time.
	 * @param simulationEngine	simulation engine to which the model is attached.
	 */
	public				WeatherStationModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		AtomicSimulatorI simulationEngine
		)
	{
		super(uri, simulatedTimeUnit, simulationEngine);

		this.getSimulationEngine().setLogger(new StandardLogger());
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		return new Duration(TRACING_STEP, simulatedTimeUnit);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		// Tracing
		if (VERBOSE) {
			this.logMessage("simulation begins.");
		}		
		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		// Tracing
		if (VERBOSE) {
			StringBuffer message = new StringBuffer();
			message.append("external (");
			message.append((new Formatter()).format("%f",
										this.externalTemperature.getValue()));
			message.append(", ");
			message.append((new Formatter()).format("%f",
						this.externalTemperature.getTime().getSimulatedTime()));
			message.append(") : internal (");
			message.append((new Formatter()).format("%f",
										this.internalTemperature.getValue()));
			message.append(", ");
			message.append((new Formatter()).format("%f",
						this.internalTemperature.getTime().getSimulatedTime()));
			message.append(")");
			this.logMessage(message.toString());
		}

		super.userDefinedInternalTransition(elapsedTime);

		// Invariant checking
		assert	WeatherStationModel.implementationInvariants(this) :
				new AssertionError(
						"Implementation Invariants violation: "
						+ "WeatherStationModel.implementationInvariants(this)");
		assert	WeatherStationModel.implementationInvariants(this) :
				new AssertionError(
						"Invariants violation: WeatherStationModel."
						+ "implementationInvariants(this)");
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime)
	{
		// Tracing
		if (VERBOSE) {
			this.logMessage("simulation ends.");
		}		
		super.endSimulation(endTime);
	}
}
// -----------------------------------------------------------------------------

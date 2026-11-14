package fr.sorbonne_u.components.hem2026e2.equipments.heater.mil;

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
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.hem2026e1.equipments.heater.HeaterConfigurationI;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterConfigurationI;
import fr.sorbonne_u.devs_simulation.utils.AssertionChecking;

// -----------------------------------------------------------------------------
/**
 * The interface <code>HeaterSimulationConfigurationI</code> defines common
 * constants and configuration parameters for the heater simulator.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code MeasurementUnit.AMPERES.equals(ElectricMeterConfigurationI.POWER_UNIT)}
 * invariant	{@code MeasurementUnit.VOLTS.equals(ElectricMeterConfigurationI.TENSION_UNIT)}
 * invariant	{@code (new Measure<Double>(220.0, ElectricMeterConfigurationI.TENSION_UNIT)).equals(ElectricMeter.TENSION)}
 * invariant	{@code MeasurementUnit.CELSIUS.equals(HeaterConfigurationI.TEMPERATURE_UNIT)}
 * invariant	{@code MeasurementUnit.WATTS.equals(HeaterConfigurationI.POWER_UNIT)}
 * invariant	{@code ElectricMeterConfigurationI.TENSION_UNIT.equals(HeaterConfigurationI.TENSION_UNIT)}
 * invariant	{@code ElectricMeter.TENSION.equals(HeaterConfigurationI.TENSION)}
 * invariant	{@code TIME_UNIT != null}
 * </pre>
 * 
 * <p>Created on : 2025-10-02</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		HeaterSimulationConfigurationI
{
	// -------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------

	/** time unit used in the heater simulator.								*/
	public static final TimeUnit	TIME_UNIT = TimeUnit.HOURS;

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
	 * @return			true if the invariants are observed, false otherwise.
	 */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= ElectricMeterConfigurationI.staticInvariants();
		ret &= HeaterConfigurationI.staticInvariants();
		ret &= AssertionChecking.checkStaticInvariant(
				MeasurementUnit.AMPERES.equals(
									ElectricMeterConfigurationI.POWER_UNIT),
				HeaterSimulationConfigurationI.class,
				"MeasurementUnit.AMPERES.equals("
				+ "ElectricMeterConfigurationI.POWER_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				MeasurementUnit.VOLTS.equals(
									ElectricMeterConfigurationI.TENSION_UNIT),
				HeaterSimulationConfigurationI.class,
				"MeasurementUnit.VOLTS.equals("
				+ "ElectricMeterConfigurationI.TENSION_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				(new Measure<Double>(220.0,
									 ElectricMeterConfigurationI.TENSION_UNIT)).
						equals(ElectricMeter.TENSION),
				HeaterSimulationConfigurationI.class,
				"(new Measure<Double>(220.0, ElectricMeterConfigurationI."
				+ "TENSION_UNIT)).equals(ElectricMeter.TENSION)");
		ret &= AssertionChecking.checkStaticInvariant(
				MeasurementUnit.CELSIUS.equals(
										HeaterConfigurationI.TEMPERATURE_UNIT),
				HeaterSimulationConfigurationI.class,
				"MeasurementUnit.CELSIUS.equals("
				+ "HeaterConfigurationI.TEMPERATURE_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				MeasurementUnit.WATTS.equals(HeaterConfigurationI.POWER_UNIT),
				HeaterSimulationConfigurationI.class,
				"MeasurementUnit.WATTS.equals(HeaterConfigurationI.POWER_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				ElectricMeterConfigurationI.TENSION_UNIT.equals(
											HeaterConfigurationI.TENSION_UNIT),
				HeaterSimulationConfigurationI.class,
				"ElectricMeterConfigurationI.TENSION_UNIT.equals("
				+ "HeaterExternalControlI.TENSION_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				ElectricMeter.TENSION.equals(HeaterConfigurationI.TENSION),
				HeaterSimulationConfigurationI.class,
				"ElectricMeter.TENSION.equals(HeaterConfigurationI.TENSION)");
		ret &= AssertionChecking.checkStaticInvariant(
				TIME_UNIT != null,
				HeaterSimulationConfigurationI.class,
				"TIME_UNIT != null");
		return ret;
	}
}
// -----------------------------------------------------------------------------

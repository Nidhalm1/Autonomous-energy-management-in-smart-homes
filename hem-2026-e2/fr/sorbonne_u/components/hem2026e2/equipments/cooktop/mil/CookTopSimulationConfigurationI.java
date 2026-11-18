package fr.sorbonne_u.components.hem2026e2.equipments.cooktop.mil;

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTopConfigurationI;
import fr.sorbonne_u.components.hem2026e1.equipments.cooktop.CookTopImplementationI;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterConfigurationI;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterImplementationI;
import fr.sorbonne_u.devs_simulation.utils.AssertionChecking;
// -----------------------------------------------------------------------------
/**
 * The interface <code>CookTopSimulationConfigurationI</code> defines
 * configuration parameters for the cook top simulator.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The cook top simulator assumes that the cook top uses
 * {@code MeasurementUnit.WATTS} as its power unit and that its tension is
 * 220.0 volts. It also assumes that the power unit used by the electric meter
 * is {@code }.
 * </p>
 * 
 * <p><strong>Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code MeasurementUnit.AMPERES.equals(ElectricMeterConfigurationI.POWER_UNIT)}
 * invariant	{@code MeasurementUnit.VOLTS.equals(ElectricMeterConfigurationI.TENSION_UNIT)}
 * invariant	{@code (new Measure<Double>(220.0, ElectricMeterConfigurationI.TENSION_UNIT)).equals(ElectricMeter.TENSION)}
 * invariant	{@code MeasurementUnit.WATTS.equals(HairDryerConfigurationI.POWER_UNIT)}
 * invariant	{@code ElectricMeterImplementationI.TENSION_UNIT.equals(HairDryerConfigurationI.TENSION_UNIT)}
 * invariant	{@code ElectricMeter.TENSION.equals(HairDryer.TENSION)}
 * invariant	{@code TIME_UNIT != null}
 * </pre>
 * 
 * <p>Created on : 2026-11-14</p>
 * 
 * @author	<a href="mailto:florent.chen94@gmail.com">Florent CHEN</a>
 */
public interface CookTopSimulationConfigurationI {
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------
    /** The time unit used for the simulation. */
    public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

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
        ret &= ElectricMeterImplementationI.staticInvariants();
        ret &= CookTopImplementationI.staticInvariants();
        ret &= CookTopConfigurationI.staticInvariants();
        ret &= AssertionChecking.checkStaticInvariant(
                MeasurementUnit.AMPERES.equals(ElectricMeterConfigurationI.POWER_UNIT),
                CookTopSimulationConfigurationI.class,
                "MeasurementUnit.AMPERES.equals(" + "ElectricMeterConfigurationI.POWER_UNIT)");
        ret &= AssertionChecking.checkStaticInvariant(
                MeasurementUnit.VOLTS.equals( ElectricMeterConfigurationI.TENSION_UNIT),
                CookTopSimulationConfigurationI.class,
                "MeasurementUnit.VOLTS.equals(" + "ElectricMeterConfigurationI.TENSION_UNIT)");
        ret &= AssertionChecking.checkStaticInvariant(
                (new Measure<Double>(230.0, ElectricMeterConfigurationI.TENSION_UNIT)).equals(ElectricMeter.TENSION),
                CookTopSimulationConfigurationI.class,
                "(new Measure<Double>(230.0, ElectricMeterConfigurationI." + "TENSION_UNIT)).equals(ElectricMeter.TENSION)");
        ret &= AssertionChecking.checkStaticInvariant(
                MeasurementUnit.WATTS.equals(CookTopConfigurationI.POWER_UNIT),
                CookTopSimulationConfigurationI.class,
                "MeasurementUnit.WATTS.equals(CookTopConfigurationI.POWER_UNIT)");
        ret &= AssertionChecking.checkStaticInvariant(
                ElectricMeterConfigurationI.TENSION_UNIT.equals(CookTopConfigurationI.TENSION_UNIT),
                CookTopSimulationConfigurationI.class,
                "ElectricMeterConfigurationI.TENSION_UNIT.equals(" + "CookTopConfigurationI.TENSION_UNIT)");
        ret &= AssertionChecking.checkStaticInvariant(
                ElectricMeter.TENSION.equals(CookTopConfigurationI.TENSION),
                CookTopSimulationConfigurationI.class,
                "ElectricMeter.TENSION.equals(CookTopConfigurationI.TENSION)");
        ret &= AssertionChecking.checkStaticInvariant(
                TIME_UNIT != null,
                CookTopSimulationConfigurationI.class,
                "TIME_UNIT != null");
        return ret;
    }
}
// -----------------------------------------------------------------------------

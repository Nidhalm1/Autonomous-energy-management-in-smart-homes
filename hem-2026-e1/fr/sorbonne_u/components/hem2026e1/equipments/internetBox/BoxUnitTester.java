package fr.sorbonne_u.components.hem2026e1.equipments.internetBox;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a basic
// household management systems as an example of a cyber-physical system.
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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.hem2026e1.CVMIntegrationTest;
import fr.sorbonne_u.components.hem2026e1.equipments.internetBox.connections.BoxExternalControlConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.internetBox.connections.BoxExternalControlOutboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.internetBox.connections.BoxUserConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.internetBox.connections.BoxUserOutboundPort;
import fr.sorbonne_u.components.utils.tests.TestsStatistics;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoxUnitTester</code> implements a component performing 
 * unit tests for the class <code>Box</code> as a BCM component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Implementation Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
 * invariant	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
 * </pre>
 * 
 * <p><strong>Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code X_RELATIVE_POSITION >= 0}
 * invariant	{@code Y_RELATIVE_POSITION >= 0}
 * </pre>
 * 
 * <p>Created on : 2021-09-13</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@RequiredInterfaces(required={BoxUserCI.class,
							  ClocksServerCI.class})
public class			BoxUnitTester
extends		AbstractComponent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**	in clock-driven scenario, the delay from the start instant at which
	 *  the Box is switched on.											*/
	public static final int		SWITCH_ON_DELAY = 2;
	/**	in clock-driven scenario, the delay from the start instant at which
	 *  the Box is switched off.											*/
	public static final int		SWITCH_OFF_DELAY = 9;

	/** when true, methods trace their actions.								*/
	public static boolean		VERBOSE = false;
	/** when tracing, x coordinate of the window relative position.			*/
	public static int			X_RELATIVE_POSITION = 0;
	/** when tracing, y coordinate of the window relative position.			*/
	public static int			Y_RELATIVE_POSITION = 0;

	/** true if the component must perform unit tests, otherwise it
	 *  executes integration tests actions.									*/
	protected final boolean		isUnitTest;
	/** URI of the user component interface inbound port.					*/
	protected String			BoxUserInboundPortURI;
	/** URI of the external control component interface inbound port.		*/
	protected String			BoxExternalControlInboundPortURI;

	/** user component interface inbound port.								*/
	protected BoxUserOutboundPort			hop;
	/** external control component interface inbound port.					*/
	protected BoxExternalControlOutboundPort	hecop;

	/** collector of test statistics.										*/
	protected TestsStatistics	statistics;

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
	 * pre	{@code ht != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param ht	instance to be tested.
	 * @return		true if the implementation invariants are observed, false otherwise.
	 */
	protected static boolean	implementationInvariants(BoxUnitTester ht)
	{
		assert	ht != null : new PreconditionException("ht != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				ht.BoxUserInboundPortURI != null &&
									!ht.BoxUserInboundPortURI.isEmpty(),
				BoxUnitTester.class, ht,
				"ht.BoxUserInboundPortURI != null && "
							+ "!ht.BoxUserInboundPortURI.isEmpty()");
		ret &=AssertionChecking.checkImplementationInvariant(
				ht.BoxExternalControlInboundPortURI != null &&
							!ht.BoxExternalControlInboundPortURI.isEmpty(),
				BoxUnitTester.class, ht,
				"ht.BoxExternalControlInboundPortURI != null &&"
						+ "!ht.BoxExternalControlInboundPortURI.isEmpty()");
		return ret;
	}

	/**
	 * return true if the static invariants are observed, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	true if the static invariants are observed, false otherwise.
	 */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
				BoxUnitTester.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
				BoxUnitTester.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}

	/**
	 * return true if the invariants is observed, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code ht != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param ht	instance to be tested.
	 * @return		true if the invariants are observed, false otherwise.
	 */
	protected static boolean	invariants(BoxUnitTester ht)
	{
		assert	ht != null : new PreconditionException("ht != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a Box test component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param isUnitTest	true if the component must perform unit tests, otherwise it executes integration tests actions.
	 * @throws Exception	<i>to do</i>.
	 */
	protected			BoxUnitTester(boolean isUnitTest) throws Exception
	{
		this(isUnitTest,
			 Box.USER_INBOUND_PORT_URI,
			 Box.EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	/**
	 * create a Box test component.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
	 * pre	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param isUnitTest							true if the component must perform unit tests, otherwise it executes integration tests actions.
	 * @param BoxUserInboundPortURI				URI of the user component interface inbound port.
	 * @param BoxExternalControlInboundPortURI	URI of the external control component interface inbound port.
	 * @throws Exception							<i>to do</i>.
	 */
	protected			BoxUnitTester(
		boolean isUnitTest,
		String BoxUserInboundPortURI,
		String BoxExternalControlInboundPortURI
		) throws Exception
	{
		super(1, 1);
		this.isUnitTest = isUnitTest;
		this.initialise(BoxUserInboundPortURI,
						BoxExternalControlInboundPortURI);
	}

	/**
	 * create a Box test component.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
	 * pre	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param isUnitTest							true if the component must perform unit tests, otherwise it executes integration tests actions.
	 * @param reflectionInboundPortURI				URI of the reflection inbound port of the component.
	 * @param BoxUserInboundPortURI				URI of the user component interface inbound port.
	 * @param BoxExternalControlInboundPortURI	URI of the external control component interface inbound port.
	 * @throws Exception							<i>to do</i>.
	 */
	protected			BoxUnitTester(
		boolean isUnitTest,
		String reflectionInboundPortURI,
		String BoxUserInboundPortURI,
		String BoxExternalControlInboundPortURI
		) throws Exception
	{
		super(reflectionInboundPortURI, 1, 1);
		this.isUnitTest = isUnitTest;
		this.initialise(BoxUserInboundPortURI,
						BoxExternalControlInboundPortURI);
	}

	/**
	 * initialise a Box test component.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
	 * pre	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param BoxUserInboundPortURI				URI of the user component interface inbound port.
	 * @param BoxExternalControlInboundPortURI	URI of the external control component interface inbound port.
	 * @throws Exception							<i>to do</i>.
	 */
	protected void		initialise(
		String BoxUserInboundPortURI,
		String BoxExternalControlInboundPortURI
		) throws Exception
	{
		this.BoxUserInboundPortURI = BoxUserInboundPortURI;
		this.hop = new BoxUserOutboundPort(this);
		this.hop.publishPort();
		this.BoxExternalControlInboundPortURI =
									BoxExternalControlInboundPortURI;
		this.hecop = new BoxExternalControlOutboundPort(this);
		this.hecop.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Box tester component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

		this.statistics = new TestsStatistics();

		assert	BoxUnitTester.implementationInvariants(this) :
				new ImplementationInvariantException(
						"BoxTester.implementationInvariants(this)");
		assert	BoxUnitTester.invariants(this) :
				new InvariantException("BoxTester.invariants(this)");
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * test getting the state of the Box.
	 * 
	 * <p><strong>Description</strong></p>
	 * 
	 * <p>Gherkin specification</p>
	 * <p></p>
	 * <pre>
	 * Feature: getting the state of the Box
	 *   Scenario: getting the state of the Box when off
	 *     Given the Box is initialised
	 *     And the Box has not been used yet
	 *     When I test the state of the Box
	 *     Then the state of the Box is off
	 * </pre>
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 */
	protected void		testOff()
	{
		this.logMessage("Feature: getting the state of the Box");
		this.logMessage("  Scenario: getting the state of the Box when off");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box has not been used yet");
		try {
			this.logMessage("    When I test the state of the Box");
			boolean result = !this.hop.on();
			if (result) {
				this.logMessage("    Then the state of the Box is off");
			} else {
				this.logMessage("     but was: on");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();
	}

	/**
	 * test switching on and off the Box.
	 * 
	 * <p><strong>Description</strong></p>
	 * 
	 * <p>Gherkin specification</p>
	 * <p></p>
	 * <pre>
	 * Feature: switching on and off the Box
	 *   Scenario: switching on the Box when off
	 *     Given the Box is initialised
	 *     And the Box has not been used yet
	 *     When I switch on the Box
	 *     Then the state of the Box is on
	 *   Scenario: switching off the Box when on
	 *     Given the Box is initialised
	 *     And the Box is on
	 *     When I switch off the Box
	 *     Then the state of the Box is off
	 * </pre>
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 */
	protected void		testSwitchOnSwitchOff()
	{
		this.logMessage("Feature: switching on and off the Box");

		this.logMessage("  Scenario: switching on the Box when off");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box has not been used yet");
		boolean result;
		try {
			this.logMessage("    When I switch on the Box");
			this.hop.switchOn();
			result = this.hop.on();
			if (result) {
				this.logMessage("    Then the state of the Box is on");
			} else {
				this.logMessage("     but was: off");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: switching off the Box when on");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box is on");
		try {
			this.logMessage("    When I switch off the Box");
			this.hop.switchOff();
			result = !this.hop.on();
			if (result) {
				this.logMessage("    Then the state of the Box is off");
			} else {
				this.logMessage("     but was: on");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();
	}

	/**
	 * test getting and setting the power level of the Box.
	 * 
	 * <p><strong>Description</strong></p>
	 * 
	 * <p>Gherkin specification</p>
	 * <p></p>
	 * <pre>
	 * Feature: getting and setting the power level of the Box
	 *   Scenario: getting the maximum power level through the user interface
	 *     Given the Box is initialised
	 *     When I get the maximum power level through the user interface
	 *     Then the result is the Box maximum power level
	 *   Scenario: getting the maximum power level through the external control interface
	 *     Given the Box is initialised
	 *     When I get the maximum power level through the external control interface
	 *     Then the result is the Box maximum power level
	 *   Scenario: getting the current power level through the user interface when just initialised
	 *     Given the Box is initialised
	 *     And the Box has not been used yet
	 *     And the Box is on
	 *     When I get the current power level through the user interface
	 *     Then the result is the Box maximum power level
	 *   Scenario: getting the current power level through the external control interface when just initialised
	 *     Given the Box is initialised
	 *     And the Box has not been used yet
	 *     And the Box is on
	 *     When I get the current power level through the external control interface
	 *     Then the result is the Box maximum power level
	 *   Scenario: setting the power level to a given level between 0 and the maximum power level through the user interface
	 *     Given the Box is initialised
	 *     And the Box is on
	 *     When I set the current power level through the user interface to a given level between 0 and the maximum power level
	 *     Then the current power level is the given power level
	 *   Scenario: setting the power level to a given level over the maximum power level through the user interface
	 *     Given the Box is initialised
	 *     And the Box is on
	 *     When I set the current power level through the user interface to a given level bover the maximum power level
	 *     Then the current power level is the maximum power level
	 *   Scenario: setting the power level to a given level between 0 and the maximum power level through the external control interface
	 *     Given the Box is initialised
	 *     And the Box is on
	 *     When I set the current power level through the external control interface to a given level between 0 and the maximum power level
	 *     Then the current power level is the given power level
	 *   Scenario: setting the power level to a given level over the maximum power level through the external control interface
	 *     Given the Box is initialised
	 *     And the Box is on
	 *     When I set the current power level through the external control interface to a given level over the maximum power level
	 *     Then the current power level is the maximum power level
	 * </pre>
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 */
	protected void		testPowerLevel()
	{
		this.logMessage("Feature: getting and setting the power level of the"
						+ " Box");

		this.logMessage("  Scenario: getting the maximum power level through "
						+ "the user interface");
		this.logMessage("    Given the Box is on");
		Measure<Double> powerLevel = null;
		try {
			this.hop.switchOn();
			this.logMessage("    When I get the maximum power level through the"
							+ " user interface");
			powerLevel = this.hop.getMaxPowerLevel();
			if (powerLevel.getData() == Box.MAX_POWER_LEVEL.getData() &&
				powerLevel.getMeasurementUnit().equals(
							Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the result is the Box maximum "
								+ "power level");
			} else {
				this.statistics.incorrectResult();
				this.logMessage("     but was: " + powerLevel.getData());
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		} finally {
			try {
				this.hop.switchOff();
			} catch (Throwable e) {
				this.logMessage("unexpected exception " + e +
								" raised by a switchOff()");
			}
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: getting the maximum power level through the"
						+ " external control interface");
		this.logMessage("    Given the Box is on");
		try {
			this.hop.switchOn();
			this.logMessage("    When I get the maximum power level through the"
							+ " external control interface");
			powerLevel = this.hecop.getMaxPowerLevel();
			if (powerLevel.getData() == Box.MAX_POWER_LEVEL.getData() &&
				powerLevel.getMeasurementUnit().equals(
							Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the result is the Box maximum "
								+ "power level");
			} else {
				this.statistics.incorrectResult();
				this.logMessage("     but was: " + powerLevel.getData());
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		} finally {
			try {
				this.hop.switchOff();
			} catch (Throwable e) {
				this.logMessage("unexpected exception " + e +
								" raised by a switchOff()");
			}
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: getting the current power level through "
						+ "the user interface when just initialised");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box has not been used yet");
		this.logMessage("    And the Box is on");
		boolean result;
		TimedMeasure<Double> powerLevelSignal = null;
		try {
			this.hop.switchOn();
			result = this.hop.on();
			if (!result) {
				this.logMessage("     but was: off");
				this.statistics.failedCondition();
			}
			this.logMessage("    When I get the current power level through the"
							+ " user interface");
			powerLevelSignal =  this.hop.getCurrentPowerLevel();
			if (powerLevelSignal.getMeasure().getData() == 
											Box.MAX_POWER_LEVEL.getData() &&
				powerLevelSignal.getMeasure().getMeasurementUnit().equals(
								Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the result is the Box maximum "
								+ "power level");
			} else {
				this.logMessage("     but was: " +
									powerLevelSignal.getMeasure().getData());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: getting the current power level through "
						+ "the external control interface when just initialised");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box has not been used yet");
		this.logMessage("    And the Box is on");
		try {
			this.logMessage("    When I get the current power level through the"
							+ " external control interface");
			powerLevelSignal =  this.hecop.getCurrentPowerLevel();
			if (powerLevelSignal.getMeasure().getData() == 
									Box.MAX_POWER_LEVEL.getData() &&
				powerLevelSignal.getMeasure().getMeasurementUnit().equals(
								Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the result is the Box maximum "
								+ "power level");
			} else {
				this.statistics.incorrectResult();
				this.logMessage("     but was: " +
									powerLevelSignal.getMeasure().getData());
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the power level through the user "
						+ "interface to a given level between 0 and the maximum"
						+ " power level");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box is on");
		try {
			this.logMessage("    When I set the current power level through the"
							+ " user interface to a given level between 0 and"
							+ " the maximum power level");
			this.hop.setCurrentPowerLevel(
					new Measure<Double>(Box.MAX_POWER_LEVEL.getData()/2.0,
										Box.POWER_UNIT));
			powerLevelSignal = this.hop.getCurrentPowerLevel();
			if (powerLevelSignal.getMeasure().getData() ==
										Box.MAX_POWER_LEVEL.getData()/2.0 &&
				powerLevelSignal.getMeasure().getMeasurementUnit().equals(
								Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the current power level is the given"
								+ " level");
			} else {
				this.statistics.incorrectResult();
				this.logMessage("     but was: " +
									powerLevelSignal.getMeasure().getData());
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the power level through the user "
						+ "interface to a given level over the maximum"
						+ " power level");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box is on");
		try {
			this.logMessage("    When I set the current power level through the"
							+ " user interface to a given level over the maximum"
							+ " power level");
			this.hop.setCurrentPowerLevel(
					new Measure<Double>(Box.MAX_POWER_LEVEL.getData() + 1.0,
										Box.POWER_UNIT));
			powerLevelSignal = this.hop.getCurrentPowerLevel();
			if (powerLevelSignal.getMeasure().getData() ==
											Box.MAX_POWER_LEVEL.getData() &&
				powerLevelSignal.getMeasure().getMeasurementUnit().equals(
								Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the current power level is the maximum"
								+ " power level");
			} else {
				this.logMessage("     but was: " +
									powerLevelSignal.getMeasure().getData());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the power level through the "
						+ "external control interface to a given level between "
						+ "0 and the maximum power level");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box is on");
		try {
			this.logMessage("    When I set the current power level through the"
							+ " external control interface to a given level "
							+ "between 0 and the maximum power level");
			this.hop.setCurrentPowerLevel(
					new Measure<Double>(Box.MAX_POWER_LEVEL.getData()/2.0,
										Box.POWER_UNIT));
			powerLevelSignal = this.hecop.getCurrentPowerLevel();
			if (powerLevelSignal.getMeasure().getData() ==
										Box.MAX_POWER_LEVEL.getData()/2.0 &&
					powerLevelSignal.getMeasure().getMeasurementUnit().equals(
								Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the current power level is the given"
								+ " level");
			} else {
				this.statistics.incorrectResult();
				this.logMessage("     but was: " +
									powerLevelSignal.getMeasure().getData());
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the power level through the "
						+ "external control interface to a given level over the"
						+ " maximum power level");
		this.logMessage("    Given the Box is initialised");
		this.logMessage("    And the Box is on");
		try {
			this.logMessage("    When I set the current power level through the"
							+ " external control interface to a given level over"
							+ " the maximum power level");
			this.hop.setCurrentPowerLevel(
					new Measure<Double>(Box.MAX_POWER_LEVEL.getData() + 1.0,
										Box.POWER_UNIT));
			powerLevelSignal = this.hecop.getCurrentPowerLevel();
			if (powerLevelSignal.getMeasure().getData() ==
											Box.MAX_POWER_LEVEL.getData() &&
				powerLevelSignal.getMeasure().getMeasurementUnit().equals(
								Box.MAX_POWER_LEVEL.getMeasurementUnit())) {
				this.logMessage("    Then the current power level is the maximum"
								+ " power level");
			} else {
				this.logMessage("     but was: " +
									powerLevelSignal.getMeasure().getData());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();
	}

	/**
	 * run all unit tests.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 */
	protected void		runAllUnitTests()
	{
		this.testOff();
		this.testSwitchOnSwitchOff();
		this.testPowerLevel();

		this.statistics.statisticsReport(this);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.hop.getPortURI(),
					this.BoxUserInboundPortURI,
					BoxUserConnector.class.getCanonicalName());
			this.doPortConnection(
					this.hecop.getPortURI(),
					BoxExternalControlInboundPortURI,
					BoxExternalControlConnector.class.getCanonicalName());
		} catch (Throwable e) {
			throw new ComponentStartException(e) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		if (this.isUnitTest) {
			this.runAllUnitTests();
		} else {
			ClocksServerOutboundPort clocksServerOutboundPort =
											new ClocksServerOutboundPort(this);
			clocksServerOutboundPort.publishPort();
			this.doPortConnection(
					clocksServerOutboundPort.getPortURI(),
					ClocksServer.STANDARD_INBOUNDPORT_URI,
					ClocksServerConnector.class.getCanonicalName());
			this.traceMessage("Box tester gets the clock.\n");
			AcceleratedClock ac =
					clocksServerOutboundPort.getClock(
										CVMIntegrationTest.CLOCK_URI);
			this.doPortDisconnection(
						clocksServerOutboundPort.getPortURI());
			clocksServerOutboundPort.unpublishPort();
			clocksServerOutboundPort = null;

			Instant startInstant = ac.getStartInstant();
			Instant BoxSwitchOn = startInstant.plusSeconds(SWITCH_ON_DELAY);
			Instant BoxSwitchOff = startInstant.plusSeconds(SWITCH_OFF_DELAY);
			this.traceMessage("Box tester waits until start.\n");
			ac.waitUntilStart();
			this.traceMessage("Box tester schedules switch on and off.\n");
			long delayToSwitchOn = ac.nanoDelayUntilInstant(BoxSwitchOn);
			long delayToSwitchOff = ac.nanoDelayUntilInstant(BoxSwitchOff);

			// This is to avoid mixing the 'this' of the task object with the 'this'
			// representing the component object in the code of the next methods run
			AbstractComponent o = this;

			// schedule the switch on Box
			this.scheduleTaskOnComponent(
					new AbstractComponent.AbstractTask() {
						@Override
						public void run() {
							try {
								o.traceMessage("Box switches on.\n");
								hop.switchOn();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, delayToSwitchOn, TimeUnit.NANOSECONDS);

			// to be completed with a more covering scenario

			// schedule the switch off Box
			this.scheduleTaskOnComponent(
					new AbstractComponent.AbstractTask() {
						@Override
						public void run() {
							try {
								o.traceMessage("Box switches off.\n");
								hop.switchOff();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, delayToSwitchOff, TimeUnit.NANOSECONDS);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.hop.getPortURI());
		this.doPortDisconnection(this.hecop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.hop.unpublishPort();
			this.hecop.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
}
// -----------------------------------------------------------------------------

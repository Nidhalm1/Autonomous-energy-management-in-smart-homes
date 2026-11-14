package fr.sorbonne_u.components.hem2026e1.equipments.Box;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to implement a mock-up
// of household energy management system.
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
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.hem2026e1.equipments.Box.connections.BoxExternalControlJava4InboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.Box.connections.BoxUserJava4InboundPort;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;

// -----------------------------------------------------------------------------
/**
 * The class <code>Box</code> implements a Box component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Implementation Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code currentState != null}
 * invariant	{@code currentPowerLevel == null || currentPowerLevel.getMeasurementUnit().equals(POWER_UNIT)}
 * invariant	{@code currentPowerLevel == null || currentPowerLevel.getData() >= 0.0 && currentPowerLevel.getData() <= MAX_POWER_LEVEL.getData()}
 * </pre>
 * 
 * <p><strong>Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code REFLECTION_INBOUND_PORT_URI != null && !REFLECTION_INBOUND_PORT_URI.isEmpty()}
 * invariant	{@code USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty()}
 * invariant	{@code EXTERNAL_CONTROL_INBOUND_PORT_URI != null && !EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()}
 * invariant	{@code X_RELATIVE_POSITION >= 0}
 * invariant	{@code Y_RELATIVE_POSITION >= 0}
 * </pre>
 * 
 * <p>Created on : 2023-09-18</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@OfferedInterfaces(offered={BoxUserJava4CI.class,
							BoxExternalControlJava4CI.class})
public class			Box
extends		AbstractComponent
implements	BoxUserI
{
	// -------------------------------------------------------------------------
	// Inner interfaces and types
	// -------------------------------------------------------------------------

	/**
	 * The enumeration <code>BoxState</code> describes the operation
	 * states of the Box.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * <p>Created on : 2021-09-10</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	public static enum	BoxState
	{
		/** Box is on.														*/
		ON,
		/** Box is off.													*/
		OFF
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	// BCM4Java information

	/** when true, methods trace their actions.								*/
	public static boolean		VERBOSE = false;
	/** when tracing, x coordinate of the window relative position.			*/
	public static int			X_RELATIVE_POSITION = 0;
	/** when tracing, y coordinate of the window relative position.			*/
	public static int			Y_RELATIVE_POSITION = 0;

	/** URI of the Box inbound port used in tests.						*/
	public static final String	REFLECTION_INBOUND_PORT_URI =
															"Box-RIP-URI";	

	/** URI of the Box port for user interactions.						*/
	public static final String	USER_INBOUND_PORT_URI =
												"Box-USER-INBOUND-PORT-URI";
	/** URI of the Box port for external control.						*/
	public static final String	EXTERNAL_CONTROL_INBOUND_PORT_URI =
									"Box-EXTERNAL-CONTROL-INBOUND-PORT-URI";


	/** inbound port offering the <code>BoxUserCI</code> interface.		*/
	protected BoxUserJava4InboundPort			hip;
	/** inbound port offering the <code>BoxExternalControlCI</code>
	 *  interface.															*/
	protected BoxExternalControlJava4InboundPort	hecip;

	// Appliance information

	/** current state (on, off) of the Box.								*/
	protected BoxState				currentState;
	/**	current power level of the Box.									*/
	protected TimedMeasure<Double>		currentPowerLevel;

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
	 * pre	{@code h != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param h	instance to be tested.
	 * @return	true if the implementation invariants are observed, false otherwise.
	 */
	protected static boolean	implementationInvariants(Box h)
	{
		assert	h != null : new PreconditionException("h != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				h.currentState != null,
				Box.class, h,
				"h.currentState != null");
		ret &= AssertionChecking.checkImplementationInvariant(
				h.currentPowerLevel == null ||
					h.currentPowerLevel.getMeasurementUnit().equals(POWER_UNIT),
				Box.class, h,
				"currentPowerLevel == null || currentPowerLevel."
				+ "getMeasurementUnit().equals(POWER_UNIT)");
		ret &= AssertionChecking.checkImplementationInvariant(
				h.currentPowerLevel.getData() >= 0.0 &&
							h.currentPowerLevel.getData() <=
													MAX_POWER_LEVEL.getData(),
				Box.class, h,
				"currentPowerLevel.getData() >= 0.0 && "
				+ "currentPowerLevel.getData() <= "
				+ "MAX_POWER_LEVEL.getData()");
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
	 * @return	true if the invariants are observed, false otherwise.
	 */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= BoxExternalControlI.staticInvariants();
		ret &= AssertionChecking.checkStaticInvariant(
				REFLECTION_INBOUND_PORT_URI != null &&
									!REFLECTION_INBOUND_PORT_URI.isEmpty(),
				Box.class,
				"REFLECTION_INBOUND_PORT_URI != null && "
								+ "!REFLECTION_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty(),
				Box.class,
				"USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				EXTERNAL_CONTROL_INBOUND_PORT_URI != null &&
								!EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty(),
				Box.class,
				"EXTERNAL_CONTROL_INBOUND_PORT_URI != null &&"
							+ "!EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
				Box.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
				Box.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}

	/**
	 * return true if the invariants are observed, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code h != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param h	instance to be tested.
	 * @return	true if the invariants are observed, false otherwise.
	 */
	protected static boolean	invariants(Box h)
	{
		assert	h != null : new PreconditionException("h != null");

		boolean ret = true;
		ret &= staticInvariants();
		ret &= BoxUserI.invariants(h);
		return ret;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new Box.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 * 
	 * @throws Exception <i>to do</i>.
	 */
	protected			Box() throws Exception
	{
		this(USER_INBOUND_PORT_URI, EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	/**
	 * create a new Box.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
	 * pre	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param BoxUserInboundPortURI				URI of the inbound port to call the Box component for user interactions.
	 * @param BoxExternalControlInboundPortURI	URI of the inbound port to call the Box component for external control.
	 * @throws Exception							<i>to do</i>.
	 */
	protected			Box(
		String BoxUserInboundPortURI,
		String BoxExternalControlInboundPortURI
		) throws Exception
	{
		super(1, 0);
		this.initialise(BoxUserInboundPortURI,
						BoxExternalControlInboundPortURI);
	}

	/**
	 * create a new Box.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code reflectionInboundPortURI != null && !reflectionInboundPortURI.isEmpty()}
	 * pre	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
	 * pre	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI				URI of the reflection inbound port of the component.
	 * @param BoxUserInboundPortURI				URI of the inbound port to call the Box component for user interactions.
	 * @param BoxExternalControlInboundPortURI	URI of the inbound port to call the Box component for external control.
	 * @throws Exception							<i>to do</i>.
	 */
	protected			Box(
		String reflectionInboundPortURI,
		String BoxUserInboundPortURI,
		String BoxExternalControlInboundPortURI
		) throws Exception
	{
		super(reflectionInboundPortURI, 1, 0);

		this.initialise(BoxUserInboundPortURI,
						BoxExternalControlInboundPortURI);
	}

	/**
	 * initialise a new Box.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty()}
	 * pre	{@code BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty()}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param BoxUserInboundPortURI				URI of the inbound port to call the Box component for user interactions.
	 * @param BoxExternalControlInboundPortURI	URI of the inbound port to call the Box component for external control.
	 * @throws Exception							<i>to do</i>.
	 */
	protected void		initialise(
		String BoxUserInboundPortURI,
		String BoxExternalControlInboundPortURI
		) throws Exception
	{
		assert	BoxUserInboundPortURI != null && !BoxUserInboundPortURI.isEmpty();
		assert	BoxExternalControlInboundPortURI != null && !BoxExternalControlInboundPortURI.isEmpty();

		this.currentState = BoxState.OFF;
		this.currentPowerLevel =
			new TimedMeasure<>(
					new Measure<>(
							MAX_POWER_LEVEL.getData(),
							MAX_POWER_LEVEL.getMeasurementUnit()));

		this.hip = new BoxUserJava4InboundPort(BoxUserInboundPortURI, this);
		this.hip.publishPort();
		this.hecip = new BoxExternalControlJava4InboundPort(
									BoxExternalControlInboundPortURI, this);
		this.hecip.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Box component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();		
		}

		assert	Box.implementationInvariants(this) :
				new ImplementationInvariantException(
						"Box.implementationInvariants(this)");
		assert	Box.invariants(this) :
				new InvariantException("Box.invariants(this)");
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.hip.unpublishPort();
			this.hecip.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxStateI#on()
	 */
	@Override
	public boolean		on()
	{
		if (Box.VERBOSE) {
			this.traceMessage("Box returns its state: " +
											this.currentState + ".\n");
		}
		return this.currentState == BoxState.ON;
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserI#switchOn()
	 */
	@Override
	public void			switchOn()
	{
		if (Box.VERBOSE) {
			this.traceMessage("Box switches on.\n");
		}

		assert	!this.on() : new PreconditionException("!on()");

		this.currentState = BoxState.ON;

		assert	 this.on() : new PostconditionException("on()");
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserI#switchOff()
	 */
	@Override
	public void			switchOff()
	{
		if (Box.VERBOSE) {
			this.traceMessage("Box switches off.\n");
		}

		assert	this.on() : new PreconditionException("on()");

		this.currentState = BoxState.OFF;

		assert	 !this.on() : new PostconditionException("!on()");
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxExternalControlI#getMaxPowerLevel()
	 */
	@Override
	public Measure<Double>	getMaxPowerLevel()
	{
		if (Box.VERBOSE) {
			this.traceMessage("Box returns its max power level " + 
					MAX_POWER_LEVEL + ".\n");
		}

		assert	this.on() : new PreconditionException("on()");

		return MAX_POWER_LEVEL;
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxExternalControlI#getCurrentPowerLevel()
	 */
	@Override
	public TimedMeasure<Double>	getCurrentPowerLevel()
	{
		if (Box.VERBOSE) {
			this.traceMessage("Box returns its current power level " + 
					this.currentPowerLevel + ".\n");
		}

		assert	this.on() : new PreconditionException("on()");

		TimedMeasure<Double> ret = this.currentPowerLevel;

		assert	ret != null && ret.getMeasure().getMeasurementUnit().
															equals(POWER_UNIT) :
				new PreconditionException(
						"return != null && return.getMeasure()."
						+ "getMeasurementUnit().equals(POWER_UNIT)");
		assert	ret.getMeasure().getData() >= 0.0 &&
					ret.getMeasure().getData() <= getMaxPowerLevel().getData() :
				new PostconditionException(
							"return.getMeasure().getData() >= 0.0 && "
							+ "return.getMeasure().getData() <= "
							+ "getMaxPowerLevel().getData()");

		return ret;
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxExternalControlI#setCurrentPowerLevel(fr.sorbonne_u.alasca.physical_data.Measure)
	 */
	@Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	{
		if (Box.VERBOSE) {
			this.traceMessage("Box sets its power level to " + 
														powerLevel + ".\n");
		}

		assert	this.on() : new PreconditionException("on()");
		assert	powerLevel != null && powerLevel.getData() >= 0.0 &&
							powerLevel.getMeasurementUnit().equals(POWER_UNIT) :
				new PreconditionException(
						"powerLevel != null && powerLevel.getData() >= 0.0 && "
						+ "powerLevel.getMeasurementUnit().equals(POWER_UNIT)");

		if (powerLevel.getData() <= getMaxPowerLevel().getData()) {
			this.currentPowerLevel = new TimedMeasure<Double>(powerLevel);
		} else {
			this.currentPowerLevel =
					new TimedMeasure<Double>(
							new Measure<>(
									MAX_POWER_LEVEL.getData(),
									MAX_POWER_LEVEL.getMeasurementUnit()));
		}

		assert	powerLevel.getData() > getMaxPowerLevel().getData() ||
						getCurrentPowerLevel().getMeasure().getData() ==
														powerLevel.getData() :
				new PostconditionException(
						"powerLevel.getData() > getMaxPowerLevel().getData() "
						+ "|| getCurrentPowerLevel().getData() == "
						+ "powerLevel.getData()");
	}
}
// -----------------------------------------------------------------------------

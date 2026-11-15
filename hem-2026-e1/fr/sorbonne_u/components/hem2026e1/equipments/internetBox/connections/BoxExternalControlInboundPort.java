package fr.sorbonne_u.components.hem2026e1.equipments.internetBox.connections;

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

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2026e1.equipments.internetBox.BoxExternalControlCI;
import fr.sorbonne_u.components.hem2026e1.equipments.internetBox.BoxExternalControlI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoxExternalControlInboundPort</code> implements the
 * inbound port for the {@code BoxExternalControlCI} component interface.
 *
 * <p><strong>Description</strong></p>
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
 * invariant	{@code getOwner() instanceof BoxExternalControlI}
 * </pre>
 * 
 * <p>Created on : 2023-09-19</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			BoxExternalControlInboundPort
extends		AbstractInboundPort
implements	BoxExternalControlCI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create an inbound port.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner != null}
	 * pre	{@code owner instanceof BoxExternalControlI}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param owner			component that owns this port.
	 * @throws Exception	<i>to do</i>.
	 */
	public				BoxExternalControlInboundPort(ComponentI owner)
	throws Exception
	{
		this(BoxExternalControlCI.class, owner);
	}

	/**
	 * create an inbound port.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code uri != null && !uri.isEmpty()}
	 * pre	{@code owner != null}
	 * pre	{@code owner instanceof BoxExternalControlI}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param uri			unique identifier of the port.
	 * @param owner			component that owns this port.
	 * @throws Exception	<i>to do</i>.
	 */
	public				BoxExternalControlInboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		this(uri, BoxExternalControlCI.class, owner);
	}

	/**
	 * create an inbound port.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner != null}
	 * pre	{@code implementedInterface != null}
	 * pre	{@code owner instanceof BoxExternalControlI}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param implementedInterface	interface implemented by this port.
	 * @param owner					component that owns this port.
	 * @throws Exception			<i>to do</i>.
	 */
	public				BoxExternalControlInboundPort(
		Class<? extends BoxExternalControlCI> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(implementedInterface, owner);

		assert	implementedInterface != null &&
						BoxExternalControlCI.class.isAssignableFrom(
														implementedInterface)  :
				new PreconditionException(
						"implementedInterface != null && "
						+ "BoxExternalControlCI.class.isAssignableFrom("
						+ "implementedInterface)");
		assert	owner instanceof BoxExternalControlI :
				new PreconditionException(
						"owner instanceof BoxExternalControlI");
	}

	/**
	 * create an inbound port.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code uri != null && !uri.isEmpty()}
	 * pre	{@code implementedInterface != null}
	 * pre	{@code owner != null}
	 * pre	{@code owner instanceof BoxExternalControlI}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param uri					unique identifier of the port.
	 * @param implementedInterface	interface implemented by this port.
	 * @param owner					component that owns this port.
	 * @throws Exception			<i>to do</i>.
	 */
	public				BoxExternalControlInboundPort(
		String uri,
		Class<? extends BoxExternalControlCI> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(uri, implementedInterface, owner);

		assert	implementedInterface != null &&
						BoxExternalControlCI.class.isAssignableFrom(
														implementedInterface)  :
				new PreconditionException(
						"implementedInterface != null && "
						+ "BoxExternalControlCI.class.isAssignableFrom("
						+ "implementedInterface)");
		assert	owner instanceof BoxExternalControlI :
				new PreconditionException(
						"owner instanceof BoxExternalControlI");
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.internetBox.BoxExternalControlCI#on()
	 */
	@Override
	public boolean		on() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((BoxExternalControlI)o).on());
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.internetBox.BoxExternalControlCI#getMaxPowerLevel()
	 */
	@Override
	public Measure<Double>	getMaxPowerLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((BoxExternalControlI)o).getMaxPowerLevel());
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.internetBox.BoxExternalControlCI#setCurrentPowerLevel(fr.sorbonne_u.alasca.physical_data.Measure)
	 */
	@Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((BoxExternalControlI)o).
											setCurrentPowerLevel(powerLevel);
						return null;
					 });
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.internetBox.BoxExternalControlCI#getCurrentPowerLevel()
	 */
	@Override
	public TimedMeasure<Double>	getCurrentPowerLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((BoxExternalControlI)o).getCurrentPowerLevel());
	}
}
// -----------------------------------------------------------------------------

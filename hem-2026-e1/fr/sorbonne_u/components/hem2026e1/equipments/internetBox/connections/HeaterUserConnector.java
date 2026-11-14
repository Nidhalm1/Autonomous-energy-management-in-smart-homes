package fr.sorbonne_u.components.hem2026e1.equipments.Box.connections;

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

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoxConnector</code> implements a connector for the
 * {@code BoxUserCI} component interface.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Implementation Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code true}	// no more invariant
 * </pre>
 * 
 * <p><strong>Implementation Invariants</strong></p>
 * 
 * <pre>
 * invariant	{@code true}	// no more invariant
 * </pre>
 * 
 * <p>Created on : 2021-09-13</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			BoxUserConnector
extends		AbstractConnector
implements	BoxUserCI
{
	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI#on()
	 */
	@Override
	public boolean		on() throws Exception
	{
		return ((BoxUserCI)this.offering).on();
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI#switchOn()
	 */
	@Override
	public void			switchOn() throws Exception
	{
		((BoxUserCI)this.offering).switchOn();
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI#switchOff()
	 */
	@Override
	public void			switchOff() throws Exception
	{
		((BoxUserCI)this.offering).switchOff();
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI#getMaxPowerLevel()
	 */
	@Override
	public Measure<Double>	getMaxPowerLevel() throws Exception
	{
		return ((BoxUserCI)this.offering).getMaxPowerLevel();
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI#setCurrentPowerLevel(fr.sorbonne_u.alasca.physical_data.Measure)
	 */
	@Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		((BoxUserCI)this.offering).setCurrentPowerLevel(powerLevel);
	}

	/**
	 * @see fr.sorbonne_u.components.hem2026e1.equipments.Box.BoxUserCI#getCurrentPowerLevel()
	 */
	@Override
	public TimedMeasure<Double>	getCurrentPowerLevel() throws Exception
	{
		return ((BoxUserCI)this.offering).getCurrentPowerLevel();
	}
}
// -----------------------------------------------------------------------------

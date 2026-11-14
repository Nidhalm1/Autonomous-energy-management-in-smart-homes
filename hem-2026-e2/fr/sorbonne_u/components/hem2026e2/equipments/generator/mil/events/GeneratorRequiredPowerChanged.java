package fr.sorbonne_u.components.hem2026e2.equipments.generator.mil.events;

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

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;

// -----------------------------------------------------------------------------
/**
 * The class <code>GeneratorRequiredPowerChanged</code> implements the event
 * that signals a significant event concerning the shared continuous variable
 * {@code generatorRequiredPower}.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This event is a pure simulation artifact, as it does not represent an actual
 * physical event but rather allows the code to be more efficient. When a
 * generator user model is connected to the generator power model, the two
 * models share a variable that indicates the power required from the generator.
 * The user model sets the required power and the generator power model reacts
 * by setting its output power to the required level if possible. In NeoSim4Java,
 * setting variables do not trigger internal nor external transitions in models.
 * Hence, in this scenario, if the generator power model want to detect a change
 * in the value of the required power, it must repetitively perform an internal
 * transition to check if the value of this variable has changed. In many simu-
 * lations, the required power will change relatively rarely, hence the
 * repetitive internal transitions of the generator power model becomes a form
 * of polling that should be avoided. This event is used for that. When the user
 * model sets a new value for the required power, it emits immediately a
 * {@code GeneratorRequiredPowerChanged} event towards the generator power
 * model, which will perform an external transition and, <i>in fine</i>, will
 * adjust its output power accordingly.
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
 * <p>Created on : 2025-10-27</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			GeneratorRequiredPowerChanged
extends		Event
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a <code>SharedContinuousStateChange</code> event.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code timeOfOccurrence != null}
	 * post	{@code getTimeOfOccurrence().equals(timeOfOccurrence)}
	 * post	{@code getEventInformation() == null}
	 * </pre>
	 *
	 * @param timeOfOccurrence	time of occurrence of the event.
	 */
	public				GeneratorRequiredPowerChanged(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------

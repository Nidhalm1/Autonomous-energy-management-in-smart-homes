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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

// -----------------------------------------------------------------------------
public class Test {

	public static void main(String[] args)
	{
		/** the start instant used in the test scenarios.						*/
		Instant	current = Instant.parse("2026-10-01T00:00:00.00Z");

		for (long i = 0 ; i <= 24 ; i++) {
			ZonedDateTime zdt =
					ZonedDateTime.ofInstant(
							current.plusSeconds(i*3600),
							ZoneId.of("Z"));
			double h = zdt.getHour();
			double delta = zdt.getMinute();
			h += delta/((double)TimeUnit.HOURS.toMinutes(1));
			delta = zdt.getSecond();
			h += delta/((double)TimeUnit.HOURS.toSeconds(1));
			delta = zdt.getNano();
			h += delta/((double)TimeUnit.HOURS.toNanos(1));
			double cycleTime = h + 18.0;
			if (cycleTime >= 24.0) {
				cycleTime -= 24.0;
			}
			System.out.println(i + " : " + h + ", " + cycleTime);
		}
		
	}
}
// -----------------------------------------------------------------------------

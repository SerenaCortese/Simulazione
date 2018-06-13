package it.polito.tdp.flightdelays.model;

import java.util.HashMap;
import java.util.Map;

public class FlightIdMap {
	
	private Map<Integer, Flight> map;

	public FlightIdMap() {
		this.map = new HashMap<>();
	}
	
	public Flight get(Flight flight) {
		Flight old = map.get(flight.getId());
		if(old==null) {
			//nella mappa non c'è questo corso => LO AGGIUNGO
			map.put(flight.getId(), flight);
			return flight;
		}
		
		//avevo già inserito quell'oggetto
		return old;
	
	}
	
	public Flight get(int flightId) {
		return map.get(flightId);
	}
	
	public void put(Flight flight, int flightId) {
		map.put(flightId, flight);
	}

}

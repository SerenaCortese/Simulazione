package it.polito.tdp.flightdelays.model;

import java.util.HashMap;
import java.util.Map;

public class AirlineIdMap {
	
	private Map<String, Airline> map;

	public AirlineIdMap() {
		this.map = new HashMap<>();
	}
	
	public Airline get(Airline airline) {
		Airline old = map.get(airline.getId());
		if(old==null) {
			//nella mappa non c'è questo corso => LO AGGIUNGO
			map.put(airline.getId(), airline);
			return airline;
		}
		
		//avevo già inserito quell'oggetto
		return old;
	
	}
	
	public Airline get(String airlineId) {
		return map.get(airlineId);
	}
	
	public void put(Airline airline, String airlineId) {
		map.put(airlineId, airline);
	}

}

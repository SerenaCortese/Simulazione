package it.polito.tdp.flightdelays.model;

import java.util.HashMap;
import java.util.Map;

public class AirportIdMap {
	
	private Map<String, Airport> map;

	public AirportIdMap() {
		this.map = new HashMap<>();
	}
	
	public Airport get(Airport airport) {
		Airport old = map.get(airport.getId());
		if(old==null) {
			//nella mappa non c'è questo corso => LO AGGIUNGO
			map.put(airport.getId(), airport);
			return airport;
		}
		
		//avevo già inserito quell'oggetto
		return old;
	
	}
	
	public Airport get(String airportId) {
		return map.get(airportId);
	}
	
	public void put(Airport airport, String airportId) {
		map.put(airportId, airport);
	}

}

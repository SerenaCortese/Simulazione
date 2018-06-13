package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulazione {
	
	private int k; //numero di passeggeri
	private int v; //numero voli
	private Model model;
	private LocalDateTime inizio;
	private List<Flight> voli;
	
	private PriorityQueue<Event> pq;
	private Map<Airport,Integer> personeAeroporto; //num di persone in ogni aroporto
	
	//output
	private int ritardoComplessivo;
	

	public Simulazione(int k, int v, Model model) {
		this.k = k;
		this.v = v;
		this.model = model;
		pq = new PriorityQueue<>();
		ritardoComplessivo =0;
		voli = model.getPrimoVolo();
		inizio = voli.get(0).getScheduledDepartureDate();
		
		
	}
	private enum EventType {
		PARTENZA, ARRIVO;
	}
	 
	public void run() {
		
		this.personeAeroporto = new HashMap<>();
		
		
		// Aggiungere gli eventi alla pq
		for (Flight f : voli) {
			pq.add(new Event(EventType.PARTENZA,f.getScheduledDepartureDate(), f));
		}
		
		// Inizializzo il numero di biciclette per ogni stazione
		for (Station s : model.getStations()) {
			stationCount.put(s, (int)(s.getDockCount() * k));
		}
		
		// processare gli eventi
		while(!pq.isEmpty()) {
			Event e = pq.poll();
			
			switch(e.type) {
			case PICK:
				Station station = model.stationIdMap.get(e.trip.getStartStationID());
				int count = stationCount.get(station);
				
				if (count > 0) {
					// bicicletta disponibile
					count--;
					stationCount.put(station, count);
					// aggiungo un nuovo evento DROP
					pq.add(new Event(EventType.DROP, e.trip.getEndDate(), e.trip));
				} else {
					// l'utente non è riuscito a prendere la bicicletta
					PICKmiss++;
				}
				break;
			
			case DROP:
				station = model.stationIdMap.get(e.trip.getEndStationID());
				count = stationCount.get(station);
				
				if (station.getDockCount() > count) {
					// ci sono ancora dei posti disponibili
					count++;
					stationCount.put(station, count);
				} else {
					DROPmiss++;
				}
				break;
			}
		}
		
	}
	
	private class Event implements Comparable<Event> {
		
		EventType type;
		LocalDateTime date;
		Flight flight;
		
		public Event(EventType type, LocalDateTime date, Trip trip) {
			this.type = type;
			this.date = date;
			this.trip = trip;
		}

		@Override
		public int compareTo(Event o) {
			return date.compareTo(o.date);
		}
		
		
	}
	
	
}

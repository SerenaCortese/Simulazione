package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private FlightDelaysDAO dao;
	private List<Airline> airlines;
	private List<Airport> airports;
//	private List<Flight> flights;
	
	private AirlineIdMap airlineIdMap;
	private AirportIdMap airportIdMap;
//	private FlightIdMap flightIdMap;
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private List<Arco> archi;
	
	public Model() {
		dao = new FlightDelaysDAO();
		
		this.airlineIdMap = new AirlineIdMap();
		this.airportIdMap = new AirportIdMap();
//		this.flightIdMap = new FlightIdMap();
		
		
		airlines = dao.loadAllAirlines(airlineIdMap);
		airports = dao.loadAllAirports(airportIdMap);
		
		archi = new ArrayList<>();
		
	}

	public List<Airline> getAirlines() {
		if(this.airlines == null)
			return new ArrayList<Airline>(); 

		return airlines;
	}

	public void creaGrafo(Airline airline) {
		
		//creo grafo
		grafo = new SimpleDirectedWeightedGraph<Airport,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.airports);
		System.out.println("Ho aggiunto vertici"+ grafo.vertexSet().size());
		int count= 0;
		//creo archi(=>itero sulle rotte)
		
		List<Arco> tratte = dao.getArchiConMedia(airportIdMap, airline);
		for(Arco a: tratte) {
			Airport sourceAirport = a.getPartenza();
			Airport destinationAirport = a.getDestinazione();
			if(sourceAirport!=null && destinationAirport!= null) {
				double distanza = LatLngTool.distance(new LatLng(sourceAirport.getLatitude(), sourceAirport.getLongitude()),
						new LatLng(destinationAirport.getLatitude(), destinationAirport.getLongitude()), LengthUnit.KILOMETER);
				
				double mediaRitardi = a.getPeso();
				double weight = mediaRitardi/distanza;
				Graphs.addEdge(this.grafo, sourceAirport, destinationAirport, weight);
				archi.add(new Arco(sourceAirport, destinationAirport, weight));
			}
			
		}
		
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
		
	}
	
	public List<Arco> getArchiPeggiori(){
		
		Collections.sort(archi);
		if(archi.size()<=10)
			return archi;
		List<Arco> peggiori = new ArrayList<>();
		for(int i = 0; i<10; i++) {
			peggiori.add(archi.get(i));
		}
		return peggiori;
	}

	public void simula(int k, int v) {
		Simulazione sim = new Simulazione(k,v,this);
		sim.run();
		
	}
	
	public List<Flight> getPrimoVolo(){
		return dao.getprimoVolo(airportIdMap);
	}
	
	
	

}

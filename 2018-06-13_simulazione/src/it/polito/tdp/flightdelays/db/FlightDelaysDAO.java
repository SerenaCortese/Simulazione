package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.AirlineIdMap;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.AirportIdMap;
import it.polito.tdp.flightdelays.model.Arco;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.FlightIdMap;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines(AirlineIdMap airlineIdMap) {
		String sql = "SELECT id, airline from airlines ORDER BY airline ASC";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airline a = new Airline(rs.getString("ID"), rs.getString("airline")); 
				result.add(airlineIdMap.get(a));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(AirportIdMap airportIdMap) {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airportIdMap.get(airport));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights(FlightIdMap flightIdMap, AirportIdMap airportIdMap, AirlineIdMap airlineIdMap) {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport source = airportIdMap.get(rs.getString("origin_airport_id"));
				Airport destination = airportIdMap.get(rs.getString("destination_airport_id"));
				//Airline airline = airlineIdMap.get(rs.getString("airline"));
				if(destination!=null && source!=null) {
					Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
							source, destination,rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
							rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
							rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
					//result.add(flightIdMap.get(flight));
					result.add(flight);
				}
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Arco> getArchiConMedia(AirportIdMap airportIdmap, Airline airline) {
		String sql = "select f.ORIGIN_AIRPORT_ID as origin, f.DESTINATION_AIRPORT_ID as dest, avg(arrival_delay) as media " + 
				"from flights as f where f.airline= ? " + 
				"Group by f.ORIGIN_AIRPORT_ID , f.DESTINATION_AIRPORT_ID";
		List<Arco> archi = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Airport source = airportIdmap.get(rs.getString("origin"));
				Airport destination = airportIdmap.get(rs.getString("dest"));
		
				Arco a = new Arco(source, destination, rs.getDouble("media"));
				archi.add(a);
			}
			

			conn.close();
			return archi;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	public List<Flight> getprimoVolo(AirportIdMap airportMap) {
		String sql = "SELECT * " + 
				"FROM flights  " + 
				"ORDER BY SCHEDULED_DEP_DATE asc";
		
		List<Flight> primiVoli = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Airport source = airportMap.get(rs.getString("ORIGIN_AIRPORT_ID"));
				Airport destination = airportMap.get(rs.getString("DESTINATION_AIRPORT_ID"));
				
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						source, destination,rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				primiVoli.add(flight);
			}
			
			conn.close();
			return primiVoli;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}

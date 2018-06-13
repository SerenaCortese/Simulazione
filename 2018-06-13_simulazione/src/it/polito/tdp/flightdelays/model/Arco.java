package it.polito.tdp.flightdelays.model;

public class Arco implements Comparable<Arco>{
	
	private Airport partenza;
	private Airport destinazione;
	private double peso;
	public Arco(Airport partenza, Airport destinazione, double peso) {
		super();
		this.partenza = partenza;
		this.destinazione = destinazione;
		this.peso = peso;
	}
	public Airport getPartenza() {
		return partenza;
	}
	public void setPartenza(Airport partenza) {
		this.partenza = partenza;
	}
	public Airport getDestinazione() {
		return destinazione;
	}
	public void setDestinazione(Airport destinazione) {
		this.destinazione = destinazione;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "partenza="+ partenza + ", destinazione=" + destinazione + ", peso=" + peso + "\n";
	}
	@Override
	public int compareTo(Arco altro) { //this>altro dà 1=>ordine decrescente devo mettere -1
		if(this.peso > altro.peso)
			return -1;
		else if(this.peso<altro.peso)
			return 1;
		else return 0;
	}
	
	

}

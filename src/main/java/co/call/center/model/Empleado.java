package co.call.center.model;

import java.util.concurrent.ThreadLocalRandom;

public class Empleado {

	private String codigo;

	private Enum< TipoEmpleado > tipo;

	private boolean snDisponible = true;
	
	public Empleado( String codigo, TipoEmpleado tipo ) {
		this.codigo = codigo;
		this.tipo = tipo;
	}
	
	public Empleado( TipoEmpleado tipo ) {
		this.codigo = String.valueOf( ThreadLocalRandom.current().nextInt ( 1, 999 ) );
		this.tipo = tipo;
	}

	public String getCodigo ( ) {
		return codigo;
	}

	public void setCodigo ( String codigo ) {
		this.codigo = codigo;
	}

	public Enum< TipoEmpleado > getTipo ( ) {
		return tipo;
	}

	public void setTipoEmpleado ( Enum< TipoEmpleado > tipo ) {
		this.tipo = tipo;
	}

	public boolean isSnDisponible ( ) {
		return snDisponible;
	}

	public void setSnDisponible ( boolean snDisponible ) {
		this.snDisponible = snDisponible;
	}

	public void ocupar ( ) {
		snDisponible = false;
	}

	public void liberar ( ) {
		snDisponible = true;
	}

}

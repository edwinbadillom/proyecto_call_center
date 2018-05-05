package co.call.center.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import co.call.center.exceptions.CallCenterDAOException;
import co.call.center.model.Empleado;
import co.call.center.model.TipoEmpleado;

/**
 * Clase que simula la capa de persistencia con el fin de administrar (habilitar o deshabilitar) 
 * los empleados del call center.
 * 
 * @author edwin.badillo
 * @version 1.0
 */
public class CallCenterDAOImpl implements CallCenterDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( CallCenterDAOImpl.class );

	private List< Empleado > empleados;

	public CallCenterDAOImpl() {

		empleados = new ArrayList< Empleado >();
	}
	
	public Empleado buscarEmpleadoPorCodigo( String codigoEmpleado ) throws CallCenterDAOException {
		if ( StringUtils.isEmpty( codigoEmpleado ) ) {
			return null;
		}
		
		if ( CollectionUtils.isEmpty( empleados ) ) {
			return null;
		}
		
		Optional< Empleado > emp = empleados.stream()
				.filter( e -> e.getCodigo().equalsIgnoreCase( codigoEmpleado ) )
				.findFirst();

		return emp.isPresent() ? emp.get() : null;
	}
	
	public void agregarEmpleado( Empleado empleado ) throws CallCenterDAOException {
		if ( empleado == null ) {
			throw new CallCenterDAOException ( "Empleado no puede ser nulo." );
		}
		
		empleados.add( empleado );
	}

	public synchronized void inhabilitarEmpleado ( String codigoEmpleado ) throws CallCenterDAOException {
		
		procesarDisponibilidad( codigoEmpleado, false );
	}

	public synchronized void habilitarEmpleado ( String codigoEmpleado ) throws CallCenterDAOException {
		
		procesarDisponibilidad( codigoEmpleado, true );
	}
	
	private void procesarDisponibilidad ( String codigoEmpleado, boolean snDisponible ) throws CallCenterDAOException {
		Empleado empleado = null;

		try {
			empleado = buscarEmpleadoPorCodigo( codigoEmpleado );
			
			if ( empleado == null ) {
				throw new Exception( "No existe empleado con codigo: " + codigoEmpleado );
			}
			
			empleado.setSnDisponible( snDisponible );
			
			empleados.removeIf( e -> e.getCodigo().equals( codigoEmpleado ) );

			empleados.add( empleado );
			
			LOGGER.info( "Empleado " + ( snDisponible ? "habilitado" : "inahilitado" ) + " [codigo = " + codigoEmpleado + ", tipo = " + empleado.getTipo() + "]" );
			
		} catch ( Exception e ) {
			throw new CallCenterDAOException( e );
		}
		
	}

	public synchronized Empleado buscarEmpleadoDisponible ( ) throws CallCenterDAOException {

		Empleado retorno = null;

		try {

			retorno = buscarOperadorDisponible();

			if ( retorno == null ) {

				retorno = buscarSupervisorDisponible();

				if ( retorno == null ) {

					retorno = buscarDirectorDisponible();
				}

			}

			if ( retorno != null ) {
				inhabilitarEmpleado( retorno.getCodigo() );
			}

		} catch ( CallCenterDAOException e ) {
			throw e;
		}

		return retorno;
	}

	/**
	 * Busca un operador disponible
	 * 
	 * @return Empleado[Operador]
	 */
	private Empleado buscarOperadorDisponible ( ) throws CallCenterDAOException {
		
		if ( CollectionUtils.isEmpty( empleados ) ) {
			return null;
		}
		
		Optional< Empleado > emp = empleados.stream()
				.filter( e -> e.getTipo().compareTo( TipoEmpleado.OPERADOR ) == 0 && e.isSnDisponible() )
				.findFirst();

		return emp.isPresent() ? emp.get() : null;
	}

	/**
	 * Busca un supervisor disponible
	 * 
	 * @return Empleado[Supervisor]
	 */
	private Empleado buscarSupervisorDisponible ( ) throws CallCenterDAOException {
		
		if ( CollectionUtils.isEmpty( empleados ) ) {
			return null;
		}
		
		Optional< Empleado > emp = empleados.stream()
				.filter( e -> e.getTipo().compareTo( TipoEmpleado.SUPERVISOR ) == 0 && e.isSnDisponible() )
				.findFirst();
		
		return emp.isPresent() ? emp.get() : null;
	}

	/**
	 * Busca un director disponible
	 * 
	 * @return Empleado[Director]
	 */
	private Empleado buscarDirectorDisponible ( ) throws CallCenterDAOException {
		
		if ( CollectionUtils.isEmpty( empleados ) ) {
			return null;
		}
		
		Optional< Empleado > emp = empleados.stream()
				.filter( e -> e.getTipo().compareTo( TipoEmpleado.DIRECTOR ) == 0 && e.isSnDisponible() )
				.findFirst();
		
		return emp.isPresent() ? emp.get() : null;
	}
	
	public List< Empleado > getEmpleados ( ) {
		return empleados;
	}

	public void setEmpleados ( List< Empleado > empleados ) {
		this.empleados = empleados;
	}

}

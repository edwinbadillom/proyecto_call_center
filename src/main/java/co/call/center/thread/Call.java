package co.call.center.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import co.call.center.exceptions.CallCenterDAOException;
import co.call.center.model.Empleado;
import co.call.center.repository.CallCenterDAO;

/**
 * Clase que permite ejecutar una llamada y asignarle un asesor.
 * 
 * @author edwin.badillo
 * @version 1.0
 */

@Component
@Scope( "prototype" )
public class Call implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger( Call.class );
	
	private String telephoneNumber;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private CallCenterDAO callCenterDAO;
	
	/**
	 * Permite conectar al usuario con un asesor de la compañía.
	 */
	@Override
	public void run() {
		Empleado empleado = null;
		
		String codigoEmpleado = null;
		String tipoEmpleado = null;
		
		int timeToWait = 0;
		
		try {
			callCenterDAO = applicationContext.getBean( CallCenterDAO.class );
			
			//Busca a un empleado disponible e inmediatamente lo marca como no disponible.
			empleado = callCenterDAO.buscarEmpleadoDisponible();
			
			if ( empleado == null ) {
				LOGGER.error( "No hay asesores disponibles, por favor intente más tarde. " + telephoneNumber );
				return;
			} 
			
			LOGGER.info( "Inicia llamada [nroTelefono = " + telephoneNumber + "]" );
			
			codigoEmpleado = empleado.getCodigo();
			tipoEmpleado = empleado.getTipo().name();
			
			//Se genera aleatoriamente la duración de la llamada entre 5 a 10 seg.
			timeToWait = ThreadLocalRandom.current().nextInt ( 5000, 10000 );
			
			LOGGER.info( "Llamada en curso... [nroTelefono = " + telephoneNumber + ", codigo = " + codigoEmpleado + ", tipoEmpleado = " + tipoEmpleado + "] " + ( timeToWait /1000 ) + " seg." );
			
			Thread.sleep( timeToWait );
			
		} catch ( RejectedExecutionException e ) {
			LOGGER.error( "No hay asesores disponibles, por favor intente más tarde. " + telephoneNumber );
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			
			if ( !StringUtils.isEmpty( codigoEmpleado ) ) {
				try {
					callCenterDAO.habilitarEmpleado( codigoEmpleado );
				} catch ( CallCenterDAOException e ) {
					e.printStackTrace();
				} 
			}
			codigoEmpleado = null;
			timeToWait = 0;
			
			LOGGER.info( "Termina llamada [nroTelefono = " + telephoneNumber + "]" );
		}

	}
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

}

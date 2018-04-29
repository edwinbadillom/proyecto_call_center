package co.call.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import co.call.center.exceptions.CallCenterDAOException;
import co.call.center.exceptions.CallCenterServiceException;
import co.call.center.model.Empleado;
import co.call.center.repository.CallCenterDAO;
import co.call.center.thread.Call;

/**
 * Clase que permite despachar una llamada entrante
 * 
 * @author edwin.badillo
 * @version 1.0
 */

@Service
public class DispatcherService {

	private static final Logger LOGGER = LoggerFactory.getLogger( DispatcherService.class );
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	private ThreadPoolTaskExecutor poolTaskExecutor;
	
	private CallCenterDAO callCenterDAO;
	
	private void setUp() {
		poolTaskExecutor = applicationContext.getBean( ThreadPoolTaskExecutor.class );
		callCenterDAO = applicationContext.getBean( CallCenterDAO.class );
	}
	
	/**
	 * Recibe las llamadas entrantes y crea un hilo de ejecución por cada una de ellas.
	 * Si se llega al tope máximo de llamadas en espera entonces la llamada no se atiende.
	 * 
	 * @param telephoneNumber
	 */
	
	@Async
	public void dispatchCall ( String telephoneNumber ) {
		
		setUp();
		
		int cantLlamadasEnCurso = poolTaskExecutor.getActiveCount();

		LOGGER.debug( "Cantidad llamadas en curso = " + cantLlamadasEnCurso );

		if ( cantLlamadasEnCurso == poolTaskExecutor.getCorePoolSize() ) {

			LOGGER.error( "Llamada en espera, no hay asesores disponibles. " + telephoneNumber );
			
		} else {
			
			LOGGER.info( "Llamada será atendida. " + telephoneNumber );
		}
		
		Call call = applicationContext.getBean( Call.class );
		call.setTelephoneNumber( telephoneNumber );
		
		poolTaskExecutor.execute( call );
	}
	
	/**
	 * Permite setear lista de empleados;
	 *  
	 * @param empleados Lista de objetos Empleados.
	 */
	public void agregarEmpleados ( List< Empleado > empleados ) {
		setUp();
		callCenterDAO.setEmpleados( empleados );
	}
	
	/**
	 *  Permite agregar un nuevo Empleado para poder atender llamadas.
	 *  
	 * @param empleado Objecto Empleado.
	 * @throws CallCenterDAOException
	 */
	public void agregarEmpleado ( Empleado empleado ) throws CallCenterServiceException {
		try {
			setUp();
			callCenterDAO.agregarEmpleado( empleado );
		} catch ( CallCenterDAOException e ) {
			throw new CallCenterServiceException( e );
		}
	}
	
	/**
	 * Retorna el listado de Empleados existentes.
	 * 
	 * @return Lista de objetos Empleado.
	 */
	public List< Empleado > getEmpleados ( ) {
		setUp();
		return callCenterDAO.getEmpleados();
	}
	
	/**
	 * Busca un empleado por código.
	 * 
	 * @param codigoEmpleado Código del empleado.
	 * @return Objeto Empleado.
	 * @throws CallCenterDAOException
	 */
	public Empleado buscarEmpleadoPorCodigo( String codigoEmpleado ) throws CallCenterServiceException {
		try {
			setUp();
			return callCenterDAO.buscarEmpleadoPorCodigo( codigoEmpleado );
		} catch ( CallCenterDAOException e ) {
			throw new CallCenterServiceException( e );
		}
	}
	
	/**
	 * Buscar un empleado disponible para atender una llamada
	 * 
	 * @return  Objeto Empleado.
	 * @throws CallCenterDAOException
	 */
	public Empleado buscarEmpleadoDisponible ( ) throws CallCenterServiceException {
		try {
			setUp();
			return callCenterDAO.buscarEmpleadoDisponible();
		} catch ( CallCenterDAOException e ) {
			throw new CallCenterServiceException( e );
		}
	}

}

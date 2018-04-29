package co.call.center.repository;

import java.util.List;

import co.call.center.exceptions.CallCenterDAOException;
import co.call.center.model.Empleado;

public interface CallCenterDAO {
	
	/**
	 *  Permite agregar un nuevo Empleado para poder atender llamadas.
	 *  
	 * @param empleado Objecto Empleado.
	 * @throws CallCenterDAOException
	 */
	public void agregarEmpleado( Empleado empleado ) throws CallCenterDAOException;

	/**
	 * Inhabilita a un empleado para no contestar llamadas
	 * 
	 * @param codigoEmpleado Código del empleado
	 * @throws CallCenterDAOException
	 */
	void inhabilitarEmpleado ( String codigoEmpleado ) throws CallCenterDAOException;

	/**
	 * Habilita a un empleado para contestar llamadas
	 * 
	 * @param codigoEmpleado Código del empleado
	 * @throws CallCenterDAOException
	 */
	void habilitarEmpleado ( String codigoEmpleado ) throws CallCenterDAOException;

	/**
	 * Buscar un empleado disponible para atender una llamada
	 * 
	 * @return  Objeto Empleado.
	 * @throws CallCenterDAOException
	 */
	Empleado buscarEmpleadoDisponible ( ) throws CallCenterDAOException;
	
	/**
	 * Permite setear lista de empleados;
	 *  
	 * @param empleados Lista de objetos Empleados.
	 */
	void setEmpleados ( List< Empleado > empleados );
	
	
	/**
	 * Retorna el listado de Empleados existentes.
	 * 
	 * @return Lista de objetos Empleado.
	 */
	List< Empleado > getEmpleados ( );
	
	/**
	 * Busca un empleado por código.
	 * 
	 * @param codigoEmpleado Código del empleado.
	 * @return Objeto Empleado.
	 * @throws CallCenterDAOException
	 */
	Empleado buscarEmpleadoPorCodigo( String codigoEmpleado ) throws CallCenterDAOException;
	

}
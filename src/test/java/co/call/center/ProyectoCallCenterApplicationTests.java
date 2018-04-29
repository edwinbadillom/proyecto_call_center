package co.call.center;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.call.center.model.Empleado;
import co.call.center.model.TipoEmpleado;
import co.call.center.service.DispatcherService;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ProyectoCallCenterApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	private DispatcherService service;
	private ThreadPoolTaskExecutor poolTaskExecutor;

	@Before
	public void testApplicationContext ( ) {
		service = applicationContext.getBean( DispatcherService.class );
		service.agregarEmpleados( new ArrayList< Empleado >() );
	}

	@Test
	public void t0_agregarEmpleado ( ) throws Exception {

		Empleado empleado = new Empleado( "10", TipoEmpleado.OPERADOR );

		service.agregarEmpleado( empleado );

		empleado = service.buscarEmpleadoPorCodigo( "10" );

		assertNotNull( empleado );
		assertEquals( empleado.getCodigo(), "10" );
	}

	@Test
	public void t1_agregarEmpleados ( ) throws Exception {

		List< Empleado > emps = Arrays.asList( 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ), 
				new Empleado( TipoEmpleado.OPERADOR ) );
		
		service.agregarEmpleados( emps );
		
		emps = service.getEmpleados();
		
		for ( Empleado empleado : emps ) {
			assertNotNull( service.buscarEmpleadoPorCodigo( empleado.getCodigo() ) );
		}
	}
	
	@Test
	public void t2_buscarEmpleadoDisponible_ListaVacia ( ) throws Exception {

		assertNull( service.buscarEmpleadoDisponible() );
	}
	
	@Test
	public void t3_buscarEmpleadoDisponible_ListaLLena ( ) throws Exception {

		List< Empleado > emps = Arrays.asList( 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ), 
				new Empleado( TipoEmpleado.SUPERVISOR ) );
		
		service.agregarEmpleados( emps );
		
		assertNotNull( service.buscarEmpleadoDisponible() );
	}
	
	@Test
	public void t4_unaLlamadaEntrante ( ) throws Exception {

		List< Empleado > emps = Arrays.asList( 
				new Empleado( TipoEmpleado.OPERADOR ) );
		
		service.agregarEmpleados( emps );
		
		service.dispatchCall( "1234567890" );
		
		poolTaskExecutor = applicationContext.getBean( ThreadPoolTaskExecutor.class );
		
		assertEquals( 1, poolTaskExecutor.getActiveCount() );	
		
		TimeUnit.SECONDS.sleep( 12 );
	}
	
	@Test
	public void t5_diezLlamadasSimultaneas_NoSufientesAsesores ( ) throws Exception {

		List< Empleado > emps = Arrays.asList( 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ), 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.SUPERVISOR ), 
				new Empleado( TipoEmpleado.DIRECTOR ) );
		
		service.agregarEmpleados( emps );
		
		for ( int i = 0; i < 10; i++ ) {
			service.dispatchCall( "11111_" + i );
		}
		
		poolTaskExecutor = applicationContext.getBean( ThreadPoolTaskExecutor.class );
		
		assertTrue( poolTaskExecutor.getPoolSize() > 0 );
		
		TimeUnit.SECONDS.sleep( 25 );
	}
	
	@Test
	public void t6_diezLlamadasSimultaneas_ConAsesoresSuficientes ( ) throws Exception {

		List< Empleado > emps = Arrays.asList( 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ), 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.SUPERVISOR ), 
				new Empleado( TipoEmpleado.SUPERVISOR ),
				new Empleado( TipoEmpleado.SUPERVISOR ), 
				new Empleado( TipoEmpleado.DIRECTOR ) );
		
		service.agregarEmpleados( emps );
		
		for ( int i = 0; i < 10; i++ ) {
			service.dispatchCall( "11111_" + i );
		}
		
		poolTaskExecutor = applicationContext.getBean( ThreadPoolTaskExecutor.class );
		
		assertTrue( poolTaskExecutor.getPoolSize() > 0 );
		
		TimeUnit.SECONDS.sleep( 25 );
	}
	
	@Test
	public void t7_masDeDiezLlamadasSimultaneas ( ) throws Exception {

		List< Empleado > emps = Arrays.asList( 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ), 
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.OPERADOR ),
				new Empleado( TipoEmpleado.SUPERVISOR ), 
				new Empleado( TipoEmpleado.SUPERVISOR ),
				new Empleado( TipoEmpleado.SUPERVISOR ), 
				new Empleado( TipoEmpleado.DIRECTOR ) );
		
		service.agregarEmpleados( emps );
		
		for ( int i = 0; i < 20; i++ ) {
			service.dispatchCall( "222222_" + i );
		}
		
		poolTaskExecutor = applicationContext.getBean( ThreadPoolTaskExecutor.class );
		
		assertTrue( poolTaskExecutor.getPoolSize() > 0 );	
		
		TimeUnit.SECONDS.sleep( 50 );
	}

}

package co.call.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import co.call.center.repository.CallCenterDAO;
import co.call.center.repository.CallCenterDAOImpl;

@SpringBootApplication
@EnableAsync
public class ProyectoCallCenterApplication extends SpringBootServletInitializer {

	public static void main ( String[ ] args ) {
		SpringApplication.run( ProyectoCallCenterApplication.class, args );
	}

	@Bean
	public TaskExecutor threadPoolTaskExecutor ( ) {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize( 10 );
		executor.setMaxPoolSize( 13 );
		executor.setKeepAliveSeconds( 15 );
		executor.setQueueCapacity( 10 );
		executor.setWaitForTasksToCompleteOnShutdown( true );
		executor.setThreadNamePrefix( "task_executor_thread" );

		executor.initialize();

		return executor;
	}

	@Bean
	public CallCenterDAO obtenerCallCenterDAOImpl( ) {
	
		return new CallCenterDAOImpl();
	}
}

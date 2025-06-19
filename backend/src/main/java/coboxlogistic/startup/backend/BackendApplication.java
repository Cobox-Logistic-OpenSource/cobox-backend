package coboxlogistic.startup.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Cobox Logistic Backend Application
 */
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		System.out.println("🚀 Cobox Backend Platform started successfully!");
		System.out.println("✅ Health Check: http://localhost:8080/api/v1/health");
	}

	/**
	 * Simple health check endpoint
	 */
	@RestController
	static class HealthController {

		@GetMapping("/health")
		public Map<String, Object> health() {
			return Map.of(
					"status", "UP",
					"application", "Cobox Backend Platform",
					"timestamp", LocalDateTime.now(),
					"message", "Sistema funcionando correctamente!"
			);
		}

		@GetMapping("/")
		public Map<String, Object> welcome() {
			return Map.of(
					"message", "🚛 Bienvenido a Cobox Backend Platform",
					"description", "Sistema de gestión logística"
			);
		}
	}
}
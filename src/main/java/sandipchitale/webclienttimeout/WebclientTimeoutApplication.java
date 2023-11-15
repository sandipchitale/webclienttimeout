package sandipchitale.webclienttimeout;

import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.netty.http.client.HttpClient;

import java.net.SocketTimeoutException;
import java.time.Duration;

@SpringBootApplication
public class WebclientTimeoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebclientTimeoutApplication.class, args);
	}

	@RestController
	public static class IndexController {
		private final WebClient.Builder webClientBuilder;
		private final RestTemplateBuilder restTemplateBuilder;

		public IndexController(WebClient.Builder webClientBuilder,
							   RestTemplateBuilder restTemplateBuilder) {
			this.webClientBuilder = webClientBuilder;
			this.restTemplateBuilder = restTemplateBuilder;
		}

		@GetMapping("/webclient")
	    public String webclient(@RequestHeader(value = "X-TIMEOUT-MILLIS", required = false) String xTimeoutMillis) {
			WebClient webClient = webClientBuilder.build();
			if (xTimeoutMillis != null) {
				HttpClient httpClient = HttpClient
						.create()
						.responseTimeout(Duration.ofMillis(Long.parseLong(xTimeoutMillis)));
				webClient = webClientBuilder
						.clientConnector(new ReactorClientHttpConnector(httpClient))
						.build();
			}
			return webClient.get()
					.uri("http://localhost:9090/")
					.retrieve()
					.bodyToMono(String.class)
					.onErrorMap(WebClientRequestException.class,
							(WebClientRequestException webClientRequestException) -> {
						if (webClientRequestException.getCause() instanceof ReadTimeoutException) {
							return webClientRequestException.getCause();
						}
						return webClientRequestException;
					})
					.block();
	    }

		@GetMapping("/resttemplate")
		public String resttemplate(@RequestHeader(value = "X-TIMEOUT-MILLIS", required = false) String xTimeoutMillis) {
			RestTemplate restTemplate = restTemplateBuilder.build();
			if (xTimeoutMillis != null) {
				restTemplate = restTemplateBuilder
						.setReadTimeout(Duration.ofMillis(Long.parseLong(xTimeoutMillis)))
						.build();
			}
			return restTemplate.getForObject("http://localhost:9090/", String.class);
		}

		@ExceptionHandler({ReadTimeoutException.class, SocketTimeoutException.class})
		public ResponseEntity<String> handleException() {
			return ResponseEntity
					.status(HttpStatus.GATEWAY_TIMEOUT)
					.body(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase());
		}
	}
}

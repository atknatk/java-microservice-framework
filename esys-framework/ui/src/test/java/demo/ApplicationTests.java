package demo;


//@RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "security.user.password:foo")
public class ApplicationTests {

/*	@LocalServerPort
	private int port;

	private TestRestTemplate template = new TestRestTemplate();*/
/*
	@Test
	public void homePageLoads() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void userEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/user", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	public void resourceEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/resource", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	public void loginSucceeds() {
		TestRestTemplate template = new TestRestTemplate("user", "foo");
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/user", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}*/

}

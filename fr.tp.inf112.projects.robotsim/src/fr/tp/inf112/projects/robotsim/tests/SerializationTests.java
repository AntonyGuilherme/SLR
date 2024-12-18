package fr.tp.inf112.projects.robotsim.tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import fr.tp.inf112.projects.robotsim.infrasturcture.FactorySerialyzer;
import fr.tp.inf112.projects.robotsim.model.Factory;

public class SerializationTests {

	@Test
	public void factoryShouldBeSerialized() throws JsonMappingException, JsonProcessingException {
		final FactorySerialyzer serialyzer = new FactorySerialyzer();
		
		final Factory factory = FactorySerialyzer.createDefaultFactory();		
		final String factoryAsJsonString = serialyzer.toJSON(factory);		
		final Factory roundTrip = serialyzer.createFactoryFrom(factoryAsJsonString);
		
		assertEquals(serialyzer.toJSON(factory), serialyzer.toJSON(roundTrip));
	}
	
	@Test
	public void shouldSerializedOpenPropertyInDoors() {
		FactorySerialyzer serialyzer = new FactorySerialyzer();
		Factory factory = FactorySerialyzer.createDefaultFactory();
		
		String json = serialyzer.toJSON(factory);
		
		assertTrue(json.contains("open"));
	}
	
	@Test
	public void shouldSerializedTargetComponetsInRobots() {
		FactorySerialyzer serialyzer = new FactorySerialyzer();
		Factory factory = FactorySerialyzer.createDefaultFactory();
		
		String json = serialyzer.toJSON(factory);
		
		assertTrue(json.contains("targetComponents"));
	}
	
	@Test
	public void shouldSerializedInRobots() {
		FactorySerialyzer serialyzer = new FactorySerialyzer();
		Factory factory = FactorySerialyzer.createDefaultFactory();
		
		String json = serialyzer.toJSON(factory);
		
		assertTrue(json.contains("battery"));
		assertTrue(json.contains("speed"));
		assertTrue(json.contains("charging"));
		assertTrue(json.contains("radius"));
	}
}

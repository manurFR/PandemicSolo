package pandemic;

import java.io.InputStream;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pandemic.util.GenericResourceProvider;
import pandemic.util.ResourceProvider;

public class TestGenericResourceProvider {

	ResourceProvider provider;
	
	@Before
	public void setUp() {
		provider = new GenericResourceProvider();
	}
	
	@Test
	public void testGetIcon() {
		ImageIcon icon = provider.getIcon("card0.jpg");
		
		Assert.assertNotNull(icon);
		Assert.assertTrue(icon.getDescription().endsWith("images/card0.jpg"));
	}
	
	@Test
	public void testGetAudioStream() {
		InputStream stream = provider.getAudioStream("effect1.wav");
		
		Assert.assertNotNull(stream);
	}	
	
	@Test
	public void testGetBundle() {
		ResourceBundle bundle = provider.getBundle("componentsCoordinates");
		
		Assert.assertNotNull(bundle);
	}
	
}

package pandemic;

import org.junit.Before;
import org.junit.Test;

import pandemic.dialog.ConfigDialog;
import pandemic.model.DifficultyLevel;
import pandemic.model.Expansion;
import pandemic.util.GameConfig;

import static org.junit.Assert.*;

public class TestConfigDialog {

	GameConfig config;
	ConfigDialog dialog;
	
	@Before
	public void setUp() {
		dialog = new ConfigDialog(null);
		config = GameConfig.defaultConfigFactory();
		dialog.setModelConfig(config);
		dialog.viewRefresh();
	}
	
	@Test
	public void testNbOfRoles() {
		dialog.comboBoxNbOfRoles.setSelectedItem(3);
		dialog.controllerValidateAndClose();
		assertEquals(3, config.getNbOfRoles());
	}
	
	@Test
	public void testUseNonCoreRoles() {
		dialog.chckbxRolesStateOfEmergency.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.getRolesExpansions().contains(Expansion.STATE_OF_EMERGENCY));
	}
	
	@Test
	public void testDifficultyLevel() {
		dialog.comboBoxDifficultyLevel.setSelectedItem(DifficultyLevel.LEGENDARY);
		dialog.controllerValidateAndClose();
		assertEquals(DifficultyLevel.LEGENDARY, config.getDifficultyLevel());
	}
	
	@Test
	public void testUseNonCoreExpansionEventCards() {
		dialog.chckbxEventsInTheLab.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.getEventCardsExpansions().contains(Expansion.IN_THE_LAB));
	}

	@Test
	public void testVirulentStrain() {
		dialog.chckbxVirulentStrain.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.isPlayVirulentStrain());
	}
	
	@Test
	public void testMutation() {
		dialog.chckbxMutation.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.isPlayMutation());
	}
	
}

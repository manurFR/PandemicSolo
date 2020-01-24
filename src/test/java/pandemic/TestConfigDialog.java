package pandemic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pandemic.dialog.ConfigDialog;
import pandemic.model.DifficultyLevel;
import pandemic.util.GameConfig;

public class TestConfigDialog {

	GameConfig config;
	ConfigDialog dialog;
	
	@Before
	public void setUp() throws Exception {
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
	public void testAddNewRoles() {
		dialog.chckbxAddNewRoles.setSelected(false);
		dialog.controllerValidateAndClose();
		assertTrue(!config.isUseAllRoles());
	}
	
	@Test
	public void testDifficultyLevel() {
		dialog.comboBoxDifficultyLevel.setSelectedItem(DifficultyLevel.LEGENDARY);
		dialog.controllerValidateAndClose();
		assertEquals(DifficultyLevel.LEGENDARY, config.getDifficultyLevel());
	}
	
	@Test
	public void testUseNewSpecialEvents() {
		dialog.chckbxNewSpecialEvents.setSelected(false);
		dialog.controllerValidateAndClose();
		assertTrue(!config.isUseNewSpecialEvents());
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

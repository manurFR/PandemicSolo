package pandemic.dialog;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import pandemic.dialog.ConfigDialog;
import pandemic.model.DifficultyLevel;
import pandemic.model.Expansion;
import pandemic.util.GameConfig;
import pandemic.util.ResourceProvider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestConfigDialog {

	GameConfig config;
	ConfigDialog dialog;
	
	@Before
	public void setUp() {
		dialog = spy(new ConfigDialog(null));
		config = GameConfig.defaultConfigFactory();
		dialog.setModelConfig(config);
		dialog.viewRefresh();
	}
	
	@Test
	public void testNbOfRoles() {
		dialog.comboBoxNbOfRoles.setSelectedItem(3);
		dialog.controllerValidateAndClose();
		assertEquals(3, config.getNbOfRoles());
		verify(dialog).dispose();
	}
	
	@Test
	public void testRolesExpansions() {
		dialog.lblRolesWarning.setVisible(false);
		dialog.chckbxRolesCore.setSelected(true);
		dialog.chckbxRolesOnTheBrink.setSelected(false);
		dialog.chckbxRolesInTheLab.setSelected(false);
		dialog.chckbxRolesStateOfEmergency.setSelected(true);

		dialog.controllerValidateAndClose();

		assertEquals(2, config.getRolesExpansions().size());
		assertTrue(config.getRolesExpansions().contains(Expansion.CORE));
		assertTrue(config.getRolesExpansions().contains(Expansion.STATE_OF_EMERGENCY));
		assertFalse(dialog.lblRolesWarning.isVisible());
		verify(dialog).dispose();
	}

	@Test
	public void testDoNotValidateIfThereAreNoRoleExpansionsChecked() {
		dialog.lblRolesWarning.setVisible(false);
		dialog.chckbxRolesCore.setSelected(false);
		dialog.chckbxRolesOnTheBrink.setSelected(false);
		dialog.chckbxRolesInTheLab.setSelected(false);
		dialog.chckbxRolesStateOfEmergency.setSelected(false);

		dialog.controllerValidateAndClose();

		assertTrue(dialog.lblRolesWarning.isVisible());
		verify(dialog, never()).dispose();
	}

	@Test
	public void testDoNotValidateIfThereAreNotEnoughRoleExpansionsChecked() {
		dialog.lblRolesWarning.setVisible(false);
		dialog.comboBoxNbOfRoles.setSelectedItem(4);
		dialog.chckbxRolesCore.setSelected(false);
		dialog.chckbxRolesOnTheBrink.setSelected(false);
		dialog.chckbxRolesInTheLab.setSelected(false);
		dialog.chckbxRolesStateOfEmergency.setSelected(true); // only 3 roles in this expansion

		dialog.controllerValidateAndClose();

		assertTrue(dialog.lblRolesWarning.isVisible());
		verify(dialog, never()).dispose();
	}

	@Test
	public void testDifficultyLevel() {
		dialog.comboBoxDifficultyLevel.setSelectedItem(DifficultyLevel.LEGENDARY);
		dialog.controllerValidateAndClose();
		assertEquals(DifficultyLevel.LEGENDARY, config.getDifficultyLevel());
		verify(dialog).dispose();
	}

	@Test
	public void testNumberOfEventCards() {
		dialog.rbEventsAlwaysFive.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.isFiveEvents());
		verify(dialog).dispose();
	}
	
	@Test
	public void testUseNonCoreExpansionEventCards() {
		dialog.chckbxEventsInTheLab.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.getEventCardsExpansions().contains(Expansion.IN_THE_LAB));
		verify(dialog).dispose();
	}

	@Test
	public void testVirulentStrain() {
		dialog.chckbxVirulentStrain.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.isPlayVirulentStrain());
		verify(dialog).dispose();
	}
	
	@Test
	public void testMutation() {
		dialog.chckbxMutation.setSelected(true);
		dialog.controllerValidateAndClose();
		assertTrue(config.isPlayMutation());
		verify(dialog).dispose();
	}
	
}

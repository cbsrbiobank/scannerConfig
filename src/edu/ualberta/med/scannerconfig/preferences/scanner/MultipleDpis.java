package edu.ualberta.med.scannerconfig.preferences.scanner;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.ualberta.med.scannerconfig.ScannerConfigPlugin;
import edu.ualberta.med.scannerconfig.preferences.PreferenceConstants;
import edu.ualberta.med.scannerconfig.scanlib.ScanLib;
import edu.ualberta.med.scannerconfig.widgets.AdvancedRadioGroupFieldEditor;

public class MultipleDpis extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public MultipleDpis() {
		super(GRID);
		setPreferenceStore(ScannerConfigPlugin.getDefault()
				.getPreferenceStore());
	}

	@Override
	public void createFieldEditors() {
		AdvancedRadioGroupFieldEditor rgFe;
		int scannerCap = ScanLib.getInstance().slGetScannerCapability();
		for (int i = 0; i < 3; ++i) {
			rgFe = new AdvancedRadioGroupFieldEditor(
					PreferenceConstants.SCANNER_MULTIPLE_DPIS[i], "DPI "
							+ (i + 1), 5, new String[][] { { "300", "300" },
							{ "400", "400" }, { "600", "600" } },
					getFieldEditorParent(), true);
			
			rgFe.setEnabledArray(new boolean[] {
					(scannerCap & ScanLib.CAP_DPI_300) != 0,
					(scannerCap & ScanLib.CAP_DPI_400) != 0,
					(scannerCap & ScanLib.CAP_DPI_600) != 0},
					getFieldEditorParent());
			
			addField(rgFe);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}

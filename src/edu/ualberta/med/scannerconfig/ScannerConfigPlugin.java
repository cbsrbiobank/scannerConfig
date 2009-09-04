package edu.ualberta.med.scannerconfig;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.ualberta.med.scanlib.ScanCell;
import edu.ualberta.med.scanlib.ScanLib;
import edu.ualberta.med.scannerconfig.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class ScannerConfigPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "scannerConfig";

    // The shared instance
    private static ScannerConfigPlugin plugin;

    /**
     * The constructor
     */
    public ScannerConfigPlugin() {
        String osname = System.getProperty("os.name");
        if (osname.startsWith("Windows")) {
            System.loadLibrary("scanlib");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static ScannerConfigPlugin getDefault() {
        return plugin;
    }

    public static ScanCell[][] scan(int plateNumber) throws Exception {
        String dpiString = getDefault().getPreferenceStore().getString(
            PreferenceConstants.SCANNER_DPI);
        if (dpiString.length() == 0) {
            throw new Exception("bad value in preferences for scanner DPI");
        }
        int dpi = Integer.valueOf(dpiString);
        int res = ScanLib.getInstance().slDecodePlate(dpi, plateNumber);
        if (res < ScanLib.SC_SUCCESS) {
            throw new Exception("Could not decode image. "
                + ScanLib.getErrMsg(res));
        }
        return ScanCell.getScanLibResults();
    }
}

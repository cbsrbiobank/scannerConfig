package edu.ualberta.med.scannerconfig.preferences.scanner;

import java.io.File;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import edu.ualberta.med.scannerconfig.ScannerConfigPlugin;
import edu.ualberta.med.scannerconfig.dmscanlib.ScanLib;
import edu.ualberta.med.scannerconfig.preferences.PreferenceConstants;

public class PlateImage {
    private static PlateImage instance = null;

    /* please note that PALLET_IMAGE_DPI may change value */
    public static double PLATE_IMAGE_DPI = 300.0;

    public static final String PALLET_IMAGE_FILE = "plates.bmp";

    protected ListenerList listenerList = new ListenerList();

    private Image scannedImage;

    protected PlateImage() {
        cleanAll();
    }

    public static PlateImage instance() {
        if (instance == null) {
            instance = new PlateImage();
        }
        return instance;
    }

    public boolean exists() {
        return (scannedImage != null);
    }

    public Image getScannedImage() {
        return scannedImage;
    }

    public void cleanAll() {
        final File platesFile = new File(PlateImage.PALLET_IMAGE_FILE);
        platesFile.delete();

        if (scannedImage != null) {
            scannedImage.dispose();
            scannedImage = null;
        }
    }

    public void scanPlateImage() {
        int brightness =
            ScannerConfigPlugin.getDefault().getPreferenceStore()
                .getInt(PreferenceConstants.SCANNER_BRIGHTNESS);
        int contrast =
            ScannerConfigPlugin.getDefault().getPreferenceStore()
                .getInt(PreferenceConstants.SCANNER_CONTRAST);
        int debugLevel =
            ScannerConfigPlugin.getDefault().getPreferenceStore()
                .getInt(PreferenceConstants.DLL_DEBUG_LEVEL);

        cleanAll();
        final int result =
            ScanLib.getInstance().slScanImage(debugLevel,
                (int) PlateImage.PLATE_IMAGE_DPI, brightness, contrast, 0, 0,
                20, 20, PlateImage.PALLET_IMAGE_FILE);

        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
            .getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (result != ScanLib.SC_SUCCESS) {
                        MessageDialog.openError(PlatformUI.getWorkbench()
                            .getActiveWorkbenchWindow().getShell(),
                            "Scanner error", ScanLib.getErrMsg(result));
                        return;
                    }
                    Assert.isTrue((new File(PlateImage.PALLET_IMAGE_FILE))
                        .exists());
                    scannedImage =
                        new Image(
                            PlatformUI.getWorkbench()
                                .getActiveWorkbenchWindow().getShell()
                                .getDisplay(), PlateImage.PALLET_IMAGE_FILE);
                    notifyListeners();
                }
            });
    }

    public void addScannedImageChangeListener(PlateImageListener listener) {
        listenerList.add(listener);
    }

    public void removeScannedImageChangeListener(PlateImageListener listener) {
        listenerList.remove(listener);
    }

    private void notifyListeners() {
        Object[] listeners = listenerList.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final PlateImageListener l = (PlateImageListener) listeners[i];
            SafeRunnable.run(new SafeRunnable() {
                @Override
                public void run() {
                    l.newImage();
                }
            });
        }
    }
}

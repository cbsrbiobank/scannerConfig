package edu.ualberta.med.scannerconfig.dialogs;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import edu.ualberta.med.biobank.gui.common.dialogs.PersistedDialog;
import edu.ualberta.med.biobank.gui.common.events.SelectionListener;
import edu.ualberta.med.biobank.gui.common.widgets.Event;
import edu.ualberta.med.scannerconfig.BarcodeImage;
import edu.ualberta.med.scannerconfig.BarcodePosition;
import edu.ualberta.med.scannerconfig.ImageSource;
import edu.ualberta.med.scannerconfig.PlateDimensions;
import edu.ualberta.med.scannerconfig.PlateOrientation;
import edu.ualberta.med.scannerconfig.ScanPlate;
import edu.ualberta.med.scannerconfig.ScannerConfigPlugin;
import edu.ualberta.med.scannerconfig.dmscanlib.CellRectangle;
import edu.ualberta.med.scannerconfig.dmscanlib.DecodeOptions;
import edu.ualberta.med.scannerconfig.dmscanlib.DecodeResult;
import edu.ualberta.med.scannerconfig.dmscanlib.DecodedWell;
import edu.ualberta.med.scannerconfig.dmscanlib.ScanLib;
import edu.ualberta.med.scannerconfig.dmscanlib.ScanLibResult;
import edu.ualberta.med.scannerconfig.imageregion.Swt2DUtil;
import edu.ualberta.med.scannerconfig.preferences.PreferenceConstants;
import edu.ualberta.med.scannerconfig.preferences.scanner.ScannerDpi;
import edu.ualberta.med.scannerconfig.widgets.ImageSourceAction;
import edu.ualberta.med.scannerconfig.widgets.ImageSourceWidget;
import edu.ualberta.med.scannerconfig.widgets.imageregion.PlateGridWidget;

/**
 * A dialog box that is used to project a grid on top of a scanned plate image. The grid is then
 * used to provide regions where 2D barcodes will be searched for and if found decoded.
 * 
 * Allows the user to aquire an image from a flatbed scanner or to import an image from a file.
 * 
 * @author nelson
 * 
 */
public class DecodeImageDialog extends PersistedDialog implements SelectionListener {

    private static final I18n i18n = I18nFactory.getI18n(DecodeImageDialog.class);

    private static Logger log = LoggerFactory.getLogger(DecodeImageDialog.class);

    public enum ScanMode {
        SCAN,
        RESCAN;
    }

    private static final String SCANNING_DIALOG_SETTINGS =
        DecodeImageDialog.class.getSimpleName() + "_SETTINGS";

    private static final int CONTROLS_MIN_WIDTH = 160;

    @SuppressWarnings("nls")
    private static final String TITLE = i18n.tr("Decode pallet");

    private static final String TITLE_AREA_MESSAGE_SELECT_PLATE =
        i18n.tr("Select the options to match the image you are decoding");

    @SuppressWarnings("nls")
    private static final String TITLE_AREA_MESSAGE_ADJUST_GRID =
        i18n.tr("Adjust the grid to the barcodes contained in the image.");

    @SuppressWarnings("nls")
    private static final String TITLE_AREA_MESSAGE_DECODING_COMPLETED =
        i18n.tr("Decoding completed.");

    private static final String PROGRESS_MESSAGE_SCANNING =
        i18n.tr("Retrieving image from the flatbed scanner...");

    private static final String PROGRESS_MESSAGE_DECODING =
        i18n.tr("Decoding barcodes in image...");

    private ImageSourceWidget imageSourceWidget;

    private PlateGridWidget plateGridWidget;

    private BarcodeImage imageToDecode;

    private final List<PlateDimensions> validPlateDimensions;

    private ScanMode scanMode;

    private Button decodeButton;

    private DecodeResult result;

    /**
     * Use this constructor to limit the valid plate dimensions the user can choose from.
     * 
     * @param parentShell
     * @param validPlateDimensions
     */
    public DecodeImageDialog(Shell parentShell, List<PlateDimensions> validPlateDimensions) {
        super(parentShell);
        this.validPlateDimensions = validPlateDimensions;
    }

    /**
     * Use this constructor to allow any plate dimension defined in {@link PlateDimensions}.
     * 
     * @param parentShell
     */
    public DecodeImageDialog(Shell parentShell) {
        this(parentShell, Arrays.asList(PlateDimensions.values()));

    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = super.getDialogSettings();
        IDialogSettings section = settings.getSection(SCANNING_DIALOG_SETTINGS);
        if (section == null) {
            section = settings.addNewSection(SCANNING_DIALOG_SETTINGS);
        }
        return section;
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return getDialogSettings();
    }

    @Override
    protected String getTitleAreaMessage() {
        return TITLE_AREA_MESSAGE_SELECT_PLATE;
    }

    @Override
    protected String getTitleAreaTitle() {
        return TITLE;
    }

    @Override
    protected String getDialogShellTitle() {
        return TITLE;
    }

    @Override
    protected void createDialogAreaInternal(Composite parent) throws Exception {
        final Composite contents = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        contents.setLayout(layout);

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        contents.setLayoutData(gd);
        createControls(contents);
        createImageControl(contents);
    }

    private void createControls(Composite parent) {
        imageSourceWidget = new ImageSourceWidget(parent, CONTROLS_MIN_WIDTH,
            widgetCreator, getDialogSettings(), validPlateDimensions);
        imageSourceWidget.addSelectionListener(this);

        decodeButton = createDecodeButton(imageSourceWidget);
        decodeButton.setEnabled(false);
    }

    private Button createDecodeButton(Composite parent) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(i18n.tr("Decode"));
        button.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                decode();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        return button;
    }

    private void createImageControl(Composite parent) {
        plateGridWidget = new PlateGridWidget(parent);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, i18n.tr("Done"), false);
    }

    /*
     * Called when something is selected on one of the sub widgets.
     * 
     * (non-Javadoc)
     * 
     * @see
     * edu.ualberta.med.biobank.gui.common.events.SelectionListener#widgetSelected(edu.ualberta.
     * med.biobank.gui.common.widgets.Event)
     */
    @Override
    public void widgetSelected(Event e) {
        ImageSourceAction imageSourceAction = ImageSourceAction.getFromId(e.detail);

        switch (imageSourceAction) {
        case IMAGE_SOURCE_CHANGED:
            setMessage(TITLE_AREA_MESSAGE_SELECT_PLATE);
            removeImage();
            break;

        case PLATE_ORIENTATION:
            PlateOrientation orientation = (PlateOrientation) e.data;
            plateGridWidget.setPlateOrientation(orientation);
            break;

        case PLATE_DIMENSIONS:
            PlateDimensions dimensions = (PlateDimensions) e.data;
            plateGridWidget.setPlateDimensions(dimensions);
            break;

        case BARCODE_POSITION:
            BarcodePosition barcodePosition = (BarcodePosition) e.data;
            plateGridWidget.setBarcodePosition(barcodePosition);
            break;

        case SCAN:
            saveGridRectangle();
            removeDecodeInfo();
            scanMode = ScanMode.SCAN;
            scanPlate(e);
            break;

        case RESCAN:
            saveGridRectangle();
            scanMode = ScanMode.RESCAN;
            scanPlate(e);
            break;

        case FILENAME:
            removeImage();
            String filename = (String) e.data;
            scanMode = ScanMode.SCAN;
            loadFile(filename, ImageSource.FILE);
            break;

        default:
            throw new IllegalArgumentException("invalid image source action: " + imageSourceAction);
        }
    }

    private void scanPlate(Event e) {
        final ImageSource imageSource = (ImageSource) e.data;
        final ScanPlate plateToScan = imageSource.getScanPlate();
        final String filename = imageSourceWidget.getFileName();
        final ScannerDpi dpi = imageSourceWidget.getDpi();
        final Display display = Display.getDefault();

        IRunnableWithProgress op = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.beginTask(PROGRESS_MESSAGE_SCANNING, IProgressMonitor.UNKNOWN);
                ScanLibResult.Result result = ScannerConfigPlugin.scanPlate(
                    plateToScan, filename, dpi);

                if ((result == ScanLibResult.Result.SUCCESS)
                    || ((result == ScanLibResult.Result.FAIL)
                    && imageSourceWidget.haveFakePlateImage())) {
                    if ((result == ScanLibResult.Result.FAIL) &&
                        imageSourceWidget.haveFakePlateImage()) {
                        // fake scanning an image
                        // try {
                        // Thread.sleep(4000);
                        // } catch (InterruptedException e) {
                        // e.printStackTrace();
                        // }
                    }

                    display.asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            loadFile(filename, imageSource);
                        }
                    });
                } else {
                    display.asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            setMessage(i18n.tr("Could not scan the plate region"), IMessageProvider.ERROR);
                        }
                    });
                }
                monitor.done();
            }
        };

        try {
            new ProgressMonitorDialog(PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getShell()).run(true, false, op);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadFile(String filename, ImageSource imageSource) {
        setMessage(TITLE_AREA_MESSAGE_ADJUST_GRID, IMessageProvider.NONE);
        imageToDecode = new BarcodeImage(filename, imageSource);
        Rectangle2D.Double gridRectangle = (Rectangle2D.Double) imageSourceWidget.getGridRectangle().clone();

        // gridRectangle must fit inside the image rectangle
        if ((gridRectangle.x < 0) || !imageToDecode.contains(gridRectangle)) {
            // needs initialization
            AffineTransform t = AffineTransform.getScaleInstance(0.98, 0.98);
            Rectangle2D.Double rectangle = imageToDecode.getRectangle();
            gridRectangle = Swt2DUtil.transformRect(t, rectangle);
            log.trace("loadFile: initialization for gridRect");
        }

        decodeButton.setEnabled(true);
        plateGridWidget.updateImage(
            imageToDecode,
            gridRectangle,
            imageSourceWidget.getPlateOrientation(),
            imageSourceWidget.getPlateDimensions(),
            imageSourceWidget.getBarcodePosition());
    }

    private void removeImage() {
        imageToDecode = null;
        plateGridWidget.removeImage();
        decodeButton.setEnabled(false);
        removeDecodeInfo();
    }

    private void removeDecodeInfo() {
        plateGridWidget.removeDecodeInfo();
    }

    // FIXME: decode button should be disabled if there is no image loaded
    private void decode() {
        IPreferenceStore prefs = ScannerConfigPlugin.getDefault().getPreferenceStore();
        final int debugLevel = prefs.getInt(PreferenceConstants.DLL_DEBUG_LEVEL);
        final int edgeThresh = prefs.getInt(PreferenceConstants.LIBDMTX_EDGE_THRESH);
        final double scanGap = prefs.getDouble(PreferenceConstants.LIBDMTX_SCAN_GAP);
        final int squareDev = prefs.getInt(PreferenceConstants.LIBDMTX_SQUARE_DEV);
        final int corrections = prefs.getInt(PreferenceConstants.LIBDMTX_CORRECTIONS);
        final String filename = imageSourceWidget.getFileName();

        saveGridRectangle();
        plateGridWidget.removeDecodeInfo();

        final Display display = Display.getDefault();

        IRunnableWithProgress op = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.beginTask(PROGRESS_MESSAGE_DECODING, IProgressMonitor.UNKNOWN);
                Set<CellRectangle> wells = plateGridWidget.getCellsInInches();

                result = ScanLib.getInstance().decodeImage(
                    debugLevel,
                    filename,
                    new DecodeOptions(scanGap, squareDev, edgeThresh, corrections, 1),
                    wells.toArray(new CellRectangle[] {}));

                // log.debug("createDecodeButton: tubes decoded: {}",
                // result.getDecodedWells().size());

                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        setMessage(TITLE_AREA_MESSAGE_DECODING_COMPLETED, IMessageProvider.NONE);
                        plateGridWidget.setDecodedWells(result.getDecodedWells(), scanMode);
                    }
                });
                monitor.done();
            }
        };

        try {
            new ProgressMonitorDialog(PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getShell()).run(true, false, op);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveGridRectangle() {
        if (imageToDecode != null) {
            Rectangle2D.Double gridRegion = plateGridWidget.getUserRegionInInches();
            if (gridRegion == null) {
                throw new IllegalStateException("grid region is null");
            }
            imageSourceWidget.setGridRectangle(gridRegion);
        }
    }

    @Override
    protected void okPressed() {
        if (imageToDecode != null) {
            saveGridRectangle();
        }
        imageSourceWidget.saveSettings();
        super.okPressed();
    }

    public Set<DecodedWell> getDecodeResult() {
        if (result == null) {
            throw new IllegalStateException("decode result is null");
        }
        return result.getDecodedWells();
    }
}
package edu.ualberta.med.scannerconfig.preferences;

@SuppressWarnings("nls")
public class PreferenceConstants {

    public static final String SCANNER_DPI = "scanner.dpi";

    public static final String SCANNER_300_DPI = "300";

    public static final String SCANNER_400_DPI = "400";

    public static final String SCANNER_600_DPI = "600";

    public static final String SCANNER_BRIGHTNESS = "scanner.brightness";

    public static final String SCANNER_CONTRAST = "scanner.contrast";

    public static final String DLL_DEBUG_LEVEL = "scanner.dll_debug_level";

    public static final String SCANNER_DRV_TYPE = "scanner.drvtype";

    public static final String SCANNER_DRV_TYPE_NONE = "scanner.drvtype.none";

    public static final String SCANNER_DRV_TYPE_WIA = "scanner.drvtype.wia";

    public static final String SCANNER_DRV_TYPE_TWAIN = "scanner.drvtype.twain";

    public static final String LIBDMTX_EDGE_THRESH = "libdmtx.edge_thresh";

    public static final String LIBDMTX_SCAN_GAP = "libdmtx.scan_gap";

    public static final String LIBDMTX_SQUARE_DEV = "libdmtx.square_dev";

    public static final String LIBDMTX_CORRECTIONS = "libdmtx.corrections";

    public static final String SCANNER_PALLET_PROFILES = "scanner.pallet.profiles";

    public static final String[] SCANNER_PALLET_ENABLED = {
        "scanner.plate.coords.enabled.1", "scanner.plate.coords.enabled.2",
        "scanner.plate.coords.enabled.3", "scanner.plate.coords.enabled.4",
        "scanner.plate.coords.enabled.5" };

    public static final String[][] SCANNER_PALLET_CONFIG = {
        { "scanner.plate.coords.left.1", "scanner.plate.coords.top.1",
            "scanner.plate.coords.right.1", "scanner.plate.coords.bottom.1",
            "scanner.plate.coords.gapx.1", "scanner.plate.coords.gapy.1" },

        { "scanner.plate.coords.left.2", "scanner.plate.coords.top.2",
            "scanner.plate.coords.right.2", "scanner.plate.coords.bottom.2",
            "scanner.plate.coords.gapx.2", "scanner.plate.coords.gapy.2" },

        { "scanner.plate.coords.left.3", "scanner.plate.coords.top.3",
            "scanner.plate.coords.right.3", "scanner.plate.coords.bottom.3",
            "scanner.plate.coords.gapx.3", "scanner.plate.coords.gapy.3" },

        { "scanner.plate.coords.left.4", "scanner.plate.coords.top.4",
            "scanner.plate.coords.right.4", "scanner.plate.coords.bottom.4",
            "scanner.plate.coords.gapx.4", "scanner.plate.coords.gapy.4" },

        { "scanner.plate.coords.left.5", "scanner.plate.coords.top.5",
            "scanner.plate.coords.right.5", "scanner.plate.coords.bottom.5",
            "scanner.plate.coords.gapx.5", "scanner.plate.coords.gapy.5" } };

    public static final String[] SCANNER_PALLET_ORIENTATION = {
        "scanner.plate.orientation.1", "scanner.plate.orientation.2",
        "scanner.plate.orientation.3", "scanner.plate.orientation.4",
        "scanner.plate.orientation.5" };

    public static final String[] SCANNER_PALLET_GRID_DIMENSIONS = {
        "scanner.plate.griddimensions.1", "scanner.plate.griddimensions.2",
        "scanner.plate.griddimensions.3", "scanner.plate.griddimensions.4",
        "scanner.plate.griddimensions.5" };

    public static final String SCANNER_PALLET_ORIENTATION_LANDSCAPE = "Landscape";

    public static final String SCANNER_PALLET_ORIENTATION_PORTRAIT = "Portrait";

    public static final String SCANNER_PALLET_GRID_DIMENSIONS_ROWS8COLS12 = "8x12";

    public static final String SCANNER_PALLET_GRID_DIMENSIONS_ROWS10COLS10 = "10x10";

    public static final String[] SCANNER_PALLET_GRID_DIMENSIONS_ROWSCOLS = {
        SCANNER_PALLET_GRID_DIMENSIONS_ROWS8COLS12,
        SCANNER_PALLET_GRID_DIMENSIONS_ROWS10COLS10
    };

    public static final String[] SCANNER_PLATE_BARCODES = {
        "scanner.plate.barcode.1", "scanner.plate.barcode.2",
        "scanner.plate.barcode.3", "scanner.plate.barcode.4",
        "scanner.plate.barcode.5" };

    public static final String SCANNER_PLATE_SHOW_BARCODE_PREF = "scanner.plate.show.barcode.pref";

    public static int gridRows(String gridDimensions) {
        if (gridDimensions.equals(SCANNER_PALLET_GRID_DIMENSIONS_ROWS8COLS12)) return 8;
        else if (gridDimensions.equals(SCANNER_PALLET_GRID_DIMENSIONS_ROWS10COLS10)) return 10;
        else return -1;
    }

    public static int gridRows(String gridDimensions, String orientation) {
        if (orientation.equals(SCANNER_PALLET_ORIENTATION_LANDSCAPE)) return gridRows(gridDimensions);
        else if (orientation.equals(SCANNER_PALLET_ORIENTATION_PORTRAIT)) return gridCols(gridDimensions);
        else return -1;
    }

    public static int gridCols(String gridDimensions) {
        if (gridDimensions.equals(SCANNER_PALLET_GRID_DIMENSIONS_ROWS8COLS12)) return 12;
        else if (gridDimensions.equals(SCANNER_PALLET_GRID_DIMENSIONS_ROWS10COLS10)) return 10;
        else return -1;
    }

    public static int gridCols(String gridDimensions, String orientation) {
        if (orientation.equals(SCANNER_PALLET_ORIENTATION_LANDSCAPE)) return gridCols(gridDimensions);
        else if (orientation.equals(SCANNER_PALLET_ORIENTATION_PORTRAIT)) return gridRows(gridDimensions);
        else return -1;
    }
}

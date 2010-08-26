/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.ualberta.med.scannerconfig.dmscanlib;

public class DmScanLibWin32Wrapper {
  public static int slIsTwainAvailable() {
    return DmScanLibWin32WrapperJNI.slIsTwainAvailable();
  }

  public static int slSelectSourceAsDefault() {
    return DmScanLibWin32WrapperJNI.slSelectSourceAsDefault();
  }

  public static int slGetScannerCapability() {
    return DmScanLibWin32WrapperJNI.slGetScannerCapability();
  }

  public static int slScanImage(long verbose, long dpi, int brightness, int contrast, double left, double top, double right, double bottom, String filename) {
    return DmScanLibWin32WrapperJNI.slScanImage(verbose, dpi, brightness, contrast, left, top, right, bottom, filename);
  }

  public static int slDecodePlate(long verbose, long dpi, int brightness, int contrast, long plateNum, double left, double top, double right, double bottom, double scanGap, long squareDev, long edgeThresh, long corrections, double cellDistance, double gapX, double gapY, long profileA, long profileB, long profileC, long isHorizontal) {
    return DmScanLibWin32WrapperJNI.slDecodePlate(verbose, dpi, brightness, contrast, plateNum, left, top, right, bottom, scanGap, squareDev, edgeThresh, corrections, cellDistance, gapX, gapY, profileA, profileB, profileC, isHorizontal);
  }

  public static int slDecodeImage(long verbose, long plateNum, String filename, double scanGap, long squareDev, long edgeThresh, long corrections, double cellDistance, double gapX, double gapY, long profileA, long profileB, long profileC, long isHorizontal) {
    return DmScanLibWin32WrapperJNI.slDecodeImage(verbose, plateNum, filename, scanGap, squareDev, edgeThresh, corrections, cellDistance, gapX, gapY, profileA, profileB, profileC, isHorizontal);
  }

}

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.40
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.ualberta.med.scannerconfig.scanlib;

public class ScanLibWin32Wrapper {
  public static int slIsTwainAvailable() {
    return ScanLibWin32WrapperJNI.slIsTwainAvailable();
  }

  public static int slSelectSourceAsDefault() {
    return ScanLibWin32WrapperJNI.slSelectSourceAsDefault();
  }

  public static boolean slIsValidDpi(int dpi) {
    return ScanLibWin32WrapperJNI.slIsValidDpi(dpi);
  }

  public static int slScanImage(long verbose, long dpi, int brightness, int contrast, double left, double top, double right, double bottom, String filename) {
    return ScanLibWin32WrapperJNI.slScanImage(verbose, dpi, brightness, contrast, left, top, right, bottom, filename);
  }

  public static int slDecodePlate(long verbose, long dpi, int brightness, int contrast, long plateNum, double left, double top, double right, double bottom, double scanGap, long squareDev, long edgeThresh, long corrections, double cellDistance) {
    return ScanLibWin32WrapperJNI.slDecodePlate(verbose, dpi, brightness, contrast, plateNum, left, top, right, bottom, scanGap, squareDev, edgeThresh, corrections, cellDistance);
  }

  public static int slDecodePlateMultipleDpi(long verbose, long dpi1, long dpi2, long dpi3, int brightness, int contrast, long plateNum, double left, double top, double right, double bottom, double scanGap, long squareDev, long edgeThresh, long corrections, double cellDistance) {
    return ScanLibWin32WrapperJNI.slDecodePlateMultipleDpi(verbose, dpi1, dpi2, dpi3, brightness, contrast, plateNum, left, top, right, bottom, scanGap, squareDev, edgeThresh, corrections, cellDistance);
  }

  public static int slDecodeImage(long verbose, long plateNum, String filename, double scanGap, long squareDev, long edgeThresh, long corrections, double cellDistance) {
    return ScanLibWin32WrapperJNI.slDecodeImage(verbose, plateNum, filename, scanGap, squareDev, edgeThresh, corrections, cellDistance);
  }

}

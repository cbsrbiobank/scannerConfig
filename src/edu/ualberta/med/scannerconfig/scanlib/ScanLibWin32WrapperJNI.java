/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.40
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.ualberta.med.scannerconfig.scanlib;

class ScanLibWin32WrapperJNI {
  public final static native int slIsTwainAvailable();
  public final static native int slSelectSourceAsDefault();
  public final static native boolean slIsValidDpi(int jarg1);
  public final static native int slScanImage(long jarg1, long jarg2, int jarg3, int jarg4, double jarg5, double jarg6, double jarg7, double jarg8, String jarg9);
  public final static native int slDecodePlate(long jarg1, long jarg2, int jarg3, int jarg4, long jarg5, double jarg6, double jarg7, double jarg8, double jarg9, double jarg10, long jarg11, long jarg12, long jarg13, double jarg14);
  public final static native int slDecodePlateMultipleDpi(long jarg1, long jarg2, long jarg3, long jarg4, int jarg5, int jarg6, long jarg7, double jarg8, double jarg9, double jarg10, double jarg11, double jarg12, long jarg13, long jarg14, long jarg15, double jarg16);
  public final static native int slDecodeImage(long jarg1, long jarg2, String jarg3, double jarg4, long jarg5, long jarg6, long jarg7, double jarg8);
}

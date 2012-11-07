package edu.ualberta.med.scannerconfig.dmscanlib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ualberta.med.scannerconfig.ScannerConfigPlugin;

public class TestDmScanLibWindows {

    private static Logger log = LoggerFactory
        .getLogger(TestDmScanLibWindows.class);

    @Test
    public void scanImage() throws Exception {
        // this test is valid only when not running on windows
        Assert.assertEquals(true, LibraryLoader.getInstance()
            .runningMsWindows());

        ScanLib scanLib = ScanLib.getInstance();
        ScanLibResult r = scanLib.isTwainAvailable();
        Assert.assertEquals(ScanLib.SC_SUCCESS, r.getResultCode());

        BoundingBox region = new BoundingBox(new Point(0, 0),
            new Point(4, 4));

        final int dpi = 300;
        String filename = "tempscan.bmp";
        File file = new File(filename);
        file.delete(); // dont care if file doesn't exist

        r = scanLib.scanImage(0, dpi, 0, 0, region, filename);

        File imageFile = new File(filename);
        Assert
            .assertTrue(Math.abs(dpi - ImageInfo.getImageDpi(imageFile)) <= 1);

        BufferedImage image = ImageIO.read(imageFile);
        Assert.assertEquals(new Double(region.getWidth() * dpi).intValue(),
            image.getWidth());
        Assert.assertEquals(new Double(region.getHeight() * dpi).intValue(),
            image.getHeight());
    }

    @Test
    public void scanFlatbed() throws Exception {
        // this test is valid only when not running on windows
        Assert.assertEquals(true, LibraryLoader.getInstance()
            .runningMsWindows());

        ScanLib scanLib = ScanLib.getInstance();
        ScanLibResult r = scanLib.isTwainAvailable();
        Assert.assertEquals(ScanLib.SC_SUCCESS, r.getResultCode());

        final int dpi = 300;
        String filename = "flatbed.bmp";
        File file = new File(filename);
        file.delete(); // dont care if file doesn't exist

        r = scanLib.scanFlatbed(0, dpi, 0, 0, filename);

        File imageFile = new File(filename);
        Assert
            .assertTrue(Math.abs(dpi - ImageInfo.getImageDpi(imageFile)) <= 1);
    }

    @Test
    public void scanAndDecode() throws Exception {
        // this test is valid only when not running on windows
        Assert.assertEquals(true, LibraryLoader.getInstance()
            .runningMsWindows());

        ScanLib scanLib = ScanLib.getInstance();
        ScanLibResult r = scanLib.isTwainAvailable();
        Assert.assertEquals(ScanLib.SC_SUCCESS, r.getResultCode());

        BoundingBox scanRegion =
            new BoundingBox(new Point(0.400, 0.265), new Point(4.566, 3.020));

        BoundingBox wellsBbox =
            ScannerConfigPlugin.getWellsBoundingBox(scanRegion);
        BoundingBox scanBbox =
            ScannerConfigPlugin.getWiaBoundingBox(scanRegion);

        final int dpi = 300;

        Set<WellRectangle> wells =
            WellRectangle.getWellRectanglesForBoundingBox(
                wellsBbox, 8, 12, dpi);

        DecodeResult dr = scanLib.scanAndDecode(3, dpi, 0, 0, scanBbox,
            DecodeOptions.getDefaultDecodeOptions(),
            wells.toArray(new WellRectangle[] {}));

        Assert.assertNotNull(dr);
        Assert.assertTrue(dr.getDecodedWells().size() > 0);

        for (DecodedWell decodedWell : dr.getDecodedWells()) {
            log.debug("decoded well: {}", decodedWell);
        }

        log.debug("wells decoded: {}", dr.getDecodedWells().size());
    }
}
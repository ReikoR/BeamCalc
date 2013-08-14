import com.bc.ceres.core.ProgressMonitor;
//import com.bc.ceres.glevel.MultiLevelImage;
import org.esa.beam.framework.dataio.ProductIO;
//import org.esa.beam.framework.dataio.ProductReader;
import org.esa.beam.framework.dataio.ProductWriter;
import org.esa.beam.framework.datamodel.*;
import org.esa.beam.util.ProductUtils;
import org.esa.beam.util.SystemUtils;
import org.esa.beam.util.VersionChecker;
import org.esa.beam.util.logging.BeamLogManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class BeamTest {
    public static void calcIndex(String filePath) {
        try {
            BeamLogManager.removeRootLoggerHandlers();
            //System.out.println("\nLoading product: " + filePath);
            Product product = ProductIO.readProduct(filePath);

            //ProductIO.readProduct()

            int rasterWidth = product.getSceneRasterWidth();
            int rasterHeight = product.getSceneRasterHeight();

            //Product targetProduct = product;
            //Product targetProduct = new Product(new File(filePath + "_test.dim").getName(), "MER_FR__2P", rasterWidth, rasterHeight);

            ProductWriter productWriter = ProductIO.getProductWriter("BEAM-DIMAP");
            //ProductReader productReader = ProductIO.getProductReaderForInput(filePath);
            //targetProduct.setProductWriter(productWriter);
            //product.setProductReader(productReader);
            product.setProductWriter(productWriter);
            //productWriter.flush();

            productWriter.writeProductNodes(product, "out/" + filePath + ".dim");

            //System.out.println(productWriter.getOutput());

            //String[] bandNames = product.getBandNames();
            Band[] bands = product.getBands();
            for (int i = 0; i < bands.length; i++) {
                //System.out.println(bands[i].getName());
                System.out.print(".");
                //System.out.println(bandNames[i]);
                //ProductUtils.copyBand(bandNames[i], product, targetProduct, true);
                //bands[i].setSourceImage(product.getBand(bands[i].getName()).getSourceImage());
                //bands[i].fireProductNodeDataChanged();
                bands[i].writeRasterDataFully();
            }

            Band kd490 = new Band("kd_490", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            Band CY = new Band("CY", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            Band MCI = new Band("MCI", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            Band aPig = new Band("a_pig", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            Band c490 = new Band("c_490", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            Band secchiPhot = new Band("Secchi_phot", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            Band secchiBS = new Band("Secchi_BS", ProductData.TYPE_FLOAT64, rasterWidth, rasterHeight);
            //targetProduct.addBand(kd_490);
            product.addBand(kd490);
            product.addBand(CY);
            product.addBand(MCI);
            product.addBand(aPig);
            product.addBand(c490);
            product.addBand(secchiPhot);
            product.addBand(secchiBS);

            productWriter.writeProductNodes(product, "out/" + filePath + ".dim");

            //ProductUtils.copyTiePointGrids(product, targetProduct);
            //ProductUtils.copyGeoCoding(product, targetProduct);
            //ProductUtils.copyMetadata(product, targetProduct);


            //ProductUtils.copyBand("l2_flags", product, targetProduct, true);


            //productWriter.writeProductNodes(product, "test.dim");

            //targetBand.setSourceImage(product.getBand("l2_flags").getSourceImage());
            //targetBand.writePixels(0, 0, targetBand.getSceneRasterWidth(), targetBand.getSceneRasterHeight(), product.getBand("l2_flags").getSourceImage().getData());


            Band reflec3Band = product.getBand("reflec_3");
            Band reflec5Band = product.getBand("reflec_5");
            Band reflec7Band = product.getBand("reflec_7");
            Band reflec8Band = product.getBand("reflec_8");
            Band reflec9Band = product.getBand("reflec_9");
            Band reflec10Band = product.getBand("reflec_10");
            Band algal2Band = product.getBand("algal_2");
            Band yellowSubsBand = product.getBand("yellow_subs");
            Band totalSuspBand = product.getBand("total_susp");
           // Band l2Flags = product.getBand("l2_flags");
            Raster reflec3 = reflec3Band.getSourceImage().getData();
            Raster reflec5 = reflec5Band.getSourceImage().getData();
            Raster reflec7 = reflec7Band.getSourceImage().getData();
            Raster reflec8 = reflec8Band.getSourceImage().getData();
            Raster reflec9 = reflec9Band.getSourceImage().getData();
            Raster reflec10 = reflec10Band.getSourceImage().getData();
            Raster algal_2 = algal2Band.getSourceImage().getData();
            Raster yellow_subs = yellowSubsBand.getSourceImage().getData();
            Raster total_susp = totalSuspBand.getSourceImage().getData();
            //productWriter.writeBandRasterData(l2Flags, 0, 0, l2f.getWidth(), l2f.getHeight(), product, ProgressMonitor.NULL);

            Mask waterMask = product.getMaskGroup().get("water");
            Mask bpacOnMask = product.getMaskGroup().get("bpac_on");
            Mask uncertainAerosolModelMask = product.getMaskGroup().get("uncertain_aerosol_model");

            /*for (int i = 0; i < product.getMaskGroup().getNodeCount(); i++){
                System.out.println(product.getMaskGroup().getNodeDisplayNames()[i]);
            }*/

            Raster waterMaskData = waterMask.getSourceImage().getData();
            Raster bpacOnMaskData = bpacOnMask.getSourceImage().getData();
            Raster uncertainAerosolModelMaskData = uncertainAerosolModelMask.getSourceImage().getData();

            //System.out.println(waterMask.getDescription());
            //System.out.println(waterMask.getDisplayName());

            //double[] data = new double[rasterWidth];

            for (int y = 0; y < rasterHeight; y++) {

                if (y % 100 == 0) {
                    System.out.print(":");
                }

                int[] pixel3row = new int[rasterWidth];
                int[] pixel5row = new int[rasterWidth];
                int[] pixel7row = new int[rasterWidth];
                int[] pixel8row = new int[rasterWidth];
                int[] pixel9row = new int[rasterWidth];
                int[] pixel10row = new int[rasterWidth];
                int[] algal2row = new int[rasterWidth];
                int[] yellowSubsRow = new int[rasterWidth];
                int[] totalSuspRow = new int[rasterWidth];

                reflec3.getPixels(0, y, rasterWidth, 1, pixel3row);
                reflec5.getPixels(0, y, rasterWidth, 1, pixel5row);
                reflec7.getPixels(0, y, rasterWidth, 1, pixel7row);
                reflec8.getPixels(0, y, rasterWidth, 1, pixel8row);
                reflec9.getPixels(0, y, rasterWidth, 1, pixel9row);
                reflec10.getPixels(0, y, rasterWidth, 1, pixel10row);
                algal_2.getPixels(0, y, rasterWidth, 1, algal2row);
                yellow_subs.getPixels(0, y, rasterWidth, 1, yellowSubsRow);
                total_susp.getPixels(0, y, rasterWidth, 1, totalSuspRow);

                double[] kd490row = new double[rasterWidth];
                double[] CYrow = new double[rasterWidth];
                double[] MCIrow = new double[rasterWidth];
                double[] aPigRow = new double[rasterWidth];
                double[] c490row = new double[rasterWidth];
                double[] secchiPhotRow = new double[rasterWidth];
                double[] secchiBSrow = new double[rasterWidth];

                double[] waterMaskRow = new double[rasterWidth];
                double[] bpacOnMaskRow = new double[rasterWidth];
                double[] uncertainAerosolModelMaskRow = new double[rasterWidth];

                waterMaskData.getPixels(0, y, rasterWidth, 1, waterMaskRow);
                bpacOnMaskData.getPixels(0, y, rasterWidth, 1, bpacOnMaskRow);
                uncertainAerosolModelMaskData.getPixels(0, y, rasterWidth, 1, uncertainAerosolModelMaskRow);

                for (int x = 0; x < rasterWidth; x++) {

                    double reflec3Pixel,
                           reflec5Pixel,
                           reflec7Pixel,
                           reflec8Pixel,
                           reflec9Pixel,
                           reflec10Pixel,
                           algal2Pixel,
                           yellowSubsPixel,
                           totalSuspPixel;

                    /*double[] kd490pixel = new double[1];
                    double[] CYpixel = new double[1];
                    double[] MCIpixel = new double[1];
                    double[] aPigPixel = new double[1];
                    double[] c490pixel = new double[1];
                    double[] secchiPhotPixel = new double[1];
                    double[] secchiBSpixel = new double[1];*/



                    /*double[] waterMaskPixel = new double[1];
                    double[] bpacOnMaskPixel = new double[1];
                    double[] uncertainAerosolModelMaskPixel = new double[1];

                    waterMaskData.getPixel(x, y, waterMaskPixel);
                    bpacOnMaskData.getPixel(x, y, bpacOnMaskPixel);
                    uncertainAerosolModelMaskData.getPixel(x, y, uncertainAerosolModelMaskPixel);*/

                    //System.out.println(waterMaskRow[x] + " " + bpacOnMaskRow[x] + " " + uncertainAerosolModelMaskRow[x]);
                    if (waterMaskRow[x] > 0.0 && bpacOnMaskRow[x] > 0.0 && uncertainAerosolModelMaskRow[x] == 0.0)   {
                        reflec3Pixel = reflec3Band.scale(pixel3row[x]);
                        reflec5Pixel = reflec5Band.scale(pixel5row[x]);
                        reflec7Pixel = reflec7Band.scale(pixel7row[x]);
                        reflec8Pixel = reflec8Band.scale(pixel8row[x]);
                        reflec9Pixel = reflec9Band.scale(pixel9row[x]);
                        reflec10Pixel = reflec10Band.scale(pixel10row[x]);
                        algal2Pixel = algal2Band.scale(algal2row[x]);
                        yellowSubsPixel = yellowSubsBand.scale(yellowSubsRow[x]);
                        totalSuspPixel = totalSuspBand.scale(totalSuspRow[x]);

                        //System.out.println(algal2Pixel);

                        kd490row[x] = calcKd490(reflec3Pixel, reflec5Pixel, reflec9Pixel);
                        aPigRow[x] = calcAPig(algal2Pixel);
                        c490row[x] = calcC490(algal2Pixel, yellowSubsPixel, totalSuspPixel);
                        CYrow[x] = calcCY(reflec7Pixel, reflec8Pixel, reflec9Pixel);
                        MCIrow[x] = calcMCI(reflec8Pixel, reflec9Pixel, reflec10Pixel);
                        secchiPhotRow[x] = calcSecchiPhot(c490row[x], kd490row[x]);
                        secchiBSrow[x] = calcSecchiBS(kd490row[x]);
                    } else {
                        kd490row[x] = Double.NaN;
                        aPigRow[x] = Double.NaN;
                        c490row[x] = Double.NaN;
                        CYrow[x] = Double.NaN;
                        MCIrow[x] = Double.NaN;
                        secchiPhotRow[x] = Double.NaN;
                        secchiBSrow[x] = Double.NaN;
                    }

                    //System.out.println(aPigRow[x] + " " + yellowSubsBand.scale(yellowSubsRow[x]) + " " + totalSuspBand.scale(totalSuspRow[x]));
                    //System.out.println(c490row[x]);
                    //System.out.println(pixel3row[x] + " " + pixel5row[x] + " " + pixel9row[x]);
                    //System.out.println(kd490row[x]);

                    //kd490.writePixels(x, y, rasterWidth, 1, data);
                    /*kd490.writePixels(x, y, 1, 1, kd490pixel);
                    c490.writePixels(x, y, 1, 1, c490pixel);
                    CY.writePixels(x, y, 1, 1, CYpixel);
                    MCI.writePixels(x, y, 1, 1, MCIpixel);
                    aPig.writePixels(x, y, 1, 1, aPigPixel);
                    secchiPhot.writePixels(x, y, 1, 1, secchiPhotPixel);
                    secchiBS.writePixels(x, y, 1, 1, secchiBSpixel);*/
                }

                kd490.writePixels(0, y, rasterWidth, 1, kd490row);
                c490.writePixels(0, y, rasterWidth, 1, c490row);
                CY.writePixels(0, y, rasterWidth, 1, CYrow);
                MCI.writePixels(0, y, rasterWidth, 1, MCIrow);
                aPig.writePixels(0, y, rasterWidth, 1, aPigRow);
                secchiPhot.writePixels(0, y, rasterWidth, 1, secchiPhotRow);
                secchiBS.writePixels(0, y, rasterWidth, 1, secchiBSrow);
            }

            //kd_490.writeRasterDataFully();
            //kd_490.setSourceImage(kd_490.getSourceImage());
            //System.out.println(kd_490.isSourceImageSet());
            //ProductIO.writeProduct(product, filePath + "_test.dim", ProductIO.DEFAULT_FORMAT_NAME);

            product.dispose();
            //targetProduct.dispose();

            //BeamLogManager.removeRootLoggerHandlers(); // get rid of BEAM console logging
            //ImageInfo imageInfo = ProductUtils.createImageInfo(rgbBands, true, ProgressMonitor.NULL);
            //BufferedImage image = ProductUtils.createRgbImage(rgbBands, imageInfo, ProgressMonitor.NULL);
            //File imageFile = new File(product.getName() + ".png");
            //System.out.println("Writing RGB image: " + imageFile);
            //ImageIO.write(image, "PNG", imageFile);
            //System.out.println("RGB Image written: " + imageFile);
            //System.out.println("Product loaded: " + product.getName());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static double calcKd490(double reflec3, double reflec5, double reflec9) {
        double weight = reflec5 / reflec9;
        double result = Double.NaN;
        double eq1;
        double eq2;

        if (weight < 0) {
            weight = 0;
        } else if (weight > 1) {
            weight = 1;
        }

        if (reflec3 >= 0 && reflec5 >= 0 && reflec9 >= 0) {
            eq1 = Math.exp(-0.9461 * (Math.log(reflec3 / reflec9)) + 0.4305) + 0.0166;
            eq2 = Math.exp(-1.1198 * (Math.log(reflec5 / reflec9)) + 1.4141) + 0.0166;
            result = ((1 - weight) * eq1) + (weight * eq2);
        }

        if (weight == 1 && reflec9 >= 0 && reflec5 >= 0) {
            result = Math.exp(-1.1198 * (Math.log(reflec5 / reflec9)) + 1.4141) + 0.0166;
        }

        if (reflec5 < 0 || reflec9 < 0) {
            result = Double.NaN;
        }

        return result;
    }

    private static double calcC490(double algal2, double yellowSubs, double totalSusp) {
        return ((Math.exp(Math.log(algal2 / 21) / 1.04)) * 0.67) + ((yellowSubs * 0.5) + 0.015) + ((totalSusp / 1.73 + 0.0049) * 0.95);
    }

    private static double calcCY(double reflec7, double reflec8, double reflec9) {
        return -1 * (reflec8 - reflec7 - (reflec9 - reflec7) * 0.36);
    }

    private static double calcMCI(double reflec8, double reflec9, double reflec10) {
        return reflec9 - reflec8 - 0.389 * (reflec10 - reflec8);
    }

    private static double calcAPig(double algal2) {
        return Math.exp(Math.log(algal2 / 21) / 1.04);
    }

    private static double calcSecchiPhot(double c490, double kd490) {
        return 8.35 / (0.7782*(c490 + kd490) + 0.4132);
    }

    private static double calcSecchiBS(double kd490) {
        return 2.4 * Math.pow(kd490, -0.86);
    }

    public static void createRgbImage(String filePath) {
        try {
            System.out.println("Loading product: " + filePath);
            Product product = ProductIO.readProduct(filePath);
            Band[] rgbBands = new Band[] {
                    product.getBand("radiance_13"),
                    product.getBand("radiance_4"),
                    product.getBand("radiance_2")
            };
            ImageInfo imageInfo = ProductUtils.createImageInfo(rgbBands, true, ProgressMonitor.NULL);
            BufferedImage image = ProductUtils.createRgbImage(rgbBands, imageInfo, ProgressMonitor.NULL);
            File imageFile = new File(product.getName() + ".png");
            System.out.println("Writing RGB image: " + imageFile);
            ImageIO.write(image, "PNG", imageFile);
            System.out.println("RGB Image written: " + imageFile);
            System.out.println("Product loaded: " + product.getName());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String test(String filePath) {
        try {
            Product product = ProductIO.readProduct(filePath);
            return "success";
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    public static String testSimple() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String appName = SystemUtils.getApplicationName();
        try {
            String version = new VersionChecker().getLocalVersion();
            System.out.println(appName + " " + version);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
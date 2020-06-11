
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


 
public class Sort {
    static final String OUTPUT_FILE = "/home/oseias/Downloads/compress.gz"; 
    static final String INPUT_FILE = "/home/oseias/Downloads/impact.pdf"; 
 

    public static void main(String arg[]) {
        EnergyCheckUtils.init();
        
        
        
       
        double[] beforeCompress = EnergyCheckUtils.getEnergyStats();
        compressZip();
        double[] afterCompress = EnergyCheckUtils.getEnergyStats();
        double consumptionCompress = ((afterCompress[0] - beforeCompress[0]) + (afterCompress[1] - beforeCompress[1]) + (afterCompress[2] - beforeCompress[2])) / 10.0;
        
         
        System.out.println("Spent in Compress Method:  " + consumptionCompress);
        
       
        
       
    }
    
    public static void compressZip() 
    { 
    	
    	
    	try{
                double[] beforeUserDefined = EnergyCheckUtils.getEnergyStats();
    		byte[] buffer = new byte[256000];
    		FileOutputStream fos = new FileOutputStream(OUTPUT_FILE);
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry("impact.pdf");
    		zos.putNextEntry(ze);
    		FileInputStream in = new FileInputStream(INPUT_FILE);
                int len;
                int i = 0;
                double[] afterUserDefined = EnergyCheckUtils.getEnergyStats();
                double consumptionUserDefined = ((afterUserDefined[0] - beforeUserDefined[0]) + (afterUserDefined[1] - beforeUserDefined[1]) + (afterUserDefined[2] - beforeUserDefined[2])) / 10.0;
                System.out.println("Spent in User Defined:  " + consumptionUserDefined);
                
                double total = 0;
    		double[] beforeLoop = EnergyCheckUtils.getEnergyStats();
    		while ((len = in.read(buffer)) > 0) {
                    double[] beforeIteration = EnergyCheckUtils.getEnergyStats();
                    zos.write(buffer, 0, len);
                    
                    double[] afterIteration = EnergyCheckUtils.getEnergyStats();
                    double consumptionIteration = ((afterIteration[0] - beforeIteration[0]) + (afterIteration[1] - beforeIteration[1]) + (afterIteration[2] - beforeIteration[2])) / 10.0;
                    total += consumptionIteration;
                    System.out.println("Iteration = "+ i + " Spent:  " + consumptionIteration);
                    i++;
                            
    		}
                double[] afterLoop = EnergyCheckUtils.getEnergyStats();
                double consumptionLoop = ((afterLoop[0] - beforeLoop[0]) + (afterLoop[1] - beforeLoop[1]) + (afterLoop[2] - beforeLoop[2])) / 10.0;
                System.out.println("Spent in Loop:  " + consumptionLoop + "Spent in total: " +total);
                
                
                double[] beforeUserDefined2 = EnergyCheckUtils.getEnergyStats();
    		in.close();
    		zos.closeEntry();
          
    		//remember close it
    		zos.close();
                double[] afterUserDefined2 = EnergyCheckUtils.getEnergyStats();
                double consumptionUserDefined2 = ((afterUserDefined2[0] - beforeUserDefined2[0]) + (afterUserDefined2[1] - beforeUserDefined2[1]) + (afterUserDefined2[2] - beforeUserDefined2[2])) / 10.0;
                System.out.println("Spent in UserDefined2:  " + consumptionUserDefined2);
    		System.out.println("Done");

    	}catch(IOException ex){
    	   ex.printStackTrace();
    	}
    } 
    
    public static void decompressZip(String zipFilePath, String destDir) throws FileNotFoundException, IOException 
    { 
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
    
}
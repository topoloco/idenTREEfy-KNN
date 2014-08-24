package reconocimiento;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import reconocimiento.KNN;
import reconocimiento.ExtraerCaracteristicas;

public class Test {
	public static void main(String[] args) throws IOException {
		
		//Leo la imagen
    	IplImage imageSrc = null;
    	imageSrc = cvLoadImage("resources/test.jpg");

		// Analizo imagen
		System.out.println("Analizando...");
		ExtraerCaracteristicas ec = new ExtraerCaracteristicas(imageSrc.getBufferedImage());
		ec.analizarImagen();
		BufferedImage imagenAnalizada = ec.getImagenAnalizada();
		System.out.println("Analizado");
		
		
		//Guardo las caracteristicas de la hoja en un archivo
		BufferedWriter dataset;
		dataset = new BufferedWriter(new FileWriter("resources/hoja-analizada.txt"));
		String registro = "";
		registro += ec.getAspectRatio();
		registro += ",";
		registro += ec.getFactorForma();
		registro += ",";
		registro += ec.getRectangularidad();
		registro += ",";
		registro += ec.getPerimRatio();
		registro += ",";
		registro += ec.getNarrowFactor();
		registro += ",";
		registro += "Desconocida";
		
		System.out.println(">>" + registro);
		dataset.write(registro);
		dataset.close();
		
		//Proceso el dataset
		KNN.generarDataset();
		
		int idEspecie = 0;
		String especie = null;
		try{
			especie = KNN.getEspecie();
			Map<String, Integer> especies = getEspecies();
			idEspecie = especies.get(especie);
		}catch(Exception e){
			System.out.println("Se produjo un error al evaluar la hoja: " + e.getMessage());
		}
	}
	
	public static Map<String, Integer> getEspecies() throws NumberFormatException, IOException{
		BufferedReader br;
		String line =  null;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(new FileReader("resources/especies.txt"));

			while((line = br.readLine())!=null){
				String str[] = line.split(",");
		        for(int i=1;i<str.length;i++){
		            map.put(str[0], Integer.parseInt(str[1]));
		        }	
			 }
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}

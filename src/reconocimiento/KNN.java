package reconocimiento;

/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2009, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;


import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

/**
 * This tutorial show how to use a the k-nearest neighbors classifier.
 * 
 * @author Thomas Abeel
 * 
 */
public class KNN {
    /**
     * Shows the default usage of the KNN algorithm.
     */
    public static String getEspecie()throws Exception {

        /* Load a data set */
        Dataset data = FileHandler.loadDataset(new File("resources/hojas-dataset.txt"), 4, ",");
        /*
         * Contruct a KNN classifier that uses 5 neighbors to make a decision.
         */
        Classifier knn = new KNearestNeighbors(5);
        knn.buildClassifier(data);

        /*
         * Load a data set for evaluation, this can be a different one, but we
         * will use the same one.
         */
        String especie = null;
        Dataset dataForClassification = FileHandler.loadDataset(new File("resources/hoja-analizada.txt"), 4, ",");
        /* Classify all instances */
        for (Instance inst : dataForClassification) {
            Object predictedClassValue = knn.classify(inst);
            System.out.println("Especie predecida: " + predictedClassValue);
            especie = (String) predictedClassValue;
        }
        
       
        return especie;

    }
    
    public static void generarDataset(){
    	BufferedWriter dataset;
    	BufferedWriter especiesTxt;
		try {
			//Creo el archivo para guardar el dataset
			dataset = new BufferedWriter(new FileWriter("resources/hojas-dataset.txt"));
			especiesTxt = new BufferedWriter(new FileWriter("resources/especies.txt"));
			
			//Borro el directorio de imagenes procesadas
			File procesadas = new File("./resources/procesadas");
			borrarContenidoCarpeta(procesadas);
	    	
	    	File carpetaEspecies = new File("./resources/dataset");
			// Recorro especies
			File[] especies = carpetaEspecies.listFiles();
			int idEspecie = 1;
			for (File especie : especies) {
				System.out.println(especie.getName());
				
				//Guardo las especies y su ID en un archivo
				especiesTxt.write(especie.getName() + "," + idEspecie);
				especiesTxt.newLine();

				//Creo el directorio para guardar las imagenes procesadas de la especie
				File especieProcesadas = new File("./resources/procesadas/" + especie.getName());
				especieProcesadas.mkdir();
				
				//Recorro hojas de cada especie
				File[] hojas = especie.listFiles();
				for (File hoja : hojas) {
					
					if(!hoja.getName().equals("Thumbs.db")){
						
						System.out.println(">" + hoja.getName());
						//Calculo las características de cada hoja
						try {
							BufferedImage hojaBuffered = ImageIO.read(hoja);
							ExtraerCaracteristicas ec = new ExtraerCaracteristicas(hojaBuffered);
							ec.analizarImagen();
							
							//Formato
							// [Aspect Ratio], [Factor Forma], [Rectangularidad], [Ratio Perimetro], [Especie]
							String registro="";
							registro += ec.getAspectRatio();
							registro += ",";
							registro += ec.getFactorForma();
							registro += ",";
							registro += ec.getRectangularidad();
							registro += ",";
							registro += ec.getPerimRatio();
							registro += ",";
							//registro += ec.getCircularidad();
							//registro += ",";
							//Grabo la especie a la que pertenece
							registro += especie.getName();
							
							System.out.println(">>" + registro);
							dataset.write(registro);
							dataset.newLine();
	
							//Guardo la imagen procesada
							File outputfile = new File("./resources/procesadas/" + especie.getName() + "/" + hoja.getName());
						    ImageIO.write(ec.getImagenAnalizada(), "jpg", outputfile);

							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				}
				idEspecie++;
			}
			dataset.close();
			especiesTxt.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    public static void borrarContenidoCarpeta(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                	borrarContenidoCarpeta(f);
                } else {
                    f.delete();
                }
            }
        }
    }

}
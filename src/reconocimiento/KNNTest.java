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
public class KNNTest {
    /**
     * Shows the default usage of the KNN algorithm.
     */
    public static void main(String[] args)throws Exception {

    	//generarDataset();
    	
        /* Load a data set */
        Dataset data = FileHandler.loadDataset(new File("resources/hojas-dataset.txt"), 5, ",");
        /*
         * Contruct a KNN classifier that uses 5 neighbors to make a decision.
         */
        Classifier knn = new KNearestNeighbors(5);
        knn.buildClassifier(data);

        /*
         * Load a data set for evaluation, this can be a different one, but we
         * will use the same one.
         */
        Dataset dataForClassification = FileHandler.loadDataset(new File("resources/hojas-dataset.txt"), 5, ",");
        /* Counters for correct and wrong predictions. */
        int correct = 0, wrong = 0;
        /* Classify all instances and check with the correct class values */
        for (Instance inst : dataForClassification) {
            Object predictedClassValue = knn.classify(inst);
            Object realClassValue = inst.classValue();
            if (predictedClassValue.equals(realClassValue))
                correct++;
            else
                wrong++;
        }
        System.out.println("Testeando con el mismo dataset:");
        System.out.println(">Predicciones correctas  " + correct);
        System.out.println(">Predicciones erroneas " + wrong);
        
        //System.out.println("Testeando con Cross Validation:");
        CrossValidation cv = new CrossValidation(knn);
        /*
         * Perform 5-fold cross-validation on the data set with a user-defined
         * random generator
         */
        Map<Object, PerformanceMeasure> a = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> p = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> t = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> g = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> q = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> f = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> m = cv.crossValidation(data, 5, new Random(5));
        Map<Object, PerformanceMeasure> l = cv.crossValidation(data, 5, new Random(5));

        System.out.println(">Eucalyptus_Melliodora - Accuracy=" + a.get("Eucalyptus_Melliodora").getAccuracy());
        System.out.println(">Ficus_Benjamina - Accuracy=" + p.get("Ficus_Benjamina").getAccuracy());
        System.out.println(">Ginkgo_Biloba - Accuracy=" + t.get("Ginkgo_Biloba").getAccuracy());
        System.out.println(">Melia_Azedarach - Accuracy=" + g.get("Melia_Azedarach").getAccuracy());
        System.out.println(">Platanus_Hispanica - Accuracy=" + q.get("Platanus_Hispanica").getAccuracy());
        System.out.println(">Populus_Nigra - Accuracy=" + f.get("Populus_Nigra").getAccuracy());
        System.out.println(">Quercus_Palustris - Accuracy=" + m.get("Quercus_Palustris").getAccuracy());
        System.out.println(">Tilia_Cordata - Accuracy=" + l.get("Tilia_Cordata").getAccuracy());
        
        System.out.println(a);
        System.out.println(p);
        System.out.println(t);
        System.out.println(g);
        System.out.println(q);
        System.out.println(f);
        System.out.println(m);
        System.out.println(l);
        

    }
    
    public static void generarDataset(){
    	BufferedWriter dataset;
		try {
			//Creo el archivo para guardar el dataset
			dataset = new BufferedWriter(new FileWriter("resources/hojas-dataset.txt"));
			
			//Borro el directorio de imagenes procesadas
			File procesadas = new File("./resources/procesadas");
			borrarContenidoCarpeta(procesadas);
	    	
	    	File carpetaEspecies = new File("./Dataset_Hojas2");
			// Recorro especies
			File[] especies = carpetaEspecies.listFiles();
			for (File especie : especies) {
				System.out.println(especie.getName());
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
							// [Aspect Ratio], [Factor Forma], [Rectangularidad], [Ratio Perimetro], [NarrowFactor], [Especie]
							String registro="";
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
			}
			dataset.close();
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
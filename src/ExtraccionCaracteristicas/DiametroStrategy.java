package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_core.CV_32SC1;
import static com.googlecode.javacv.cpp.opencv_core.CV_32SC2;
import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.CV_WHOLE_SEQ;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.cvCvtSeqToArray;
import static com.googlecode.javacv.cpp.opencv_core.cvLine;
import static com.googlecode.javacv.cpp.opencv_core.cvMat;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_COUNTER_CLOCKWISE;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvConvexHull2;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvConvexityDefects;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;

public class DiametroStrategy implements ExtraccionCaracteristicaStrategy {

	public DiametroStrategy() {
		super();
	}

	@Override
	public double extraerCaracteristica(Imagen imagen) {
		//Encuentro un convex hull de la hoja
		CvMemStorage storage = CvMemStorage.create();
        CvPoint pointArray = new CvPoint(imagen.contour.total()); 
        cvCvtSeqToArray(imagen.contour, pointArray, CV_WHOLE_SEQ); 
        CvMat pointMatrix = cvMat(1, imagen.contour.total(), CV_32SC2, pointArray); 
        CvMat hullMatrix = cvCreateMat(1, imagen.contour.total(), CV_32SC1); 
        cvConvexHull2(imagen.contour, hullMatrix, CV_COUNTER_CLOCKWISE, 0); 
        CvSeq defects = cvConvexityDefects(imagen.contour, hullMatrix, storage); 

        if (hullMatrix!=null && !hullMatrix.isNull()) { 
            int hullCount = hullMatrix.cols(); 
            CvPoint pt = pointArray.position((int) hullMatrix.get(hullCount-1)); 
            CvPoint pt0 = new CvPoint(pt.x(), pt.y()); 
            for (int j=0; j<hullCount; j++) { 
                pt = pointArray.position((int) hullMatrix.get(j)); 
                pt0 = new CvPoint(pt.x(), pt.y()); 
            } 
        } 
        
        //Calcular segmento máximo dentro del convexhull 
        //TODO usar Rotating Calipers y testear colisiones de los segmentos contra los defectos del convexhull
        double maxDistance = 0;
        CvPoint diam0 = null;
        CvPoint diam1 = null;
        if (hullMatrix!=null && !hullMatrix.isNull()) { 
            int hullCount = hullMatrix.cols(); 
            CvPoint pt = pointArray.position((int) hullMatrix.get(hullCount-1)); 
            CvPoint pt0 = new CvPoint(pt.x(), pt.y()); 
            CvPoint pt1 = pointArray.position((int) hullMatrix.get(hullCount-1)); 
            
            for (int j = 0; j < hullCount; j++) { 
                pt = pointArray.position((int) hullMatrix.get(j)); 
                pt0 = new CvPoint(pt.x(), pt.y()); 
                for (int k = 0; k < hullCount; k++) { 
                	pt1 = pointArray.position((int) hullMatrix.get(k)); 
                	//Calculo la distancia entre pt y pt1
                	double xDiff = Math.abs(pt0.x() - pt1.x());
            		double yDiff = Math.abs(pt0.y() - pt1.y());
            		double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
            		//System.out.println(distance);
            		if(maxDistance < distance ){
            			maxDistance = distance;
            			diam0 = new CvPoint(pt0.x(), pt0.y());
            			diam1 = new CvPoint(pt1.x(), pt1.y());
            		}
                	
                }
            }
        }
		return maxDistance;
	}
}

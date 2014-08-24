package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_core.cvPointFrom32f;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMinEnclosingCircle;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class AreaMinCirculoStrategy implements ExtraccionCaracteristicaStrategy {

	public AreaMinCirculoStrategy() {
		super();
	}

	@Override
	public double extraerCaracteristica(Imagen imagen) {
		// Hallo el bounding circle
		CvPoint2D32f circle = new CvPoint2D32f();
        float[] radio = new float[1];
        cvMinEnclosingCircle(imagen.contour, circle, radio);

		return Math.sqrt(radio[0]) * Math.PI; 
	}
}

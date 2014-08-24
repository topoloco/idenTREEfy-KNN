package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class AltoMaximoStrategy implements ExtraccionCaracteristicaStrategy {

	public AltoMaximoStrategy() {
		super();
	}

	@Override
	public double extraerCaracteristica(Imagen imagen) {
		// Hallo el bounding box recto, me define ancho max y alto max
		CvRect rect = cvBoundingRect(imagen.contour, 0);
		return rect.height();
	}
}

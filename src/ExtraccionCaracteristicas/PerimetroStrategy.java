package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_core.CV_WHOLE_SEQ;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvArcLength;

public class PerimetroStrategy implements ExtraccionCaracteristicaStrategy {

	public PerimetroStrategy() {
		super();
	}

	@Override
	public double extraerCaracteristica(Imagen imagen) {
		return cvArcLength(imagen.contour, CV_WHOLE_SEQ, 1);
	}
}

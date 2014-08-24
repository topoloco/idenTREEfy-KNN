package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class AreaBboxStrategy implements ExtraccionCaracteristicaStrategy {

	public AreaBboxStrategy() {
		super();
	}

	@Override
	public double extraerCaracteristica(Imagen imagen) {
		CvRect rect = cvBoundingRect(imagen.contour, 0);
		int h = rect.height(), w = rect.width();
		return h*w;
	}
}

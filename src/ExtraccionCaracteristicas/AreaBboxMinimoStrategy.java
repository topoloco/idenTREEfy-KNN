package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_imgproc.cvMinAreaRect2;

import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;

public class AreaBboxMinimoStrategy implements ExtraccionCaracteristicaStrategy {

	public AreaBboxMinimoStrategy() {
		super();
	}

	@Override
	public double extraerCaracteristica(Imagen imagen) {
		CvMemStorage storage = CvMemStorage.create();
		CvBox2D box = cvMinAreaRect2(imagen.contour, storage);
		
		return (box.size().width() * box.size().height());
	}
}

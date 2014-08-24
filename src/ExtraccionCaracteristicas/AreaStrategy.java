package ExtraccionCaracteristicas;

import static com.googlecode.javacv.cpp.opencv_core.CV_WHOLE_SEQ;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvContourArea;

public class AreaStrategy implements ExtraccionCaracteristicaStrategy {
	
    public AreaStrategy(){
        super();
    }
    @Override
    public double extraerCaracteristica(Imagen imagen) {
        return Math.abs(cvContourArea(imagen.contour, CV_WHOLE_SEQ, 0));
    }
}

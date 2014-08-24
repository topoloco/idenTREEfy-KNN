package reconocimiento;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

import ExtraccionCaracteristicas.AltoMaximoStrategy;
import ExtraccionCaracteristicas.AnchoMaximoStrategy;
import ExtraccionCaracteristicas.AreaBboxMinimoStrategy;
import ExtraccionCaracteristicas.AreaBboxStrategy;
import ExtraccionCaracteristicas.AreaMinCirculoStrategy;
import ExtraccionCaracteristicas.AreaStrategy;
import ExtraccionCaracteristicas.DiametroStrategy;
import ExtraccionCaracteristicas.Imagen;
import ExtraccionCaracteristicas.PerimetroStrategy;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
//non-static imports
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ExtraerCaracteristicas {
	static CvScalar min = cvScalar(0, 0, 0, 0);// BGR-A
	static CvScalar max = cvScalar(200, 200, 200, 0);// BGR-A
	private BufferedImage imagen = null;
	private BufferedImage imagenAnalizada = null;
	private String especie;
	private String nombre;

	// Caracteristicas
	private double smoothFactor = 0.0;// TODO no comprendou
	private double aspectRatio = 0.0;
	private double factorForma = 0.0;
	private double rectangularidad = 0.0;
	private double circularidad = 0.0;
	private double narrowFactor = 0.0;
	private double perimRatio = 0.0;
	private double area = 0.0;

	public ExtraerCaracteristicas(BufferedImage imagen) {
		this.imagen = imagen;
	}

	public void analizarImagen() {
		IplImage imageSrc = null;
		// Cargo la imagen original
		// imageSrc = cvLoadImage(this.pathImagen);

		imageSrc = IplImage.createFrom(this.imagen);

		Imagen imagen = new Imagen();
		imagen.setImage(imageSrc);

		// Cargo la imagen en escala de grises
		// TODO
		// image = imageSrc;
		// cvConvertImage(imageSrc, image, CV_BGR2GRAY);
		// image = cvLoadImage(this.pathImagen, CV_LOAD_IMAGE_GRAYSCALE);
		// HoughLines hl = new HoughLines(image, "probabilistic");

		// Convertir la imagen a blanco y negro binario aplicando thresholding
		IplImage grayImage = cvCreateImage(cvGetSize(imageSrc), 8, 1);

		// Aplico un filtro de suavizado para los bordes
		// TODO: que sea proporcional a la calidad/tamaï¿½o de la imagen
		cvSmooth(grayImage, grayImage, CV_BLUR, 5);

		//Paso la imagen a escalada de grises
		cvCvtColor(imageSrc, grayImage, CV_BGR2GRAY);
		
		//cvDilate(grayImage, grayImage, null, 1);
		cvErode(grayImage, grayImage, null, 1);
		
		//Aplico un filtro tipo treshold adaptativo
		cvAdaptiveThreshold(grayImage, grayImage, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 5, 4);
		
		//Aplico un filtro Dilate para cerrar el poligono
		cvDilate(grayImage, grayImage, null, 2);
		
		//Guardo la imagen filtrada
		cvSaveImage("resources/threshold.jpg", grayImage);


		// Busco poligonos de contorno de la hoja en la imagen
		CvMemStorage storage = CvMemStorage.create();
		CvSeq contour = new CvSeq(CV_SEQ_FLAG_CLOSED);
		CvSeq contourMax = new CvSeq(CV_SEQ_FLAG_CLOSED);
		cvFindContours(grayImage, storage, contour,
				Loader.sizeof(CvContour.class), CV_RETR_TREE,
				CV_CHAIN_APPROX_SIMPLE);
		CvSeq points = null;

		// RUTINA QUE ENCUENTRA EL MAYOR CONTORNO (LA HOJA)
		// Itero por cada polï¿½gono encontrado
		int i = 0;
		while (contour != null && !contour.isNull()) {
			if ((contour.elem_size() > 0)) {
				points = cvApproxPoly(contour, Loader.sizeof(CvContour.class),
						storage, CV_POLY_APPROX_DP,
						cvContourPerimeter(contour) * 0.001, 1);
				if ((i == 0)
						|| (Math.abs(cvContourArea(contourMax, CV_WHOLE_SEQ, 0)) < Math
								.abs(cvContourArea(contour, CV_WHOLE_SEQ, 0)))) {
					contourMax = contour;
				}

			}
			i++;
			contour = contour.h_next();
		}
		// FIN RUTINA QUE ENCUENTRA EL MAYOR CONTORNO (LA HOJA)

		//DIBUJAR IMAGEN PROCESADA
		cvDrawContours(imageSrc, contourMax, CvScalar.BLUE, CvScalar.MAGENTA, 0, 2, CV_AA);
		
		CvRect rect=cvBoundingRect(contourMax, 0);
        int x=rect.x(),y=rect.y(),h=rect.height(),w=rect.width();
        cvRectangle(imageSrc, cvPoint(x, y), cvPoint(x+w, y+h), CvScalar.RED, 2, CV_AA, 0);
        
        //Hallo el bounding box de mínima area que contiene a la hoja
        CvBox2D box = cvMinAreaRect2(contourMax, storage);
        CvPoint2D32f rect_points = new CvPoint2D32f(4);
        cvBoxPoints(box, rect_points);

        //Defino los vertices del bounding box minimo
        CvPoint x1 = cvPointFrom32f(rect_points.position(0));
        CvPoint x2 = cvPointFrom32f(rect_points.position(1));
        CvPoint x3 = cvPointFrom32f(rect_points.position(2));
        CvPoint x4 = cvPointFrom32f(rect_points.position(3));
        
        //Dibujo las lineas del bounding box minimo
        cvLine(imageSrc, x1, x2, CvScalar.BLUE, 3, CV_AA, 0);
        cvLine(imageSrc, x3, x4, CvScalar.BLUE, 3, CV_AA, 0);
        cvLine(imageSrc, x2, x3, CvScalar.BLUE, 3, CV_AA, 0);
        cvLine(imageSrc, x1, x4, CvScalar.BLUE, 3, CV_AA, 0);
        
        //Busco Bounding Circle
        CvPoint2D32f circle = new CvPoint2D32f();
        float[] radio = new float[1];
        cvMinEnclosingCircle(contourMax, circle, radio);

        CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle.x(), circle.y()));
        cvCircle(imageSrc, center, (int) radio[0], CvScalar.YELLOW, 2, CV_AA, 0);
		
		imagen.setContour(contourMax);

		// Caracteristicas Principales
		double altoMaximo = imagen
				.extraerCaracteristica(new AltoMaximoStrategy());
		double anchoMaximo = imagen
				.extraerCaracteristica(new AnchoMaximoStrategy());
		this.area = imagen.extraerCaracteristica(new AreaStrategy());
		double perimetro = imagen
				.extraerCaracteristica(new PerimetroStrategy());
		double areaBbox = imagen.extraerCaracteristica(new AreaBboxStrategy());
		double areaBboxMinimo = imagen
				.extraerCaracteristica(new AreaBboxMinimoStrategy());
		double areaMinCirculo = imagen.extraerCaracteristica(new AreaMinCirculoStrategy());
		double diametro = imagen.extraerCaracteristica(new DiametroStrategy());// TODO
																				// encontrar
																				// diametros

		// Caracteristicas computadas
		smoothFactor = 0.0;// TODO no comprendou
		aspectRatio = altoMaximo / anchoMaximo;
		factorForma = (4 * Math.PI * area) / Math.pow(perimetro, 2);
		rectangularidad = (anchoMaximo * altoMaximo) / area;
		circularidad = area / areaMinCirculo;
		narrowFactor = diametro / altoMaximo;
		perimRatio = perimetro / (altoMaximo + anchoMaximo);
		
        //Calculo el área del bounding box mínimo
        double areaBboxMin = (box.size().width() * box.size().height());

        CvFont font = new CvFont();
        double hScale=0.5;
        double vScale=0.5;
        int    lineWidth=1;
        cvInitFont(font,CV_FONT_HERSHEY_SIMPLEX|CV_FONT_ITALIC, hScale,vScale,0,lineWidth, 8);
        
        cvPutText (imageSrc, "Perimetro: "+ (new DecimalFormat("##.##").format(perimetro)), cvPoint(10,40), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Area: "+ area, cvPoint(10,60), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Area BBoxMin: "+ areaBboxMin, cvPoint(10,80), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Area BBox: "+ areaBbox, cvPoint(10,100), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Alto Max: "+ altoMaximo, cvPoint(10,120), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Ancho Max: "+ anchoMaximo, cvPoint(10,140), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Rectangularidad: "+ rectangularidad, cvPoint(10,160), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Ratio Perimetro: "+ perimRatio, cvPoint(10,180), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Factor Forma: "+ factorForma, cvPoint(10,200), font, CvScalar.BLACK);
        cvPutText (imageSrc, "Circularidad: "+ circularidad, cvPoint(10,220), font, CvScalar.BLACK);
		
		// System.out.println("ALTO MAXIMO: " + altoMaximo);
		// System.out.println("ANCHO MAXIMO: " + anchoMaximo);
		// System.out.println("PERIMETRO: " + perimetro);
		// System.out.println("AREA BBOX: " + areaBbox);
		// System.out.println("AREA BBOX MIN: " + areaBboxMinimo);
		// System.out.println("DIAMETRO: " + diametro);

        
		//System.out.println("ASPECT RATIO: " + aspectRatio);
		//System.out.println("FACTOR FORMA: " + factorForma);
		//System.out.println("RECTANGULARIDAD: " + rectangularidad);
		//System.out.println("NARROW FACTOR: " + narrowFactor);
		//System.out.println("PERIMETRO RATIO: " + perimRatio);
		//System.out.println("AREA: " + area);

        

		String capFileName = "resources/analizada.jpg";
		//System.out.println(capFileName);
		cvSaveImage(capFileName, imageSrc);
		//final CanvasFrame canvas = new CanvasFrame("Procesada");
		//canvas.setCanvasSize(480, 640);
		//canvas.showImage(imageSrc);
		this.imagenAnalizada = imageSrc.getBufferedImage();

	}

	public BufferedImage getImagenAnalizada() {
		return this.imagenAnalizada;
	}

	public static CvScalar getMin() {
		return min;
	}

	public static CvScalar getMax() {
		return max;
	}

	public BufferedImage getImagen() {
		return imagen;
	}

	public double getSmoothFactor() {
		return smoothFactor;
	}

	public double getAspectRatio() {
		return aspectRatio;
	}

	public double getFactorForma() {
		return factorForma;
	}

	public double getRectangularidad() {
		return rectangularidad;
	}

	public double getNarrowFactor() {
		return narrowFactor;
	}

	public double getPerimRatio() {
		return perimRatio;
	}
	
	public double getArea(){
		return area;
	}
	
	public double getCircularidad(){
		return circularidad;		
	}

}

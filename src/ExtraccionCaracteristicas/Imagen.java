package ExtraccionCaracteristicas;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Imagen {
	private int idImagen;
	private String path;
	private String filename;
	private int filesize;
	private int height;
	private int width;
	private IplImage image;
	CvSeq contour;

	public Imagen(){
		super();
	}
	
	public Imagen(int idImagen, String path, String filename, int filesize,
			int height, int width, IplImage image, CvSeq contour) {
		super();
		this.idImagen = idImagen;
		this.path = path;
		this.filename = filename;
		this.filesize = filesize;
		this.height = height;
		this.width = width;
		this.image = image;
		this.contour = contour;
	}

	public int getIdImagen() {
		return idImagen;
	}

	public String getPath() {
		return path;
	}

	public String getFilename() {
		return filename;
	}

	public int getFilesize() {
		return filesize;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public IplImage getImage() {
		return image;
	}

	public void setImage(IplImage image) {
		this.image = image;
		
	}

	public double extraerCaracteristica(ExtraccionCaracteristicaStrategy extractor){
        return extractor.extraerCaracteristica(this);
    }

	public void setContour(CvSeq contour) {
		this.contour = contour;
	}
}

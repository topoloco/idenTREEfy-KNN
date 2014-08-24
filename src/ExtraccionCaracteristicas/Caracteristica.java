package ExtraccionCaracteristicas;

public class Caracteristica {
	private int idCaracteristica;
	private String descripcion;
	private Double value;
	private Double minVal;
	private Double maxVal;
	private Double avgVal;

	public Caracteristica(int idCaracteristica, String descripcion,
			Double value, Double minVal, Double maxVal, Double avgVal) {
		super();
		this.idCaracteristica = idCaracteristica;
		this.descripcion = descripcion;
		this.value = value;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.avgVal = avgVal;
	}

	public int getIdCaracteristica() {
		return idCaracteristica;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Double getValue() {
		return value;
	}

	public Double getMinVal() {
		return minVal;
	}

	public Double getMaxVal() {
		return maxVal;
	}

	public Double getAvgVal() {
		return avgVal;
	}

}

package at.fraubock.spendenverwaltung.interfaces.domain.filter.cells;

public abstract class FilterCell {
	
	protected Integer cell_id;
	
	public abstract String createSqlExpression();
	
	public Integer getCellId() {
		return this.cell_id;
	}
	
	public void setCellId(Integer id) {
		this.cell_id = id;
	}
	
}

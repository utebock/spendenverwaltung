package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import domain.Person;

public class DeleteTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private List<Person> list;
	private Vector<String> header;
	
	public DeleteTableModel(List<Person> list, Vector<String> header){
		if(list == null){
			this.list = new ArrayList<Person>();
		}
		else{
			this.list = list;
		}
		this.header = header;
	}
	@Override
	public int getRowCount() {
		if(list == null){
			return 0;
		}
		else{
			return list.size();
		}
	}

	@Override
	public int getColumnCount() {
		return header.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person p = list.get(rowIndex);
		switch(columnIndex){
		case 0:
			return p.getId();
		case 1: 
			return p.getSalutation();
		case 2: 
			return p.getGivenName();
		case 3:
			return p.getSurname();
		case 4:
			return p.getMailingAddress();
		}
		return null;
	}
	
	public List<Person> addRow(Person p){
		list.add(p);
		return list;
	}
	
	public String getColumnName(int col){
		return header.get(col);
	}
	
	public Person getRow(int row){
		if(list == null){
			return null;
		}
		else{
			return list.get(row);
		}
	}
	
	public Vector<String> getHeader(){
		return this.header;
	}
	
	public List<Person> getPersons(){
		return list;
	}

}

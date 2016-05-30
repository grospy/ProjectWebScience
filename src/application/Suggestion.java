package application;

import java.util.ArrayList;

public class Suggestion implements Comparable<Suggestion> {
	
	private Vertex vertex;
	private String name;
	private float grade;
	private ArrayList<Float> allGrades = new ArrayList<Float>();
	
	public Suggestion(Vertex vertex, float grade) {
		this.vertex = vertex;
		this.name = vertex.getName();
		this.allGrades.add(new Float(grade));
	}
	
	public float getGrade() {
		return grade;
	}
	
	public void setGrade(float grade) {
		this.grade = grade;
	}

	public String getName() {
		return name;
	}
	
	public Vertex getVertex() {
		return vertex;
	}

	@Override
	public int compareTo(Suggestion other) {
		float otherGrade = other.getGrade();
		if ((otherGrade - this.grade) > 0) {
			return 1;
		} else if ((otherGrade - this.grade) < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public String print() {
		String output = "Restaurant: " + this.name + ", with a grade of: " + this.grade + "\n";
		return output;
	}	
	
	public void calculateAV() {
		float result = 0;
		int i = 0;
		for (Float f: allGrades) {
			result += f.floatValue();
			i++;
		}
		this.grade = result / (float) i;
	}
	
	public void add(Float f) {
		this.allGrades.add(f);
	}
}

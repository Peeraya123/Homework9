package model;

public class Student {
		private int id;
		private String name,gpa;
		
		public Student(int id, String name, String gpa) {
			super();
			this.id = id;
			this.name = name;
			this.gpa = gpa;
		}
		public Student(String name, String gpa) {
			super();
			this.name = name;
			this.gpa = gpa;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getGpa() {
			return gpa;
		}
		public void setGpa(String gpa) {
			this.gpa = gpa;
		}	
}

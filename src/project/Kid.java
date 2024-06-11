package project;

public class Kid {
	public Person data;
	public String allergy;

	public Kid() {
		this.data = Person.empty();
		this.data.setExp(-1);
	}

	public Kid(Person per, String allergys) {
		this.data = per;
		this.data.setExp(-1);
		this.allergy = allergys;
	}

	public Kid(Person per) {
		this.data = per;
		this.data.setExp(-1);
		this.allergy = "";
	}

	// other
	final static Kid empty() {
		Kid temp = new Kid();
		temp.setData(Person.empty());
		temp.setAllergy("");
		;
		return temp;
	}

	public boolean equals(Kid k) {
		if (this.data.equals(k.getData()) && this.allergy.equals(k.getAllergy())) {
			return true;
		}
		return false;
	}

	// get/set
	public Person getData() {
		return data;
	}

	public void setData(Person data) {
		this.data = data;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

}

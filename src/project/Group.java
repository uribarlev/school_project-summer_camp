package project;

public class Group {
	public Kid[] kids = new Kid[0];
	public Adult adult;
	public Helper helper;
	public String name;

	public Group() {
		this.adult = Adult.empty();
		this.helper = Helper.empty();
		this.name = "new";
	}

	public Group(Kid[] k, Adult a, Helper h) {
		this.kids = k;
		this.adult = a;
		this.helper = h;
	}

	public Group(Adult a, Helper h) {
		this.adult = a;
		this.helper = h;
	}

	public Group(Kid[] k, Adult a, Helper h, String str) {
		this.kids = k;
		this.adult = a;
		this.helper = h;
		this.name = str;
	}

	public Group(Adult a, Helper h, String str) {
		this.adult = a;
		this.helper = h;
		this.name = str;
	}

	public Group(String str) {
		this.adult = Adult.empty();
		this.helper = Helper.empty();
		this.name = str;
	}
	public Group(String str,Kid[] k) {
		this.adult = Adult.empty();
		this.helper = Helper.empty();
		this.name = str;
		this.kids=k;
	}

	// other
	public static Group empty() {
		Group temp = new Group();
		return temp;
	}

	// get/set
	public Kid[] getKids() {
		return kids;
	}

	public void setKids(Kid[] kids) {
		this.kids = kids;
	}

	public Adult getAdult() {
		return adult;
	}

	public void setAdult(Adult adult) {
		this.adult = adult;
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// other
	public void add(Kid k) {
		Kid[] temp = new Kid[this.kids.length + 1];
		for (int i = 0; i < this.kids.length; i++) {
			temp[i] = this.kids[i];
		}
		temp[this.kids.length] = k;
		this.kids = temp;
	}

	public void remove(Kid k) {
		Kid[] temp = new Kid[this.kids.length - 1];
		int change = 0;
		for (int i = 0; i < this.kids.length; i++) {
			if (!this.kids[i].equals(k)) {
				temp[i + change] = this.kids[i];
			} else {
				change = -1;
			}
		}
		this.kids = temp;
	}

}

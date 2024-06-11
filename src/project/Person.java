package project;

public class Person {
	public String id;
	public String name;
	public String gender;
	// 0-girl
	// 1-boy
	// 2-other
	public int age;
	public int exp = 0;
	public String username;
	public String password;
	public String phone;

	public Person() {
	}

	public Person(String str, String str4, String str1, String str2, String num, int num1, int num2, int num3) {
		this.username = str;
		this.id = num;
		String gender = "male";
		switch (num1) {
		case 1:
			gender = "female";
			break;
		case 2:
			gender = "other";
			break;
		}
		this.gender = gender;
		this.age = num2;
		this.exp = num3;
		this.password = str1;
		this.phone = str2;
		this.name = str4;
	}

	public Person(String str, String str1, String str2, String str3, String num, int num1, int num2) {
		this.username = str;
		this.name = str3;
		this.id = num;
		String gender = "male";
		switch (num1) {
		case 1:
			gender = "female";
			break;
		case 2:
			gender = "other";
			break;
		}
		this.gender = gender;
		this.age = num2;
		this.password = str1;
		this.phone = str2;
	}

	// other
	public static Person empty() {
		Person temp = new Person();
		temp.setId("-1");
		temp.setAge(-1);
		temp.setGender("-1");
		temp.setExp(-1);
		temp.setUsername("#");
		temp.setPassword("-1");
		temp.setPhone("-1");
		temp.setName("-1");
		return temp;

	}

	// get/set
	public void set(Person other) {
		this.age = other.getAge();
		this.exp = other.getExp();
		this.gender = other.getGender();
		this.id = other.getId();
		this.name = other.getName();
		this.password = other.getPassword();
		this.phone = other.getPhone();
		this.username = other.getUsername();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
package project;

public class Adult {
	public Person data;
	public String bank;
	public int hours;

	public Adult() {
	}

	public Adult(Person per, String num, int num1) {
		this.data = per;
		this.bank = num;
		this.hours = num1;
	}

	// other
	final static Adult empty() {
		Adult temp = new Adult();
		temp.setData(Person.empty());
		temp.setHours(-1);
		temp.setBank("-1");
		return temp;
	}

	// set/get
	public Person getData() {
		return data;
	}

	public void setData(Person data) {
		this.data = data;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}
}

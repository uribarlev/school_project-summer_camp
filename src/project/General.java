package project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;

public class General {
	// general data
	public Person manager = new Person();
	public JFrame window = new JFrame("project");
	public Group[] groups = { new Group() };
	public Adult[] a_non_group = new Adult[0];
	public Helper[] h_non_group = new Helper[0];
	// frame data
	private Person user = new Person();
	// panels
	private CardLayout layout = new CardLayout();
	private JPanel login_panel = new JPanel();
	private JPanel signup_panel = new JPanel();
	private JPanel profile_panel = new JPanel();
	private JPanel search_panel = new JPanel();
	// transistors
	private JButton login_signup;
	private JButton signup_login;
	// other
	private boolean page = false;
	private int search_type;
	private JPanel cen_panel = new JPanel();
	private JTextField search_text = new JTextField();
	private Person move = Person.empty();
	public File fil;

	// converting from file to usefull data
	private void read() throws FileNotFoundException {
		Scanner fill = new Scanner(fil);
		fill.useDelimiter("\\z");
		// manager
		String str = fill.nextLine().substring(8);
		decipher(this.manager, str, 0);
		// groups
		fill.nextLine();
		while (fill.hasNextLine()) {
			str = fill.nextLine();
			str = str.substring(str.indexOf("<") + 1);
			String n = str.substring(0, str.indexOf("("));
			Person temp_p1 = new Person();
			str = str.substring(str.indexOf("A[") + 1);
			str = decipher(temp_p1, str, 0);
			Adult a = new Adult(temp_p1, str.substring(0, str.indexOf("]")), 0);
			str = str.substring(str.indexOf("H[") + 1);
			Person temp_p2 = new Person();
			str = decipher(temp_p2, str, 0);
			Helper h = new Helper(temp_p2, str.substring(0, str.indexOf("]")), 0);
			Group temp = new Group(a, h, n);
			while (str.charAt(str.indexOf(")") + 1) != ']') {
				Person temp_p = new Person();
				str = str.substring(str.indexOf("K(") + 1);
				str = decipher(temp_p, str, 1);
				Kid temp_k = new Kid(temp_p, str.substring(0, str.indexOf(")")));
				temp.add(temp_k);
			}
			if (n.equals(("new"))) {
				this.groups[0] = temp;
			} else {
				add_group(temp);
			}
		}
	}

	private String decipher(Person per, String str, int num) {
		per.setUsername(str.substring(1, str.indexOf(",")));
		str = str.substring(str.indexOf(",") + 1);
		per.setName(str.substring(0, str.indexOf(",")));
		str = str.substring(str.indexOf(",") + 1);
		per.setPassword(str.substring(0, str.indexOf(",")));
		str = str.substring(str.indexOf(",") + 1);
		per.setPhone(str.substring(0, str.indexOf(",")));
		str = str.substring(str.indexOf(",") + 1);
		per.setId(str.substring(0, str.indexOf(",")));
		str = str.substring(str.indexOf(",") + 1);
		per.setGender(str.substring(0, str.indexOf(",")));
		str = str.substring(str.indexOf(",") + 1);
		per.setAge(convert(str.substring(0, str.indexOf(","))));
		str = str.substring(str.indexOf(",") + 1);
		if (num == 0) {
			per.setExp(convert(str.substring(0, str.indexOf(","))));
			str = str.substring(str.indexOf(",") + 1);
		}
		return str;
	}

	// convert string to int
	private static int convert(String str) {
		int num = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(str.length() - i - 1) == '-') {
				num *= -1;
			} else {
				num += (str.charAt(str.length() - i - 1) - '0') * (int) Math.pow(10, (double) i);
			}
		}
		return num;
	}

	public General(File data) throws FileNotFoundException {
		this.fil = data;
		read();
		// start
		this.window.setPreferredSize(new Dimension(400, 350));
		this.window.setLayout(this.layout);
		this.window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		login_setup(this.login_panel);
		signup_setup(this.signup_panel);
		this.window.add(this.login_panel, "login_panel");
		this.window.add(this.signup_panel, "signup_panel");
		this.window.add(this.profile_panel, "profile_panel");
		this.window.add(this.search_panel, "search_panel");
		this.window.pack();
		this.window.setVisible(true);
		this.layout.show(this.window.getContentPane(), "login_panel");
		// moving from one panel to another
		this.login_signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layout.show(window.getContentPane(), "signup_panel");
			}
		});
		signup_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layout.show(window.getContentPane(), "login_panel");
			}
		});
	}

	// other
	private int[] count_groups(Kid[] k) {
		int[] output = new int[0];
		for (int i = 0; i < k.length; i++) {
			if (!array_find(output, find_group_kid(k[i].getData().getUsername()))) {
				output = array_add(output, find_group_kid(k[i].getData().getUsername()));
			}
		}
		return output;
	}

	private boolean array_find(int[] array, int num) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == num) {
				return true;
			}
		}
		return false;
	}

	private int locate(JLabel[] op, Kid k) {
		int per_group = find_group_kid(k.getData().getUsername());
		for (int i = 0; i < op.length; i++) {
			if (per_group == find_group(op[i].getText().substring(6))) {
				return i;
			}
		}
		return -1;
	}

	// GUI
	// login panel
	private void login_setup(JPanel panel) {
		panel.setVisible(true);
		panel.setLayout(new BorderLayout());
		JPanel login_cen_panel = new JPanel();
		JPanel login_nor_panel = new JPanel();
		JPanel login_name = new JPanel();
		JPanel login_password = new JPanel();
		JPanel login_buttons = new JPanel();
		JLabel label_name = new JLabel("username:");
		JTextField text_name = new JTextField("");
		JLabel label_password = new JLabel("password:");
		JTextField text_password = new JTextField("");
		JLabel login_label = new JLabel("login");
		GridBagConstraints location = new GridBagConstraints();
		JLabel error = new JLabel("");
		this.login_signup = new JButton("sign up");
		JButton login_button = new JButton("log in");
		error.setForeground(Color.red);
		login_cen_panel.setLayout(new GridBagLayout());
		login_nor_panel.setLayout(new FlowLayout());
		login_nor_panel.add(login_label);
		location.fill = GridBagConstraints.HORIZONTAL;
		text_name.setPreferredSize(new Dimension(100, 25));
		text_password.setPreferredSize(new Dimension(100, 25));
		login_name.setBounds(new Rectangle(200, 20));
		login_password.setBounds(new Rectangle(100, 20));
		location.gridx = 0;
		location.gridy = 0;
		login_name.add(label_name, location);
		login_password.add(label_password, location);
		login_buttons.add(this.login_signup, location);
		location.gridx = 1;
		login_name.add(text_name, location);
		login_password.add(text_password, location);
		login_buttons.add(login_button, location);
		location.gridx = 0;
		location.gridy = 1;
		login_cen_panel.add(login_name, location);
		location.gridy = 2;
		login_cen_panel.add(login_password, location);
		location.gridy = 0;
		login_cen_panel.add(error, location);
		location.gridy = 3;
		login_cen_panel.add(login_buttons, location);
		panel.add(login_cen_panel, BorderLayout.CENTER);
		panel.add(login_nor_panel, BorderLayout.NORTH);
		login_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user = find(text_name.getText());
				if (user.getAge() != -1) {
					if (user.getPassword().equals(text_password.getText())) {
						search_setup(search_panel);
						if (user.equals(manager)) {
							layout.show(window.getContentPane(), "search_panel");
						} else {
							if (user.getExp() >= 0) {
								layout.show(window.getContentPane(), "search_panel");
							} else {
								int[] k = find_kid(user.getUsername());
								profile_setup(profile_panel, groups[k[0]].getKids()[k[1]].getData());
								layout.show(window.getContentPane(), "profile_panel");
							}
						}
					} else {
						error.setText("incorect password!");
					}
				} else {
					error.setText("user not found!");
				}
			}
		});
	}

	// signup panel
	private void signup_setup(JPanel panel) {
		// GUI
		JPanel title_panel = new JPanel();
		JPanel nor_panel = new JPanel();
		JPanel buttons_panel = new JPanel();
		JLabel title_label = new JLabel("signup");
		JPanel kid_panel = new JPanel();
		JPanel kid_panel2 = new JPanel();
		JPanel left_panel = new JPanel();
		JPanel grow_panel = new JPanel();
		JPanel cen_panel = new JPanel();
		JPanel title_cen = new JPanel();
		JPanel error_panel = new JPanel();
		JButton kid = new JButton("kid");
		JButton grow = new JButton("instructor");
		JButton confirm = new JButton("confirm");
		this.signup_login = new JButton("back");
		title_cen.setLayout(new FlowLayout());
		title_panel.setLayout(new GridLayout(1, 3));
		cen_panel.setLayout(new GridLayout(1, 2));
		panel.setLayout(new BorderLayout());
		nor_panel.setLayout(new GridLayout(2, 1));
		buttons_panel.setLayout(new GridLayout(1, 2));
		left_panel.setLayout(new GridLayout(2, 1));
		cen_panel.add(kid_panel);
		cen_panel.add(left_panel);
		left_panel.add(kid_panel2);
		left_panel.add(grow_panel);
		title_cen.add(title_label);
		title_panel.add(this.signup_login);
		title_panel.add(title_cen);
		title_panel.add(confirm);
		nor_panel.add(title_panel);
		nor_panel.add(buttons_panel);
		buttons_panel.add(kid);
		buttons_panel.add(grow);
		// kid_panel-

		kid_panel.setLayout(new GridLayout(6, 1));
		kid_panel2.setLayout(new GridLayout(3, 1));
		error_panel.setLayout(new GridLayout(2, 1));
		// error
		JLabel error = new JLabel("");
		error_panel.add(error);
		error.setForeground(Color.red);
		error.setFont(new Font("Tahoma", Font.PLAIN, 10));
		// error2
		JLabel error2 = new JLabel("");
		error_panel.add(error2);
		error2.setForeground(Color.red);
		error2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		error_panel.setVisible(false);
		kid_panel.add(error_panel);
		// name
		JPanel name_panel = new JPanel();
		JLabel name_label = new JLabel("name");
		JTextField name_text = new JTextField();
		name_text.setPreferredSize(new Dimension(100, 25));
		name_panel.add(name_label);
		name_panel.add(name_text);
		kid_panel.add(name_panel);
		// id
		JPanel id_panel = new JPanel();
		JLabel id_label = new JLabel("id");
		JTextField id_text = new JTextField();
		id_text.setPreferredSize(new Dimension(100, 25));
		id_panel.add(id_label);
		id_panel.add(id_text);
		kid_panel.add(id_panel);
		// gender
		JPanel gender_panel = new JPanel();
		JLabel gender_label = new JLabel("gender");
		JTextField gender_text = new JTextField();
		gender_text.setPreferredSize(new Dimension(100, 25));
		gender_panel.add(gender_label);
		gender_panel.add(gender_text);
		kid_panel.add(gender_panel);
		// age
		JPanel age_panel = new JPanel();
		JLabel age_label = new JLabel("age");
		JTextField age_text = new JTextField();
		age_text.setPreferredSize(new Dimension(100, 25));
		age_panel.add(age_label);
		age_panel.add(age_text);
		kid_panel.add(age_panel);
		// password
		JPanel password_panel = new JPanel();
		JLabel password_label = new JLabel("password");
		JTextField password_text = new JTextField();
		password_text.setPreferredSize(new Dimension(100, 25));
		password_panel.add(password_label);
		password_panel.add(password_text);
		kid_panel.add(password_panel);
		// phone
		JPanel phone_panel = new JPanel();
		JLabel phone_label = new JLabel("phone");
		JTextField phone_text = new JTextField();
		phone_text.setPreferredSize(new Dimension(100, 25));
		phone_panel.add(phone_label);
		phone_panel.add(phone_text);
		kid_panel2.add(phone_panel);
		// allergy
		JPanel allergy_panel = new JPanel();
		JLabel allergy_label = new JLabel("allergy");
		JTextField allergy_text = new JTextField();
		allergy_text.setPreferredSize(new Dimension(100, 25));
		allergy_panel.add(allergy_label);
		allergy_panel.add(allergy_text);
		kid_panel2.add(allergy_panel);
		// username
		JPanel username_panel = new JPanel();
		JLabel username_label = new JLabel("username");
		JTextField username_text = new JTextField();
		username_text.setPreferredSize(new Dimension(100, 25));
		username_panel.add(username_label);
		username_panel.add(username_text);
		kid_panel2.add(username_panel);

		// grow panel

		grow_panel.setLayout(new GridLayout(3, 1));
		// exp
		JPanel exp_panel = new JPanel();
		JLabel exp_label = new JLabel("exp");
		JTextField exp_text = new JTextField();
		exp_text.setPreferredSize(new Dimension(100, 25));
		exp_panel.add(exp_label);
		exp_panel.add(exp_text);
		grow_panel.add(exp_panel);
		// bank
		JPanel bank_panel = new JPanel();
		JLabel bank_label = new JLabel("bank");
		JTextField bank_text = new JTextField();
		bank_text.setPreferredSize(new Dimension(100, 25));
		bank_panel.add(bank_label);
		bank_panel.add(bank_text);
		grow_panel.add(bank_panel);
		grow_panel.setVisible(false);
		kid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page = false;
				grow_panel.setVisible(false);
			}
		});
		grow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page = true;
				grow_panel.setVisible(true);
			}
		});
		// confirm?
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!page) {
					// kids
					if (name_text.getText().equals("") || id_text.getText().length() != 9
							|| age_text.getText().equals("") || password_text.getText().equals("")
							|| (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
									&& !gender_text.getText().equals("other"))
							|| phone_text.getText().length() != 10 || username_text.getText().equals("")) {
						error.setText("");
						error2.setText("");
						if (name_text.getText().equals("")) {
							error.setText(error.getText() + ", invalid name");
						}
						if (id_text.getText().length() != 9) {
							error.setText(error.getText() + ", invalid id");
						}
						if (age_text.getText().equals("")) {
							error.setText(error.getText() + ", invalid age");
						}
						if (password_text.getText().equals("")) {
							error.setText(error.getText() + ", invalid password");
						}
						if (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
								&& !gender_text.getText().equals("other")) {
							error2.setText(error2.getText() + ", invalid gender");
						}
						if (phone_text.getText().length() != 10) {
							error2.setText(error2.getText() + ", invalid phone");
						}
						if (username_text.getText().equals("")) {
							error2.setText(error2.getText() + ", invalid username");
						}
						error_panel.setVisible(true);
					} else {
						if (find_kid(name_text.getText())[0] != -1) {
							error.setText("name is taken");
						} else {
							int gender = 0;
							switch (gender_text.getText()) {
							case "female":
								gender = 1;
								break;
							case "other":
								gender = 2;
								break;
							}
							Person temp = new Person(username_text.getText(), password_text.getText(),
									phone_text.getText(), name_text.getText(), id_text.getText(), gender,
									convert(age_text.getText()));
							Kid temp_kid = new Kid(temp, allergy_text.getText());
							groups[0].add(temp_kid);
							layout.show(window.getContentPane(), "login_panel");
						}
					}
				} else {
					// instractors
					if (name_text.getText().equals("") || id_text.getText().length() != 9
							|| age_text.getText().equals("") || password_text.getText().equals("")
							|| (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
									&& !gender_text.getText().equals("other"))
							|| phone_text.getText().length() != 10 || bank_text.getText().equals("")
							|| exp_text.getText().equals("") || username_text.getText().equals("")) {
						error.setText("");
						error2.setText("");
						if (name_text.getText().equals("")) {
							error.setText(error.getText() + ", invalid name");
						}
						if (id_text.getText().length() != 9) {
							error.setText(error.getText() + ", invalid id");
						}
						if (age_text.getText().equals("")) {
							error.setText(error.getText() + ", invalid age");
						}
						if (password_text.getText().equals("")) {
							error.setText(error.getText() + ", invalid password");
						}
						if (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
								&& !gender_text.getText().equals("other")) {
							error.setText(error.getText() + ", invalid gender");
						}
						if (phone_text.getText().length() != 10) {
							error2.setText(error2.getText() + ", invalid phone");
						}
						if (bank_text.getText().equals("")) {
							error2.setText(error2.getText() + ", invalid bank");
						}
						if (exp_text.getText().equals("")) {
							error2.setText(error2.getText() + ", invalid exp");
						}
						if (username_text.getText().equals("")) {
							error2.setText(error2.getText() + ", invalid username");
						}
						error_panel.setVisible(true);
					} else {
						int gender = 0;
						switch (gender_text.getText()) {
						case "female":
							gender = 1;
							break;
						case "other":
							gender = 2;
							break;
						}
						Person temp = new Person(username_text.getText(), name_text.getText(), password_text.getText(),
								phone_text.getText(), id_text.getText(), gender, convert(age_text.getText()),
								convert(exp_text.getText()));
						if (convert(age_text.getText()) < 16) {
							Helper temp_helper = new Helper(temp, bank_text.getText(), 0);
							add(temp_helper);
						} else {
							Adult temp_adult = new Adult(temp, bank_text.getText(), 0);
							add(temp_adult);
						}
						layout.show(window.getContentPane(), "login_panel");
					}
				}
				try {
					save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		panel.add(nor_panel, BorderLayout.NORTH);
		panel.add(cen_panel);
	}

	// profile panel
	private void profile_setup(JPanel panel, Person user_data) {
		panel.removeAll();
		int user_type = 0;
		// 0=kid
		// 2=adult
		// 1=helper
		// 3=manager
		if (user_data.getAge() >= 14) {
			if (user_data.getAge() < 16) {
				user_type = 1;
			} else {
				if (user_data.equals(manager)) {
					user_type = 3;
				} else {
					user_type = 2;
				}
			}

		}
		JPanel title_panel = new JPanel();
		JPanel nor_panel = new JPanel();
		JPanel cen_panel = new JPanel();
		JPanel error_panel = new JPanel();
		JPanel group_panel = new JPanel();
		JLabel title_label = new JLabel("profile");
		JLabel error = new JLabel("");
		JLabel error2 = new JLabel("");
		JLabel group_label = new JLabel();
		switch (user_type) {
		case 0:
			group_label = new JLabel("group " + find_kid(user_data.getUsername())[0]);
			break;
		case 2:
			group_label = new JLabel("group " + find_adult(user_data.getUsername()));
			break;
		case 1:
			group_label = new JLabel("group " + find_helper(user_data.getUsername()));
			break;
		case 3:
			group_label = new JLabel("non");
			break;
		}
		JButton edit_button = new JButton("edit");
		JButton back_button = new JButton("back");
		boolean edit = false;
		if (this.user.equals(user_data) || this.user.equals(this.manager)) {
			edit = true;
		}
		edit_button.setVisible(edit);
		panel.setLayout(new BorderLayout());
		title_panel.setLayout(new FlowLayout());
		nor_panel.setLayout(new GridLayout(1, 2));
		if (user_type == 0) {
			cen_panel.setLayout(new GridLayout(5, 2));
		} else {
			cen_panel.setLayout(new GridLayout(6, 2));
		}
		error_panel.setLayout(new GridLayout(2, 1));
		group_panel.setLayout(new GridLayout(2, 1));
		panel.add(nor_panel, BorderLayout.NORTH);
		nor_panel.add(title_panel);
		title_panel.add(title_label);
		panel.add(cen_panel);
		error_panel.add(error);
		error_panel.add(error2);
		group_panel.add(group_label);
		// edit
		if (edit) {
			cen_panel.add(error_panel);
			if (user_data.age < 14 && !this.user.equals(this.manager)) {
				group_panel.add(edit_button);
			} else {
				JPanel place_older = new JPanel();
				place_older.setLayout(new GridLayout(1, 2));
				place_older.add(back_button);
				place_older.add(edit_button);
				group_panel.add(place_older);
			}
		} else {
			JPanel empty2 = new JPanel();
			cen_panel.add(back_button);
			group_panel.add(empty2);
		}
		cen_panel.add(group_panel);
		group_panel.add(group_label);
		error.setFont(new Font("Tahoma", Font.PLAIN, 10));
		error2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		error.setForeground(Color.red);
		error2.setForeground(Color.red);
		// name
		JPanel name_panel = new JPanel();
		JLabel name_label = new JLabel("name");
		JTextField name_text = new JTextField(user_data.getUsername());
		name_text.setPreferredSize(new Dimension(100, 25));
		name_panel.add(name_label);
		name_panel.add(name_text);
		cen_panel.add(name_panel);
		name_text.setEditable(false);
		// id
		JPanel id_panel = new JPanel();
		JLabel id_label = new JLabel("id");
		JTextField id_text = new JTextField(user_data.getId());
		id_text.setPreferredSize(new Dimension(100, 25));
		id_panel.add(id_label);
		id_panel.add(id_text);
		cen_panel.add(id_panel);
		id_text.setEditable(false);
		// gender
		JPanel gender_panel = new JPanel();
		JLabel gender_label = new JLabel("gender");
		JTextField gender_text = new JTextField(user_data.getGender());
		gender_text.setPreferredSize(new Dimension(100, 25));
		gender_panel.add(gender_label);
		gender_panel.add(gender_text);
		cen_panel.add(gender_panel);
		gender_text.setEditable(false);
		// age
		JPanel age_panel = new JPanel();
		JLabel age_label = new JLabel("age");
		JTextField age_text = new JTextField("" + user_data.getAge());
		age_text.setPreferredSize(new Dimension(100, 25));
		age_panel.add(age_label);
		age_panel.add(age_text);
		cen_panel.add(age_panel);
		age_text.setEditable(false);
		// password
		JPanel password_panel = new JPanel();
		JLabel password_label = new JLabel("password");
		JTextField password_text = new JTextField(user_data.getPassword());
		password_text.setPreferredSize(new Dimension(100, 25));
		password_panel.add(password_label);
		password_panel.add(password_text);
		cen_panel.add(password_panel);
		password_text.setEditable(false);
		// phone
		JPanel phone_panel = new JPanel();
		JLabel phone_label = new JLabel("phone");
		JTextField phone_text = new JTextField(user_data.getPhone());
		phone_text.setPreferredSize(new Dimension(100, 25));
		phone_panel.add(phone_label);
		phone_panel.add(phone_text);
		cen_panel.add(phone_panel);
		phone_text.setEditable(false);
		// allergy
		JPanel allergy_panel = new JPanel();
		JLabel allergy_label = new JLabel("allergy");
		JTextField allergy_text = new JTextField();
		if (user_type == 0) {
			allergy_text.setText(
					this.groups[find_group_kid(user_data.getUsername())].getKids()[find_kid(user_data.getUsername())[1]]
							.getAllergy());
			allergy_text.setPreferredSize(new Dimension(100, 25));
			allergy_panel.add(allergy_label);
			allergy_panel.add(allergy_text);
			cen_panel.add(allergy_panel);
			allergy_text.setEditable(false);
		}
		// hours
		JPanel hours_panel = new JPanel();
		JLabel hours_label = new JLabel("hours");
		JTextField hours_text = new JTextField();
		if (user_type == 1) {
			hours_text.setText("" + this.groups[find_helper(user_data.getUsername())].getHelper().getHours());
		}
		if (user_type == 2) {
			hours_text.setText("" + this.groups[find_adult(user_data.getUsername())].getAdult().getHours());
		}
		if (user_type == 1 || user_type == 2) {
			hours_text.setPreferredSize(new Dimension(100, 25));
			hours_panel.add(hours_label);
			hours_panel.add(hours_text);
			cen_panel.add(hours_panel);
			hours_text.setEditable(false);
		}
		// username
		JPanel username_panel = new JPanel();
		JLabel username_label = new JLabel("username");
		JTextField username_text = new JTextField(user_data.getUsername());
		username_text.setPreferredSize(new Dimension(100, 25));
		username_panel.add(username_label);
		username_panel.add(username_text);
		cen_panel.add(username_panel);
		username_text.setEditable(false);
		// bank/exp
		JPanel bank_panel = new JPanel();
		JLabel bank_label = new JLabel();
		JTextField bank_text = new JTextField();
		JPanel exp_panel = new JPanel();
		JLabel exp_label = new JLabel();
		JTextField exp_text = new JTextField();
		switch (user_type) {
		case 1:
			// bank
			bank_label.setText("bank");
			bank_text.setText(this.groups[find_helper(user_data.getUsername())].getHelper().getBank());
			bank_text.setPreferredSize(new Dimension(100, 25));
			bank_panel.add(bank_label);
			bank_panel.add(bank_text);
			cen_panel.add(bank_panel);
			bank_text.setEditable(false);
			// exp
			exp_label.setText("exp");
			exp_text.setText("" + user_data.getExp());
			exp_text.setPreferredSize(new Dimension(100, 25));
			exp_panel.add(exp_label);
			exp_panel.add(exp_text);
			cen_panel.add(exp_panel);
			exp_text.setEditable(false);
			break;
		case 2:
			// bank
			bank_label.setText("bank");
			bank_text.setText(this.groups[find_adult(user_data.getUsername())].getAdult().getBank());
			bank_text.setPreferredSize(new Dimension(100, 25));
			bank_panel.add(bank_label);
			bank_panel.add(bank_text);
			cen_panel.add(bank_panel);
			bank_text.setEditable(false);
			// exp
			exp_label.setText("exp");
			exp_text.setText("" + user_data.getExp());
			exp_text.setPreferredSize(new Dimension(100, 25));
			exp_panel.add(exp_label);
			exp_panel.add(exp_text);
			cen_panel.add(exp_panel);
			exp_text.setEditable(false);
			break;
		}
		int user_type_f = user_type;
		edit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (edit_button.getText().equals("edit")) {
					edit_button.setText("done");
					username_text.setEditable(true);
					name_text.setEditable(true);
					id_text.setEditable(true);
					allergy_text.setEditable(true);
					phone_text.setEditable(true);
					password_text.setEditable(true);
					age_text.setEditable(true);
					gender_text.setEditable(true);
					bank_text.setEditable(true);
					exp_text.setEditable(true);
					hours_text.setEditable(true);
				} else {
					if (user_type_f == 0 || user_type_f == 3) {
						if (name_text.getText().equals("") || id_text.getText().length() != 9
								|| age_text.getText().equals("") || password_text.getText().equals("")
								|| (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
										&& !gender_text.getText().equals("other"))
								|| phone_text.getText().length() != 10 || username_text.getText().equals("")) {
							error.setText("");
							error2.setText("");
							if (name_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid name");
							}
							if (id_text.getText().length() != 9) {
								error.setText(error.getText() + ", invalid id");
							}
							if (age_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid age");
							}
							if (password_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid password");
							}
							if (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
									&& !gender_text.getText().equals("other")) {
								error2.setText(error2.getText() + ", invalid gender");
							}
							if (phone_text.getText().length() != 10) {
								error2.setText(error2.getText() + ", invalid phone");
							}
							if (username_text.getText().equals("")) {
							}
							error_panel.setVisible(true);
						} else {
							error.setText("the change was successfull");
							error.setForeground(Color.green);
							error2.setText("");
							edit_button.setText("edit");
							username_text.setEditable(false);
							name_text.setEditable(false);
							id_text.setEditable(false);
							allergy_text.setEditable(false);
							phone_text.setEditable(false);
							password_text.setEditable(false);
							age_text.setEditable(false);
							gender_text.setEditable(false);
							bank_text.setEditable(false);
							exp_text.setEditable(false);
							user_data.setGender(gender_text.getText());
							user_data.setUsername(username_text.getText());
							user_data.setName(name_text.getText());
							user_data.setId(id_text.getText());
							user_data.setPhone(phone_text.getText());
							user_data.setPassword(password_text.getText());
							user_data.setAge(convert(age_text.getText()));
							if (user_type_f == 0) {
								groups[find_group_kid(user_data.getUsername())]
										.getKids()[find_kid(user_data.getUsername())[1]]
										.setAllergy(allergy_text.getText());
							}
						}
					}
					if (user_type_f == 1 || user_type_f == 2) {
						if (name_text.getText().equals("") || id_text.getText().length() != 9
								|| age_text.getText().equals("") || password_text.getText().equals("")
								|| (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
										&& !gender_text.getText().equals("other"))
								|| phone_text.getText().length() != 10 || username_text.getText().equals("")
								|| bank_text.getText().equals("") || exp_text.getText().equals("")
								|| hours_text.getText().equals("")) {
							error.setText("");
							error2.setText("");
							if (name_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid name");
							}
							if (id_text.getText().length() != 9) {
								error.setText(error.getText() + ", invalid id");
							}
							if (age_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid age");
							}
							if (password_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid password");
							}
							if (hours_text.getText().equals("")) {
								error.setText(error.getText() + ", invalid hours");
							}
							if (!gender_text.getText().equals("male") && !gender_text.getText().equals("female")
									&& !gender_text.getText().equals("other")) {
								error2.setText(error2.getText() + ", invalid gender");
							}
							if (phone_text.getText().length() != 10) {
								error2.setText(error2.getText() + ", invalid phone");
							}
							if (username_text.getText().equals("")) {
								error2.setText(error2.getText() + ", invalid username");
							}
							if (bank_text.getText().equals("")) {
								error2.setText(error2.getText() + ", invalid bank");
							}
							if (exp_text.getText().equals("")) {
								error2.setText(error2.getText() + ", invalid exp");
							}
							error_panel.setVisible(true);
						} else {
							error.setText("the change was successfull");
							error.setForeground(Color.green);
							error2.setText("");
							edit_button.setText("edit");
							username_text.setEditable(false);
							name_text.setEditable(false);
							id_text.setEditable(false);
							allergy_text.setEditable(false);
							phone_text.setEditable(false);
							password_text.setEditable(false);
							age_text.setEditable(false);
							gender_text.setEditable(false);
							bank_text.setEditable(false);
							exp_text.setEditable(false);
							user_data.setGender(gender_text.getText());
							user_data.setUsername(username_text.getText());
							user_data.setName(name_text.getText());
							user_data.setId(id_text.getText());
							user_data.setPhone(phone_text.getText());
							user_data.setPassword(password_text.getText());
							user_data.setAge(convert(age_text.getText()));
							user_data.setExp(convert(exp_text.getText()));
							if (user_type_f == 1) {
								groups[find_helper(user_data.getUsername())].getHelper().setBank(bank_text.getText());
							} else {
								groups[find_adult(user_data.getUsername())].getHelper().setBank(bank_text.getText());
							}
						}
					}
					try {
						save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		back_button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				layout.show(window.getContentPane(), "search_panel");
			}

		});
	}

	// search panel
	private void search_setup(JPanel panel) {
		panel.setLayout(new BorderLayout());
		JPanel nor_panel = new JPanel();
		JPanel title_panel = new JPanel();
		JPanel search_panel = new JPanel();
		JButton remove_button = new JButton("remove");
		JLabel title_label = new JLabel("search");
		JLabel search_label = new JLabel("search:");
		JButton search_button = new JButton("by name");
		JButton profile_label_button = new JButton("profile");
		JButton add_button = new JButton("add");
		search_type = 0;
		// 0 = by name
		// 1 = by allergy
		nor_panel.setLayout(new GridLayout(2, 1));
		title_panel.setLayout(new FlowLayout());
		cen_panel.setLayout(new GridLayout(1, 0));
		search_text.setPreferredSize(new Dimension(70, 25));
		search_button.setPreferredSize(new Dimension(100, 25));
		profile_label_button.setPreferredSize(new Dimension(60, 25));
		remove_button.setPreferredSize(new Dimension(70, 25));
		profile_label_button.setBackground(Color.blue);
		remove_button.setBackground(Color.red);
		add_button.setBackground(Color.white);
		profile_label_button.setForeground(Color.white);
		profile_label_button.setFont(new Font("Tahoma", Font.ITALIC, 8));
		remove_button.setFont(new Font("Tahoma", Font.ITALIC, 10));
		remove_button.setFont(new Font("Tahoma", Font.ITALIC, 10));
		title_panel.add(title_label);
		if (user.equals(manager)) {
			search_panel.add(add_button);
		} else {
			search_panel.add(search_label);
		}
		search_panel.add(search_text);
		search_panel.add(search_button);
		search_panel.add(profile_label_button);
		search_panel.add(remove_button);
		nor_panel.add(title_panel);
		nor_panel.add(search_panel);
		panel.add(nor_panel, BorderLayout.NORTH);
		panel.add(cen_panel);
		search();
		remove_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (move.getAge() != -1) {
					if (!find_kid(move).equals(Kid.empty())) {
						groups[find_kid(move.getUsername())[0]].remove(find_kid(move));
					}
					if (find_adult(move.getUsername()) != -2) {
						groups[find_adult(move.getUsername())].setAdult(Adult.empty());
					}
					if (find_helper(move.getUsername()) != -2) {
						groups[find_helper(move.getUsername())].setHelper(Helper.empty());
					}
					if (find_adult(move.getUsername()) == -1) {
						remove_a(move);
					}
					if (find_helper(move.getUsername()) == -1) {
						remove_h(move);
					}
					move = Person.empty();
				}
				for (int i = 1; i < groups.length; i++) {
					if (groups[i].getKids().length == 0) {
						remove_group(groups[i]);
					}
				}
				search();
			}
		});
		add_button.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				JTextField name = new JTextField();
				name.setPreferredSize(add_button.getSize());
				search_panel.removeAll();
				search_panel.add(name);
				search_panel.add(search_text);
				search_panel.add(search_button);
				search_panel.add(profile_label_button);
				search_panel.add(remove_button);
				window.resize(window.getWidth() - 1, window.getHeight() - 1);
				window.resize(window.getWidth() + 1, window.getHeight() + 1);
				name.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Kid[] temp_k = { Kid.empty() };
						temp_k[0].getData().setAge(0);
						Group temp = new Group(name.getText(), temp_k);
						add_group(temp);
						search_panel.removeAll();
						search_panel.add(add_button);
						search_panel.add(search_text);
						search_panel.add(search_button);
						search_panel.add(profile_label_button);
						search_panel.add(remove_button);
						search();
					}
				});
			}
		});
		search_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (search_type == 1) {
					search_button.setText("by group");
				}
				if (search_type == 0) {
					search_button.setText("by allergy");
				}
				if (search_type == 2) {
					search_button.setText("by name");
				}
				search_type = (search_type + 1) % 3;

			}
		});
		profile_label_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				profile_setup(profile_panel, user);
				layout.show(window.getContentPane(), "profile_panel");
			}
		});
		search_text.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				search();
			}

			public void removeUpdate(DocumentEvent e) {
				search();
			}

			public void insertUpdate(DocumentEvent e) {
				search();
			}
		});
	}

	// the main search function
	@SuppressWarnings("deprecation")
	private void search() {
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cen_panel.removeAll();
		window.resize(window.getWidth() - 1, window.getHeight() - 1);
		window.resize(window.getWidth() + 1, window.getHeight() + 1);
		Kid[] temp_k = new Kid[0];
		if (search_type == 0) {
			temp_k = search_kids_s(search_text.getText());
		}
		if (search_type == 1) {
			temp_k = search_kids_a(search_text.getText());
		}
		if (search_type == 2) {
			temp_k = search_kids_g(search_text.getText());
		}
		int[] numbers = count_groups(temp_k);
		JPanel[] group_title = new JPanel[numbers.length];
		JPanel[] options = new JPanel[temp_k.length];
		JLabel[] group_label = new JLabel[group_title.length];
		JButton[] group_button = new JButton[group_title.length];
		for (int i = 0; i < group_title.length; i++) {
			// deffing group button
			group_button[i] = new JButton();
			group_button[i].setPreferredSize(new Dimension(10, 10));
			group_button[i].putClientProperty("group", numbers[i]);
			group_button[i].setVisible(false);
			// rest of the code
			JPanel place_holder = new JPanel();
			group_title[i] = new JPanel();
			group_title[i].setLayout(new GridLayout(0, 1));
			group_label[i] = new JLabel("group " + groups[numbers[i]].getName());
			group_label[i].setFont(new Font("Tahoma", Font.BOLD, 12));
			place_holder.add(group_label[i]);
			place_holder.add(group_button[i]);
			group_title[i].add(place_holder);
			// movment move
			int index = i;
			group_button[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int from = find_kid(move.getUsername())[0];
					int to = convert("" + group_button[index].getClientProperty("group"));
					if (groups[to].getKids()[0].getData().getAge() == 0) {
						groups[to].remove(groups[to].getKids()[0]);
					}
					groups[to].add(find_kid(move));
					groups[from].remove(find_kid(move));
					if (groups[from].getKids().length == 0) {
						add(groups[from].getAdult());
						add(groups[from].getHelper());
						remove_group(groups[from]);
					}
					move = Person.empty();
					search();
				}
			});
		}
		for (int i = 0; i < options.length; i++) {
			options[i] = new JPanel();
			JLabel name_label = new JLabel(temp_k[i].getData().getUsername());
			JButton enter_button = new JButton();
			JButton change_button = new JButton();
			change_button.setBackground(Color.red);
			enter_button.setBackground(Color.blue);
			enter_button.putClientProperty("tag", temp_k[i].getData());
			change_button.putClientProperty("tag", temp_k[i].getData());
			options[i].setLayout(new FlowLayout());
			change_button.setPreferredSize(new Dimension(15, 15));
			enter_button.setPreferredSize(new Dimension(15, 15));
			options[i].add(name_label);
			options[i].add(enter_button);
			options[i].add(change_button);
			group_title[locate(group_label, temp_k[i])].add(options[i]);
			enter_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					profile_setup(profile_panel, (Person) enter_button.getClientProperty("tag"));
					layout.show(window.getContentPane(), "profile_panel");
				}
			});
			change_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < group_button.length; i++) {
						group_button[i].setVisible(false);
					}
					Kid temp_kid = groups[find_group_kid(
							((Person) change_button.getClientProperty("tag")).getUsername())]
							.getKids()[find_kid(((Person) change_button.getClientProperty("tag")).getUsername())[1]];
					for (int i = 0; i < group_button.length; i++) {
						if (!group_button[i].getClientProperty("group")
								.equals(find_group_kid(temp_kid.getData().getUsername()))) {
							group_button[i].setVisible(true);
						}
					}
					move = temp_kid.getData();
				}
			});
		}
		for (int i = 0; i < group_title.length; i++) {
			cen_panel.add(group_title[i]);
		}
		array_add(numbers, search_type);
	}

	// search
	private Kid[] search_kids_s(String str) {
		Kid[] temp = new Kid[0];
		for (int i = 0; i < groups.length; i++) {
			for (int h = 0; h < groups[i].getKids().length; h++) {
				if (this.groups[i].getKids()[h].getData().getUsername().indexOf(str) != -1) {
					temp = add(temp, groups[i].getKids()[h]);
				}
			}
		}
		return temp;
	}

	private Kid[] search_kids_a(String str) {
		Kid[] temp = new Kid[0];
		for (int i = 0; i < groups.length; i++) {
			for (int h = 0; h < groups[i].getKids().length; h++) {
				if (groups[i].getKids()[h].getAllergy().indexOf(str) != -1) {
					temp = add(temp, groups[i].getKids()[h]);
				}
			}
		}
		return temp;
	}

	private Kid[] search_kids_g(String str) {
		Kid[] temp = new Kid[0];
		for (int i = 0; i < groups.length; i++) {
			if (groups[i].getName().indexOf(str) != -1) {
				temp = add(temp, groups[i].getKids());
			}
		}
		return temp;
	}

	// find
	public Person find(String name) {
		if (this.manager.getUsername().equals(name)) {
			return this.manager;
		}
		for (int i = 0; i < a_non_group.length; i++) {
			if (a_non_group[i].getData().getUsername().equals(name)) {
				return a_non_group[i].getData();
			}
		}
		for (int i = 0; i < h_non_group.length; i++) {
			if (h_non_group[i].getData().getUsername().equals(name)) {
				return h_non_group[i].getData();
			}
		}
		for (int i = 0; i < this.groups.length; i++) {
			if (this.groups[i].getAdult().getData().getUsername().equals(name)) {
				return this.groups[i].adult.data;
			}
			if (this.groups[i].getHelper().getData().getUsername().equals(name)) {
				return this.groups[i].helper.data;
			}
			for (int h = 0; h < this.groups[i].kids.length; h++) {
				if (this.groups[i].getKids()[h].getData().getUsername().equals(name)) {
					return this.groups[i].getKids()[h].getData();
				}
			}
		}
		return Person.empty();
	}

	public int find_adult(String name) {
		for (int i = 0; i < this.a_non_group.length; i++) {
			if (this.a_non_group[i].getData().getUsername().equals(name)) {
				return -1;
			}
		}
		for (int i = 0; i < this.groups.length; i++) {
			if (this.groups[i].getAdult().getData().getUsername().equals(name)) {
				return i;
			}
		}
		return -2;
	}

	public int find_helper(String name) {
		for (int i = 0; i < this.h_non_group.length; i++) {
			if (this.h_non_group[i].getData().getUsername().equals(name)) {
				return -1;
			}
		}
		for (int i = 0; i < this.groups.length; i++) {
			if (this.groups[i].getHelper().getData().getUsername().equals(name)) {
				return i;
			}
		}
		return -2;
	}

	public int[] find_kid(String name) {
		int[] temp = { -1, -1 };
		for (int i = 0; i < this.groups.length; i++) {
			for (int h = 0; h < this.groups[i].getKids().length; h++) {
				if (this.groups[i].getKids()[h].getData().getUsername().equals(name)) {
					temp[0] = i;
					temp[1] = h;
					return temp;
				}
			}
		}
		return temp;
	}

	public Kid find_kid(Person per) {
		for (int i = 0; i < this.groups.length; i++) {
			for (int h = 0; h < this.groups[i].getKids().length; h++) {
				if (this.groups[i].getKids()[h].getData().equals(per)) {
					return this.groups[i].getKids()[h];
				}
			}
		}
		return Kid.empty();
	}

	public int find_group_kid(String name) {
		for (int i = 0; i < this.groups.length; i++) {
			for (int h = 0; h < this.groups[i].getKids().length; h++) {
				if (this.groups[i].getKids()[h].getData().getUsername().equals(name)) {
					return i;
				}
			}
		}
		return -1;
	}

	public int find_group(String str) {
		for (int i = 0; i < this.groups.length; i++) {
			if (str.equals(this.groups[i].getName())) {
				return i;
			}
		}
		return -1;
	}

	// add
	private int[] array_add(int[] array, int num) {
		int[] temp = new int[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			temp[i] = array[i];
		}
		temp[array.length] = num;
		array = temp;
		return temp;
	}

	public Kid[] add(Kid[] array, Kid k) {
		Kid[] temp_array = new Kid[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			temp_array[i] = array[i];
		}
		temp_array[array.length] = k;
		return temp_array;
	}

	public Kid[] add(Kid[] array, Kid[] k) {
		Kid[] temp_array = new Kid[array.length + k.length];
		for (int i = 0; i < array.length; i++) {
			temp_array[i] = array[i];
		}
		for (int i = 0; i < k.length; i++) {
			temp_array[i + array.length] = k[i];
		}
		return temp_array;
	}

	public void add_group(Group g) {
		Group[] temp = new Group[this.groups.length + 1];
		for (int i = 0; i < this.groups.length; i++) {
			temp[i] = this.groups[i];
		}
		temp[this.groups.length] = g;
		this.groups = temp;
	}

	public void add(Helper temp_h) {
		Helper[] temp = new Helper[this.h_non_group.length + 1];
		for (int i = 0; i < this.h_non_group.length; i++) {
			temp[i] = this.h_non_group[i];
		}
		temp[this.h_non_group.length] = temp_h;
		this.h_non_group = temp;
	}

	public void add(Adult temp_h) {
		Adult[] temp = new Adult[this.a_non_group.length + 1];
		for (int i = 0; i < this.a_non_group.length; i++) {
			temp[i] = this.a_non_group[i];
		}
		temp[this.h_non_group.length] = temp_h;
		this.a_non_group = temp;
	}

	public void add(Kid temp_k) {
		groups[0].add(temp_k);
	}

	// remove
	public Kid[] remove(Kid[] array, Kid k) {
		Kid[] temp_array = new Kid[array.length - 1];
		int change = 0;
		for (int i = 0; i < array.length; i++) {
			if (!array[i].equals(k)) {
				temp_array[i + change] = array[i];
			} else {
				change = -1;
			}
		}
		return temp_array;
	}

	public void remove_group(Group g) {
		int change = 0;
		Group[] temp = new Group[this.groups.length - 1];
		for (int i = 0; i < this.groups.length; i++) {
			if (!this.groups[i].equals(g)) {
				temp[i + change] = this.groups[i];
			} else {
				change = -1;
			}
		}
		this.groups = temp;
	}

	public void remove_h(Person temp_h) {
		int change = 0;
		Helper[] temp = new Helper[this.h_non_group.length - 1];
		for (int i = 0; i < this.h_non_group.length; i++) {
			if (!this.h_non_group[i].getData().equals(temp_h)) {
				temp[i + change] = this.h_non_group[i];
			} else {
				change = -1;
			}
		}
		this.h_non_group = temp;
	}

	public void remove_a(Person temp_h) {
		int change = 0;
		Adult[] temp = new Adult[this.a_non_group.length - 1];
		for (int i = 0; i < this.a_non_group.length; i++) {
			if (!this.a_non_group[i].getData().equals(temp_h)) {
				temp[i + change] = this.a_non_group[i];
			} else {
				change = -1;
			}
		}
		this.a_non_group = temp;
	}

	public void remove(Kid temp_k) {
		groups[0].remove(temp_k);
	}

	// get/set
	public JFrame getWindow() {
		return window;
	}

	public void setWindow(JFrame login) {
		this.window = login;
	}

	public Group[] getGroups() {
		return groups;
	}

	public void setGroups(Group[] groups) {
		this.groups = groups;
	}

	public Person getManager() {
		return manager;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	// to save data
	public void save() throws IOException {
		try {
			PrintWriter text_file = new PrintWriter(new FileWriter(fil));
			Person m = this.manager;
			text_file.println(
					"manager (" + m.getUsername() + "," + m.getName() + "," + m.getPassword() + "," + m.getPhone() + ","
							+ m.getId() + "," + m.getGender() + "," + m.getAge() + "," + m.getExp() + ",)");
			text_file.println("groups{");
			for (int i = 0; i < this.groups.length; i++) {
				Group g = this.groups[i];
				text_file.print("	<" + g.getName() + "(");
				// print the adult
				text_file.print("A[" + g.getAdult().getData().getUsername() + "," + g.getAdult().getData().getName()
						+ "," + g.getAdult().getData().getPassword() + "," + g.getAdult().getData().getPhone() + ","
						+ g.getAdult().getData().getId() + "," + g.getAdult().getData().getGender() + ","
						+ g.getAdult().getData().getAge() + "," + g.getAdult().getData().getExp() + ","
						+ g.getAdult().getBank() + "]");
				// print the helper
				text_file.print(",H[" + g.getHelper().getData().getUsername() + "," + g.getHelper().getData().getName()
						+ "," + g.getHelper().getData().getPassword() + "," + g.getHelper().getData().getPhone() + ","
						+ g.getHelper().getData().getId() + "," + g.getHelper().getData().getGender() + ","
						+ g.getHelper().getData().getAge() + "," + g.getHelper().getData().getExp() + ","
						+ g.getHelper().getBank() + "]");
				// print the kids
				text_file.print(",K[");
				if (g.getKids().length == 0) {
					text_file.print(")");
				}
				for (int h = 0; h < g.getKids().length; h++) {
					Kid k = g.getKids()[h];
					text_file.print("K(" + k.getData().getUsername() + "," + k.getData().getName() + ","
							+ k.getData().getPassword() + "," + k.getData().getPhone() + "," + k.getData().getId() + ","
							+ k.getData().getGender() + "," + k.getData().getAge() + "," + k.getAllergy() + ")");
				}
				text_file.println("])");
			}
			text_file.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
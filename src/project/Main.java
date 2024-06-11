package project;

//19.5.2024
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
	public static void main(String[] args) throws IOException {
		File data = new File("C:\Users\uriba\OneDrive\מסמכים\Visual Studio Code\summer_camp\src\project\data.txt");
		General normal = new General(data);
	}
}
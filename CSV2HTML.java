

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class CSV2HTML {
	/**
	 * 
	 * @param sc Scanner object
	 * @throws CSVAttributeMissing
	 * @throws CSVDataMissing
	 */
	// Checks if a there is a missing data or missing attribute in the file
	public static void CheckifMissing(Scanner sc) throws CSVAttributeMissing, CSVDataMissing {
		String s;
		int linenum = 3;
		sc.nextLine();
		sc.nextLine();
		s = sc.nextLine();
		// splits the line at "," and checks for missing attribute
		for (String data : s.split(",")) {
			if (data == "") {
				throw new CSVAttributeMissing("Error: there is/are attribute(s) missing in line " + linenum);
			}

		}
		// splits the line at "," and checks for missing data
		while (sc.hasNextLine()) {
			s = sc.nextLine();
			linenum++;
			if (s.contains("Note:")) {
				break;
			}
			for (String data : s.split(",")) {
				if (data == "") {
				throw new CSVDataMissing("Error: there is data missing in line " + linenum);

				}
			}
		}
	}

	/**
	 * 
	 * @param sc Scanner object
	 * @param pw PrintWriter object
	 * @throws CSVAttributeMissing
	 * @throws CSVDataMissing
	 */
	// Converts the CSV file to HTML file
	public static void ConvertCSVtoHTML(Scanner sc, PrintWriter pw) throws CSVAttributeMissing, CSVDataMissing {
		String s;
		int linenum = 2;

		// HTML header
		pw.write("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<style>\r\n"
				+ "table {font-family: arial, sans-serif;border-collapse: collapse;}\r\n"
				+ "td, th {border: 1px solid #000000;text-align: left;padding: 8px;}\r\n"
				+ "tr:nth-child(even) {background-color: #dddddd;}\r\n" + "span{font-size: small}\r\n" + "</style>\r\n"
				+ "<body>\r\n" + "\r\n" + "<table>");

		s = sc.nextLine();

		pw.println("<caption>" + s.substring(0, s.indexOf(",")) + "</caption>");
		s = sc.nextLine();

		pw.println("\n<tr>");

		// splits the line at "," and checks for missing attribute
		for (String data : s.split(",")) {
			pw.println("<th>" + data + "</th>");
			if (data == "") {
				throw new CSVAttributeMissing("Error: there is an attribute missing in line " + linenum);

			}
		}
		pw.println("</tr>");
		// splits the line at "," and checks for missing data
		while (sc.hasNextLine()) {
			s = sc.nextLine();
			linenum++;

			// checks if line starts with note and puts the line in span tags
			if (s.contains("Note:")) {
				pw.println("</table>\n<span>" + s.substring(0, s.indexOf(",")) + "</span>");
				break;
			}
			pw.println("\n<tr>");
			for (String data : s.split(",")) {
				pw.println("<td>" + data + "</td>");
				if (data == "") {
					throw new CSVDataMissing("Error: there is a data missing in line " + linenum);
				}
			}
			pw.println("</tr>");
		}
		pw.print("</body></html>");
		System.out.println("Conversion terminated successfully.");
		pw.close();
		sc.close();
	}

	/**
	 * 
	 * @param br Buffered Reader object
	 * @throws IOException
	 */
	// Display the content of the file to view
	public static void displayContent(BufferedReader br) throws IOException {
		int x;

		x = br.read();
		while (x != -1) {

			System.out.print((char) x);
			x = br.read();
		}
		br.close();
	}

	public static void main(String[] args) {

		PrintWriter pw = null;
		PrintWriter pw1 = null;
		Scanner sc = null;
		Scanner sc2 = null;

		BufferedReader br = null;
		String fileName;
		String s;
		Scanner keyboard = new Scanner(System.in);

		// prompt the user for the name of the CSV file to convert to HTML
		System.out.print("Please enter a file name to convert: ");

		fileName = keyboard.nextLine();

		try {
			pw1 = new PrintWriter(new FileOutputStream("Exceptions.log", true));
			sc = new Scanner(new FileInputStream(fileName));

			// checks if there is a missing data/attribute in the file
			CheckifMissing(sc);

		} catch (FileNotFoundException e) {
			System.out.print("Could not open input file " + fileName
					+ " for reading.\nPlease check that the file exists and is readable. This program will terminate after closing any opened files.");
			pw1.println("Could not open input file a for reading.\r\n"
					+ "Please check that the file exists and is readable. This program will terminate after closing any opened files.\n");
			pw1.close();

			System.exit(0);
		}

		catch (CSVAttributeMissing csvm) {
			String str = csvm.getMessage();
			System.out.println(str);
			pw1.println("Error: In file " + fileName + " Missing Attribute. File is not converted to HTML.\n");
			System.out.println("\nProgram will now terminate.");

			pw1.close();
			System.exit(0);
		}

		catch (CSVDataMissing csvd) {
			String str = csvd.getMessage();
			System.out.println(str);
			pw1.println("Warning: In file " + fileName + " Data Missing. File is not converted to HTML.\n");
			System.out.println("\nProgram will now terminate.");

			pw1.close();
			System.exit(0);
		}

		try {
			sc2 = new Scanner(new FileInputStream(fileName));

			pw = new PrintWriter(new FileOutputStream(fileName.substring(0, fileName.indexOf(".")) + ".html"));

			pw1 = new PrintWriter(new FileOutputStream("Exceptions.log"));

		} catch (FileNotFoundException e) {
			System.out.print("Could not open input file " + fileName
					+ " for reading.\nPlease check that the file exists and is readable. This program will terminate after closing any opened files.");

			System.exit(0);
		}

		// Converts CSV file to HTML
		try {
			ConvertCSVtoHTML(sc2, pw);
		}

		catch (CSVAttributeMissing csvm) {
			String str = csvm.getMessage();
			System.out.println(str);
			pw1.write("Error: In file " + fileName + " Missing Attribute. File is not converted to HTML.\n");
			System.out.println("\nProgram will now terminate.");

			pw1.close();
			System.exit(0);
		}

		catch (CSVDataMissing csvd) {
			String str = csvd.getMessage();
			System.out.println(str);
			pw1.println("Warning: In file " + fileName + " Data Missing. File is not converted to HTML.\n");
			System.out.println("\nProgram will now terminate.");

			pw1.close();
			System.exit(0);
		}

		// Message if no exceptions were found
		pw1.println("No exceptions found. " + fileName + " was successfully converted to HTML.\n");
		pw1.close();

		// prompt the user for a file name to view the content of
		System.out.print("\nEnter the name of the file you would like to view: ");
		String fileView = keyboard.nextLine();
		try {
			br = new BufferedReader(new FileReader(fileView));
		} catch (FileNotFoundException e) {
			System.out.println(
					"There is no file called " + fileView + ".\nYou have one last chance to enter a valid file name.");
			System.out.print("Enter another file name: ");
			fileView = keyboard.nextLine();
			try {
				br = new BufferedReader(new FileReader(fileView));
			} catch (FileNotFoundException e2) {
				System.out.println(
						"This is not a valid file name.\nYou have exhausted all your chances.\nProgram will now terminate.");
				System.exit(0);
			}
		}

		// Display the content of the file
		try {
			displayContent(br);
		}

		catch (IOException e) {
			System.out.println("Error: and error has occured while reading from the " + fileView
					+ "file. \nProgram will now terminate.");
			System.exit(0);
		}

	}

}

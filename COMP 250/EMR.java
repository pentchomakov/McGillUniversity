/* ACADEMIC INTEGRITY STATEMENT
 * 
 * By submitting this file, we state that all group members associated
 * with the assignment understand the meaning and consequences of cheating, 
 * plagiarism and other academic offenses under the Code of Student Conduct 
 * and Disciplinary Procedures (see www.mcgill.ca/students/srr for more information).
 * 
 * By submitting this assignment, we state that the members of the group
 * associated with this assignment claim exclusive credit as the authors of the
 * content of the file (except for the solution skeleton provided).
 * 
 * In particular, this means that no part of the solution originates from:
 * - anyone not in the assignment group
 * - Internet resources of any kind.
 * 
 * This assignment is subject to inspection by plagiarism detection software.
 * 
 * Evidence of plagiarism will be forwarded to the Faculty of Science's disciplinary
 * officer.
 */

package assignment2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import assignment2.Patient.Insurance;

import java.io.*;

/* A basic command line interface for an Electronic Medical Record System.
 * 
 * The simplest way to complete this assignment is to perform 1 functionality at a time. Start
 * with the code for the EMR constructor to import all data and then perform tasks 1-10
 * 		1.	Add a new patient to the EMR system
 *  	2.	Add a new Doctor to the EMR system
 *  	3.	Record new patient visit to the department
 *  	4.	Edit patient information *  	5.	Display list of all Patient IDs
 *  	6.	Display list of all Doctor IDs
 *  	7.	Print a Doctor's record
 *  	8.	Print a Patient's record
 *  	9.	Exit and save modifications
 * 	
 *	Complete the code provided as part of the assignment package. Fill in the \\TODO sections
 *  
 *  Do not change any of the function signatures. However, you can write additional helper functions 
 *  and test functions if you want.
 *  
 *  Do not define any new classes. Do not import any data structures. Do not call the sort functions
 *  of ArrayList class. Implement your own sorting functions and implement your own search function.
 *  
 *  Make sure your entire solution is in this file.
 *  
 *  We have simplified the task of reading the data from the Excel files. Instead of reading directly
 *  from Excel, each Sheet of the Excel file is saved as a comma separated file (csv) 
 * 
 */


public class EMR
{
	private String aDoctorFilePath;
	private String aPatientFilePath;
	private String aVisitsFilePath;
	private ArrayList<Doctor> doctorList;
	private ArrayList<Patient> patientList;
	
	/**
     * Used to invoke the EMR command line interface. You only need to change
     * the 3 filepaths.
	 */
	public static void main(String[] args) throws IOException
	{
		EMR system = new EMR("./Data/Doctors.csv", "./Data/Patients.csv", "./Data/Visits.csv");		//set the filepaths to be relative to the src folder
		system.displayMenu();
	}
	
	
	/**
	 * You don't have to modify the constructor, nor its code
	 * @param pDoctorFilePath
	 * @param pPatientFilePath
	 * @param pVisitsFilePath
	 */
	public EMR(String pDoctorFilePath, String pPatientFilePath, String pVisitsFilePath){
		this.aDoctorFilePath = pDoctorFilePath;
		this.aPatientFilePath = pPatientFilePath;
		this.aVisitsFilePath = pVisitsFilePath;
		importDoctorsInfo(this.aDoctorFilePath);
		importPatientInfo(this.aPatientFilePath);
		importVisitData(this.aVisitsFilePath);
		sortDoctors(this.doctorList);
		sortPatients(this.patientList);
	}

	/**
	 * This method should sort the doctorList in time O(n^2). It should sort the Doctors
	 * based on their ID 
	 */
	private void sortDoctors(ArrayList<Doctor> docs){
		if (docs.size() < 2 || docs == null){		//arraylist is sorted if theres only 1 element
			return;
		}
		
		int minIndex;
		long minID;
		
		for (int i = 0; i < docs.size(); i++){
			minID = docs.get(i).getID();			//get smallest ID value
			minIndex = i;							//get smallest index
			for (int j = i; j < docs.size(); j++){	
				if (minID > docs.get(j).getID()){		//if next ID value is smaller
					minID = docs.get(j).getID();
					minIndex = j;
				}
			}
			Doctor temp = docs.get(minIndex);			//swap
			docs.set(minIndex, docs.get(i));
			docs.set(i, temp);
		}
		this.doctorList = docs;		
	}
	
	/**
	 * This method should sort the patientList in time O(n log n). It should sort the 
	 * patients based on the hospitalID
	 */
	private void sortPatients(ArrayList<Patient> patients){
		patients = mergeSort(patients);
		this.patientList = patients;
	}
	
	private ArrayList<Patient> mergeSort(ArrayList<Patient> patientID){
		ArrayList<Patient> left = new ArrayList<Patient>();					
		ArrayList<Patient> right = new ArrayList<Patient>();
		ArrayList<Patient> mergedPatients = new ArrayList<Patient>();
		
		if(patientID.size() < 2){		//sorted if size is 1
			return patientID;
		}
		
		int mid = patientID.size()/2;		//split array in 2
			
		for (int i = 0; i < mid; i++){		//add half to left
			left.add(patientID.get(i));
		}
		
		for (int j = mid; j < patientID.size(); j++){	//add other half to right
			right.add(patientID.get(j));
		}
		
		left = mergeSort(left);
		right = mergeSort(right);
		
		mergedPatients = merge(left, right);
		return mergedPatients;
	}
	
	private ArrayList<Patient> merge(ArrayList<Patient> left, ArrayList<Patient> right){
		ArrayList<Patient> merged = new ArrayList<Patient>();	
		int leftIndex = 0;
		int rightIndex = 0;
	
		while (leftIndex != left.size() || rightIndex != right.size()){			//while there are still elements to sort in either arraylists
			if (leftIndex != left.size() && rightIndex != right.size()){		//if there are still elements to sort in both arraylists
				if (Long.parseLong(left.get(leftIndex).getHospitalID()) <= Long.parseLong(right.get(rightIndex).getHospitalID())){	//if the left element is smaller
					merged.add(left.get(leftIndex));		//add left element and increment to next left element
					leftIndex++;
				}
				else{
					merged.add(right.get(rightIndex));	//else add right and increment right
					rightIndex++;
				}
			}
			else if (leftIndex != left.size()){	//if left still has elements to sort
				merged.add(left.get(leftIndex));	//add all from left
				leftIndex++;
			}
			else if (rightIndex != right.size()){	//if right still has elements to sort
				merged.add(right.get(rightIndex));	//add all from right
				rightIndex++;
			}
		}
		return merged;
	}
	
	/**
	 * This method adds takes in the path of the Doctor sheet csv file and imports
	 * all doctors data into the doctorList ArrayList
	 * @throws IOException 
	 */
	private ArrayList<Doctor> importDoctorsInfo(String doctorFilePath){
		this.doctorList = new ArrayList<Doctor>();
		FileReader fr;
		BufferedReader br;
		
		try{
			fr = new FileReader(doctorFilePath);
			br = new BufferedReader(fr);
			String headerLine = br.readLine();	//gets rid of the FirstName, LastName, ... line at the beginning
			String currentLine = br.readLine();
		
			while (currentLine != null){
				String [] csvDoctors = currentLine.split(",");	//split elements into an array by separator ","
				this.doctorList.add(new Doctor(csvDoctors[0].trim(), csvDoctors[1].trim(), csvDoctors[2].trim(), Long.parseLong(csvDoctors[3].trim())));	//add new doctor
				currentLine = br.readLine();
			}
			br.close();
			fr.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method adds takes in the path of the Patient sheet csv file and imports
	 * all Patient data into the patientList ArrayList
	 */
	private ArrayList<Patient> importPatientInfo(String patientFilePath){
		this.patientList = new ArrayList<Patient>();
		FileReader fr;
		BufferedReader br;
	
		try{
			fr = new FileReader(patientFilePath);
			br = new BufferedReader(fr);
			String headerLine = br.readLine();		//gets rid of the firstname, lastname, ... line
			String currentLine = br.readLine();
			
			while (currentLine != null){
				String [] csvPatients = currentLine.split(",");	//split elements into an array by separator ","
				String InsuranceType = csvPatients[3].trim();	//get rid of spaces 
				if (InsuranceType.toUpperCase().equals("NONE")){	//insurance type is {RAMQ,NONE,Private} so we must change the input to be on of them
					InsuranceType = InsuranceType.toUpperCase();
				}
				else if (InsuranceType.toUpperCase().equals("RAMQ")){
					InsuranceType = InsuranceType.toUpperCase();
				}
				else if (InsuranceType.toUpperCase().equals("PRIVATE")){
					InsuranceType = "Private";
				}
				//add new patient and trim off the spaces from each element
				this.patientList.add(new Patient(csvPatients[0].trim(), csvPatients[1].trim(), Double.parseDouble(csvPatients[2].trim()), csvPatients[4].trim(), Insurance.valueOf(InsuranceType), Long.parseLong(csvPatients[5].trim()), csvPatients[6].trim()));
				currentLine = br.readLine();
			}
			br.close();
			fr.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method adds takes in the path of the Visit sheet csv file and imports
	 * every Visit data. It appends Visit objects to their respective Patient
	 */
	private void importVisitData(String visitsFilePath){
		FileReader fr;
		BufferedReader br;
		
		try{
			fr = new FileReader(visitsFilePath);
			br = new BufferedReader(fr);
			String headerLine = br.readLine();	//gets rid of the first line 
			String currentLine = br.readLine();
		
			while (currentLine != null){
				String [] csvVisits = currentLine.split(",");	
				findPatient(Long.parseLong(csvVisits[0].trim())).addVisit(csvVisits[2].trim(), findDoctor(Long.parseLong(csvVisits[1].trim())), csvVisits[3].trim());	//add a visit to a patient's visitlist
				currentLine = br.readLine();
			}
			br.close();
			fr.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method uses an infinite loop to simulate the interface of the EMR system.
	 * A user should be able to select 10 options. The loop terminates when a user 
	 * chooses option 10: EXIT. You do not have to modify this code.
	 */
	public void displayMenu(){
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println("Welcome to The Royal Victoria EMR Interface V1.0");
		System.out.println("");
		System.out.println("This system will allow you to access and modify the health records of the hospital");
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
		
		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		while(!exit){
			
			System.out.println("Please select one of the following options and click enter:");
			System.out.println("   (1) Add a new patient to the EMR system\n" +
								"   (2) Add a new Doctor to the EMR system\n" +
								"   (3) Record new patient visit to the department\n" +
								"   (4) Edit patient information\n" +
								"   (5) Display list of all Patient IDs\n" +
								"   (6) Display list of all Doctor IDs\n" +
								"   (7) Print a Patient's record\n" +
								"   (8) Print a Doctor's record\n" +
								"   (9) Exit and save modifications\n");
			System.out.print("   ENTER YOUR SELECTION HERE: ");
			
			int choice = 0;
			try{
				choice = Integer.parseInt(scan.next());
			}
			catch(Exception e){
				;
			}
			
			System.out.println("\n");
			
			switch(choice){
				case 1: 
					option1();
					break;
				case 2: 
					option2();
					break;
				case 3: 
					option3();
					break;
				case 4: 
					option4();
					break;
				case 5: 
					option5();
					break;
				case 6: 
					option6();
					break;
				case 7: 
					option7();
					break;
				case 8: 
					option8();
					break;
				case 9: 
					option9();
					exit = true;	//end while loop
					break;	
				default:
					System.out.println("   *** ERROR: You entered an invalid input, please try again ***\n");
					break;
			}
		}
	}
	
	/**
	 * This method adds a patient to the end of the patientList ArrayList. It 
	 * should ask the user to provide all the input to create a Patient object. The 
	 * user should not be able to enter empty values. The input should be supplied
	 * to the addPatient method
	 */
	private void option1(){
		String firstname;
		String lastname;
		double height;
		String Gender;
		Insurance type;
		Long hospitalID;
		String DOB;
		
		Scanner input = new Scanner(System.in);
		assert input.nextLine() != null || input.nextLine() != "";	//make sure there are no null inputs
		
		//Sets patient's first name
		//Legal inputs are uppercase and lowercase letters and spaces, if the input format is not respected, re-enter input
		System.out.print("Enter patient's first name: ");
		firstname = input.nextLine();
		while (!firstname.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Name must not contain special characters or numbers.");
			System.out.print("Enter patient's first name: ");
			firstname = input.nextLine();
		}
		
		//Sets patient's last name
		//Legal inputs are uppercase and lowercase letters and spaces, if the input format is not respected, re-enter input
		System.out.print("Enter patient's last name: ");
		lastname = input.nextLine();
		while (!lastname.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Name must not contain special characters or numbers.");
			System.out.print("Enter patient's last name: ");
			lastname = input.nextLine();
		}
		
		//Sets patient's height
		//legal inputs are doubles, if the input format is not respected, re-enter input
		System.out.print("Enter patient's height in cm: ");
		height = 0;
		while (height == 0){
			try{
				height = Double.parseDouble(input.nextLine());
			}
			catch (NumberFormatException e){
				System.out.print("Please enter a valid number: ");
			}
		}
		
		//Sets patient's gender
		//legal inputs are any string that is equal to M, F, MALE, FEMALE in uppercase, if the input is not respected, re-enter input
		System.out.print("Enter patient's gender (M/F): ");
		Gender = input.nextLine();
		while (! Gender.equals("Male") || ! Gender.equals("Female")){
			if (Gender.toUpperCase().equals("M") || Gender.toUpperCase().equals("MALE")){
				Gender = "Male";
				break;
			}
			else if (Gender.toUpperCase().equals("F") || Gender.toUpperCase().equals("FEMALE")){
				Gender = "Female";
				break;
			}
			else{
				System.out.print("Invalid Input, re-enter gender (M/F): ");
				Gender = input.nextLine();
			}
		}
		
		//Sets patients' insurance type
		//legal inputs are any string that is equal to RAMQ, NONE, PRIVATE in uppercase, if the input is not respected, re-enter input
		System.out.print("Enter patient's insurance type (RAMQ, Private, NONE): ");
		String iType = input.nextLine();
		type = null;
		while (! iType.equals("RAMQ") || ! iType.equals("PRIVATE") || ! iType.equals("NONE")){
			if (iType.toUpperCase().equals("RAMQ")){
				type = Insurance.RAMQ;
				break;
			}
			else if (iType.toUpperCase().equals("PRIVATE")){
				type = Insurance.Private;
				break;
			}
			else if (iType.toUpperCase().equals("NONE")){
				type = Insurance.NONE;
				break;
			}
			else{
				System.out.print("Invalid Input, re-enter insurance type (RAMQ, Private, NONE): ");
				iType = input.nextLine();
			}
		}

		//Sets patient's hospital ID
		//legal inputs are long, if the input is not respected, re-enter input
		System.out.print("Enter patient's hospital ID: ");
		hospitalID = 0l;
		while (hospitalID == 0){
			try {
				hospitalID = Long.parseLong(input.nextLine());
			}
			catch (NumberFormatException e){
				System.out.print("Please enter a valid number: ");
			}
		}
		
		/*Sets patient's date of birth
		input must be of format MM-DD-YYYY where the stars are digits
		if the format is not respected, re-enter
		if the month is greater than 12 or less than 1, invalid input 
		if the day is greater than 31 or less than 1, invalid input
		if the year is less than 1900 (assuming no 115+ year old people) or more than 2015, invalid input*/		
		
		System.out.print("Enter patient's date of birth (MM-DD-YYYY): ");
		DOB = input.nextLine();
		
		
		while (!DOB.matches("(\\d{2})-(\\d{2})-(\\d{4})")){
			System.out.print("Wrong date format, re-enter the date (MM-DD-YYYY): ");
			DOB = input.nextLine();
		}
		
		int month = Integer.parseInt(DOB.substring(0,2));
		int day = Integer.parseInt(DOB.substring(3,5));
		int year = Integer.parseInt(DOB.substring(6,10));
		
		while (month < 1 || month > 12 || day < 1 || day > 31 || year < 1900 || year > 2015){
			System.out.print("Invalid date, re-enter the date (MM-DD-YYYY): ");
			DOB = input.nextLine();
			
			while (!DOB.matches("(\\d{2})-(\\d{2})-(\\d{4})")){	//regex for the MM-DD-YYYY format
				System.out.print("Wrong date format, re-enter the date (MM-DD-YYYY): ");
				DOB = input.nextLine();
			}
			
			month = Integer.parseInt(DOB.substring(0,2));	//change MM into an int
			day = Integer.parseInt(DOB.substring(3,5));		//change DD into an int
			year = Integer.parseInt(DOB.substring(6,10));	//change YYYY into an int
		}
		
		addPatient(firstname, lastname, height, Gender, type, hospitalID, DOB);
		
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	/**
	 * This method adds a patient object to the end of the patientList ArrayList. 
	 */
	private void addPatient(String firstname, String lastname, double height, String Gender, Insurance type, Long hospitalID, String DOB){
		patientList.add(new Patient(firstname, lastname, height, Gender, type, hospitalID, DOB));
		return;
	}
	
	
	/**
	 * This method adds a doctor to the end of the doctorList ArrayList. It 
	 * should ask the user to provide all the input to create a Doctor object. The 
	 * user should not be able to enter empty values.
	 */
	private void option2(){
		String firstname;
		String lastname;
		String specialty;
		Long doctor_id;
		
		Scanner input = new Scanner (System.in);
		assert input.nextLine() != null || input.nextLine() != "";
		
		//Sets doctor's first name
		//same as option 1
		System.out.print("Enter doctor's first name: ");
		firstname = input.nextLine();
		while (!firstname.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Name must not contain special characters or numbers.");
			System.out.print("Enter doctor's first name: ");
			firstname = input.nextLine();
		}
		
		//Sets doctor's last name
		//same as option 2
		System.out.print("Enter doctor's last name: ");
		lastname = input.nextLine();
		while (!lastname.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Name must not contain special characters or numbers.");
			System.out.print("Enter doctor's last name: ");
			lastname = input.nextLine();
		}

		//Sets doctor's specialty
		//same legal inputs as name
		System.out.print("Enter doctor's specialty: ");
		specialty = input.nextLine();
		while (!specialty.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Specialty must not contain special characters or numbers.");
			System.out.print("Enter doctor's specialty: ");
			specialty = input.nextLine();
		}
		
		//Sets doctor's ID
		//same as for patient
		System.out.print("Enter doctor's ID: ");
		doctor_id = null;
		while (doctor_id == null){
			try{
				doctor_id = Long.parseLong(input.nextLine());
			}
			catch (NumberFormatException E){
				System.out.print("Please enter a valid number: ");				
			}
		}

		//create a new doctor
		addDoctor(firstname, lastname, specialty, doctor_id);
		
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	/**
	 * This method adds a doctor to the end of the doctorList ArrayList.
	 */
	private void addDoctor(String firstname, String lastname, String specialty, Long docID){
		doctorList.add(new Doctor(firstname, lastname, specialty, docID));
		return;
	}
	
	/**
	 * This method creates a Visit record. 
	 */
	private void option3(){
		Long doctorID = 0l;
		Long patientID = 0l;
		String date = null;
		String note = null;
		
		Scanner input = new Scanner(System.in);
		boolean isNull = true;
		while (isNull){
			isNull = false;
			patientID = null;
			System.out.print("Enter a patient ID: ");
			
			//Check if the next input is a long, if not loop and re-enter
			while (!input.hasNextLong()){
				System.out.print("Please enter a valid number: ");
				input.next();
			}
			patientID = input.nextLong();
			
			//if the patient does not exist, display existing patients and reset the external while loop
			if (findPatient(patientID) == null){
				System.out.println("ID does not exist.\nHere are the existing patients and their ID's: \n");
				option5();
				isNull = true;
			}
		}
		
		//same as patient
		isNull = true;
	 	while (isNull){
			isNull = false;
			doctorID = null;
			System.out.print("Enter a doctor ID: ");
			while (!input.hasNextLong()){
				System.out.print("Please enter a valid number: ");
				input.next();
			}
			doctorID = input.nextLong();
			if (findDoctor(doctorID) == null){
				System.out.println("ID does not exist.\nHere are the existing doctors and their ID's: \n");
				option6();
				isNull = true;
			}
		}
		
	 	//same as birthday input
		input.nextLine();
		System.out.print("Enter a date (MM-DD-YYYY): ");
		date = input.nextLine();
		while (!date.matches("(\\d{2})-(\\d{2})-(\\d{4})")){
			System.out.print("Wrong date format, re-enter the date (MM-DD-YYYY): ");
			date = input.nextLine();
		}
		
		int month = Integer.parseInt(date.substring(0,2));
		int day = Integer.parseInt(date.substring(3,5));
		int year = Integer.parseInt(date.substring(6,10));
		
		while (month < 1 || month > 12 || day < 1 || day > 31 || year < 1900 || year > 2015){
			System.out.print("Invalid date, re-enter the date (MM-DD-YYYY): ");
			date = input.nextLine();
			
			while (!date.matches("(\\d{2})-(\\d{2})-(\\d{4})")){
				System.out.print("Wrong date format, re-enter the date (MM-DD-YYYY): ");
				date = input.nextLine();
			}
			
			month = Integer.parseInt(date.substring(0,2));
			day = Integer.parseInt(date.substring(3,5));
			year = Integer.parseInt(date.substring(6,10));
		}
		
		System.out.print("Enter a note: ");	//enter any note, all characters are accepted
		note = input.nextLine();
		
		//Use above variables to find which Doctor the patient saw
		Doctor d = findDoctor(doctorID);
		Patient p = findPatient(patientID);

		recordPatientVisit(d, p, date, note);
		
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	/**
	 * This method creates a Visit record. It adds the Visit to a Patient object.
	 */
	private void recordPatientVisit(Doctor doctor, Patient patient, String date, String note){
		patient.addVisit(date, doctor, note);
	}
	
	/**
	 * This method edits a Patient record. Only the firstname, lastname, height,
	 * Insurance type, and date of birth could be changed. You should ask the user to supply the input.
	 */
	private void option4(){
		String newFirstname;
		String newLastname;
		double newHeight;
		Insurance newType;
		String newDOB;
		
		Scanner input  = new Scanner (System.in);
		Long patientID = null;
		boolean isNull = true;
		
		//find a patient by ID, if the ID searched does not correspond to an existing patient, re-enter
		while (isNull){
			isNull = false;
			patientID = null;
			System.out.print("Enter a patient ID: ");
			
			while (!input.hasNextLong()){
				System.out.print("Please enter a valid number: ");
				input.next();
			}
			patientID = input.nextLong();
			
			if (findPatient(patientID) == null){
				System.out.println("ID does not exist.\nHere are the existing patient's and their ID's: ");
				option5();
				isNull = true;
			}
		}	
		input.nextLine();
		
		//Copy of option 1's with the last line calling a different method
		
		//Sets patient's first name
		System.out.print("Enter patient's first name: ");
		newFirstname = input.nextLine();
		while (!newFirstname.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Name must not contain special characters or numbers.");
			System.out.print("Enter patient's first name: ");
			newFirstname = input.nextLine();
		}
		
		//Sets patient's last name
		System.out.print("Enter patient's last name: ");
		newLastname = input.nextLine();
		while (!newLastname.matches("([a-zA-Z]+\\s+)*[a-zA-Z]+")){
			System.out.println("Name must not contain special characters or numbers.");
			System.out.print("Enter patient's last name: ");
			newLastname = input.nextLine();
		}
		
		//Sets patient's height
		System.out.print("Enter patient's height in cm: ");
		newHeight = 0;
		while (newHeight == 0){
			try{
				newHeight = Double.parseDouble(input.nextLine());
			}
			catch (NumberFormatException e){
				System.out.print("Please enter a valid number: ");
			}
		}
		
		//Sets patients' insurance type
		System.out.print("Enter patient's insurance type (RAMQ, Private, NONE): ");
		String iType = input.nextLine();
		newType = null;
		while (! iType.equals("RAMQ") || ! iType.equals("PRIVATE") || ! iType.equals("NONE")){
			if (iType.toUpperCase().equals("RAMQ")){
				newType = Insurance.RAMQ;
				break;
			}
			else if (iType.toUpperCase().equals("PRIVATE")){
				newType = Insurance.Private;
				break;
			}
			else if (iType.toUpperCase().equals("NONE")){
				newType = Insurance.NONE;
				break;
			}
			else{
				System.out.print("Invalid Input, re-enter insurance type (RAMQ, Private, NONE): ");
				iType = input.nextLine();
			}
		}
		
		//Sets patient's date of birth
		System.out.print("Enter patient's date of birth (MM-DD-YYYY): ");
		newDOB = input.nextLine();
		
		while (!newDOB.matches("(\\d{2})-(\\d{2})-(\\d{4})")){
			System.out.print("Wrong date format, re-enter the date (MM-DD-YYYY): ");
			newDOB = input.nextLine();
		}
		
		int month = Integer.parseInt(newDOB.substring(0,2));	
		int day = Integer.parseInt(newDOB.substring(3,5));
		int year = Integer.parseInt(newDOB.substring(6,10));
		
		while (month < 1 || month > 12 || day < 1 || day > 31 || year < 1900 || year > 2015){
			System.out.print("Invalid date, re-enter the date (MM-DD-YYYY): ");
			newDOB = input.nextLine();
			
			while (!newDOB.matches("(\\d{2})-(\\d{2})-(\\d{4})")){
				System.out.print("Wrong date format, re-enter the date (MM-DD-YYYY): ");
				newDOB = input.nextLine();
			}
			
			month = Integer.parseInt(newDOB.substring(0,2));
			day = Integer.parseInt(newDOB.substring(3,5));
			year = Integer.parseInt(newDOB.substring(6,10));
		}
		
		
		editPatient(newFirstname, newLastname, newHeight, newType, newDOB, patientID);
		
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	/**
	 * This method edits a Patient record. Only the firstname, lastname, height, 
	 * Insurance type, address could be changed, and date of birth. 
	 */
	private void editPatient(String firstname, String lastname, double height, Insurance type, String DOB, Long ID){
		//use the setter methods to edit the patient info
		Patient patient = findPatient(ID);
		patient.setFirstName(firstname);
		patient.setLastName(lastname);
		patient.setHeight(height);
		patient.setInsurance(type);
		patient.setDateOfBirth(DOB);
	}
	
	/**
	 * This method should first sort the patientList and then print to screen 
	 * one Patient at a time by calling the displayPatients() method
	 */
	private void option5(){
		sortPatients(this.patientList);
		displayPatients(this.patientList);
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	/**
	 * This method should print to screen 
	 * one Patient at a time by calling the Patient toString() method
	 */
	private void displayPatients(ArrayList<Patient> patients){
		System.out.println(patientList.toString());
	}
	
	/**
	 * This method should first sort the doctorList and then print to screen 
	 * one Doctor at a time by calling the displayDoctors() method
	 */
	private void option6(){
		sortDoctors(this.doctorList);
		displayDoctors(this.doctorList);
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}

	/**
	 * This method should first sort the doctorList and then print to screen 
	 * one Doctor at a time by calling the Doctor toString() method
	 */
	private void displayDoctors(ArrayList<Doctor> docs){
		System.out.println(doctorList.toString());
	}

	private ArrayList<Visit> sortByDate(ArrayList<Visit> visitList){
		if (visitList.size() < 2 || visitList == null){	//if there is 1 element, the arraylist is already sorted
			return visitList;
		}
		
		int mostRecent;	
		
		//nested for loop to compare arraylist at index i and i+1, and then compare each element with every other element
		//if an element with value x has an index lower than element y and x > y, swap
		//to compare dates, split the date string into arrays of 3 elements {day,month,year}
		//parse the values and compare them
		for (int i = 0; i < visitList.size()-1; i++){
			mostRecent = i;
			
			for (int j = i+1; j < visitList.size(); j++){
				String[] splitDate1 = visitList.get(mostRecent).getDate().split("-");
				int year1 = Integer.parseInt(splitDate1[2]);
				int month1 = Integer.parseInt(splitDate1[0]);
				int day1 = Integer.parseInt(splitDate1[1]);
				
				String[] splitDate2 = visitList.get(j).getDate().split("-");
				int year2 = Integer.parseInt(splitDate2[2]);
				int month2 = Integer.parseInt(splitDate2[0]);
				int day2 = Integer.parseInt(splitDate2[1]);
				
				if (year1 > year2){
					mostRecent = j;
				}
				else if (year1 == year2 && month1 > month2){
					mostRecent = j;
				}
				else if (year1 == year2 && month1 == month2 && day1 > day2){
					mostRecent = j;
				}
			}
			
			if (mostRecent != i){
				Visit temp = visitList.get(i);
				visitList.set(i, visitList.get(mostRecent));
				visitList.set(mostRecent, temp);
			}
		}
		return visitList;
	}
	
	/**
	 * This method should ask the user to supply an id of the patient they want info about
	 */
	private void option7(){
		Scanner input = new Scanner (System.in);
		Long patientID = null;
		boolean isNull = true;
		while (isNull){
			isNull = false;
			patientID = null;
			System.out.print("Enter a patient ID: ");
			
			//check if ID entered is a number
			while (!input.hasNextLong()){
				System.out.print("Please enter a valid number: ");
				input.next();
			}
			patientID = input.nextLong();
			
			//check if ID exists
			if (findPatient(patientID) == null){
				System.out.println("ID does not exist.\nHere are the existing patients and their ID's: \n");
				option5();
				isNull = true;
			}
		}
		System.out.println();
		printPatientRecord(patientID);	
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	private Patient findPatient(Long id){
		//user binary search to find the patient in a sorted list of patients
		int min = 0;
		int max = patientList.size() - 1;	
		int mid;
		sortPatients(patientList);	//sort the list in order to do binary search
		while (max >= min){
			mid = (min + max)/2;
			
			if (Long.parseLong(patientList.get(mid).getHospitalID()) == id){	//if the value we're looking for is equal to value we're pointing to, return that value
				return patientList.get(mid);
			}
			else if (Long.parseLong(patientList.get(mid).getHospitalID()) > id){	//if the value we're looking for is greater than the value we're pointing to, search the upper half
				max = mid - 1;
			}
			else if (Long.parseLong(patientList.get(mid).getHospitalID()) < id){	//if the value we're looking for is less than the value we're poiting to, search the lower half
				min = mid + 1;
			}
		}
		return null;
	}
	
	/**
	 * This method should call the toString method of a specific Patient. It should
	 * also list all the patient's Visit objects sorted in order by date (earliest first). For
	 * every Visit, the doctor's firstname, lastname and id should be printed as well.
	 */
	private void printPatientRecord(Long patientID){
		Patient patient = findPatient(patientID);
		patient.toString();
		System.out.println("****************************************************************");
		patient.aVisitList = sortByDate(patient.aVisitList);
		
		for (int i = 0; i < patient.aVisitList.size(); i++){		//iterate through each element of the list and print them
			System.out.println(patient.aVisitList.get(i).toString());
		}
	}
	
	/**
	 * This method should ask the user to supply an id of a doctor they want info about
	 */
	private void option8(){
		//same thing as option 7
		Scanner input = new Scanner (System.in);
		Long doc_id = null;
		boolean isNull = true;
	 	while (isNull){
			isNull = false;
			doc_id = null;
			System.out.print("Enter a doctor ID: ");
			while (!input.hasNextLong()){
				System.out.print("Please enter a valid number: ");
				input.next();
			}
			doc_id = input.nextLong();
			if (findDoctor(doc_id) == null){
				System.out.println("ID does not exist.\nHere are the existing doctors and their ID's: \n");
				option6();
				isNull = true;
			}
		}
	 	System.out.println();
		Doctor d = findDoctor(doc_id);
		printDoctorRecord(d);
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
	}
	
	/**
	 * Searches in O(log n) time the doctorList to find the correct doctor with doctorID = id
	 * @param id
	 * @return
	 */
	private Doctor findDoctor(Long id){
		//same thing as findPatient
		int min = 0;
		int max = doctorList.size() - 1;
		int mid;
		sortDoctors(doctorList);
		while (max >= min){
			mid = (min + max)/2;
			
			if (doctorList.get(mid).getID().equals(id)){
				return doctorList.get(mid);
			}
			else if (doctorList.get(mid).getID().compareTo(id) > 0){
				max = mid - 1;
			}
			else if (doctorList.get(mid).getID().compareTo(id) < 0){
				min = mid + 1;
			}
		}
		return null;
	}
	
	/**
	 * This method should call the toString() method of a specific Doctor. It should
	 * also find and list all the patients that a Doctor has seen by calling their toString()
	 * method as well. It should also list the date that the doctor saw a particular patient
	 */
	private void printDoctorRecord(Doctor d){
		d.toString();
		System.out.println("****************************************************************");
		ArrayList<Visit> doctorVisits = new ArrayList <Visit> ();	//initialize a new ArrayList that will hold the visits involving the doctor
		
		for (int i = 0; i < patientList.size(); i++){		//iterate through each of the patients in the list
			for (int j = 0; j < this.patientList.get(i).aVisitList.size(); j++){	//iterate through each patient's visit list 
				if (d == patientList.get(i).aVisitList.get(j).getDoctor()){			//if the patient's visit list's element i contains the doctor that we are printing the record's of
					doctorVisits.add(this.patientList.get(i).aVisitList.get(j));	//add the visit at element i to the doctor's visit list
				}
			}
		}
		
		sortByDate(doctorVisits);	//sort by date before printing
		
		for (int i = 0; i < doctorVisits.size(); i++){		//print each element of the doctor's visit list
			doctorVisits.get(i).getPatient().toString();
			System.out.println("Date: " + doctorVisits.get(i).getDate() + "\nNote: " + doctorVisits.get(i).getNote() + "\n");
		}
		
	}
	
	/**
	 * This method should be invoked from the command line interface if the user
	 * would like to quit the program. This method should export all the Doctor, Patient and 
	 * Visit data by overwriting the contents of the 3 original files.
	 */
	private void option9(){
		exitAndSave();
	}
	
	
	/**
	 * Export all the Doctor, Patient and Visit data by overwriting the contents of the 3 original csv files.
	 */
	private void exitAndSave(){
		try{
			// We create a BufferedWriter for the Doctor file
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(this.aDoctorFilePath));
			bw1.write("FirstName, LastName, Specialty, DoctorID\n");	//write a new header line (Firstname, Lastname, Specialty, DoctorID)
			for(int i = 0; i < doctorList.size(); i++){					//go through the whole ArrayList of Doctors and write every one of them on the file.
				bw1.write(doctorList.get(i).getFirstName() + ", " + doctorList.get(i).getLastName() + ", " +
						doctorList.get(i).getSpecialty() + ", " + doctorList.get(i).getID() + "\n");
			}
			
			// We create a BufferedWriter for the Patient file
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(this.aPatientFilePath));
			bw2.write("FirstName, LastName, Height (cm), Insurance, Gender, HospitalID, Date of Birth (mm-dd-yyyy)\n");	//write a new header line (Firstname, Lastname, ...)
			for(int i = 0; i < patientList.size(); i++){	//go through each element of the ArrayList of Patients and write them on the file
				bw2.write(patientList.get(i).getFirstName() + ", " + patientList.get(i).getLastName() + ", " + 
						patientList.get(i).getHeight() + ", " + patientList.get(i).getInsurance() + ", " + patientList.get(i).getGender() + ", " + patientList.get(i).getHospitalID()
						+ ", " + patientList.get(i).getDateOfBirth() + "\n");	
			}
			
			// We create a BufferedWriter for the Visits file
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(this.aVisitsFilePath));
			bw3.write("HospitalID, DoctorID, Date, DoctorNote\n");		//write a new header line (hospitalID, DoctorID, Date, DoctorNote)
			for(int i = 0; i < patientList.size(); i++){				// We go through each element of the ArraList of Visits and write them on the file. We have to go in the sub ArrayList
																		// of patientList to get the VisitList
				for(int j = 0; j < patientList.get(i).aVisitList.size(); j++){
					bw3.write(patientList.get(i).aVisitList.get(j).getPatient().getHospitalID() + ", "
							+ patientList.get(i).aVisitList.get(j).getDoctor().getID() + ", " 
							+ patientList.get(i).aVisitList.get(j).getDate() + ", "
							+ patientList.get(i).aVisitList.get(j).getNote() + "\n");
				}
			}
			
			// Close the BufferedWriters to avoid corruption
			bw1.close();
			bw2.close();
			bw3.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("Exiting Session.");		//case 9 calls option 9 and then sets exit to true
													//the program will quit the while(!exit) loop and end the program 
	}
}

/**
 * This simple class just keeps the information about
 * a Patient together. You will have to Modify this class
 * and fill in missing data.
 */
class Patient
{
	public enum Insurance {RAMQ, Private, NONE};
	
	private String aFirstName;
	private String aLastName;
	private double aHeight;
	private String aGender;
	private Insurance aInsurance;
	private Long aHospitalID;
	private String aDateOfBirth; //ex. 12-31-1988 (Dec. 31st, 1988)
	ArrayList<Visit> aVisitList = new ArrayList<Visit>();
	
	public Patient(String pFirstName, String pLastName, double pHeight, String pGender, Insurance pInsurance,
			Long pHospitalID, String pDateOfBirth)
	{
		this.aFirstName = pFirstName;
		this.aLastName = pLastName;
		this.aHeight = pHeight;
		this.aGender = pGender;
		this.aInsurance = pInsurance;
		this.aHospitalID = pHospitalID;
		this.aDateOfBirth = pDateOfBirth;
	}
	
	public String getFirstName()
	{
		return aFirstName;
	}
	
	public String getLastName()
	{
		return aLastName;
	}
	
	public double getHeight()
	{
		return aHeight;
	}
	
	public Insurance getInsurance()
	{
		return aInsurance;
	}
	
	public String getGender()
	{
		return aGender;
	}
	
	public String getHospitalID()		//get the hospital ID as an integer and return it as a String
	{
		String aHID = String.valueOf(aHospitalID);
		return aHID;
	}

	public String getDateOfBirth()
	{
		return aDateOfBirth;
	}

	public void addVisit(String vDate, Doctor vDoctor, String note){
		this.aVisitList.add(new Visit(vDoctor, this, vDate, note));	//the this keyword is the instance of the Patient object that we are referring to
	}
	
	public void setFirstName(String fname){
		this.aFirstName = fname;
	}
	
	public void setLastName(String lname){
		this.aLastName = lname;
	}
	
	public void setHeight(double height){
		this.aHeight = height;
	}
	
	public void setInsurance(Insurance type){
		this.aInsurance = type;
	}
	
	public void setDateOfBirth(String dob){
		this.aDateOfBirth = dob;
	}
	
	/**
	 * This method should print all the Patient's info. "ID, Lastname, Firstname, etc..."
	 */
	public String toString(){	
		System.out.print("Patient: " + this.aFirstName + ", " + this.aLastName + ", " + this.aHeight + ", " + this.aGender + ", " + this.aInsurance + ", " + this.aHospitalID + ", " + this.aDateOfBirth + "\n");
		return "";
	}
}

/**
 * This simple class just keeps the information about
 * a Doctor together. Do modify this class as needed.
 */
class Doctor
{
	private String aFirstName;
	private String aLastName;
	private String aSpecialty; 
	private Long aID;
	
	public Doctor(String pFirstName, String pLastName, String pSpecialty, Long ID)
	{
		this.aFirstName = pFirstName;
		this.aLastName = pLastName;
		this.aSpecialty = pSpecialty;
		this.aID = ID;
	}
	
	public String getFirstName()
	{
		return aFirstName;
	}
	
	public String getLastName()
	{
		return aLastName;
	}

	public String getSpecialty(){
		return aSpecialty;
	}

	public Long getID(){
		return aID;
	}
	
	/**
	 * This method should print all the Doctor's info. "ID, Lastname, Firstname, Specialty"
	 */
	public String toString(){
		System.out.print("Doctor: " + this.aID + ", " + this.aLastName + ", " + this.aFirstName + ", " + this.aSpecialty + "\n");
		return "";
	}

}

/**
 * This simple class just keeps the information about
 * a Visit together. Do modify this class as needed.
 */
class Visit
{
	private Doctor aDoctor;
	private Patient aPatient;
	private String aDate; 
	private String aNote;
	
	public Visit(Doctor vDoctor, Patient vPatient, String vDate, String vNote)
	{
		this.aDoctor = vDoctor;
		this.aPatient = vPatient;
		this.aDate = vDate;
		this.aNote = vNote;
	}
	
	public Doctor getDoctor()
	{
		return aDoctor;
	}
	
	public Patient getPatient()
	{
		return aPatient;
	}

	public String getDate(){
		return aDate;
	}
	
	public String getNote(){
		return aNote;
	}
	
	public String toString(){
		this.aDoctor.toString();
		System.out.print("Visit date: " + this.aDate + "\nNote: " + this.aNote + "\n");
		return "";
	}
}
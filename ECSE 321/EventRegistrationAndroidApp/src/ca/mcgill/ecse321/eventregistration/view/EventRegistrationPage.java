package ca.mcgill.ecse321.eventregistration.view;

import java.awt.Color;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import ca.mcgill.ecse321.eventregistration.controller.EventRegistrationController;
import ca.mcgill.ecse321.eventregistration.controller.InvalidInputException;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;

public class EventRegistrationPage extends JFrame {

	private static final long serialVersionUID = -8062635784771606869L;
	
	private JLabel errorMessage;
	private JComboBox<String> participantList;
	private JLabel participantLabel;
	private JComboBox<String> eventList;
	private JLabel eventLabel;
	private JButton registerButton;
	private JTextField participantNameTextField;
	private JLabel participantNameLabel;
	private JButton addParticipantButton;
	private JTextField eventNameTextField;
	private JLabel eventNameLabel;
	private JDatePickerImpl eventDatePicker;
	private JLabel eventDateLabel;
	private JSpinner startTimeSpinner;
	private JLabel startTimeLabel;
	private JSpinner endTimeSpinner;
	private JLabel endTimeLabel;
	private JButton addEventButton;
	
	private String error = null;
	private Integer selectedParticipant = -1;
	private HashMap<Integer, Participant> participants;
	private Integer selectedEvent = -1;
	private HashMap<Integer, Event> events;
	
	/**/
	public EventRegistrationPage() {
		initComponents();
		refreshData();
	}
	
	private void initComponents() {
		
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		// Elements for registration
		participantList = new JComboBox<String>(new String[0]);
		participantList.addActionListener(new java.awt.event.ActionListener () {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedParticipant = cb.getSelectedIndex();
			}
		});
		participantLabel = new JLabel();
		eventList = new JComboBox<String>(new String[0]);
		eventList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JComboBox<String> cb = (JComboBox<String>) evt.getSource();
				selectedEvent = cb.getSelectedIndex();
			}
		});
		eventLabel = new JLabel();
		
		registerButton = new JButton();
		
		// Elements for participant
		participantNameTextField = new JTextField();
		participantNameLabel = new JLabel();
		addParticipantButton = new JButton();
		
		// Elements for event
		eventNameTextField = new JTextField();
		eventNameLabel = new JLabel();
		
		SqlDateModel model = new SqlDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		eventDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		eventDateLabel = new JLabel();
		startTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
		startTimeSpinner.setEditor(startTimeEditor);
		startTimeLabel = new JLabel();
		endTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
		endTimeSpinner.setEditor(endTimeEditor);
		endTimeLabel = new JLabel();
		addEventButton = new JButton();
		
		// Global Settings and Listeners
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Event Registration");
		
		participantLabel.setText("Select Participant: ");
		eventLabel.setText("Select Event:");
		registerButton.setText("Register");
		registerButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				registerButtonActionPerformed(evt);
			}
		});
		
		participantNameLabel.setText("Name: ");
		addParticipantButton.setText("Add Participant");
		addParticipantButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addParticipantButtonActionPerformed(evt);
			}
		});
		
		eventNameLabel.setText("Name:");
		eventDateLabel.setText("Date:");
		startTimeLabel.setText("Start Time:");
		endTimeLabel.setText("End time:");
		addEventButton.setText("Add Event");
		addEventButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addEventButtonActionPerformed(evt);
			}
		});
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(errorMessage)
				.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup()
									.addComponent(participantLabel)
									.addComponent(registerButton)
									.addComponent(participantNameLabel))
							.addGroup(layout.createParallelGroup()
									.addComponent(participantList)
									.addComponent(participantNameTextField, 200, 200, 400)
									.addComponent(addParticipantButton))
							.addGroup(layout.createParallelGroup()
									.addComponent(eventLabel)
									.addComponent(eventNameLabel)
									.addComponent(eventDateLabel)
									.addComponent(startTimeLabel)
									.addComponent(endTimeLabel))
							.addGroup(layout.createParallelGroup()
									.addComponent(eventList)
									.addComponent(eventNameTextField, 200, 200, 400)
									.addComponent(eventDatePicker)
									.addComponent(startTimeSpinner)
									.addComponent(endTimeSpinner)
									.addComponent(addEventButton)))
				);
		
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[]{registerButton, participantLabel});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[]{addParticipantButton, participantNameTextField});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[]{addEventButton, eventNameTextField});
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(errorMessage)
				.addGroup(layout.createParallelGroup()
						.addComponent(participantLabel)
						.addComponent(participantList)
						.addComponent(eventLabel)
						.addComponent(eventList))
				.addComponent(registerButton)
				.addGroup(layout.createParallelGroup()
						.addComponent(participantNameLabel)
						.addComponent(participantNameTextField)
						.addComponent(eventNameLabel)
						.addComponent(eventNameTextField))
				.addGroup(layout.createParallelGroup()
						.addComponent(eventDateLabel)
						.addComponent(eventDatePicker))
				.addGroup(layout.createParallelGroup()
						.addComponent(startTimeLabel)
						.addComponent(startTimeSpinner))
				.addGroup(layout.createParallelGroup()
						.addComponent(endTimeLabel)
						.addComponent(endTimeSpinner))
				.addGroup(layout.createParallelGroup()
						.addComponent(addParticipantButton)
						.addComponent(addEventButton))
				);
		
		pack();
	}
	
	private void refreshData() {
		RegistrationManager rm = RegistrationManager.getInstance();
		// Error
		errorMessage.setText(error);
		if(error == null || error.length() == 0) {
			// Participant List
			participants = new HashMap<Integer, Participant>();
			participantList.removeAllItems();
			Iterator<Participant> pIt = rm.getParticipants().iterator();
			Integer index = 0;
			while(pIt.hasNext()) {
				Participant p = pIt.next();
				participants.put(index, p);
				participantList.addItem(p.getName());
				index++;
			}
			selectedParticipant = -1;
			participantList.setSelectedIndex(selectedParticipant);
			
			// Event List
			events = new HashMap<Integer, Event>();
			eventList.removeAllItems();
			Iterator<Event> eIt = rm.getEvents().iterator();
			index = 0;
			while(eIt.hasNext()) {
				Event e = eIt.next();
				events.put(index, e);
				eventList.addItem(e.getName());
				index++;
			}
			selectedEvent = -1;
			eventList.setSelectedIndex(selectedEvent);
			
			// Participant
			participantNameTextField.setText("");
			
			// Event
			eventNameTextField.setText("");
			eventDatePicker.getModel().setValue(null);
			startTimeSpinner.setValue(new Date());
			endTimeSpinner.setValue(new Date());
		}
		
		pack();
	}
	
	private void addParticipantButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Call the controller
		EventRegistrationController erc = new EventRegistrationController();
		
		error = null;
		try {
			erc.createParticipant(participantNameTextField.getText());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		// Update Visuals
		refreshData();
	}
	
	private void addEventButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Call the controller
		EventRegistrationController erc = new EventRegistrationController();
		// JSpinner actually returns a date and time
		// force the same date for start and end time to ensure that only the times differ
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)startTimeSpinner.getValue());
		calendar.set(2000, 1, 1);
		Time startTime = new Time(calendar.getTime().getTime());
		calendar.setTime((Date)endTimeSpinner.getValue());
		calendar.set(2000, 1, 1);
		Time endTime = new Time(calendar.getTime().getTime());
		
		error = null;
		try {
			erc.createEvent(eventNameTextField.getText(), (java.sql.Date)eventDatePicker.getModel().getValue(), startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		// Update Visuals
		refreshData();
}
	
	private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		if(selectedParticipant < 0) {
			error = error + "Participant needs to be selected for registration! ";
		}
		if(selectedEvent < 0) {
			error = error + "Event needs to be selected for registration! ";
		}
		error = error.trim();
		if(error.length() == 0) {
			// Call the Controller
			EventRegistrationController erc = new EventRegistrationController();
			try {
				erc.register(participants.get(selectedParticipant), events.get(selectedEvent));
			} catch(InvalidInputException e) {
				error = e.getMessage();
			}
		}
		// Update Visuals
		refreshData();
	}
}

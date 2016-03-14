package view;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import model.ChatroomDTO;
import model.MessagesDTO;
import model.Service;

public class ChatView {

	private JFrame frame;
	private JButton employeeFinderButton;
	private JButton leaveButton;
	private JButton changeTopicButton;
	private JButton sendButton;
	private ChatroomDTO c;
	private JPanel chatroom;
	private JPanel employees;
	private JTextArea inputfield;
	private ArrayList<MessagesDTO> messagesDTOList;
	private ArrayList<String> employeesList;
	private Service db_service;
	private JScrollPane jscrollpane;
	private JScrollPane jscrollpane2;
	private boolean change= false;

	public ChatView(Service db_service, ChatroomDTO c) {
		this.c = c;
		this.db_service = db_service;
		getData();
		prepareGui();
		show();
	}

	private void getData() {
		messagesDTOList = db_service.getChatmessages(c);
		employeesList = db_service.getUsersInThisChat(c);
	}

	private void prepareGui() {
		frame = new JFrame("topic: " + c.getTopic());
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLayout(null);
		
		employees = new JPanel();
		employees.setLayout(new BoxLayout(employees, BoxLayout.PAGE_AXIS));
		employees.setSize(200, 310);
		employees.setLocation(0, 0);
		employees.setBorder(BorderFactory.createLineBorder(Color.black));
		
		jscrollpane2 = new JScrollPane(employees);
		jscrollpane2.setSize(200, 310);
		jscrollpane2.setLocation(0, 0);
		
		chatroom = new JPanel();
		chatroom.setLayout(new BoxLayout(chatroom, BoxLayout.PAGE_AXIS));
		chatroom.setSize(400, 400);
		chatroom.setLocation(200, 0);
		chatroom.setBorder(BorderFactory.createLineBorder(Color.black));
		
		jscrollpane = new JScrollPane(chatroom);
		jscrollpane.setSize(400, 400);
		jscrollpane.setLocation(200, 0);

		inputfield = new JTextArea("...");
		inputfield.setSize(500, 200);
		inputfield.setLocation(0, 400);
		inputfield.setBorder(BorderFactory.createLineBorder(Color.black));

		sendButton = new JButton("SEND");
		sendButton.setSize(100, 200);
		sendButton.setLocation(500, 400);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(inputfield.getText() != ""){
					MessagesDTO m =new MessagesDTO();
					m.setChatroomId(c.getChatroomId());
					m.setTopic(c.getTopic());
					m.setDate(new Date());
					m.setMessage(inputfield.getText());
					m.setSender(c.getUser());
					m.setSenderId(c.getUserId());
					db_service.createMessage(m);
					getData();
					showData();
					inputfield.setText("");
				}
			}
		});
		
		showData();

		employeeFinderButton = new JButton("Add User");
		employeeFinderButton.setSize(200, 30);
		employeeFinderButton.setLocation(0, 310);
		employeeFinderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new EmployeeFinderView(db_service, c, employeesList);
			}
		});

		leaveButton = new JButton("Leave Chatroom");
		leaveButton.setSize(200, 30);
		leaveButton.setLocation(0, 340);
		leaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MainMenuView(db_service, c.getUser());
			}
		});

		changeTopicButton = new JButton("Change Topic");
		changeTopicButton.setSize(200, 30);
		changeTopicButton.setLocation(0, 370);
		changeTopicButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (change == false){
					inputfield.setText(c.getTopic());
					change = true;
				}else{
					if (!inputfield.getText().equals("") && !inputfield.getText().equals(c.getTopic())){
						String newTopic = inputfield.getText();
						c = db_service.changeTopic(c, newTopic);
						frame.setTitle(c.getTopic());
						inputfield.setText("");
					}
					change = false;
				}
			}
		});

		frame.add(employeeFinderButton);
		frame.add(leaveButton);
		frame.add(changeTopicButton);
		frame.add(inputfield);
		frame.add(sendButton);
		frame.add(jscrollpane2);
		frame.add(jscrollpane);
		

	}

	private void showData() {
		chatroom.removeAll();
		for (MessagesDTO m : messagesDTOList) {
			Label l = new Label("["+m.getDate()+"] "+m.getSender()+": "+ m.getMessage());
			chatroom.add(l);
		}
		employees.removeAll();
		for (String s : employeesList) {
			Label l = new Label(s);
			employees.add(l);
		}
		show();
	}

	public void show() {
		frame.setVisible(true);
	}

	public void dispose() {
		frame.setVisible(false);
	}

}
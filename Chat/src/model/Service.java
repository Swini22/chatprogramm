package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Service {

	private Connection connect;

	public Service(Connection connect) {
		this.connect = connect;
	}

	public boolean checkLogin(String username, String password) {
		String statement = "SELECT * FROM EMPLOYEE WHERE USERNAME = ? AND PASSWORD = ?";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setString(1, username);
			query.setString(2, password);
			ResultSet rs = query.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ChatroomDTO getLastMessage(ChatroomDTO c) {
		String statement = "SELECT * FROM CHATMESSAGE WHERE CHATROOM_ID = ? ORDER BY DATE DESC";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setInt(1, c.getChatroomId());
			ResultSet rs = query.executeQuery();
			if (rs.next()) {
				c.setDateFromLastMessage(rs.getTimestamp("DATE"));
				c.setLastmessage(rs.getString("MESSAGE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public ArrayList<ChatroomDTO> getChatroomsAndLastMessage(String name) {
		ArrayList<ChatroomDTO> list = new ArrayList<ChatroomDTO>();
		String statement = "SELECT * FROM (MULTIPLECHAT AS M INNER JOIN EMPLOYEE AS E ON M.EMPLOYEE_ID = E.ID) INNER JOIN CHATROOM AS C ON M.CHATROOM_ID = C.ID WHERE USERNAME = ?";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setString(1, name);
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				ChatroomDTO c = new ChatroomDTO();
				c.setUser(name);
				c.setUserId(rs.getInt("EMPLOYEE_ID"));
				c.setChatroomId(rs.getInt("CHATROOM_ID"));
				c.setTopic(rs.getString("TOPIC"));
				getLastMessage(c);
				list.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getIdFromEmployee(String name) {
		int id = 0;
		String statement = "SELECT * FROM EMPLOYEE WHERE USERNAME = ?";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setString(1, name);
			ResultSet rs = query.executeQuery();
			if (rs.next()) {
				id = rs.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public ArrayList<MessagesDTO> getChatmessages(ChatroomDTO c) {
		ArrayList<MessagesDTO> list = new ArrayList<MessagesDTO>();
		String statement = "SELECT * FROM (CHATMESSAGE AS C INNER JOIN EMPLOYEE AS E ON C.SENDER_ID = E.ID) WHERE CHATROOM_ID = ? ORDER BY DATE ASC";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setInt(1, c.getChatroomId());
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				MessagesDTO m = new MessagesDTO();
				m.setChatroomId(c.getChatroomId());
				m.setTopic(c.getTopic());
				m.setDate(rs.getTimestamp("DATE"));
				m.setSender(rs.getString("USERNAME"));
				m.setSenderId(rs.getInt("SENDER_ID"));
				m.setMessage(rs.getString("MESSAGE"));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ChatroomDTO changeTopic(ChatroomDTO c, String newTopic) {
		String statement = "UPDATE CHATROOM SET TOPIC = ? WHERE ID = ?";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setString(1, newTopic);
			query.setInt(2, c.getChatroomId());
			query.execute();
			c.setTopic(newTopic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public void createMessage(MessagesDTO m) {
		String statement = "INSERT INTO CHATMESSAGE (SENDER_ID, CHATROOM_ID, DATE, MESSAGE) VALUES (?, ?, ?, ?)";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setInt(1, m.getSenderId());
			query.setInt(2, m.getChatroomId());
			query.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			query.setString(4, m.getMessage());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getUsersInThisChat(ChatroomDTO c) {
		ArrayList<String> list = new ArrayList<String>();
		String statement = "SELECT * FROM (MULTIPLECHAT AS M INNER JOIN EMPLOYEE AS E ON M.EMPLOYEE_ID = E.ID) INNER JOIN CHATROOM AS C ON M.CHATROOM_ID = C.ID WHERE CHATROOM_ID = ?";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setInt(1, c.getChatroomId());
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				String s = rs.getString("USERNAME");
				list.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<EmployeeDepartmentDTO> searchAll() {
		ArrayList<EmployeeDepartmentDTO> list = new ArrayList<EmployeeDepartmentDTO>();
		String statement = "SELECT * FROM (EMPLOYEE AS E INNER JOIN DEPARTMENT AS D ON E.DEPARTMENT_ID = D.ID)";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				EmployeeDepartmentDTO edDTO = new EmployeeDepartmentDTO();
				edDTO.setUser(rs.getString("USERNAME"));
				edDTO.setUserId(rs.getInt("E.ID"));
				edDTO.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				edDTO.setDepartment(rs.getString("D.NAME"));
				list.add(edDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<String> getAllDepartments() {
		ArrayList<String> list = new ArrayList<String>();
		String statement = "SELECT * FROM DEPARTMENT ORDER BY NAME DESC";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				String s = rs.getString("NAME");
				list.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void addUserToChatroom(String user, int chatroomId) {
		int employeeId = getIdFromEmployee(user);
		String statement = "INSERT INTO MULTIPLECHAT (CHATROOM_ID, EMPLOYEE_ID) VALUES (?, ?)";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setInt(1, chatroomId);
			query.setInt(2, employeeId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ChatroomDTO createNewChatroom(String username2, String username1) {
		String statement = "INSERT INTO CHATROOM (TOPIC) VALUES (?)";
		ChatroomDTO c1 = new ChatroomDTO();
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setString(1, "NEW CHATROOM");
			query.executeUpdate();

			int chatroomId = getLastIDfromChatroom();
			if (chatroomId != -1) {
				addUserToChatroom(username1, chatroomId);
				addUserToChatroom(username2, chatroomId);
				c1.setChatroomId(chatroomId);
				c1.setTopic("NEW CHATROOM");
				c1.setUser(username1);
				c1.setUserId(getIdFromEmployee(username1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c1;
	}

	private int getLastIDfromChatroom() {
		int lastId = -1;
		String statement = "SELECT * FROM CHATROOM ORDER BY ID DESC";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			ResultSet rs = query.executeQuery();
			if (rs.next()) {
				lastId = rs.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastId;
	}

	public ArrayList<EmployeeDepartmentDTO> searchEmployee(String searchWord) {
		ArrayList<EmployeeDepartmentDTO> list = new ArrayList<EmployeeDepartmentDTO>();
		String statement = "SELECT * FROM (EMPLOYEE AS E INNER JOIN DEPARTMENT AS D ON E.DEPARTMENT_ID = D.ID) WHERE USERNAME LIKE ?";
		try (PreparedStatement query = connect.prepareStatement(statement)) {
			query.setString(1, "%"+searchWord+"%");
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				EmployeeDepartmentDTO edDTO = new EmployeeDepartmentDTO();
				edDTO.setUser(rs.getString("USERNAME"));
				edDTO.setUserId(rs.getInt("E.ID"));
				edDTO.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				edDTO.setDepartment(rs.getString("D.NAME"));
				list.add(edDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}

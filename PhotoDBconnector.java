package com.photo_editor;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PhotoDBconnector {

	/**
	 * creates user collection
	 * @param filename
	 * @param collectionName
	 * @param date
	 */
	public void createCollection(String filename, String collectionName, java.sql.Date date ) {

		try (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/PhotoDatabase", "postgres", "0202")) {

			if (conn != null) {

				File file = new File(filename);
				FileInputStream fis = new FileInputStream(file);
				PreparedStatement ps = conn.prepareStatement("INSERT INTO photo VALUES (?, ?, ?, ?)");
				ps.setString(1, collectionName);
				ps.setDate(4, date);
				ps.setString(2, filename);
				ps.setBinaryStream(3, fis, file.length());
				ps.executeUpdate();
				ps.close();
				fis.close();

				System.out.println("Connected to the database!");
				conn.close();
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * registers username
	 * @param name
	 */
	public void registerUserName(String name) {

		try (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/PhotoDatabase", "postgres", "0202")) {

			if (conn != null) {

				String query = "INSERT INTO appuser(name) VALUES (?)";
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1,name);
				ps.executeUpdate();
				ps.close();

				System.out.println("Connected to the database!");
				conn.close();
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * register collection name
	 * @param cname
	 * @param name
	 */
	public void registerCollectionName(String cname, String name) {
		try (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/PhotoDatabase", "postgres", "0202")) {

			if (conn != null) {

				PreparedStatement ps = conn.prepareStatement("INSERT INTO collection(cname,name) VALUES (?, ?)");
				ps.setString(1, cname);
				ps.setString(2, name);
				ps.executeUpdate();
				ps.close();

				conn.close();
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * check if user already exists
	 * @param name
	 * @return
	 */
	public boolean checkUserName(String name) {
          boolean isRegistered = false;
		try (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/PhotoDatabase", "postgres", "0202")) {

			if (conn != null) {

				StringBuilder user_query = new StringBuilder();
				user_query.append( "select * from appuser where name = ('" + name + "')" );

				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(user_query.toString());

				while(rs.next()) {
					isRegistered= true;
				}
				conn.close();
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} 
		return isRegistered;
	}

	/**
	 * check is collection already exists
	 * @param cname
	 * @return
	 */
	public boolean checkCollectionName(String cname) {
		 boolean isRegistered = false;
		try (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/PhotoDatabase", "postgres", "0202")) {

			if (conn != null) {

				StringBuilder user_query = new StringBuilder();
				user_query.append( "select * from collection where cname = ('" + cname + "')" );

				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(user_query.toString());

				while(rs.next()) {
					isRegistered= true;
				}
				conn.close();
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} 
		return isRegistered;
	}

}

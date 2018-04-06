package service;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.fabric.jdbc.FabricMySQLDriver;

//Connect remote MySQL Database to the program through ssh
public class DBConnector {

	// local database data (currently running on pi)
	private static final String URL = "jdbc:mysql://localhost:3306/OADTurk?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	/**
	 * connects to the remote server over ssh at port 22
	 * 
	 * @param host_
	 *            is the the remote host in form of user@host
	 * @param lportRhostRport
	 *            a string of localPort:RemoteHost:RemotePort; RemoteHost and
	 *            RemotePort are values used on the remote system
	 */
	public static void connectSSH(String host_, String lportRhostRport) {

		int lport;
		String rhost;
		int rport;

		try {
			JSch jsch = new JSch();

			// parse host and user
			String host = host_;
			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);

			Session session = jsch.getSession(user, host, 22);

			// parse lportRhostRport
			String foo = lportRhostRport;
			lport = Integer.parseInt(foo.substring(0, foo.indexOf(':')));
			foo = foo.substring(foo.indexOf(':') + 1);
			rhost = foo.substring(0, foo.indexOf(':'));
			rport = Integer.parseInt(foo.substring(foo.indexOf(':') + 1));

			// set password of ssh
			session.setPassword("MC_1919");
			session.setConfig("StrictHostKeyChecking", "no");

			session.connect();

			// TODO KRJO port cannot be bound twice; e.g. when changing something in
			// settings
			int assinged_port = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public DBConnector() {
	}

	public void connectToDB() {
		// connectSSH("pi@192.168.8.107", "4321:localhost:3306");
		connect(URL, USERNAME, PASSWORD);

	}

	public void connect(String url, String username, String password) {
		try {
			Driver driver = new FabricMySQLDriver();
			DriverManager.registerDriver(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

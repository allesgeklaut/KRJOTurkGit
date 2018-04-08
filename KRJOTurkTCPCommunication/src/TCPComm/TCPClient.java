package TCPComm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
//TCPClient.java
//A client program implementing TCP socket
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TCPClient {

	Socket m_socket;

	public void open(String ip, int port) {
		try {
			m_socket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] sendData(byte[] dataIn) {
		try {
			DataInputStream input = new DataInputStream(m_socket.getInputStream());
			DataOutputStream output = new DataOutputStream(m_socket.getOutputStream());
			output.write(dataIn);
			output.flush();

			int nb = input.readInt();
			byte[] digit = new byte[nb];
			for (int i = 0; i < nb; i++)
				digit[i] = input.readByte();

			return digit;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void main(String args[]) {// arguments supply message and hostname of destination
		Socket s = null;
		try {
			int serverPort = 8080;
			String ip = "localhost";
			int CID = 0;
			String SQLCommand = "SELECT * FROM users";
			// String data = "0001" + "UPDATE users SET `email`='asd' WHERE user_id=11";
			// String SQLCommand = "image01";

			// Bild
			BufferedImage image = ImageIO.read(new File("C:\\Users\\krems\\Pictures\\meli.jpg"));

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			if (CID == 2)
				ImageIO.write(image, "jpg", byteArrayOutputStream);

			byte[] CIDbytes = ByteBuffer.allocate(4).putInt(CID).array();
			byte[] SQLbytes = SQLCommand.getBytes(StandardCharsets.UTF_8);
			byte[] SQLByteBuffer = ByteBuffer.allocate(64).put(SQLbytes).array();
			// get data size in bytes
			int dataSize = CIDbytes.length + SQLByteBuffer.length + byteArrayOutputStream.toByteArray().length;
			byte[] dataSizeBytes = ByteBuffer.allocate(4).putInt(dataSize).array();

			// String data = bytes.toString() + byteArrayOutputStream.toString();
			s = new Socket(ip, serverPort);
			DataInputStream input = new DataInputStream(s.getInputStream());
			DataOutputStream output = new DataOutputStream(s.getOutputStream());

			System.out.println("Writing.......");
			output.write(dataSizeBytes);
			output.write(CIDbytes);
			output.write(SQLByteBuffer);
			if (CID == 2)
				output.write(byteArrayOutputStream.toByteArray());
			output.flush();

			// Step 1 read length
			int nb = input.readInt();
			byte[] digit = new byte[nb];
			// Step 2 read byte
			for (int i = 0; i < nb; i++)
				digit[i] = input.readByte();
			// input.read(digit, 0, nb);

			if (CID == 3) {
				byte[] imgBytes = Arrays.copyOfRange(digit, 1, digit.length);
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
				OutputStream fileOut = new FileOutputStream("image.jpg");
				ImageIO.write(img, "jpg", fileOut);
				fileOut.close();
			} else {
				String st = new String(digit);
				System.out.println("Received: " + st);
			}
			System.out.println("end");
		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					/* close failed */}
		}
	}
}

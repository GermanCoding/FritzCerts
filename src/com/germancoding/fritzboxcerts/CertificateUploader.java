package com.germancoding.fritzboxcerts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import org.apache.http.entity.mime.MultipartEntityBuilder;

public class CertificateUploader {

	private FritzBoxController controller;
	private String sid;

	public void login(String domain, String username, String password) throws ParserConfigurationException, URISyntaxException {
		controller = new FritzBoxController(domain, username, password);
		sid = controller.getSID();
		if (sid != null && !sid.trim().isEmpty() && !sid.equalsIgnoreCase("0000000000000000")) {
			System.out.println("Login succesfull.");
		}
	}

	public void uploadCertificateFile(String filepath) throws UnsupportedEncodingException, UnsupportedOperationException, IOException {
		File file = new File(filepath);
		HttpPost post = new HttpPost(controller.getFirmwareCfgAddress());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setContentType(ContentType.MULTIPART_FORM_DATA);
		builder.addTextBody("sid", sid);
		builder.addBinaryBody("BoxCertImportFile", file, ContentType.create("application/x-x509-ca-cert"), file.getName());
		HttpEntity multipart = builder.build();
		post.setEntity(multipart);
		CloseableHttpResponse response = controller.sendPost(post);
		System.out.println(response.getStatusLine());
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String lines = "";
		String line;
		boolean success = false;
		while ((line = reader.readLine()) != null) {
			lines += line + "\n";
			if (line.toLowerCase().contains("erfolgreich importiert")) {
				success = true;
				System.out.println(line);
			}
		}
		response.close();
		if (!success) {
			System.out.println(lines);
			System.out.println("Failed to read success line, assuming failure!");
			System.exit(1);
		}
	}

	public static void main(String[] args) throws ParserConfigurationException, URISyntaxException, UnsupportedEncodingException, UnsupportedOperationException, IOException {
		if (args.length < 3) {
			System.out.println("Usage: program name <filepath> <domain> [username] <password>");
			return;
		}

		String filepath = args[0];
		String domain = args[1];
		String password;
		String username;

		if (args.length > 3) {
			username = args[2];
			password = args[3];
		} else {
			password = args[2];
			username = null;
		}

		CertificateUploader upload = new CertificateUploader();
		upload.login(domain, username, password);
		upload.uploadCertificateFile(filepath);
	}
}

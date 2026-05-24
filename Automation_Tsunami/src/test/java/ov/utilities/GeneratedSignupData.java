package ov.utilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GeneratedSignupData {

	private static final Logger logger = LogManager.getLogger(GeneratedSignupData.class);
	private static final DateTimeFormatter FILE_TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static String email;
	private static String password;
	private static String fullName;
	private static String startupCompanyName;

	private GeneratedSignupData() {
	}

	public static void setEmail(String value) {
		email = value;
	}

	public static void setPassword(String value) {
		password = value;
	}

	public static void setFullName(String value) {
		fullName = value;
	}

	public static void setStartupCompanyName(String value) {
		startupCompanyName = value;
	}

	public static String getEmail() {
		return email;
	}

	public static String getPassword() {
		return password;
	}

	public static String getFullName() {
		return fullName;
	}

	public static String getStartupCompanyName() {
		return startupCompanyName;
	}

	public static String getEmailLocalPart() {
		if (email == null || !email.contains("@")) {
			return email;
		}
		return email.substring(0, email.indexOf('@'));
	}

	public static boolean hasRequiredSignupData() {
		return email != null && password != null && startupCompanyName != null;
	}

	public static boolean appendToFile(String relativePath, String targetProgramTitle) {
		try {
			Path filePath = Paths.get(System.getProperty("user.dir")).resolve(relativePath);
			Files.createDirectories(filePath.getParent());

			String timestamp = LocalDateTime.now().format(FILE_TS);
			StringBuilder entry = new StringBuilder();
			entry.append("[").append(timestamp).append("]").append(System.lineSeparator());
			entry.append("email=").append(nullToEmpty(email)).append(System.lineSeparator());
			entry.append("password=").append(nullToEmpty(password)).append(System.lineSeparator());
			entry.append("startupCompanyName=").append(nullToEmpty(startupCompanyName)).append(System.lineSeparator());
			entry.append("targetProgramTitle=").append(nullToEmpty(targetProgramTitle)).append(System.lineSeparator());
			entry.append(System.lineSeparator());

			if (Files.exists(filePath)) {
				Files.writeString(
						filePath,
						entry.toString(),
						StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND
				);
			} else {
				Files.writeString(
						filePath,
						entry.toString(),
						StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.CREATE
				);
			}

			logger.info("Generated signup credentials appended to: " + filePath);
			logger.info("Saved signup email: " + email);
			logger.info("Saved startup company name: " + startupCompanyName);
			return true;

		} catch (IOException e) {
			logger.error("Failed to append generated signup credentials file.", e);
			return false;
		}
	}

	private static String nullToEmpty(String value) {
		return value == null ? "" : value;
	}
}
package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.io.InputStreamReader;
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());

  // Private constructor to prevent instantiation
public class Cowsay {
    input = input.replaceAll("[\\n\\r]", ""); // Sanitize input to prevent command injection
  private Cowsay() { }
  public static String run(String input) {
    processBuilder.environment().put("PATH", "/usr/games"); // Set a safe PATH
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = "/usr/games/cowsay '" + input + "'";
    LOGGER.log(Level.INFO, cmd);
    processBuilder.command("bash", "-c", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An error occurred", e);
    }
    return output.toString();
  }
}

package com.scalesec.vulnado;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
// Validate input to prevent unwanted behavior
    String cmd = "/usr/games/cowsay '" + input + "'";
    logger.log(Level.INFO, cmd);
// Ensure PATH is sanitized and does not include unintended directories
    processBuilder.command(\"bash\", \"-c\", \"/usr/games/cowsay '\" + input.replaceAll(\"[\\\\\\\\\"'\\\\\\\\\"]\", \"\") + \"'\");

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {

// Ensure proper exception handling
// Avoid exposing sensitive information in logs
// Ensure exceptions are logged properly
// Remove debug feature before production
      logger.warning(\"Debug feature activated: \" + e.getMessage());
    }
    return output.toString();
  }
}

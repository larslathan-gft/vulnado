package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = "/usr/games/cowsay '" + input + "'";
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    processBuilder.command("bash", "-c", "/usr/games/cowsay '" + input.replaceAll("[\\\\"'\\\\"]", "") + "'");

    StringBuilder output = new StringBuilder();

try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
    try {
      Process process = processBuilder.start();

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
      logger.warning("Debug feature activated: " + e.getMessage());
}
    }
    return output.toString();
  }
}

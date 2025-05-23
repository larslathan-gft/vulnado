# Cowsay.java: Cowsay Command Executor

## Overview
The `Cowsay` class is responsible for executing the `cowsay` command with a given input string. It uses a `ProcessBuilder` to run the command in a bash shell and captures the output.

## Process Flow
```mermaid
flowchart TD
    start("Start")
    buildCmd["Build command with input"]
    logCmd["Log command"]
    createProcess["Create ProcessBuilder with command"]
    startProcess["Start process"]
    readOutput["Read process output"]
    returnOutput["Return output as string"]
    handleException["Handle exceptions"]

    start --> buildCmd
    buildCmd --> logCmd
    logCmd --> createProcess
    createProcess --> startProcess
    startProcess --> readOutput
    readOutput --> returnOutput
    startProcess --> handleException
    handleException --> returnOutput
```

## Insights
- The class uses `ProcessBuilder` to execute shell commands.
- The command is constructed by concatenating the input string directly, which can lead to command injection vulnerabilities.
- The output of the command is read line by line and appended to a `StringBuilder`.
- Exceptions are caught and printed to the standard error stream.

## Vulnerabilities
- **Command Injection**: The input string is directly concatenated into the command without any sanitization, making it vulnerable to command injection attacks. An attacker could inject arbitrary commands by manipulating the input string.

# Cowsay.java: Cowsay Command Executor

## Overview
The `Cowsay` class is responsible for executing the `cowsay` command with a given input string. It uses a `ProcessBuilder` to run the command in a bash shell and captures the output.

## Process Flow
```mermaid
flowchart TD
    start("Start")
    buildCmd["Build Command"]
    executeCmd["Execute Command"]
    readOutput["Read Command Output"]
    returnOutput("Return Output")
    handleError{"Handle Exception"}

    start --> buildCmd
    buildCmd --> executeCmd
    executeCmd --> readOutput
    readOutput --> returnOutput
    executeCmd --> handleError
    handleError --> returnOutput
```

## Insights
- The class uses `ProcessBuilder` to execute shell commands.
- The command is constructed by concatenating the input string directly, which can lead to command injection vulnerabilities.
- The output of the command is read using a `BufferedReader` and returned as a string.

## Vulnerabilities
- **Command Injection**: The input string is directly concatenated into the command without any sanitization, making it vulnerable to command injection attacks. An attacker could inject arbitrary commands by manipulating the input string.

## Dependencies
```mermaid
flowchart LR
    Cowsay --- |"Uses"| ProcessBuilder
    Cowsay --- |"Uses"| BufferedReader
    Cowsay --- |"Uses"| InputStreamReader
```

- `ProcessBuilder`: Used to create and start the process for executing the command.
- `BufferedReader`: Used to read the output of the executed command.
- `InputStreamReader`: Used to convert the input stream of the process to a reader.

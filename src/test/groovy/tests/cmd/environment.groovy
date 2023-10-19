package tests.cmd

ProcessBuilder processBuilder = new ProcessBuilder();

Map<String, String> environmentVariables = processBuilder.environment();
environmentVariables.forEach((key, value) -> System.out.println(key + " : " + value));
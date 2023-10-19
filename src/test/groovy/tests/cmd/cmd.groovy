package tests.cmd

// Создаем объект ProcessBuilder и указываем команду, которую нужно выполнить
ProcessBuilder processBuilder = new ProcessBuilder("ls", "-l")

// Запускаем процесс
Process process = Runtime.getRuntime().exec("ping www.stackabuse.com")

// Получаем вывод команды
BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
String line
while ((line = reader.readLine()) != null) {
    System.out.println(line)
}

// Ждем завершения процесса
int exitCode = process.waitFor();
System.out.println("Код завершения: " + exitCode)
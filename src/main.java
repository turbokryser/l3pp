import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.*;
import java.util.*;
import java.util.regex.*;

public class main {
    // Метод для проверки корректности даты
    public static boolean isValidDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        // Чтение исходной строки и разделителей из файла
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String lexemesLine = reader.readLine(); // Исходная строка
        String delimiters = reader.readLine(); // Разделители
        reader.close();

        System.out.println("Исходная строка: " + lexemesLine);
        System.out.println("Разделители: " + delimiters);

        // Разделение строки на лексемы
        String[] lexemes = lexemesLine.split("[" + Pattern.quote(delimiters) + "]+");
        ArrayList<String> processedLexemes = new ArrayList<>();
        ArrayList<Integer> octalNumbers = new ArrayList<>();
        Random random = new Random();

        // Регулярные выражения для поиска дат и восьмеричных чисел
        Pattern datePattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}"); // Даты в формате ДД:ММ:ГГ
        Pattern octalPattern = Pattern.compile("^[0-7]$+"); // Восьмеричные числа

        // Обработка каждой лексемы
        for (String lex : lexemes) {
            if (!lex.isEmpty()) {
                Matcher dateMatcher = datePattern.matcher(lex);
                if (dateMatcher.matches() && isValidDate(lex)) {
                    System.out.println("Найдена корректная дата: " + lex);
                    lex = lex + " " + random.nextInt(100); // Добавляем случайное число к дате
                } else if (octalPattern.matcher(lex).matches()) {
                    try {
                        int octalValue = Integer.parseInt(lex, 8); // Преобразование восьмеричного числа
                        octalNumbers.add(octalValue);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка преобразования числа: " + lex);
                    }
                }
                processedLexemes.add(lex); // Добавляем обработанную лексему
            }
        }

        // Поиск минимальной подстроки, заканчивающейся на цифру
        String minSubstring = null;
        Matcher matcher = Pattern.compile("\\S*\\d").matcher(lexemesLine); // Любая подстрока, заканчивающаяся на цифру

        while (matcher.find()) {
            String substring = matcher.group(); // Получаем текущую подстроку
            if (minSubstring == null || substring.length() < minSubstring.length()) {
                minSubstring = substring; // Сохраняем минимальную подстроку
            }
        }

        // Удаление минимальной подстроки из строки
        if (minSubstring != null) {
            lexemesLine = lexemesLine.replaceFirst(Pattern.quote(minSubstring), ""); // Удаляем только первое вхождение
            System.out.println("Удалена подстрока: " + minSubstring);
        }

        // Сортировка оставшихся лексем по длине
        //processedLexemes.sort(Comparator.comparingInt(String::length));

        // Формирование итоговой строки
        StringBuilder resultString = new StringBuilder();
        for (String lex : processedLexemes) {
            resultString.append(lex).append(" ");
        }

        // Запись результатов в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write("Обработанные лексемы: " + resultString.toString().trim());
            writer.newLine();
            writer.write("Числа в 8-й системе счисления: " + octalNumbers);
        }

        // Вывод результатов в консоль
        System.out.println("Обработанные лексемы: " + resultString.toString().trim());
        System.out.println("Числа в 8-й системе счисления: " + octalNumbers);
    }
}


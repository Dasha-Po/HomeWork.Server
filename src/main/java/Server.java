import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
// запускаем сервер
        try (ServerSocket server = new ServerSocket(ServerConfig.PORT);) {
            System.out.println("Сервер запущен");
            int count = 0;
            String city = null;
            while (true) {
                //запускаем бесконечный цикл и далее ждем подключение клиента
                // ридер и райтер для общения с клиентом
                try (Socket client = server.accept();
                     PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    count += 1;
                    System.out.println("Подключен клиент №" + count + " порт " + client.getPort());

                    // отправляем сообщение клиенту
                    if (count == 1) {
                        writer.println("???");
                        city = reader.readLine();
                        writer.println(String.format("Вы ввели слово: %s", city));
                    } else {
                        writer.println(city);
                        String attempt = reader.readLine();

                        if (check(city, attempt)) {
                            writer.println("OK");
                            city = attempt;
                            writer.println(String.format("Последнее слово теперь: %s", city));
                        } else {
                            writer.println("NOT OK");
                            writer.println(String.format("Последнее слово было: %s", city));
                        }
                    }
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }

    public static boolean check(String city, String attempt) {
        char last = city.charAt(city.length() - 1);
        char first = attempt.charAt(0);
        return last == first;
    }
}

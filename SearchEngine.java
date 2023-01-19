import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler2 implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> list = new ArrayList<>();
    int count = 0;

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            String result = "";
            for (int i = 0; i < count; i++) {
                result += list.get(i) + " ";
            }
            return "Welcome! To add a new word, use \"/add?s=<word>\". To search for a word, use \"/search?s=<word>\".\nCurrently stored words: " + result;
        } else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    list.add(parameters[1]);
                    count++;
                    return String.format("%s added! There are now %d words in the searchbase", parameters[1], count);
                }
            }
            else if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                if (count == 0) {
                    return "No words currently in the searchbase!";
                }
                if (parameters[0].equals("s")) {
                    String result = "";
                    int num = 0;
                    for (int i = 0; i < count; i++) {
                        if (list.get(i).contains(parameters[1])) {
                            result += list.get(i) + " ";
                            num++;
                        }
                    }
                    return String.format("%d matches found: %s", num, result);
                }
            }
        }
        return "404 Not Found!";

    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler2());
    }
}

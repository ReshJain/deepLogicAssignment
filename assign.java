import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class assign {

    public static List<Story> timeStories() {
        List<Story> stories = new ArrayList<>();
        HttpURLConnection connection = null;
        BufferedReader bff = null;
        try {
            URL url = new URL("https://time.com");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            bff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bff.readLine()) != null) {
                response.append(line);
            }

            String web = response.toString();

            int i = 0;
            while ((i = web.indexOf("<h3 class=\"headline\">", i)) != -1) {
                int ts = web.indexOf(">", i) + 1;
                int te = web.indexOf("</h3>", ts);
                String title = web.substring(ts, te);

                int ls = web.indexOf("href=\"", te) + "href=\"".length();
                int le = web.indexOf("\"", ls);
                String link = web.substring(ls, le);

                stories.add(new Story(title, link));

                i = le;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bff != null) {
                try {
                    bff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return stories;
    }

    static class Story {
        private String title;
        private String link;

        public Story(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }
    }

    public static void main(String[] args) {
        List<Story> tStories = timeStories();
        if (tStories.isEmpty()) {
            System.out.println("[]");
        } else {
            System.out.println("[");
            for (int i = 0; i < tStories.size(); i++) {
                Story story = tStories.get(i);
                System.out.println("  {");
                System.out.println("    \"title\": \"" + story.getTitle() + "\",");
                System.out.println("    \"link\": \"" + story.getLink() + "\"");
                if (i < tStories.size() - 1) {
                    System.out.println("  },");
                } else {
                    System.out.println("  }");
                }
            }
            System.out.println("]");
        }
    }

}

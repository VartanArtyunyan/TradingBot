import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IT {
    
    public static void main(String[] args) {
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "Host");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://calendar-api.fxstreet.com/de/api/v1/eventDates/2022-05-03T08:40:27Z/2022-05-05T10:40:27Z?volatilities=NONE&volatilities=LOW&volatilities=MEDIUM&volatilities=HIGH&countries=US&countries=UK&countries=EMU&countries=DE&countries=CN&countries=JP&countries=CA&countries=AU&countries=NZ&countries=CH&countries=FR&countries=IT&countries=ES&countries=UA&categories=8896AA26-A50C-4F8B-AA11-8B3FCCDA1DFD&categories=FA6570F6-E494-4563-A363-00D0F2ABEC37&categories=C94405B5-5F85-4397-AB11-002A481C4B92&categories=E229C890-80FC-40F3-B6F4-B658F3A02635&categories=24127F3B-EDCE-4DC4-AFDF-0B3BD8A964BE&categories=DD332FD3-6996-41BE-8C41-33F277074FA7&categories=7DFAEF86-C3FE-4E76-9421-8958CC2F9A0D&categories=1E06A304-FAC6-440C-9CED-9225A6277A55&categories=33303F5E-1E3C-4016-AB2D-AC87E98F57CA&categories=9C4A731A-D993-4D55-89F3-DC707CC1D596&categories=91DA97BD-D94A-4CE8-A02B-B96EE2944E4C&categories=E9E957EC-2927-4A77-AE0C-F5E4B5807C16"))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .header("Accept", "application/json")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Referer", "https://www.fxstreet.de.com/")
                .header("Origin", "https://www.fxstreet.de.com")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "cross-site")
                .header("Pragma", "no-cache")
                .header("Cache-Control", "no-cache")
                .header("TE", "trailers")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
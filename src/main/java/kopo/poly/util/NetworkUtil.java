package kopo.poly.util;

import org.springframework.lang.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NetworkUtil {
    /**
     * Geocoding  API에 GET 방식으로 요청을 보내는 메서드
     *
     * @param apiUrl         API의 URL
     * @param requestHeaders 요청 헤더
     * @param param
     * @return API 응답 결과
     */
    public static String get(String apiUrl, String param, @Nullable Map<String, String> requestHeaders ) {
        HttpURLConnection con = connection(apiUrl);

        try {
            // GET 요청 설정
            con.setRequestMethod("GET");

            // 요청 헤더 추가
            if (requestHeaders != null) {
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            // API 호출 후 결과 받기
            int responseCode = con.getResponseCode();

            // API 호출이 성공하면..
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());  // 성공 결과 값을 문자열로 변환
            } else {    // 에러 발생
                return readBody(con.getErrorStream());  // 실패 결과 값을 문자열로 변환
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    /**
     * HttpURLConnection 객체 생성하는 메서드
     */
    private static HttpURLConnection connection(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}

package com.nonfollowers.finder.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {

    private static final Pattern INSTAGRAM_URL_PATTERN =
            Pattern.compile("https?://(?:www\\.)?instagram\\.com/(?:_u/)?([a-zA-Z0-9._]+)");

    private static final Pattern H2_PATTERN =
            Pattern.compile("<h2[^>]*>([^<]+)</h2>");

    public static Set<String> extractUsernames(InputStream inputStream) throws IOException {
        Set<String> usernames = new HashSet<>();
        StringBuilder content = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }

        String html = content.toString();

        Matcher urlMatcher = INSTAGRAM_URL_PATTERN.matcher(html);
        while (urlMatcher.find()) {
            String username = urlMatcher.group(1);
            if (!username.isEmpty() && !"_u".equals(username)) {
                usernames.add(username);
            }
        }

        Matcher h2Matcher = H2_PATTERN.matcher(html);
        while (h2Matcher.find()) {
            String name = h2Matcher.group(1).trim();
            if (!name.isEmpty()) {
                usernames.add(name);
            }
        }

        return usernames;
    }
}

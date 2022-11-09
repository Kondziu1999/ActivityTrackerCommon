package com.agh.activitytrackerclient.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
public class NonAssetRequestVerifier {
    public static Pattern extRegex = Pattern.compile(".*\\..+");

    private Set<String> standardAssetsExtensions = Set.of(
        ".ico",
        ".png",
        ".gif",
        ".svg",
        ".jpg",
        ".html",
        ".css",
        ".js"
    );

    public void registerAdditionalAssetsExtensions(String[] extensions) {
        Arrays.stream(extensions)
            .filter(ext -> extRegex.matcher(ext).matches())
            .forEach(ext -> standardAssetsExtensions.add(ext));
    }


    // TODO cache
    public boolean verifyNotAsset(HttpServletRequest request) {
        var uri = request.getRequestURI();

        return standardAssetsExtensions
                .stream()
                .noneMatch(uri::endsWith);
    }
}

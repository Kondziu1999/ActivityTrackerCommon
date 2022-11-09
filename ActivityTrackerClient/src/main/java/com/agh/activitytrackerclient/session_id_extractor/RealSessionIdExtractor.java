package com.agh.activitytrackerclient.session_id_extractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class RealSessionIdExtractor implements SessionIdExtractor {

    @Override
    public Optional<String> retrieveSessionId(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession()).map(HttpSession::getId);
    }
}


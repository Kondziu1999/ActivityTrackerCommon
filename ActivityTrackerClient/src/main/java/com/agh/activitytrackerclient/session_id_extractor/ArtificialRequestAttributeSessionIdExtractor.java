package com.agh.activitytrackerclient.session_id_extractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ArtificialRequestAttributeSessionIdExtractor implements SessionIdExtractor {
    private String headerName;

    public ArtificialRequestAttributeSessionIdExtractor(String headerName){
        this.headerName = headerName;
    }

    @Override
    public Optional<String> retrieveSessionId(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute(headerName)).map(Object::toString);
    }
}

package com.agh.activitytrackerclient.interceptor;

import com.agh.activitytrackerclient.models.ActivityUser;
import com.agh.activitytrackerclient.models.UserLog;
import com.agh.activitytrackerclient.repository.ActivityUserClientRepository;
import com.agh.activitytrackerclient.repository.UserLogClientRepository;
import com.agh.activitytrackerclient.session_id_extractor.SessionIdExtractor;
import com.agh.activitytrackerclient.utils.ActivityUserDetailsProvider;
import com.agh.activitytrackerclient.utils.HasStringUserIdPrincipal;
import com.agh.activitytrackerclient.utils.NonAssetRequestVerifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Optional;

public class LoggingInterceptor extends HandlerInterceptorAdapter {
    private final String startTimeAttr = "startTime";
    private final String sessionIdAttribute = "session";
    private final String userIdAttribute = "userId";

    private final String nonAssetRequestAttribute = "nonAssetRequest";

    private final UserLogClientRepository repository;
    private final SessionIdExtractor sessionIdExtractor;
    private final ActivityUserClientRepository activityUserClientRepository;
    private final NonAssetRequestVerifier nonAssetRequestVerifier;

    private final ActivityUserDetailsProvider activityUserDetailsProvider;

    public LoggingInterceptor(UserLogClientRepository repository, SessionIdExtractor sessionIdExtractor, ActivityUserClientRepository activityUserClientRepository, NonAssetRequestVerifier nonAssetRequestVerifier, ActivityUserDetailsProvider activityUserDetailsProvider) {
        this.repository = repository;
        this.sessionIdExtractor = sessionIdExtractor;
        this.activityUserClientRepository = activityUserClientRepository;
        this.nonAssetRequestVerifier = nonAssetRequestVerifier;
        this.activityUserDetailsProvider = activityUserDetailsProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (nonAssetRequestVerifier.verifyNotAsset(request)) {
            Principal principal = request.getUserPrincipal();

            if(principal != null) {
                HasStringUserIdPrincipal user = (HasStringUserIdPrincipal)(((Authentication)request.getUserPrincipal()).getPrincipal());

                request.setAttribute(userIdAttribute, user.getStringUserId());
                request.setAttribute(sessionIdAttribute, sessionIdExtractor.retrieveSessionId(request).get());
            }
            long startTime = System.currentTimeMillis();
            request.setAttribute(startTimeAttr, startTime);
            request.setAttribute(nonAssetRequestAttribute, true);
        } else {
            request.setAttribute(nonAssetRequestAttribute, false);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (Boolean.parseBoolean(request.getAttribute(nonAssetRequestAttribute).toString())) {

            Object userIdFromAttribute = request.getAttribute(userIdAttribute);
            Long startTime = Long.parseLong(request.getAttribute(startTimeAttr) + "");
            String endpoint = request.getRequestURI();
            UserLog userLog = new UserLog();
            userLog.setActivityStart(startTime);
            userLog.setEndpoint(endpoint);

            if (userIdFromAttribute != null) {
                String userId = request.getAttribute(userIdAttribute) + "";
                Optional<String> sessionsId = Optional.ofNullable(request.getAttribute(sessionIdAttribute)).map(Object::toString);

                userLog.setUserSessionId(sessionsId.get());
                userLog.setActivityUserId(userId);
                createActivityUserIfNotExists(userId);

                var logs = activityUserClientRepository.getOne(userId).getUserLogs();
            }

            userLog.setActivityEnd(System.currentTimeMillis());
            repository.save(userLog);
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    private void createActivityUserIfNotExists(String userId) {
        // TODO cache
        if (!activityUserClientRepository.existsById(userId)) {
            var details = activityUserDetailsProvider.getAppActivityUserDetails(userId);
            ActivityUser user = new ActivityUser();
            user.setId(userId);
            user.setEmail(details.getEmail());
            user.setName(details.getName());
            user.setUsername(details.getUsername());

            activityUserClientRepository.save(user);
        }
    }
}


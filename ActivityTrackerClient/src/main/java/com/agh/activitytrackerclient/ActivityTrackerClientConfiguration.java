package com.agh.activitytrackerclient;

import com.agh.activitytrackerclient.interceptor.LoggingInterceptor;
import com.agh.activitytrackerclient.repository.ActivityUserClientRepository;
import com.agh.activitytrackerclient.repository.UserLogClientRepository;
import com.agh.activitytrackerclient.session_id_extractor.ArtificialRequestAttributeSessionIdExtractor;
import com.agh.activitytrackerclient.session_id_extractor.SessionIdExtractor;
import com.agh.activitytrackerclient.utils.ActivityUserDetailsProvider;
import com.agh.activitytrackerclient.utils.NonAssetRequestVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
@EntityScan("com.agh.activitytrackerclient.*")
@ComponentScan(basePackages = "com.agh.activitytrackerclient.*")
@ConfigurationProperties(prefix = "com.agh.activitytrackerclient")
@ConditionalOnClass({DataSource.class, UserLogClientRepository.class, ActivityUserDetailsProvider.class})
@ConditionalOnProperty(prefix = "activity_tracker", name = "sessionidheader")
public class ActivityTrackerClientConfiguration {

    @Value("${activity_tracker.sessionidheader}")
    private String sessionIdHeader;


    @Bean
    @ConditionalOnMissingBean
    public SessionIdExtractor sessionIdExtractor() {
        return new ArtificialRequestAttributeSessionIdExtractor(sessionIdHeader);
    }

    @Bean
    public NonAssetRequestVerifier nonAssetRequestVerifier() {
        return new NonAssetRequestVerifier();
    }

    @Bean
    public LoggingInterceptor loggingInterceptor(UserLogClientRepository repository, SessionIdExtractor sessionIdExtractor, ActivityUserClientRepository activityUserClientRepository, NonAssetRequestVerifier nonAssetRequestVerifier, ActivityUserDetailsProvider activityUserDetailsProvider) {
        return new LoggingInterceptor(repository, sessionIdExtractor, activityUserClientRepository, nonAssetRequestVerifier, activityUserDetailsProvider);
    }
}

package me.strongwhisk.blcking;

import lombok.extern.slf4j.Slf4j;
import me.strongwhisk.GitHubCommit;
import me.strongwhisk.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by taesu on 2018-04-13.
 */
@Component
@Slf4j
public class RestTemplateEx {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public void startBlockApi(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("first api call");
        RestTemplate restTemplate = restTemplateBuilder.build();
        GitHubRepository[] repositories = restTemplate.getForObject("https://api.github.com/users/dlxotn216/repos", GitHubRepository[].class);
        Arrays.stream(repositories).forEach(item-> log.info(item.toString()));

        log.info("second api call start");
        GitHubCommit[] commits = restTemplate.getForObject("https://api.github.com/repos/dlxotn216/cooperation/commits", GitHubCommit[].class);
        Arrays.stream(commits).forEach(item->log.info(item.toString()));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
    }
}

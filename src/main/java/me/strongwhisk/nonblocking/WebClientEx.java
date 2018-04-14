package me.strongwhisk.nonblocking;

import lombok.extern.slf4j.Slf4j;
import me.strongwhisk.GitHubCommit;
import me.strongwhisk.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * Created by taesu on 2018-04-13.
 */
@Component
@Slf4j
public class WebClientEx {

    @Autowired
    WebClient.Builder builder;

    public void startNonblockingAPI() {
        log.info("===============================================================================================================");
        log.info("================================         Mono and Flux Test                   =================================");
        log.info("===============================================================================================================");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        WebClient client = builder.baseUrl("https://api.github.com").build();

        log.info("REACTIVE first api call");
        Mono<String> repos = client.get().uri("/users/{username}/repos", "dlxotn216").retrieve().bodyToMono(String.class);
        repos.subscribe(response -> log.info("Mono Repository response " + response));

        //Response를 한 건씩 Flux를 통해 흘려서 받아 처리
        Flux<GitHubRepository> repositoryFlux = client.get().uri("/users/{username}/repos", "dlxotn216").retrieve().bodyToFlux(GitHubRepository.class);
        repositoryFlux.doOnError(error->log.error("Error occur",error))
                       .subscribe(response -> log.info("Single Object Flux response: " + response.toString()));

        log.info("REACTIVE second api call");
        Mono<String> commits = client.get().uri("/repos/{username}/{repos}/commits", "dlxotn216", "cooperation").retrieve().bodyToMono(String.class);
        commits.subscribe(response -> log.info("Mono Commits response " + response));

        //response를 한꺼번에 Mono로 뭉쳐서 받음
        Mono<GitHubCommit[]> commitsFlux = client.get().uri("/repos/{username}/{repos}/commits", "dlxotn216", "cooperation").retrieve().bodyToMono(GitHubCommit[].class);
        commitsFlux.doOnError(error -> log.error("Err", error))
                   .subscribe(response -> Arrays.stream(response).forEach(item -> log.info("Array Mono response: " + item.toString())));

        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    public void doOnNextEx() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("===============================================================================================================");
        log.info("================================          Do On Next Test                     =================================");
        log.info("===============================================================================================================");
        WebClient client = builder.baseUrl("https://api.github.com").build();

        //백프레셔 만큼 받아 처리하는 동작을 정의 후 subscribe
        Flux<GitHubRepository> repositoryFlux = client.get().uri("/users/{username}/repos", "dlxotn216").retrieve().bodyToFlux(GitHubRepository.class);
        repositoryFlux.doOnNext(response -> log.info("Repository response: " + response.toString()))
                       .doOnError(error -> log.error("Error On Repository Flux", error))
                       .subscribe();

        //백프레셔 만큼 받아 처리하는 동작을 정의 후 subscribe
        Flux<GitHubCommit> commitsFlux = client.get().uri("/repos/{username}/{repos}/commits", "dlxotn216", "cooperation").retrieve().bodyToFlux(GitHubCommit.class);
        commitsFlux.doOnNext(response -> log.info("Commits response: " + response.toString()))
                    .doOnError(error -> log.error("Error On Commit Flux", error))
                    .subscribe();
    }
}

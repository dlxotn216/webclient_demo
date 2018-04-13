package me.strongwhisk;

import lombok.Data;
import lombok.ToString;

/**
 * Created by taesu on 2018-04-13.
 */
@Data
@ToString
public class GitHubCommit {
    private String url;
    private String sha;
    private String comments_url;

}

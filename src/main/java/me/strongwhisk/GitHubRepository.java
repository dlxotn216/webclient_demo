package me.strongwhisk;

import lombok.Data;
import lombok.ToString;

/**
 * Created by taesu on 2018-04-13.
 */
@Data
@ToString
public class GitHubRepository {

    private String id;
    private String name;
    private String full_name;
    private String html_url;
    private String description;
    private boolean fork;
    private String url;
    private GitHubOwner owner;
}

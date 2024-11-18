package org.sasha;

public class UserDto {
    private String id;
    private String login;
    private String password;
    private String job;

    public UserDto(String id, String login, String password, String job) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}

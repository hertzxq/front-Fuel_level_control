package org.sasha;

public class RecordDTO {
    private Long id;
    private String login;
    private String job;

    // Конструктор
    public RecordDTO(Long id, String login, String job) {
        this.id = id;
        this.login = login;
        this.job = job;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}

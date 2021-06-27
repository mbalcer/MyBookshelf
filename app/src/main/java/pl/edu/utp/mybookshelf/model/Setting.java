package pl.edu.utp.mybookshelf.model;

public enum Setting {
    CHANGE_EMAIL("Zmień email"),
    CHANGE_PASSWORD("Zmień hasło"),
    CHANGE_FULL_NAME("Zmień imię i nazwisko"),
    SIGN_OUT("Wyloguj się");

    private String name;

    Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

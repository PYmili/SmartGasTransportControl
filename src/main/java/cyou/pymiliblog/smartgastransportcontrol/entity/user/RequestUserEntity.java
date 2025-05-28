package cyou.pymiliblog.smartgastransportcontrol.entity.user;

import java.util.Objects;

public class RequestUserEntity {
    private String username;
    private String password;

    public RequestUserEntity() {
    }

    public RequestUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RequestUserEntity that = (RequestUserEntity) object;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "RequestUserEntity{" +
                "username='" + username + '\'' +
                '}';
    }
}

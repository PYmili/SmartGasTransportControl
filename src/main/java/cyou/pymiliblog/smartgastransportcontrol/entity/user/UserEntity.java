package cyou.pymiliblog.smartgastransportcontrol.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Objects;

@TableName("users")
public class UserEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private long id;
    @TableField("username")
    private String username;
    @TableField("password_hash")
    private String passwordHash;
    @TableField("email")
    private String email;
    @TableField("is_action")
    private Boolean isAction;

    public UserEntity() {
    }

    public UserEntity(long id, String username, String passwordHash, String email, Boolean isAction) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.isAction = isAction;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAction() {
        return isAction;
    }

    public void setAction(Boolean action) {
        isAction = action;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserEntity that = (UserEntity) object;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(passwordHash, that.passwordHash) && Objects.equals(email, that.email) && Objects.equals(isAction, that.isAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, passwordHash, email, isAction);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAction=" + isAction +
                '}';
    }
}

package cyou.pymiliblog.smartgastransportcontrol.entity.route;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Objects;

@TableName("routes")
public class RouteEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("long_name")
    private String longName;
    @TableField("description")
    private String description;

    public RouteEntity() {
    }

    public RouteEntity(Integer id, String longName, String description) {
        this.id = id;
        this.longName = longName;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RouteEntity that = (RouteEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(longName, that.longName) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, longName, description);
    }

    @Override
    public String toString() {
        return "RouteEntity{" +
                "id=" + id +
                ", longName='" + longName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

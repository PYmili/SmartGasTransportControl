package cyou.pymiliblog.smartgastransportcontrol.entity.pipeline;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Objects;

@TableName("pipelines")
public class PipelineEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "name")
    private String name;

    public PipelineEntity() {
    }

    public PipelineEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PipelineEntity that = (PipelineEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PipelineEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

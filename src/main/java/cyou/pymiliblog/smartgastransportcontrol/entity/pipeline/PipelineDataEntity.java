package cyou.pymiliblog.smartgastransportcontrol.entity.pipeline;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@TableName("pipeline_data")
public class PipelineDataEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("datetime")
    private LocalDateTime dateTime;
    @TableField("air_intake")
    private Double airIntake;
    @TableField("air_outlet")
    private Double airOutlet;
    @TableField("temperatures")
    private Double temperatures;
    @TableField("pressure")
    private Double pressure;
    @TableField("differential_pressure")
    private Double differentialPressure;
    @TableField("pipeline_name")
    private String pipelineName;

    public PipelineDataEntity() {
    }

    public PipelineDataEntity(Integer id, LocalDateTime dateTime, Double airIntake, Double airOutlet, Double temperatures, Double pressure, Double differentialPressure, String pipelineName) {
        this.id = id;
        this.dateTime = dateTime;
        this.airIntake = airIntake;
        this.airOutlet = airOutlet;
        this.temperatures = temperatures;
        this.pressure = pressure;
        this.differentialPressure = differentialPressure;
        this.pipelineName = pipelineName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Double getAirIntake() {
        return airIntake;
    }

    public void setAirIntake(Double airIntake) {
        this.airIntake = airIntake;
    }

    public Double getAirOutlet() {
        return airOutlet;
    }

    public void setAirOutlet(Double airOutlet) {
        this.airOutlet = airOutlet;
    }

    public Double getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(Double temperatures) {
        this.temperatures = temperatures;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getDifferentialPressure() {
        return differentialPressure;
    }

    public void setDifferentialPressure(Double differentialPressure) {
        this.differentialPressure = differentialPressure;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PipelineDataEntity that = (PipelineDataEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(dateTime, that.dateTime) && Objects.equals(airIntake, that.airIntake) && Objects.equals(airOutlet, that.airOutlet) && Objects.equals(temperatures, that.temperatures) && Objects.equals(pressure, that.pressure) && Objects.equals(differentialPressure, that.differentialPressure) && Objects.equals(pipelineName, that.pipelineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, airIntake, airOutlet, temperatures, pressure, differentialPressure, pipelineName);
    }

    @Override
    public String toString() {
        return "PipelineDataEntity{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", airIntake=" + airIntake +
                ", airOutlet=" + airOutlet +
                ", temperatures=" + temperatures +
                ", pressure=" + pressure +
                ", differentialPressure=" + differentialPressure +
                ", pipelineName='" + pipelineName + '\'' +
                '}';
    }
}

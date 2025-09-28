package org.example.wigellgym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class WorkoutDTO {

        private Integer id;
        private String name;
        private String typeOfWorkout;
        private String location;
        private Integer instructorId;
        private Integer maxParticipants;
        private Double basePriceSek;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime dateTime;
        private Long durationInMinutes;
        private Boolean cancelled;

        public WorkoutDTO() {

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

        public String getTypeOfWorkout() {
                return typeOfWorkout;
        }

        public void setTypeOfWorkout(String typeOfWorkout) {
                this.typeOfWorkout = typeOfWorkout;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public Integer getInstructorId() {
                return instructorId;
        }

        public void setInstructorId(Integer instructorId) {
                this.instructorId = instructorId;
        }

        public Integer getMaxParticipants() {
                return maxParticipants;
        }

        public void setMaxParticipants(Integer maxParticipants) {
                this.maxParticipants = maxParticipants;
        }

        public Double getBasePriceSek() {
                return basePriceSek;
        }

        public void setBasePriceSek(Double basePriceSek) {
                this.basePriceSek = basePriceSek;
        }

        public LocalDateTime getDateTime() {
                return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
                this.dateTime = dateTime;
        }

        public Boolean getCancelled() {
                return cancelled;
        }

        public void setCancelled(Boolean cancelled) {
                this.cancelled = cancelled;
        }

        public Long getDurationInMinutes() {
                return durationInMinutes;
        }

        public void setDurationInMinutes(Long durationInMinutes) {
                this.durationInMinutes = durationInMinutes;
        }

        @Override
        public String toString() {
                return "WorkoutDTO{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", typeOfWorkout='" + typeOfWorkout + '\'' +
                        ", location='" + location + '\'' +
                        ", instructorId=" + instructorId +
                        ", maxParticipants=" + maxParticipants +
                        ", basePriceSek=" + basePriceSek +
                        ", dateTime=" + dateTime +
                        ", lengthInMinutes=" + durationInMinutes +
                        ", cancelled=" + cancelled +
                        '}';
        }
}

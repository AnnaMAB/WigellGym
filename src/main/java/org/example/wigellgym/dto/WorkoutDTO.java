package org.example.wigellgym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class WorkoutDTO {

        private Integer id;
        private String name;
        private String typeOfWorkout;
        private String location;
        private InstructorDTO instructorDTO;
        private Integer maxParticipants;
        private Double basePricePerHourSek;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime dateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endTime;
        private Long durationInMinutes;
        private Boolean canceled;

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

        public InstructorDTO getInstructorDTO() {
                return instructorDTO;
        }

        public void setInstructorDTO(InstructorDTO instructorDTO) {
                this.instructorDTO = instructorDTO;
        }

        public Integer getMaxParticipants() {
                return maxParticipants;
        }

        public void setMaxParticipants(Integer maxParticipants) {
                this.maxParticipants = maxParticipants;
        }

        public Double getBasePricePerHourSek() {
                return basePricePerHourSek;
        }

        public void setBasePricePerHourSek(Double basePriceSek) {
                this.basePricePerHourSek = basePriceSek;
        }

        public LocalDateTime getDateTime() {
                return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
                this.dateTime = dateTime;
        }

        public Boolean getCanceled() {
                return canceled;
        }

        public void setCanceled(Boolean canceled) {
                this.canceled = canceled;
        }

        public Long getDurationInMinutes() {
                return durationInMinutes;
        }

        public void setDurationInMinutes(Long durationInMinutes) {
                this.durationInMinutes = durationInMinutes;
        }

        public LocalDateTime getEndTime() {
                return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
                this.endTime = endTime;
        }

        @Override
        public String toString() {
                return "WorkoutDTO{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", typeOfWorkout='" + typeOfWorkout + '\'' +
                        ", location='" + location + '\'' +
                        ", instructorDTO=" + instructorDTO +
                        ", maxParticipants=" + maxParticipants +
                        ", basePricePerHourSek=" + basePricePerHourSek +
                        ", dateTime=" + dateTime +
                        ", endTime=" + endTime +
                        ", durationInMinutes=" + durationInMinutes +
                        ", canceled=" + canceled +
                        '}';
        }
}

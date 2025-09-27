package org.example.entities;


import java.time.LocalTime;

public class MembershipType
{
        private Integer id;
        private String name;
        private LocalTime startTime;
        private LocalTime endTime;

        public Integer GetId()
        {
                return id;
        }


        public String GetName()
        {
                return name;
        }

        public void SetName(String name)
        {
                this.name = name;
        }

        public LocalTime GetStartTime()
        {
                return startTime;
        }

        public void SetStartTime(LocalTime startTime)
        {
                this.startTime = startTime;
        }

        public LocalTime GetEndTime()
        {
                return endTime;
        }

        public void SetEndTime(LocalTime endTime)
        {
                this.endTime = endTime;
        }
}

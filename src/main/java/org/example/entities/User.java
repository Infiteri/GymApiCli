package org.example.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User
{
        private Integer id;
        private String name;
        private LocalDate birthday;
        private String phoneNumber;

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

        public LocalDate GetBirthday()
        {
                return birthday;
        }

        public void SetBirthday(LocalDate birthday)
        {
                this.birthday = birthday;
        }

        public String GetPhoneNumber()
        {
                return phoneNumber;
        }

        public void SetPhoneNumber(String phoneNumber)
        {
                this.phoneNumber = phoneNumber;
        }
}

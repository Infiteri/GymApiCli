package org.example.entities;

import java.time.LocalDateTime;

public class Membership
{
        private Integer id;
        private String state;
        private MembershipType type;
        private LocalDateTime lastPayed;
        private LocalDateTime createdAt;
        private User user;

        public Integer GetId()
        {
                return id;
        }

        public void SetId(Integer id)
        {
                this.id = id;
        }

        public String GetState()
        {
                return state;
        }

        public void SetState(String state)
        {
                this.state = state;
        }

        public MembershipType GetType()
        {
                return type;
        }

        public void SetType(MembershipType type)
        {
                this.type = type;
        }

        public LocalDateTime GetLastPayed()
        {
                return lastPayed;
        }

        public void SetLastPayed(LocalDateTime lastPayed)
        {
                this.lastPayed = lastPayed;
        }

        public LocalDateTime GetCreatedAt()
        {
                return createdAt;
        }

        public void SetCreatedAt(LocalDateTime createdAt)
        {
                this.createdAt = createdAt;
        }

        public User GetUser()
        {
                return user;
        }

        public void SetUser(User user)
        {
                this.user = user;
        }
}

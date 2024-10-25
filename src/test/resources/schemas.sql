CREATE TABLE tempMeeting (
                             tempMeetingId UUID PRIMARY KEY,
                             tempMeetingTitle VARCHAR(255) NOT NULL,
                             UNIQUE (tempMeetingTitle)
);

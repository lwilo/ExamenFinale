package com.conference.common.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewsByConferenceQuery {
    private String conferenceId;
}

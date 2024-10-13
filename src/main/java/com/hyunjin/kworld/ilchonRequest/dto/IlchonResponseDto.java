package com.hyunjin.kworld.ilchonRequest.dto;

import com.hyunjin.kworld.ilchonRequest.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IlchonResponseDto {
    private Long requestId;
    private RequestStatus status;
}

package ru.practicum.ewm_main.subscriptions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long subscriber;
    private Long author;
    private String status;
}

package com.autonomous.pm.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class KeyValue
{
    @NonNull
    String key;

    @NonNull
    String value;
}
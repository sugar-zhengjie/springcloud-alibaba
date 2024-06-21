package com.zj.common.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@Data
public class RSADecodeData {
    private String key;
    private String keyVI;
    private long time;
}

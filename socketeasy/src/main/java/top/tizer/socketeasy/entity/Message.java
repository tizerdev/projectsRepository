package top.tizer.socketeasy.entity;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Message implements Serializable {

    private String username;

    private String message;

    private String to;
}

package com.buyit.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    /* status code ex: 400,302,404*/
    private int status;
    /* response data* ex : object, array*/
    private Object data;
    /* response message ex: successfully retrieved */
    private String message;
    /* response processed datetime ex: 2023-07-24 11:41:51 */
    @CreationTimestamp
    private LocalDateTime timestamp;

    private List<T> list;


}

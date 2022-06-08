package com.duwei.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author duwei
 * @since 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Node implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String ip;

    private String domain;

    private String role;

    private Integer deleted;


}

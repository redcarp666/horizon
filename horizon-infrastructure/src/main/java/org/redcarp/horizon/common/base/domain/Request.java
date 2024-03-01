package org.redcarp.horizon.common.base.domain;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class Request <T> implements Serializable {

    Page page;
    T data;

}

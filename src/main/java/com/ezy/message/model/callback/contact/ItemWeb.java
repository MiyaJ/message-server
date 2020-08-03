package com.ezy.message.model.callback.contact;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caixiaowei
 * @ClassName ItemWeb.java
 * @Description TODO
 * @createTime 2020年08月03日 14:53:00
 */
@Data
@XStreamAlias("Web")
public class ItemWeb implements Serializable {
    private static final long serialVersionUID = -5809535162675202446L;

    /**
     *
     */
    @XStreamAlias("Title")
    private String title;

    /**
     *
     */
    @XStreamAlias("Url")
    private String url;
}

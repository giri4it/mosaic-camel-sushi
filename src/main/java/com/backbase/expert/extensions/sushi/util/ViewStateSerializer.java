package com.backbase.expert.extensions.sushi.util;

import java.io.Serializable;

/**
 * User: bartv
 * Date: 19-10-13
 * Time: 14:58
 */
public interface ViewStateSerializer {

    public String writeObject(Serializable serializable);

    public Object readObject(String string);
}
